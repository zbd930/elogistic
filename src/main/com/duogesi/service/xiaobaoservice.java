package com.duogesi.service;

import com.alibaba.fastjson.JSONObject;
import com.duogesi.Utils.RedisUtil;
import com.duogesi.Utils.Date;
import com.duogesi.Utils.WXPayConstants;
import com.duogesi.Utils.WXPayUtil;
import com.duogesi.beans.amount;
import com.duogesi.beans.order;
import com.duogesi.beans.order_details;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class xiaobaoservice {
    private String secretKey = "51dac2d4115c60fec05ad7215c996ddd";
    private static String mer_id = "1573208531";//店铺支付密码
    private static String merKey = "zbd12345678912345678912345678912";//店铺支付密码
    private final static String requestUrl = "https://api.weixin.qq.com/sns/jscode2session";


    @Autowired
    private RedisUtil redisUtil;

    //普通下单
    public String wechat_pay(HttpServletRequest request, HttpServletResponse response, order order, order_details order_details, String method, String country, int supplier_id) throws IOException {
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
        amount amount = new amount();
        amount.setItem_id(order.getItem_id());
        String total = request.getParameter("total");
        BigDecimal decimal = new BigDecimal(total);
        amount.setTotal(decimal);
        //模拟已支付的金额
        BigDecimal decimal1 = new BigDecimal(2);
        decimal1 = BigDecimal.valueOf(0);
        amount.setPaid(decimal1);
        amount.setUnionId(order.getUnionId());
        if (!order.getTihuo()) {
            order.setStatus(4);
        } else order.setStatus(0);
        //邮件发送完毕，调用支付接口
        String openid = request.getParameter("openid");
        System.out.println("openid = " + openid);
        String url = "https://api.mch.weixin.qq.com/pay/unifiedorder";
        //组装预下单的请求数据
        String reqStr = getReqStr(openid, order, total);
        System.out.println("reqStr=" + reqStr);
        //发送post数据到微信预下单
        String results = sendPost(url, reqStr);
        System.out.println("prepay from weixin: \n " + results);
        Map<String, String> return_data = null;
        try {
            return_data = WXPayUtil.xmlToMap(results);//微信的一个工具类
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        String return_code = return_data.get("return_code");
        System.out.println("return_code=" + return_code);
        if (return_code.equals("SUCCESS")) {
            String prepay_id = return_data.get("prepay_id");
            Map map3 = conPayParam(prepay_id); //组装返回数据
            results = JSONObject.toJSONString(map3);
            //存入购物车500秒
            Map map = object2Map(order);
            Map map1 = object2Map(order_details);
            map.put("amount", total);
            map.put("supplier_id", supplier_id);
            map.putAll(map1);
            map.putAll(map3);
            //先存入redis，方便支付成功后添加订单
            redisUtil.hmset(number, map, 300);
            redisUtil.sSetAndTime(openid, 300, number);
        } else {
            results = "{\"return_code\":\"fail\"}";
            response.setContentType("application/json;charset=UTF-8");
            response.setHeader("catch-control", "no-catch");
            PrintWriter out = response.getWriter();
            out.write(results);
            out.flush();
            out.close();
        }
        return results;

    }


    //组装预下单的请求数据,total为总金额
    public static String getReqStr(String openid, order order, String total) {
        Map<String, String> data = new HashMap<String, String>();

        String appid = "wx8301d95291e6b82a";
//        String expireTime= com.duogesi.Utils.getOrderExpireTime.getOrderExpireTime(5*60*1000L);
//        System.out.println(expireTime);
        data.put("appid", appid);
        data.put("mch_id", mer_id);
        data.put("nonce_str", WXPayUtil.generateUUID());
        data.put("sign_type", "MD5");
        data.put("body", "prepaid");
        data.put("out_trade_no", order.getNumbers());
        data.put("fee_type", "CNY");
//        预付百分之10String.valueOf(Integer.valueOf(total)*10)
        data.put("total_fee", "1");
//        data.put("spbill_create_ip", "129.211.21.50");
//        data.put("spbill_create_ip", "192.168.1.111");
        data.put("spbill_create_ip", "192.168.3.20");
        data.put("notify_url", "http://192.168.3.20:8091/elogistic/order/update_xiaobao.do");
//        data.put("notify_url", "https://www.yikuajing.cn/elogistic/order/update_xiaobao.do");
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

    public static Map<String, Object> object2Map(Object object) {
        Map<String, Object> result = new HashMap<>();
        //获得类的的属性名 数组
        Field[] fields = object.getClass().getDeclaredFields();
        try {
            for (Field field : fields) {
                field.setAccessible(true);
                String name = new String(field.getName());
                result.put(name, field.get(object));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    //组装返回客户端的请求数据
    public static Map conPayParam(String prepayid) {
        String appid = "wx8301d95291e6b82a";
        System.out.println("根据当前的prepayid构造返回参数= " + prepayid);
        String results = "";
        Map<String, String> map = new HashMap<String, String>();
        map.put("appId", appid);
        LocalDateTime time = LocalDateTime.now();
        map.put("timeStamp", WXPayUtil.getCurrentTimestamp() + "");
        map.put("nonceStr", WXPayUtil.generateUUID());
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
        if (header != null) {
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
        if (out != null) {
            out.close();
        }
        if (in != null) {
            in.close();
        }
        return result;
    }
}
