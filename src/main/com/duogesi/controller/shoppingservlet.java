package com.duogesi.controller;

import com.duogesi.entities.items;
import com.duogesi.entities.shopping;
import com.duogesi.service.shoppingservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/shopping/")
public class shoppingservlet {
    @Autowired
    private com.duogesi.service.shoppingservice shoppingservice;

    //购物车
    @RequestMapping(value = "redis_get.do",method = RequestMethod.GET)
    @ResponseBody
    public List get_redis(String openid){
        List list=new ArrayList();
        try {
            list=shoppingservice.get_shopping(openid);
        }catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }

    //秒杀支付失败
    @RequestMapping(value = "return.do",method = RequestMethod.POST)
    @ResponseBody
    public String return1(float weight,float volume, int item_id,String numbers){
        shoppingservice.return1(weight,volume,item_id,numbers);
        return "success";
    }
}
