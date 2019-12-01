package com.duogesi.service;

import com.alibaba.fastjson.JSONObject;
import com.duogesi.Utils.WXPayConstants;
import com.duogesi.Utils.WXPayUtil;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class wechatservice {
    String results = "";
    private String appid = "wx8301d95291e6b82a";
    private String secretKey = "6fa0d5d07cad8c6a472cf2f610b1bff6";

    public String getopenid(HttpServletRequest request, String code) {
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid=" + appid + "&secret=" + secretKey
                + "&js_code=" + code + "&grant_type=authorization_code";   //接口地址
        System.out.println("url" + url);
        results = sendGetReq(url);// 发送http请求
        System.out.println("results" + results);
        return results ;
    }

    public String wechat_pay(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String openid = request.getParameter("openid");
        System.out.println("openid = " + openid);
        String url = "https://api.mch.weixin.qq.com/pay/unifiedorder";
        //组装预下单的请求数据
        String reqStr = getReqStr(openid);
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
            results = conPayParam(prepay_id); //组装返回数据
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

    //组装预下单的请求数据
    public static String getReqStr(String openid){
        Map<String,String> data = new HashMap<String,String>();
        String out_trade_no = setTradeNo();//
        //
        String appid = "wx8301d95291e6b82a";
        String mer_id = "";//店铺支付密码
        String merKey = "";//店铺支付密码


        data.put("appid", appid);
        data.put("mch_id",mer_id);
        data.put("nonce_str", WXPayUtil.generateUUID());
        data.put("sign_type", "MD5");
        data.put("body", "spy test");
        data.put("out_trade_no", out_trade_no);
        data.put("device_info", "");
        data.put("fee_type", "CNY");
        data.put("total_fee", "1");//1分钱
        data.put("spbill_create_ip", "123.12.12.123");
        data.put("notify_url", "http://xxx/wxpay/notify");
        data.put("trade_type", "JSAPI");
        data.put("product_id", "12");
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
    public static String setTradeNo(){
        String orderid = "20211909105011" + (int)((Math.random()*9+1)*100000);
        System.out.println("orderid = " + orderid);
        return orderid;
    }

    //组装返回客户端的请求数据
    public static String conPayParam(String prepayid){
        String appid = "wx8301d95291e6b82a";
        String mer_id = "";//店铺支付密码
        String merKey = "";//店铺支付密码
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
        return JSONObject.toJSONString(map);
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
