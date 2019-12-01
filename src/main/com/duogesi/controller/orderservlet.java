package com.duogesi.controller;

import com.duogesi.entities.order;
import com.duogesi.entities.order_details;
import com.duogesi.service.orderservice;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;

@Controller
@RequestMapping("/order/")
public class orderservlet {

    @Autowired
    private orderservice orderservice;


    @RequestMapping(value = "make.do",produces="text/html;charset=UTF-8")
    @ResponseBody
    public String order(String total, order order, order_details order_details){
        int i=0;
        try {
            i=orderservice.add(order,total,order_details);
        }catch (Exception e){
            e.printStackTrace();
        }
        switch (i){
            case (0):return "下单成功";
            case (1):return "订单添加有误";
            case (2):return "更新仓位情况有误";
            case (3):return "仓位不够";
            case (4):return "更新状态有误";
        }
        return null;
    }
}
