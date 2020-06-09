package com.duogesi.controller;

import com.duogesi.beans.order;
import com.duogesi.beans.order_details;
import com.duogesi.service.orderservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/order/")
public class orderservlet {

    @Autowired
    private orderservice orderservice;

    //支付成功后通知添加订单
    @RequestMapping("update.do")
    public void update(HttpServletRequest request, HttpServletResponse response) {
        orderservice.add_order(request, response);
    }

    //支付成功后通知添加订单
    @RequestMapping("update_xiaobao.do")
    public void update_xiaobao(HttpServletRequest request, HttpServletResponse response) {
        try {
            orderservice.add_order_xiaobao(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "make.do", produces = "text/html;charset=UTF-8")
    @ResponseBody
    //这个是接到请求后的第一个流程
    public String order(String total, order order, order_details order_details, HttpServletResponse response, HttpServletRequest request) {
        int i = 0;
        try {
            i = orderservice.add(order, total, order_details, request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
        switch (i) {
            case (0):
                return "下单成功";
            case (1):
                return "订单添加有误";
            case (2):
                return "更新仓位情况有误";
            case (3):
                return "仓位不够";
            case (4):
                return "更新状态有误";
        }
        return null;
    }

    @RequestMapping(value = "make_redis.do", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String make_redis(String total, order order, order_details order_details) {
        int i = 0;
        try {
            i = orderservice.add_redis(order, total, order_details);
        } catch (Exception e) {
            e.printStackTrace();
        }
        switch (i) {
            case (0):
                return "下单成功";
            case (1):
                return "订单添加有误";
            case (2):
                return "更新仓位情况有误";
            case (3):
                return "仓位不够";
            case (4):
                return "更新状态有误";
        }
        return null;
    }

//    //支付成功后删除redis购物车
//    @RequestMapping("delete_gouwuche.do",produces="text/html;charset=UTF-8")
//    public void delete_redis(){
//
//    }

}
