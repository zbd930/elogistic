package com.duogesi.service;

import com.duogesi.entities.address;
import com.duogesi.mapper.addressMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class addressservice {
    @Autowired
    private addressMapper addressMapper;


    public boolean add(address address) {
            if(addressMapper.addaddress(address)!=0){
                return true;
            }else return false;
    }
    public List<address> get(String openid){
        return addressMapper.get_address(openid);
    }

    public int delete_address(int id){
        return addressMapper.delete_address(id);
    }
}
