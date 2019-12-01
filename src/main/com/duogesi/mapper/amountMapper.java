package com.duogesi.mapper;

import com.duogesi.entities.amount;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

public interface amountMapper {

    //<!--插入金额-->
    int insert_amount(amount amount);
    //获取某个订单的金额
    amount get_amount_byid(@Param("order_id") int order_id);

   //获取本月的金额
   amount get_amount(@Param("start_data") String start_data,@Param("end_data") String end_data,@Param("openid")String openid);


}
