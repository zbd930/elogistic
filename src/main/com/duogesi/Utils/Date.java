package com.duogesi.Utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Date {
    public String getdate_chn(LocalDateTime localDateTime){
        //创建一个日期对象
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH时mm分ss秒");
        //下面两种方式都可以用来格式化成想要的格式
        String string = localDateTime.format(formatter);
        return string;
    }
    public String getdate_eng(LocalDateTime localDateTime){
        //创建一个日期对象
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm-ss");
        //下面两种方式都可以用来格式化成想要的格式
        String string = localDateTime.format(formatter);
        return string;
    }
    public String getdate(LocalDateTime localDateTime){
        //创建一个日期对象
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        //下面两种方式都可以用来格式化成想要的格式
        String string = localDateTime.format(formatter);
        return string;
    }
    public String date(LocalDateTime localDateTime){
        //创建一个日期对象
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMdd");
        //下面两种方式都可以用来格式化成想要的格式
        String string = localDateTime.format(formatter);
        return string;
    }
}
