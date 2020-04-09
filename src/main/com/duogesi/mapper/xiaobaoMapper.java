package com.duogesi.mapper;

import com.duogesi.entities.price_xiaobao;
import com.duogesi.entities.xiaobao;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface xiaobaoMapper {

    //先获取渠道
    List<xiaobao> get_channel_xiaobao(@Param("country") String country);

    //获取小包价格
    List<price_xiaobao> get_price_xiaobao(int xiaobao_id);
}
