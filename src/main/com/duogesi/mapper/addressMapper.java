package com.duogesi.mapper;

import com.duogesi.beans.address;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface addressMapper {
    int addaddress(address address);

    List<address> get_address(String unionId);

    int delete_address(int id);
}
