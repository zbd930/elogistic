package com.duogesi.mapper;

import com.duogesi.entities.order;
import com.duogesi.entities.order_details;

public interface OrderMapper {

    int addorder(order order);
    int addorder2(order_details order_details);
    //评价后更新状态为5
    int pinjie(int id);

}
