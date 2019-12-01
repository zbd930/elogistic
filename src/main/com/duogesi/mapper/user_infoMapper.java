package com.duogesi.mapper;

import com.duogesi.entities.user_info;
import org.apache.ibatis.annotations.Param;

public interface user_infoMapper {

    int get_like(int user_id);
    int update_like(@Param("post_like") int post_like,@Param("id") int id);

}
