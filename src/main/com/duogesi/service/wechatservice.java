package com.duogesi.service;

import com.alibaba.fastjson.JSONObject;
import com.duogesi.Mail.Mymail;
import com.duogesi.Mail.RedisUtil;
import com.duogesi.Utils.*;
import com.duogesi.Utils.Date;
import com.duogesi.entities.*;
import com.duogesi.mapper.ItemsMapper;
import com.duogesi.mapper.OrderMapper;
import com.duogesi.mapper.amountMapper;
import com.duogesi.mapper.user_infoMapper;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.testng.util.TimeUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDateTime;

import java.util.*;

@Component
public class wechatservice {
    String results = "";
    private String appid = "wx8301d95291e6b82a";
    private String secretKey = "51dac2d4115c60fec05ad7215c996ddd";
    private static String mer_id = "1573208531";//店铺支付密码
    private static String merKey = "zbd12345678912345678912345678912";//店铺支付密码

    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private ItemsMapper itemsMapper;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private amountMapper amountMapper;
    @Autowired
    private user_infoMapper user_infoMapper;
    @Autowired
    private Mymail mymail;
    @Autowired
    private Swtich swtich;

    public String getopenid(HttpServletRequest request, String code) {
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid=" + appid + "&secret=" + secretKey
                + "&js_code=" + code + "&grant_type=authorization_code";   //接口地址
        System.out.println("url" + url);
        results = sendGetReq(url);// 发送http请求
        System.out.println("results" + results);
        return results ;
    }

    //普通下单
    public String wechat_pay(HttpServletRequest request, HttpServletResponse response, order order, order_details order_details,String method,String country) throws IOException {
        //order设置number
        String frist = "880";
        LocalDateTime localDateTime = LocalDateTime.now();
        Date date = new Date();
        String middle = date.date(localDateTime);
        int i = (int) (Math.random() * 900 + 1000);
        String myStr = Integer.toString(i);
        String number = frist + middle + myStr;
        order.setNumbers(number);
        //测试下单的东西
         //调用第三方支付接口,并返回已支付的金额
        amount amount = new amount();
        amount.setItem_id(order.getItem_id());
        String total = request.getParameter("total");
        BigDecimal decimal = new BigDecimal(total);
        amount.setTotal(decimal);
        //模拟已支付的金额
        BigDecimal decimal1 = new BigDecimal(2);
        decimal1 = BigDecimal.valueOf(0);
        amount.setPaid(decimal1);
        amount.setOpenid(order.getOpenid());
        if (!order.getTihuo()) {
            order.setStatus(4);
        } else order.setStatus(0);
        String chaigui = order_details.getChaigui();
        if (!method.equals("海卡")&&!country.equals("American")) {
            //遍历下单的中文地址欧洲和其他分开
            if (chaigui.equals("英国") || chaigui.equals("德国") || chaigui.equals("法国") || chaigui.equals("卢森堡") || chaigui.equals("荷兰") || chaigui.equals("比利时") || chaigui.equals("爱尔兰") || chaigui.equals("西班牙") || chaigui.equals("意大利") || chaigui.equals("奥地利") || chaigui.equals("丹麦") || chaigui.equals("捷克") || chaigui.equals("其他")) {
                order_details.setChaigui(swtich.switch_mudigang_zhong_ouzhou(chaigui));
            } else {
                order_details.setChaigui(swtich.switch_mudigang_zhong(chaigui));
            }
            //遍历下单的中文地址
            order.setDest(swtich.switch_mudigang_zhong(order.getDest()));
        }
        //美国海派等
        else if(country.equals("American")&&!method.equals("海卡")){
                    order_details.setChaigui(swtich.switch_mudigang_zhong(chaigui));
                }
             //添加订单
             if (orderMapper.addorder(order)==1) {
             order_details.setOrder_id(order.getId());
             amount.setOrder_id(order.getId());
             //更新账单表和更新订单详情表
             if ((amountMapper.insert_amount(amount)==1) && (orderMapper.addorder2(order_details) == 1)) {
                 //发送邮件
                 int ship_id=order.getItem_id();
                 items items =itemsMapper.get_supplier_info(ship_id);
                 String email=items.getSupplier_companies().get(0).getContact_mail();
                 try {
                     mymail.send(email,"您发布的拼柜任务有新订单","【任务更新】");
                 } catch (Exception e) {
                     e.printStackTrace();
                 }
                 //邮件发送完毕，调用支付接口
                        String openid = request.getParameter("openid");
                        System.out.println("openid = " + openid);
                        String url = "https://api.mch.weixin.qq.com/pay/unifiedorder";
                        //组装预下单的请求数据
                        String reqStr = getReqStr(openid, order,total);
                        System.out.println("reqStr=" + reqStr);
                        //发送post数据到微信预下单
                        results = sendPost(url,reqStr);
                        System.out.println("prepay from weixin: \n " + results);
                        Map<String,String> return_data = null;
                        try {
                            return_data = WXPayUtil.xmlToMap(results);//微信的一个工具类
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                            System.out.println(e.getMessage());
                        }
                        String return_code = return_data.get("return_code");
                        System.out.println("return_code=" + return_code);
                        if("SUCCESS".equals(return_code)){
                            //调用成功，存入redis
                            String prepay_id = return_data.get("prepay_id");
                            Map map3= conPayParam(prepay_id); //组装返回数据
                            //存入购物车500秒
                            Map map =object2Map(order);
                            Map map1 =object2Map(order_details);
                            map.put("amount",total);
                            map.putAll(map1);
                            map.putAll(map3);
                            //定义定时器，恢复库存
                            Timer timer=new Timer();
                            timer.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    Map map2 =new HashMap();
                                    try {
                                        map2 = redisUtil.hmget(number);
                                    }catch (NullPointerException e){
                                       e.printStackTrace();
                                       timer.cancel();
                                    }
                                    order order1=new order();
                                    order_details order_details = new order_details();
                                    try {
                                        BeanUtils.populate(order_details,map2);
                                        BeanUtils.populate(order1,map2);
                                    } catch (IllegalAccessException e) {
                                        e.printStackTrace();
                                    } catch (InvocationTargetException e) {
                                        e.printStackTrace();
                                    }
                                    if (order1.getNumbers()!=null) {
                                        //恢复数据
                                        itemsMapper.return_items(order_details.getWeight(), order_details.getVolume(), order1.getItem_id());
                                        //删除订单
                                        orderMapper.delete_order(order.getId());
                                        orderMapper.delete_order_details(order.getId());
                                        //删除价格
                                        amountMapper.delete_order_price(order.getId());
                                    }
                                      //取消线程
                                    timer.cancel();
                                }
                            },300000);
                            //先存入redis
                            redisUtil.hmset(number,map,300);
                            redisUtil.sSetAndTime(openid,300,number);
                            results=JSONObject.toJSONString(map);
                        }else{
                            results ="{\"return_code\":\"fail\"}";
                            response.setContentType("application/json;charset=UTF-8");
                            response.setHeader("catch-control", "no-catch");
                            PrintWriter out = response.getWriter();
                            out.write(results);
                            out.flush();
                            out.close();
                        }
                    return results;
             } else return "失败";
             } else return "失败";



        //微信支付的东西
//        String openid = request.getParameter("openid");
//        String total = request.getParameter("total");
//        System.out.println("openid = " + openid);
//        String url = "https://api.mch.weixin.qq.com/pay/unifiedorder";
//        //组装预下单的请求数据
//        String reqStr = getReqStr(openid, order,total);
//        System.out.println("reqStr=" + reqStr);
//        //发送post数据到微信预下单
//        results = sendPost(url,reqStr);
//        System.out.println("prepay from weixin: \n " + results);
//        Map<String,String> return_data = null;
//        try {
//            return_data = WXPayUtil.xmlToMap(results);//微信的一个工具类
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//            System.out.println(e.getMessage());
//        }
//        String return_code = return_data.get("return_code");
//        System.out.println("return_code=" + return_code);
//        if("SUCCESS".equals(return_code)){
//            String prepay_id = return_data.get("prepay_id");
//            Map map3= conPayParam(prepay_id); //组装返回数据
//            //存入购物车500秒
//            Map map =object2Map(order);
//            Map map1 =object2Map(order_details);
//            map.put("amount",total);
//            map.putAll(map1);
//            map.putAll(map3);
//            //定义定时器，恢复库存
//            Timer timer=new Timer();
//            timer.schedule(new TimerTask() {
//                @Override
//                public void run() {
//                    Map map2 =new HashMap();
//                    try {
//                        map2 = redisUtil.hmget(number);
//                    }catch (NullPointerException e){
//                       e.printStackTrace();
//                       timer.cancel();
//                    }
//                    order order1=new order();
//                    order_details order_details = new order_details();
//                    try {
//                        BeanUtils.populate(order_details,map2);
//                        BeanUtils.populate(order1,map2);
//                    } catch (IllegalAccessException e) {
//                        e.printStackTrace();
//                    } catch (InvocationTargetException e) {
//                        e.printStackTrace();
//                    }
//                    //恢复数据
//                    itemsMapper.return_items(order_details.getWeight(),order_details.getVolume(),order1.getItem_id());
//                      //删除订单
//                            orderMapper.delete_order(order.getId());
//                            orderMapper.delete_order_details(order.getId());
//                            //删除价格
//                            amountMapper.delete_order_price(order.getId());
//                      取消线程
//                    timer.cancel();
//                }
//            },300000);
//            //先存入redis
//            redisUtil.hmset(number,map,300);
//            redisUtil.sSetAndTime(openid,300,number);
//            results=JSONObject.toJSONString(map);
//        }else{
//            results ="{\"return_code\":\"fail\"}";
//            response.setContentType("application/json;charset=UTF-8");
//            response.setHeader("catch-control", "no-catch");
//            PrintWriter out = response.getWriter();
//            out.write(results);
//            out.flush();
//            out.close();
//        }
//        return results;

    }

    //秒杀下单
    public String wechat_pay1(HttpServletRequest request, HttpServletResponse response, order order, order_details order_details) throws IOException {
        //order设置number
        String frist = "880";
        LocalDateTime localDateTime = LocalDateTime.now();
        Date date = new Date();
        String middle = date.date(localDateTime);
        int i = (int) (Math.random() * 900 + 1000);
        String myStr = Integer.toString(i);
        String number = frist + middle + myStr;
        order.setNumbers(number);
        String openid = request.getParameter("openid");
        String total = request.getParameter("total");
        System.out.println("openid = " + openid);
        String url = "https://api.mch.weixin.qq.com/pay/unifiedorder";
        //组装预下单的请求数据
        String reqStr = getReqStr(openid, order,total);
        System.out.println("reqStr=" + reqStr);
        //发送post数据到微信预下单
        results = sendPost(url,reqStr);
        System.out.println("prepay from weixin: \n " + results);
        Map<String,String> return_data = null;
        try {
            return_data = WXPayUtil.xmlToMap(results);//微信的一个工具类
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        String return_code = return_data.get("return_code");
        System.out.println("return_code=" + return_code);
        if("SUCCESS".equals(return_code)){
            String prepay_id = return_data.get("prepay_id");
            Map map3= conPayParam(prepay_id); //组装返回数据
            Map map =object2Map(order);
            Map map1 =object2Map(order_details);
            map.put("amount",total);
            map.putAll(map1);
            map.putAll(map3);
            //先存入redis
            redisUtil.hmset(number,map,300);
            //存入redis的list
            redisUtil.sSetAndTime(openid,300,number);
            results=JSONObject.toJSONString(map);
        }else{
            results ="{\"return_code\":\"fail\"}";
            response.setContentType("application/json;charset=UTF-8");
            response.setHeader("catch-control", "no-catch");
            PrintWriter out = response.getWriter();
            out.write(results);
            out.flush();
            out.close();
        }
        return results;

    }


    public static Map<String,Object> object2Map(Object object){
        Map<String,Object> result=new HashMap<>();
        //获得类的的属性名 数组
        Field[]fields=object.getClass().getDeclaredFields();
        try {
            for (Field field : fields) {
                field.setAccessible(true);
                String name = new String(field.getName());
                result.put(name, field.get(object));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    //组装预下单的请求数据,total为总金额
    public static String getReqStr(String openid,order order,String total){
        Map<String,String> data = new HashMap<String,String>();

        String appid = "wx8301d95291e6b82a";
//        String expireTime= com.duogesi.Utils.getOrderExpireTime.getOrderExpireTime(5*60*1000L);
//        System.out.println(expireTime);
        data.put("appid", appid);
        data.put("mch_id",mer_id);
        data.put("nonce_str", WXPayUtil.generateUUID());
        data.put("sign_type", "MD5");
        data.put("body", "prepaid");
        data.put("out_trade_no", order.getNumbers());
//        data.put("time_start", expireTime);
//        data.put("time_expire", expireTime);
        data.put("fee_type", "CNY");
//        预付百分之10String.valueOf(Integer.valueOf(total)*10)
        data.put("total_fee", "1");
        data.put("spbill_create_ip", "129.211.21.50");
//        data.put("spbill_create_ip", "192.168.1.111");
//        data.put("notify_url", "http://192.168.1.111:8091/elogistic/order/update.do");
        data.put("notify_url", "https://www.yikuajing.cn/elogistic/order/update.do");
        data.put("trade_type", "JSAPI");
        data.put("product_id", String.valueOf(order.getItem_id()));
        data.put("openid", openid);
        try {
            String sign = WXPayUtil.generateSignature(data, merKey, WXPayConstants.SignType.MD5);
            data.put("sign", sign);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("sign error");
        }
        String reqBody = null;
        try {
            reqBody = WXPayUtil.mapToXml(data);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return reqBody;
    }

    //保证唯一
//    public static String setTradeNo(){
//        String orderid = "20211909105011" + (int)((Math.random()*9+1)*100000);
//        System.out.println("orderid = " + orderid);
//        return orderid;
//    }

    //组装返回客户端的请求数据
    public static Map conPayParam(String prepayid){
        String appid = "wx8301d95291e6b82a";
        System.out.println("根据当前的prepayid构造返回参数= " + prepayid);
        String results = "";
        Map<String,String> map = new HashMap<String,String>();
        map.put("appId", appid);
        LocalDateTime time = LocalDateTime.now();
        map.put("timeStamp",  WXPayUtil.getCurrentTimestamp()+"");
        map.put("nonceStr", WXPayUtil.generateUUID() );
        map.put("package", "prepay_id=" + prepayid);
        map.put("signType", "MD5");
        String sign;
        try {
            sign = WXPayUtil.generateSignature(map, merKey, WXPayConstants.SignType.MD5);
            map.put("sign", sign);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return map;
    }
    public static String chinaToUnicode(String str){
        String result="";
        for (int i = 0; i < str.length(); i++){
            int chr1 = (char) str.charAt(i);
            if(chr1>=19968&&chr1<=171941){//汉字范围 \u4e00-\u9fa5 (中文)
                result+="\\u" + Integer.toHexString(chr1);
            }else{
                result+=str.charAt(i);
            }
        }
        return result;
    }
    private String sendGetReq(String url) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url;
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            connection.setRequestProperty("Charset", "UTF-8");

            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
            for (String key : map.keySet()) {
                System.out.println(key + "--->" + map.get(key));
            }
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        } // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }
    public static String sendPost(String url, String param) throws UnsupportedEncodingException, IOException {
        return sendPost(url, param, null);
    }

    public static String sendPost(String url, String param, Map<String, String> header) throws UnsupportedEncodingException, IOException {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        URL realUrl = new URL(url);
        // 打开和URL之间的连接
        URLConnection conn = realUrl.openConnection();
        //设置超时时间
        conn.setConnectTimeout(5000);
        conn.setReadTimeout(15000);
        // 设置通用的请求属性
        if (header!=null) {
            for (Map.Entry<String, String> entry : header.entrySet()) {
                conn.setRequestProperty(entry.getKey(), entry.getValue());
            }
        }
        conn.setRequestProperty("accept", "*/*");
        conn.setRequestProperty("connection", "Keep-Alive");
        conn.setRequestProperty("user-agent",
                "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
        conn.setRequestProperty("Charset", "UTF-8");

        // 发送POST请求必须设置如下两行
        conn.setDoOutput(true);
        conn.setDoInput(true);
        // 获取URLConnection对象对应的输出流
        out = new PrintWriter(conn.getOutputStream());
        // 发送请求参数
        out.print(param);
        // flush输出流的缓冲
        out.flush();
        // 定义BufferedReader输入流来读取URL的响应
        in = new BufferedReader(
                new InputStreamReader(conn.getInputStream(), "utf8"));
        String line;
        while ((line = in.readLine()) != null) {
            result += line;
        }
        if(out!=null){
            out.close();
        }
        if(in!=null){
            in.close();
        }
        return result;
    }
}
