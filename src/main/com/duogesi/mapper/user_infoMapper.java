package com.duogesi.mapper;

import com.duogesi.entities.supplier_company;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface user_infoMapper {

    int get_like(int user_id);
    int update_like(@Param("post_like") int post_like, @Param("id") int id);

    List<supplier_company> get_supplier_info(int item_id);
}
