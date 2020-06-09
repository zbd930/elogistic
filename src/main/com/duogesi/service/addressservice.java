package com.duogesi.service;

import com.duogesi.beans.address;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class addressservice {
    @Autowired
    private com.duogesi.mapper.addressMapper addressMapper;


    public boolean add(address address) {
        if (addressMapper.addaddress(address) != 0) {
            return true;
        } else return false;
    }

    public List<address> get(String unionId) {
        return addressMapper.get_address(unionId);
    }

    public int delete_address(int id) {
        return addressMapper.delete_address(id);
    }
}
