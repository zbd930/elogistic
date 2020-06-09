package com.duogesi.mapper;

import com.duogesi.beans.supplier_company;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface user_infoMapper {

    int get_like(int user_id);

    int update_like(@Param("post_like") int post_like, @Param("id") int id);

    List<supplier_company> get_company_name(int user_id);

    //获取公司id
    String get_company_email(int supplier_id);
}
