package com.duogesi.service;

import com.duogesi.Utils.Date;
import com.duogesi.entities.amount;
import com.duogesi.Utils.Swtich;
import com.duogesi.entities.details;
import com.duogesi.entities.order;
import com.duogesi.entities.order_details;
import com.duogesi.mapper.ItemsMapper;
import com.duogesi.mapper.OrderMapper;
import com.duogesi.mapper.amountMapper;
import com.duogesi.mapper.user_infoMapper;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Service
public class orderservice {
    @Autowired
    private ItemsMapper itemsMapper;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private amountMapper amountMapper;


    //  0是成功，1是订单添加有误，
    public int add(order order,String total, order_details order_details) {
        //先减少库存
        if(itemsMapper.update_items(order_details.getWeight(),order_details.getVolume(),order.getItem_id())!=0) {
            //设置订单号
                String frist = "880";
                LocalDateTime localDateTime = LocalDateTime.now();
                Date date=new Date();
                String middle = date.date(localDateTime);
                int i = (int) (Math.random() * 900 + 100);
                String myStr = Integer.toString(i);
                String number = frist + middle + myStr;
                order.setNumbers(number);

            //调用第三方支付接口,并返回已支付的金额
            amount amount=new amount();
            amount.setItem_id(order.getItem_id());
            BigDecimal decimal=new BigDecimal(total);
            amount.setTotal(decimal);
            //模拟已支付的金额
            BigDecimal decimal1=new BigDecimal(2);
            decimal1= BigDecimal.valueOf(105.57);
            amount.setPaid(decimal1);
            amount.setOpenid(order.getOpenid());
            if (!order.getTihuo()) {
                order.setStatus(4);
            } else order.setStatus(0);
            try {
                Swtich s=new Swtich();
                order.setChaigui(s.switch_mudigang_fan(order.getChaigui()));
                //添加订单

                if (orderMapper.addorder(order) == 1) {
                    order_details.setOrder_id(order.getId());
                    amount.setOrder_id(order.getId());
                    if((amountMapper.insert_amount(amount)==1)&&(orderMapper.addorder2(order_details)==1)) {
                        return 0;
                    }else return 1;
                } else return 1;
            } catch (Exception e){
                e.printStackTrace();
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            } return 1;
        }else return 1;
    }

}
