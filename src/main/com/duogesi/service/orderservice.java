package com.duogesi.service;

import com.alibaba.fastjson.JSONObject;
import com.duogesi.Mail.Mymail;
import com.duogesi.Utils.RedisUtil;
import com.duogesi.Utils.TestXml2Json;
import com.duogesi.beans.*;
import com.duogesi.mapper.ItemsMapper;
import com.duogesi.mapper.OrderMapper;
import com.duogesi.mapper.user_infoMapper;
import org.apache.commons.beanutils.BeanUtils;
import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;


@Service
public class orderservice {
    @Autowired
    private ItemsMapper itemsMapper;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private com.duogesi.mapper.amountMapper amountMapper;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private wechatservice wechatservice;
    @Autowired
    private Mymail mymail;
    @Autowired
    private user_infoMapper user_infoMapper;

    //添加订单
    public void add_order(HttpServletRequest request, HttpServletResponse response) {
        BufferedReader reader = null;
        try {
            reader = request.getReader();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String line = "";
        StringBuffer inputString = new StringBuffer();
        try {
            while ((line = reader.readLine()) != null) {
                inputString.append(line);
            }
            request.getReader().close();
            System.out.println("----接收到的报文---" + inputString.toString());
            TestXml2Json testXml2Json = new TestXml2Json();
            JSONObject json = testXml2Json.xml2Json(inputString.toString());
//          XmlUtil.XmlToJson(inputString.toString());
            if (json.getString("return_code").equals("SUCCESS")) {
                String out_trade_no = json.get("out_trade_no") + "";//订单号
                String cash_fee = json.get("cash_fee") + "";//商户订单号
                //删除redis
                redisUtil.del(out_trade_no);
                //修改已支付金额
                amountMapper.updata_paid(Integer.valueOf(cash_fee) / 100, out_trade_no);
                //告诉微信服务器，我收到信息了，不要在调用回调action了
                response.getWriter().write("<xml><return_code><![CDATA[SUCCESS]]></return_code></xml>");
                System.out.println("----结束---" + inputString.toString());
            } else if (json.getString("return_code").equals("FAIL")) {
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //添加订单
    public void add_order_xiaobao(HttpServletRequest request, HttpServletResponse response) throws IOException {
        BufferedReader reader = null;
        try {
            reader = request.getReader();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String line = "";
        StringBuffer inputString = new StringBuffer();
        try {
            while ((line = reader.readLine()) != null) {
                inputString.append(line);
            }
            request.getReader().close();
            System.out.println("----接收到的报文---" + inputString.toString());
            TestXml2Json testXml2Json = new TestXml2Json();
            JSONObject json = null;
            try {
                json = testXml2Json.xml2Json(inputString.toString());
            } catch (DocumentException e) {
                e.printStackTrace();
            }
//          XmlUtil.XmlToJson(inputString.toString());
            if (json.getString("return_code").equals("SUCCESS")) {
                String out_trade_no = json.get("out_trade_no") + "";//订单号
                String cash_fee = json.get("cash_fee") + "";//商户订单号
                Map map2 = new HashMap();
                try {
                    map2 = redisUtil.hmget(out_trade_no);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
                if (map2 != null) {
                    order order1 = new order();
                    order_details order_details = new order_details();
                    try {
                        BeanUtils.populate(order_details, map2);
                        BeanUtils.populate(order1, map2);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                    amount amount = new amount();
                    amount.setTotal((BigDecimal) map2.get("amount"));
                    //添加订单操作
                    if ((amountMapper.insert_amount(amount) == 1) && (orderMapper.addorder2(order_details) == 1)) {
                        int supplier_id = (int) map2.get("supplier_id");
                        String email = user_infoMapper.get_company_email(supplier_id);
                        try {
                            mymail.send(email, "您发布的拼柜任务有新订单", "【任务更新】");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    //删除redis
                    redisUtil.del(out_trade_no);
                    //修改已支付金额
                    amountMapper.updata_paid(Integer.valueOf(cash_fee) / 100, out_trade_no);
                    //告诉微信服务器，我收到信息了，不要在调用回调action了
                    response.getWriter().write("<xml><return_code><![CDATA[SUCCESS]]></return_code></xml>");
                    System.out.println("----结束---" + inputString.toString());
                } else if (json.getString("return_code").equals("FAIL")) {
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //  0是成功，1是订单添加有误，
    public int add(order order, String total, order_details order_details, HttpServletRequest request, HttpServletResponse response) {
        //先判断库存
        if (itemsMapper.update_items(order_details.getWeight(), order_details.getVolume(), order.getItem_id()) != 0) {
            return 0;
        } else return 3;

    }

    //  0是成功，1是订单添加有误，
    public int add_redis(order order, String total, order_details order_details) {
        int item_id = order.getItem_id();
        float order_volume = order_details.getVolume();
        float order_weight = order_details.getWeight();
        String number1 = String.valueOf(item_id);
        float result_weight = 0;
        float result_volume = 0;
        String format1 = "";
        String format2 = "";
        synchronized (this) {
            HashMap map = (HashMap) redisUtil.hmget(number1);
            try {
                format1 = new BigDecimal(String.valueOf(map.get("weight"))).divide(new BigDecimal(1)).toString();
                format2 = new BigDecimal(String.valueOf(map.get("volume"))).divide(new BigDecimal(1)).toString();
            } catch (NumberFormatException e) {
                return 3;
            }
            result_weight = Float.valueOf(format1) - order_weight;
            result_volume = Float.valueOf(format2) - order_volume;
            if (result_weight >= 0 && result_volume >= 0) {
                if (itemsMapper.update_items1(order_weight, order_volume, item_id) != 0) {
                    redisUtil.hdecr(number1, "weight", order_details.getWeight());
                    redisUtil.hdecr(number1, "volume", order_details.getVolume());
                    return 0;
                } else return 3;
            } else return 3;
        }
    }

}
