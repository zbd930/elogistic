package com.duogesi.controller;

import com.duogesi.beans.order;
import com.duogesi.beans.order_details;
import com.duogesi.service.wechatservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Controller
@RequestMapping("/wechat/")
public class wechatservlet {

    @Autowired
    private wechatservice wechatservice;

    @RequestMapping(value = "getunionId.do",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map getopenid(HttpServletRequest request, HttpServletResponse response, String encryptedData, String iv,
                         String code) throws IOException {
        //拿到微信小程序传过来的code、iv、encryptedData
        Map results = wechatservice.getSessionKeyOrOpenId(code, iv, encryptedData);
        return results;
    }

//    //普通下单
    @RequestMapping("pay.do")
    @ResponseBody
    //orderservlet判断仓位情况后，如果有仓位，再下单
    public String pay(HttpServletRequest request, HttpServletResponse response, order order, order_details order_details, String method, String country) {
        String result = "";
        try {
            result = wechatservice.wechat_pay(request, response, order, order_details, method, country);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    //秒杀下单
    @RequestMapping("pay_miaosha.do")
    @ResponseBody
    public String pay_miaosha(HttpServletRequest request, HttpServletResponse response, order order, order_details order_details) {
        String result = "";
        try {
            result = wechatservice.wechat_pay1(request, response, order, order_details);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
