package com.duogesi.controller;

import com.duogesi.redisUtils.RedisKeyUtil;
import com.duogesi.service.dianzanservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.NestedServletException;

@Controller
@RequestMapping("/dianzan/")
public class dianzanservlet {

   @Autowired
   private dianzanservice dianzanservice;

    @RequestMapping(value = "add.do",method = RequestMethod.POST)
    @ResponseBody
    public String add(String topicid,String openid){
        try {
            if (dianzanservice.add(topicid, openid)) {
                long shuliang = dianzanservice.get_count(topicid);
                return String.valueOf(shuliang);
            } else return "fail";
        }catch (Exception e){
            e.printStackTrace();
        }return "fail";
        }

    @RequestMapping(value = "delete.do",method = RequestMethod.POST)
    @ResponseBody
    public String delete(String topicid,String openid){
        if (dianzanservice.delete(topicid,openid)){
            return "code:success";
        }else return "code:fail";
    }
}
