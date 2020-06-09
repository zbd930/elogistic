package com.duogesi.Utils;

import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;

@Component
public class getOrderExpireTime {
    /**
     * 设置微信二维码失效时间，并返回具体失效的时间点
     *
     * @param expire 二维码的有效时间，单位是毫秒
     * @return
     */
    public static String getOrderExpireTime(Long expire) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        LocalDateTime localDateTime = LocalDateTime.now();
        java.sql.Date afterDate = new java.sql.Date(localDateTime.getSecond() + expire);
        return sdf.format(afterDate);
    }
}
