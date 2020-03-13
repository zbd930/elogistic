package com.duogesi.controller;

import com.duogesi.entities.order;
import com.duogesi.entities.order_details;
import com.duogesi.service.wechatservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Controller
@RequestMapping("/wechat/")
public class wechatservlet {

    @Autowired
    private wechatservice wechatservice;

    @RequestMapping("getopenid.do")
    @ResponseBody
    public String getopenid(HttpServletRequest request,HttpServletResponse response) throws IOException {
        String code = request.getParameter("code");   //拿到微信小程序传过来的code
        String results = wechatservice.getopenid(request, code);
        response.setContentType("application/json;charset=UTF-8");
        response.setHeader("catch-control", "no-catch");
        PrintWriter out = response.getWriter();
        out.write(results);
        out.flush();
        out.close();
        return null;
    }

    //普通下单
    @RequestMapping("pay.do")
    @ResponseBody
    //orderservlet判断仓位情况后，如果有仓位，再下单
    public String pay(HttpServletRequest request, HttpServletResponse response, order order, order_details order_details,String method,String country){
        String result="";
        try {
            result=wechatservice.wechat_pay(request,response,order,order_details, method,country);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
    //秒杀下单
    @RequestMapping("pay_miaosha.do")
    @ResponseBody
    public String pay_miaosha(HttpServletRequest request, HttpServletResponse response, order order, order_details order_details){
        String result="";
        try {
            result=wechatservice.wechat_pay1(request,response,order,order_details);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
