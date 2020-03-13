package com.duogesi.service;

import com.alibaba.fastjson.JSONObject;
import com.duogesi.Mail.RedisUtil;
import com.duogesi.Utils.Date;
import com.duogesi.Utils.Swtich;
import com.duogesi.Utils.TestXml2Json;
import com.duogesi.entities.amount;
import com.duogesi.entities.details;
import com.duogesi.entities.order;
import com.duogesi.entities.order_details;
import com.duogesi.mapper.ItemsMapper;
import com.duogesi.mapper.OrderMapper;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
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
                String cash_fee=json.get("cash_fee")+"";//商户订单号
//                String openid=json.get("openid")+"";
//              String trade_type=json.get("trade_type")+"";
                //接下来是做自己的业务处理
//                Map map = redisUtil.hmget(out_trade_no);
//                order order = new order();
//                order_details order_details = new order_details();
//                amount amount = new amount();
//                BeanUtils.populate(order, map);
//                BeanUtils.populate(order_details, map);
//                amount.setItem_id(order.getItem_id());
//                BigDecimal decimal = new BigDecimal(String.valueOf(map.get("amount")));
//                amount.setTotal(decimal);
//                //已支付的金额
//                BigDecimal decimal1 = new BigDecimal("0.00");
//                decimal1 = BigDecimal.valueOf(Integer.valueOf(cash_fee)/100);
//                amount.setPaid(decimal1);
//                amount.setOpenid(order.getOpenid());
//                if (!order.getTihuo()) {
//                    order.setStatus(4);
//                } else order.setStatus(0);
//                try {
//                    if (order_details.getChaigui().equals("美东") || order_details.getChaigui().equals("美中") || order_details.getChaigui().equals("美西")) {
//                        Swtich s = new Swtich();
//                        order_details.setChaigui(s.switch_mudigang_fan(order_details.getChaigui()));
//                    }
//                    //添加订单
//                    if (orderMapper.addorder(order) == 1) {
//                        order_details.setOrder_id(order.getId());
//                        amount.setOrder_id(order.getId());
//                        //更新账单表和更新订单详情表
//                        if ((amountMapper.insert_amount(amount) == 1) && (orderMapper.addorder2(order_details) == 1)) {
//                                redisUtil.del(out_trade_no);
//                        }
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
//                }
                //删除redis
                redisUtil.del(out_trade_no);
                //修改已支付金额
                amountMapper.updata_paid(Integer.valueOf(cash_fee)/100,out_trade_no);
                //告诉微信服务器，我收到信息了，不要在调用回调action了
                response.getWriter().write("<xml><return_code><![CDATA[SUCCESS]]></return_code></xml>");
                System.out.println("----结束---" + inputString.toString());
            } else if (json.getString("return_code").equals("FAIL")) {
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //  0是成功，1是订单添加有误，
    public int add(order order, String total, order_details order_details, HttpServletRequest request, HttpServletResponse response) {
       //先判断库存
        if (itemsMapper.update_items(order_details.getWeight(),order_details.getVolume(),order.getItem_id())!= 0) {
           return 0;
         } else return 3;

    }

    //  0是成功，1是订单添加有误，
    public  int add_redis(order order, String total, order_details order_details) {
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
                }else return 3;
                } else return 3;
            }
        }


//     if (itemsMapper.update_items1(order_weight, order_volume, item_id) != 0) {
//             redisUtil.hdecr(number1, "weight", order_details.getWeight());
//             redisUtil.hdecr(number1, "volume", order_details.getVolume());
//             String frist = "881";
//             LocalDateTime localDateTime = LocalDateTime.now();
//             Date date = new Date();
//             String middle = date.date(localDateTime);
//             int i = (int) (Math.random() * 900 + 1000);
//             String myStr = Integer.toString(i);
//             String number = frist + middle + myStr;
//             order.setNumbers(number);
//             //调用第三方支付接口,并返回已支付的金额
//             amount amount = new amount();
//             amount.setItem_id(order.getItem_id());
//             BigDecimal decimal = new BigDecimal(total);
//             amount.setTotal(decimal);
//             //模拟已支付的金额
//             BigDecimal decimal1 = new BigDecimal(2);
//             decimal1 = BigDecimal.valueOf(105.57);
//             amount.setPaid(decimal1);
//             amount.setOpenid(order.getOpenid());
//             if (!order.getTihuo()) {
//             order.setStatus(4);
//             } else order.setStatus(0);
//             //添加订单
//             if (orderMapper.addorder(order) == 1) {
//             order_details.setOrder_id(order.getId());
//             amount.setOrder_id(order.getId());
//             //更新账单表和更新订单详情表
//             if ((amountMapper.insert_amount(amount) == 1) && (orderMapper.addorder2(order_details) == 1)) {
//             return 0;
//             } else return 1;
//             } else return 1;




//            //先减少库存
//            if (itemsMapper.update_items(order_details.getWeight(), order_details.getVolume(), order.getItem_id()) != 0) {
//                //设置订单号
//                String frist = "880";
//                LocalDateTime localDateTime = LocalDateTime.now();
//                Date date = new Date();
//                String middle = date.date(localDateTime);
//                int i = (int) (Math.random() * 900 + 100);
//                String myStr = Integer.toString(i);
//                String number = frist + middle + myStr;
//                order.setNumbers(number);
//                //调用第三方支付接口,并返回已支付的金额
//                amount amount = new amount();
//                amount.setItem_id(order.getItem_id());
//                BigDecimal decimal = new BigDecimal(total);
//                amount.setTotal(decimal);
//                //模拟已支付的金额
//                BigDecimal decimal1 = new BigDecimal(2);
//                decimal1 = BigDecimal.valueOf(105.57);
//                amount.setPaid(decimal1);
//                amount.setOpenid(order.getOpenid());
//                if (!order.getTihuo()) {
//                    order.setStatus(4);
//                } else order.setStatus(0);
//                //添加订单
//                if (orderMapper.addorder(order) == 1) {
//                    order_details.setOrder_id(order.getId());
//                    amount.setOrder_id(order.getId());
//                    //更新账单表和更新订单详情表
//                    if ((amountMapper.insert_amount(amount) == 1) && (orderMapper.addorder2(order_details) == 1)) {
//                        return 0;
//                    } else return 1;
//                } else return 1;
//            } else return 3;
//        }

}
