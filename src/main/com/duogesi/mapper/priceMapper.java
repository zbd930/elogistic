package com.duogesi.mapper;

import com.duogesi.beans.price;
import com.duogesi.beans.price_haika;
import com.duogesi.beans.price_include;
import org.apache.ibatis.annotations.Param;

import java.sql.Date;

public interface priceMapper {

    price get_price_haipai(@Param("user_id") int user_id, @Param("date") Date date);

    price get_price_kongpai(@Param("user_id") int user_id, @Param("date") Date date);

    price_haika get_price_haika(@Param("user_id") int user_id, @Param("date") Date date, @Param("mudigang") String mudigang, @Param("qiyungang") String qiyungang);

    price_include get_price0(@Param("user_id") int user_id, @Param("date") Date date, @Param("area") String area);

    price_include get_price1(@Param("user_id") int user_id, @Param("date") Date date, @Param("area") String area);

    price_include get_price2(@Param("user_id") int user_id, @Param("date") Date date, @Param("area") String area);

    price_include get_price3(@Param("user_id") int user_id, @Param("date") Date date, @Param("area") String area);

    price_include get_price4(@Param("user_id") int user_id, @Param("date") Date date, @Param("area") String area);

}
