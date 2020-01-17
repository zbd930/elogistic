package com.duogesi.controller;

import com.duogesi.entities.comments;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/comments/")
public class commentservlet {
    @Autowired
    private com.duogesi.service.commentservice commentservice;

    //获取
    @RequestMapping(value = "get.do")
    @ResponseBody
    public List<comments> get(int user_id){
        List<comments> list=commentservice.get_comments(user_id);
        return list;
    }
    //提交
    @RequestMapping(value ="insert_comments.do",produces="text/html;charset=UTF-8")
    @ResponseBody
    public String insert(comments comments, int order){
        Boolean result=false;
        try {
            result=commentservice.insert_comments(comments, order);
        }catch (Exception e){
            e.printStackTrace();
        }
        if(result) {
            return "success";
        }else return "fail";


    }

}
