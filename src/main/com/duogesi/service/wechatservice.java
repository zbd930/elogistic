package com.duogesi.service;

import com.alibaba.fastjson.JSON;
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
import org.apache.log4j.Logger;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLConnection;
import java.security.*;
import java.security.spec.InvalidParameterSpecException;
import java.time.LocalDateTime;
import java.util.*;


@Component
public class wechatservice {
    String results = "";
    private String appid = "wx8301d95291e6b82a";
    private String secretKey = "51dac2d4115c60fec05ad7215c996ddd";
    private static String mer_id = "1573208531";//店铺支付密码
    private static String merKey = "zbd12345678912345678912345678912";//店铺支付密码
    private final static String requestUrl = "https://api.weixin.qq.com/sns/jscode2session";
    private static Logger log = Logger.getLogger(wechatservice.class);

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

    public String getopenid(HttpServletRequest request, String code, String iv, String encryptedData) {
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
        amount.setUnionId(order.getUnionId());
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

//    public Map getunionid(String encryptedData, String iv, String code) {
//        Map map = new HashMap();
//
//        //登录凭证不能为空
//        if (code == null || code.length() == 0) {
//            map.put("status", 0);
//            map.put("msg", "code 不能为空");
//            return map;
//        }
//        //授权（必填）
//        String grant_type = "authorization_code";
//        //////////////// 1、向微信服务器 使用登录凭证 code 获取 session_key 和 unionId ////////////////
//        //请求参数
//        String params = "appid=" + appid + "&secret=" + secretKey + "&js_code=" + code + "&grant_type=" + grant_type;
//        //发送请求
////        String sr = HttpRequest.sendGet("https://api.weixin.qq.com/sns/jscode2session", params);
//        String sr=sendGetReq("https://api.weixin.qq.com/sns/jscode2session?"+params);
//
//      //解析相应内容（转换成json对象）
//       JSONObject json = JSONObject.parseObject(sr);
//        //获取会话密钥（session_key）
//        String session_key = json.get("session_key").toString();
//        //用户的唯一标识（openid）
//        String openid = (String) json.get("openid");
//        //////////////// 2、对encryptedData加密数据进行AES解密 ////////////////
//        AesUtil aesUtil=new AesUtil();
//        try {
//            String result = aesUtil.decrypt(encryptedData, session_key, iv, "UTF-8");
//            System.out.println(result);
//            if (null != result && result.length() > 0) {
//                map.put("status", 1);
//                map.put("msg", "解密成功");
//
//                JSONObject userInfoJSON = JSONObject.parseObject(result);
//                Map userInfo = new HashMap();
//                userInfo.put("openId", userInfoJSON.get("openId"));
//                userInfo.put("nickName", userInfoJSON.get("nickName"));
//                userInfo.put("gender", userInfoJSON.get("gender"));
//                userInfo.put("city", userInfoJSON.get("city"));
//                userInfo.put("province", userInfoJSON.get("province"));
//                userInfo.put("country", userInfoJSON.get("country"));
//                userInfo.put("avatarUrl", userInfoJSON.get("avatarUrl"));
//                userInfo.put("unionId", userInfoJSON.get("unionId"));
//                map.put("userInfo", userInfo);
//                return map;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        map.put("status", 0);
//        map.put("msg", "解密失败");
//        return map;
//    }


        /**
     * 获取微信小程序 session_key 和 openid
     *
     * @param code 调用微信登陆返回的Code
     * @return
     * @author YeFei
     */
    public Map getSessionKeyOrOpenId(String code, String iv, String encryptedData) {
        //微信端登录code值
        Map<String, String> requestUrlParam = new HashMap<>();
        requestUrlParam.put("appid", appid);    //开发者设置中的appId
        requestUrlParam.put("secret", secretKey);    //开发者设置中的appSecret
        requestUrlParam.put("js_code", code);    //小程序调用wx.login返回的code
        requestUrlParam.put("grant_type", "authorization_code");    //默认参数

        //发送post请求读取调用微信 https://api.weixin.qq.com/sns/jscode2session 接口获取openid用户唯一标识
        JSONObject res1 = JSON.parseObject(sendPost(requestUrl, requestUrlParam));
        Map result = new HashMap();
        if (res1 != null && res1.get("errcode") != null) {
            result.put("status", 0);
            result.put("msg", "解析失败,请检查入参code!");
            return result;
        } else {
            JSONObject res2 = getUserInfo(encryptedData, res1.get("session_key").toString(), iv);
            if (null != res2) {
                Map userInfo = new HashMap();
                result.put("status", 1);
                result.put("msg", "解密成功!");
                userInfo.put("openId", res2.get("openId"));
                userInfo.put("nickName", res2.get("nickName"));
                userInfo.put("gender", res2.get("gender"));
                userInfo.put("city", res2.get("city"));
                userInfo.put("province", res2.get("province"));
                userInfo.put("country", res2.get("country"));
                userInfo.put("avatarUrl", res2.get("avatarUrl"));
                userInfo.put("unionId", res2.get("unionId"));
                result.put("userInfo", userInfo);
                return result;
            }
        }
        result.put("status", 0);
        result.put("msg", "解析失败,请检查入参!");
        return result;
    }

    /**
     * 解密用户敏感数据获取用户信息
     *
     * @param sessionKey    数据进行加密签名的密钥
     * @param encryptedData 包括敏感数据在内的完整用户信息的加密数据
     * @param iv            加密算法的初始向量
     * @return
     * @author YeFei
     */
    public  JSONObject getUserInfo(String encryptedData, String sessionKey, String iv) {
        // 被加密的数据
        byte[] dataByte = Base64.decode(encryptedData);
        // 加密秘钥
        byte[] keyByte = Base64.decode(sessionKey);
        // 偏移量
        byte[] ivByte = Base64.decode(iv);
        try {
            // 如果密钥不足16位，那么就补足.  这个if 中的内容很重要
            int base = 16;
            if (keyByte.length % base != 0) {
                int groups = keyByte.length / base + (keyByte.length % base != 0 ? 1 : 0);
                byte[] temp = new byte[groups * base];
                Arrays.fill(temp, (byte) 0);
                System.arraycopy(keyByte, 0, temp, 0, keyByte.length);
                keyByte = temp;
            }
            // 初始化
            Security.addProvider(new BouncyCastleProvider());
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC");
            SecretKeySpec spec = new SecretKeySpec(keyByte, "AES");
            AlgorithmParameters parameters = AlgorithmParameters.getInstance("AES");
            parameters.init(new IvParameterSpec(ivByte));
            cipher.init(Cipher.DECRYPT_MODE, spec, parameters);// 初始化
            byte[] resultByte = cipher.doFinal(dataByte);
            if (null != resultByte && resultByte.length > 0) {
                String result = new String(resultByte, "UTF-8");
                return JSON.parseObject(result);
            }
        } catch (NoSuchAlgorithmException e) {
            log.error(e.getMessage(), e);
        } catch (NoSuchPaddingException e) {
            log.error(e.getMessage(), e);
        } catch (InvalidParameterSpecException e) {
            log.error(e.getMessage(), e);
        } catch (IllegalBlockSizeException e) {
            log.error(e.getMessage(), e);
        } catch (BadPaddingException e) {
            log.error(e.getMessage(), e);
        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage(), e);
        } catch (InvalidKeyException e) {
            log.error(e.getMessage(), e);
        } catch (InvalidAlgorithmParameterException e) {
            log.error(e.getMessage(), e);
        } catch (NoSuchProviderException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param url      发送请求的 URL
     * @param paramMap 请求参数
     * @return 所代表远程资源的响应结果
     */
    public static String sendPost(String url, Map<String, ?> paramMap) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";

        String param = "";
        Iterator<String> it = paramMap.keySet().iterator();

        while (it.hasNext()) {
            String key = it.next();
            param += key + "=" + paramMap.get(key) + "&";
        }

        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("Accept-Charset", "utf-8");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
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
            in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        //使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }
}
