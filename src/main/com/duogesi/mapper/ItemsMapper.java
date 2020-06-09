package com.duogesi.mapper;

import com.duogesi.beans.details;
import com.duogesi.beans.items;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface ItemsMapper {

    //    获取符合方式所有订单
    List<items> get_items(items _item);

    //    获取符合方式所有订单加拿大
    List<items> get_items_Canada(items _item);

    //    获取符合方式所有订单欧线
    List<items> get_items_Europe(items _item);

    //    获取符合方式所有订单日本
    List<items> get_items_Japan(items _item);

    //限时秒杀
    List<items> ger_miaosha(String date);

    //    AOP前值
    int update_items(@Param("weight") float weight, @Param("volume") float volume, @Param("id") int id);

    //    AOP前值
    int update_items1(@Param("weight") float weight, @Param("volume") float volume, @Param("id") int id);

    //    AOP前值
    details get_para(int id);

    //    用户的所有订单
    List<items> get_orders(String unionId);

    int return_items(@Param("weight") float weight, @Param("volume") float volume, @Param("id") int id);
    //     根据shipid获取发货人信息

    items get_supplier_info(int item_id);

}
