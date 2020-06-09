package com.duogesi.mapper;

import com.duogesi.beans.order;
import com.duogesi.beans.order_details;

public interface OrderMapper {


    int addorder(order order);

    int addorder2(order_details order_details);

    //评价后更新状态为5
    int pinjie(int id);

    int delete_order_details(int id);

    int delete_order(int id);
}
