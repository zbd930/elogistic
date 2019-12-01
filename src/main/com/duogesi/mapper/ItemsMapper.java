package com.duogesi.mapper;

import com.duogesi.entities.details;
import com.duogesi.entities.items;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.util.List;
import java.util.Set;

@Component
public interface ItemsMapper {

//    获取符合方式所有订单
    List<items> get_items(items _items);


//    AOP前值
    int update_items(@Param("weight") float weight, @Param("volume") float volume, @Param("id") int id);
    //    AOP前值
    details get_para(int id);

//    用户的所有订单
    List<items> get_orders(String openid);
}
