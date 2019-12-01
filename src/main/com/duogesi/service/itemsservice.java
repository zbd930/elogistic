package com.duogesi.service;

import com.duogesi.entities.*;
import com.duogesi.mapper.ItemsMapper;
import com.duogesi.mapper.amountMapper;
import com.duogesi.mapper.priceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class itemsservice {
    @Autowired
    private ItemsMapper itemsMapper;
    @Autowired
    private priceMapper priceMapper;
    @Autowired
    private amountMapper amountMapper;
    @Autowired
    private dianzanservice dianzanservice;


    public List<items> get_items(items _items, Date date){
        //    获取符合方式所有订单
        List<items> list =itemsMapper.get_items(_items);
        Iterator<items> itemsIterator=list.iterator();
        while (itemsIterator.hasNext()){
            items items=itemsIterator.next();
            if( items.getMethod().equals("海派")){
                price_include myprices=priceMapper.get_price1(items.getUser_id(),date,area(_items));
                getprice(itemsIterator, items, myprices);
            }else if ( items.getMethod().equals("空派")) {
                price_include myprices = priceMapper.get_price0(items.getUser_id(), date, area(_items));
                getprice(itemsIterator, items, myprices);
            }else if ( items.getMethod().equals("美森")) {
                price_include myprices = priceMapper.get_price2(items.getUser_id(), date, area(_items));
                getprice(itemsIterator, items, myprices);
            }else if ( items.getMethod().equals("空派(直飞)")) {
                price_include myprices = priceMapper.get_price3(items.getUser_id(), date, area(_items));
                getprice(itemsIterator, items, myprices);
            }else if ( items.getMethod().equals("美森(敏感)")) {
                price_include myprices = priceMapper.get_price4(items.getUser_id(), date, area(_items));
                getprice(itemsIterator, items, myprices);
            }
            else if ( items.getMethod().equals("海卡")){
                price_haika price_haika = priceMapper.get_price_haika(items.getUser_id(), date, _items.getMudigang(), _items.getQiyungang());
                synchronized (items) {
                    try {
                        items.setPrice(price_haika.getPrice());
                    } catch (NullPointerException e) {
                        itemsIterator.remove();
                    }
                }
            }
            items.setMudigang(_items.getMudigang());
            items.setLike(Math.toIntExact(dianzanservice.get_count(String.valueOf(items.getUser_id()))));

        }
        Collections.sort(list, new Comparator<items>() {
            @Override
            public int compare(items u1, items u2) {
                if (u1.getPrice() > u2.getPrice()) {
                    return 1;
                }
                if (u1.getPrice() == u2.getPrice()) {
                    return 0;
                }
                return -1;
            }
        });
        return   list;
    }

    private void getprice(Iterator<items> itemsIterator, items items, price_include myprices) {
        synchronized (items) {
            items.setMyprices(myprices);
            try {
                items.setPrice(myprices.getOne());
            } catch (NullPointerException e) {
                itemsIterator.remove();
            }
        }
    }

    private String area(items _items) {
        if (_items.getMudigang().equals("美东")){
            return "east";
        }else if(_items.getMudigang().equals("美西")){
            return "west";
        }else if(_items.getMudigang().equals("美中")){
            return "middle";
        }
        return null;
    }
    //    获取所有订单
    public List<items> get_orders(String openid) {
         List<items> items=itemsMapper.get_orders(openid);
        for (items i : items
                ) {
            order order=i.getOrders().get(0);
            amount amount=amountMapper.get_amount_byid(order.getId());
                if (amount != null) {
                    synchronized (amount) {
                    amount.setLocal(amount.getTax().add(amount.getCustomer()).add(amount.getInspect()).add(amount.getAdditional()));
                }
                i.setAmount(amount);
            }
        }

        return items;
    }
    //获取当前月份总金额
    public amount get_amount_bymount(String openid){
        // 获取当前年份、月份、日期
        Calendar cale = Calendar.getInstance();
        // 获取当月第一天和最后一天
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String start_data, end_data;
        // 获取前月的第一天
        cale = Calendar.getInstance();
        cale.add(Calendar.MONTH, 0);
        cale.set(Calendar.DAY_OF_MONTH, 1);
        start_data = format.format(cale.getTime());
        // 获取前月的最后一天
        cale = Calendar.getInstance();
        cale.add(Calendar.MONTH, 1);
        cale.set(Calendar.DAY_OF_MONTH, 0);
        end_data = format.format(cale.getTime());
        amount amount= amountMapper.get_amount(start_data,end_data,openid);
        return amount;
    }
}
