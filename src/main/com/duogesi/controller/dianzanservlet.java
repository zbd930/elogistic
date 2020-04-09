package com.duogesi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/dianzan/")
public class dianzanservlet {

   @Autowired
   private com.duogesi.service.dianzanservice dianzanservice;

    @RequestMapping(value = "add.do",method = RequestMethod.POST)
    @ResponseBody
    public String add(String topicid,String unionId){
        try {
            long shuliang = dianzanservice.get_count(topicid);
            String resultes=String.valueOf(shuliang);
            if (Integer.valueOf(resultes)%10==0) {

            }
            if (dianzanservice.add(topicid, unionId)) {
                return resultes;
            } else return "fail";
        }catch (Exception e){
            e.printStackTrace();
        }return "fail";
        }

    @RequestMapping(value = "delete.do",method = RequestMethod.POST)
    @ResponseBody
    public String delete(String topicid,String unionId){
        if (dianzanservice.delete(topicid,unionId)){
            return "code:success";
        }else return "code:fail";
    }
}
