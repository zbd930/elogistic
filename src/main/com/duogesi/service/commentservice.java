package com.duogesi.service;

import com.duogesi.entities.comments;
import com.duogesi.mapper.OrderMapper;
import com.duogesi.mapper.commentsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class commentservice {
    @Autowired
    private commentsMapper commentsMapper;
    @Autowired
    private OrderMapper orderMapper;

    //获取评论
    public synchronized  List<comments> get_comments(int user_id){
        List<comments> comments=commentsMapper.get_comments(user_id);
        for (comments c: comments
             ) {
            String name=c.getAddress().getName();
            c.getAddress().setName(replaceNameX(name));
        }return comments;
    }

    //新增评论
    public boolean insert_comments(comments comments,int order){
        if(commentsMapper.insert_comment(comments)==1){
            orderMapper.pinjie(order);
            return true;
        }else return false;
    }

    private  String replaceNameX(String str){
        String reg = ".{1}";
        StringBuffer sb = new StringBuffer();
        Pattern p = Pattern.compile(reg);
        Matcher m = p.matcher(str);
        int i = 0;
        while(m.find()){
            i++;
            if(i==1)
                continue;
            m.appendReplacement(sb, "*");
        }
        m.appendTail(sb);
        return sb.toString();
    }
}
