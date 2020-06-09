package com.duogesi.mapper;

import com.duogesi.beans.addition;
import org.apache.ibatis.annotations.Param;

public interface additionMapper {

    //添加附加费
    int add_price(addition addition);

    //获取附加费
    addition get_price(@Param("category") String category, @Param("user_id") int user_id);


}
