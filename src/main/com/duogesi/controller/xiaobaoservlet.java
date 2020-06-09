package com.duogesi.controller;

import com.duogesi.beans.order;
import com.duogesi.beans.order_details;
import com.duogesi.service.xiaobaoservice;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("/xiaobao/")
public class xiaobaoservlet {

    @Autowired
    private xiaobaoservice xiaobaoservice;
    private static Logger log = Logger.getLogger(xiaobaoservlet.class);


    @RequestMapping("pay_xiaobao.do")
    @ResponseBody
    public String pay(HttpServletRequest request, HttpServletResponse response, order order, order_details order_details, String method, String country, int supplier_id) {
        String result = "";
        try {
            result = xiaobaoservice.wechat_pay(request, response, order, order_details, method, country, supplier_id);
        } catch (IOException E) {
            log.error(E.getMessage(), E);
        }
        return result;
    }

}
