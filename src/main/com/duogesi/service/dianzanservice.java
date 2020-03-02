package com.duogesi.service;

import com.duogesi.mapper.user_infoMapper;
import com.duogesi.redisUtils.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class dianzanservice {

    @Autowired
    private RedisKeyUtil redisKeyUtil;
    @Autowired
    private user_infoMapper user_infoMapper;

    public long get_count(String topid){
        return redisKeyUtil.getCount(topid);
    }

    public Boolean add(String topicid,String appid){
          redisKeyUtil.dianZan(topicid, appid, true);
            return true;
    }

    public Boolean delete(String topicid,String appid){
            redisKeyUtil.dianZanx(topicid,appid, true);
            return true;

    }

}
