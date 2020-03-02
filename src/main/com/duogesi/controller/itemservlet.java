package com.duogesi.controller;

import com.duogesi.entities.amount;
import com.duogesi.entities.items;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/items/")
public class itemservlet {
    @Autowired
    private com.duogesi.service.itemsservice itemsservice;

//获取所有
    @RequestMapping(value = "get_items.do")
    @ResponseBody
    public List<items> get(HttpServletRequest request, items _items, String category){
        try {
            List<items> list = itemsservice.get_items(_items,category);
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
        amount amount=new amount();
        try {
            amount=itemsservice.get_amount_bymount(openid);
        }catch (Exception e){
            e.printStackTrace();
        }
        return amount;
    }

    //特价专业
    @RequestMapping("redis_get.do")
    @ResponseBody
    public List<items> get_redis(){
        List<items> list=new ArrayList<>();
        try {
            list=itemsservice.redis_get();
        }catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }
}
