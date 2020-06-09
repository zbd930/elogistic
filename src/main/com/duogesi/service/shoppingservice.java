package com.duogesi.service;

import com.duogesi.Utils.RedisUtil;
import com.duogesi.mapper.ItemsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class shoppingservice {
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private ItemsMapper itemsMapper;

    public List get_shopping(String unionId) {
        List list = new ArrayList();
        Set set = redisUtil.sGet(unionId);
        String[] number = (String[]) set.toArray(new String[set.size()]);
        for (int i = 0; i < number.length; i++) {
            Map map = redisUtil.hmget(String.valueOf(number[i]));
            list.add(map);
        }
        return list;

    }

    //抢购时失败调用的函数
    public void return1(float weight, float volume, int item_id, String number) {
        if (itemsMapper.return_items(weight, volume, item_id) == 1) {
            redisUtil.hincr(String.valueOf(item_id), "weight", weight);
            redisUtil.hincr(String.valueOf(item_id), "volume", volume);
            redisUtil.del(number);
        }
    }
}
