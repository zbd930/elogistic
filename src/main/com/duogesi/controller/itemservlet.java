package com.duogesi.controller;

import com.duogesi.entities.amount;
import com.duogesi.entities.items;
import com.duogesi.service.itemsservice;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

@Controller
@RequestMapping("/items/")
public class itemservlet {
    @Autowired
    private itemsservice itemsservice;

//获取所有
    @RequestMapping(value = "get_items.do")
    @ResponseBody
    public List<items> get(HttpServletRequest request, Date date,items _items){
        try {
            List<items> list = itemsservice.get_items(_items, date);
            return list;
        }catch (Exception e){
            e.printStackTrace();
        }
       return null;
    }

//    获取所有订单
    @RequestMapping(value = "orders.do")
    @ResponseBody
    public List<items> get_order(String openid){
        try{
        return  itemsservice.get_orders(openid);
    }catch (Exception e){
        e.printStackTrace();
        }return null;
    }
    //获取当前月份总金额
    @RequestMapping(value = "get.do")
    @ResponseBody
    public amount get_order1(String openid){
        return itemsservice.get_amount_bymount(openid);

    }
}
