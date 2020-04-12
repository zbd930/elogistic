package com.duogesi.service;

import com.duogesi.Mail.RedisUtil;
import com.duogesi.Utils.Date;
import com.duogesi.Utils.SerializeUtil;
import com.duogesi.Utils.Swtich;
import com.duogesi.entities.*;
import com.duogesi.mapper.ItemsMapper;
import com.duogesi.mapper.user_infoMapper;
import com.duogesi.mapper.xiaobaoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class itemsservice {
    @Autowired
    private ItemsMapper itemsMapper;
    @Autowired
    private com.duogesi.mapper.priceMapper priceMapper;
    @Autowired
    private com.duogesi.mapper.amountMapper amountMapper;
    @Autowired
    private com.duogesi.service.dianzanservice dianzanservice;
    @Autowired
    private com.duogesi.mapper.additionMapper additionMapper;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private xiaobaoMapper xiaobaoMapper;
    @Autowired
    private user_infoMapper user_infoMapper;


    public List<items> get_items(items _items, String category) {
//        java.util.Date date= new java.util.Date();
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
//        String date1=format.format(date.getTime());
//        System.out.println(date1);
        //    获取符合方式所有订单
        String mudigang =_items.getMudigang();
        String country=_items.getCountry();
        String method=_items.getMethod();
        if (!method.equals("海卡")) {
            if (country.equals("美国")) {
                List<items> list = itemsMapper.get_items(_items);
                Iterator<items> itemsIterator = list.iterator();
                while (itemsIterator.hasNext()) {
                    items items = itemsIterator.next();
                    if (items.getMethod().equals("海派")) {
                        price_include myprices = priceMapper.get_price1(items.getUser_id(), items.getEtd(), area(_items));
                        getprice(itemsIterator, items, myprices);
                        //获取附加费
                        addition addition = additionMapper.get_price(category, items.getUser_id());
                        if (addition == null) {
                            addition addition1 = new addition();
                            addition1.setPrice(0);
                            items.setAddition(addition1);
                        } else items.setAddition(addition);
                    } else if (items.getMethod().equals("空派")) {
                        price_include myprices = priceMapper.get_price0(items.getUser_id(), items.getEtd(), area(_items));
                        getprice(itemsIterator, items, myprices);
                        //获取附加费
                        addition addition = additionMapper.get_price(category, items.getUser_id());
                        if (addition == null) {
                            addition addition1 = new addition();
                            addition1.setPrice(0);
                            items.setAddition(addition1);
                        } else items.setAddition(addition);
                    } else if (items.getMethod().equals("美森")) {
                        price_include myprices = priceMapper.get_price2(items.getUser_id(), items.getEtd(), area(_items));
                        getprice(itemsIterator, items, myprices);
                        //获取附加费
                        addition addition = additionMapper.get_price(category, items.getUser_id());
                        if (addition == null) {
                            addition addition1 = new addition();
                            addition1.setPrice(0);
                            items.setAddition(addition1);
                        } else items.setAddition(addition);
                    } else if (items.getMethod().equals("空派(直飞)")) {
                        price_include myprices = priceMapper.get_price3(items.getUser_id(), items.getEtd(), area(_items));
                        getprice(itemsIterator, items, myprices);
                        //获取附加费
                        addition addition = additionMapper.get_price(category, items.getUser_id());
                        if (addition == null) {
                            addition addition1 = new addition();
                            addition1.setPrice(0);
                            items.setAddition(addition1);
                        } else items.setAddition(addition);
                    } else if (items.getMethod().equals("空派(带电)")) {
                        //去搜索价格表
                        price_include myprices = priceMapper.get_price4(items.getUser_id(), items.getEtd(), area(_items));
                        getprice(itemsIterator, items, myprices);
                        //获取附加费
                        addition addition = additionMapper.get_price(category, items.getUser_id());
                        if (addition == null) {
                            addition addition1 = new addition();
                            addition1.setPrice(0);
                            items.setAddition(addition1);
                        } else items.setAddition(addition);
                    }
                    items.setMudigang(mudigang);
                    items.setLike(Math.toIntExact(dianzanservice.get_count(String.valueOf(items.getUser_id()))));
                }
                if (list.size() > 1) {
                    Collections.sort(list, new Comparator<items>() {
                        @Override
                        public int compare(items u1, items u2) {
                            if (u1.getEtd().before(u2.getEtd())) {
                                return -1;
                            }
                            if (u1.getEtd() == u2.getEtd()) {
                                return 0;
                            }
                            return 1;
                        }
                    });
                    return list;
                } else return list;
                //除了加拿大
            } else if (country.equals("加拿大")) {
                List<items> list = itemsMapper.get_items_Canada(_items);
                Iterator<items> itemsIterator = list.iterator();
                while (itemsIterator.hasNext()) {
                    items items = itemsIterator.next();
                    if (items.getMethod().equals("海派")) {
                        price_include myprices = priceMapper.get_price1(items.getUser_id(), items.getEtd(), area1(mudigang));
                        getprice(itemsIterator, items, myprices);
                        //获取附加费
                        addition addition = additionMapper.get_price(category, items.getUser_id());
                        if (addition == null) {
                            addition addition1 = new addition();
                            addition1.setPrice(0);
                            items.setAddition(addition1);
                        } else items.setAddition(addition);
                    } else if (items.getMethod().equals("空派")) {
                        price_include myprices = priceMapper.get_price0(items.getUser_id(), items.getEtd(), area1(mudigang));
                        getprice(itemsIterator, items, myprices);
                        //获取附加费
                        addition addition = additionMapper.get_price(category, items.getUser_id());
                        if (addition == null) {
                            addition addition1 = new addition();
                            addition1.setPrice(0);
                            items.setAddition(addition1);
                        } else items.setAddition(addition);
                    } else if (items.getMethod().equals("美森")) {
                        price_include myprices = priceMapper.get_price2(items.getUser_id(), items.getEtd(), area1(mudigang));
                        getprice(itemsIterator, items, myprices);
                        //获取附加费
                        addition addition = additionMapper.get_price(category, items.getUser_id());
                        if (addition == null) {
                            addition addition1 = new addition();
                            addition1.setPrice(0);
                            items.setAddition(addition1);
                        } else items.setAddition(addition);
                    } else if (items.getMethod().equals("空派(直飞)")) {
                        price_include myprices = priceMapper.get_price3(items.getUser_id(), items.getEtd(), area1(mudigang));
                        getprice(itemsIterator, items, myprices);
                        //获取附加费
                        addition addition = additionMapper.get_price(category, items.getUser_id());
                        if (addition == null) {
                            addition addition1 = new addition();
                            addition1.setPrice(0);
                            items.setAddition(addition1);
                        } else items.setAddition(addition);
                    } else if (items.getMethod().equals("空派(带电)")) {
                        //去搜索价格表
                        price_include myprices = priceMapper.get_price4(items.getUser_id(), items.getEtd(), area1(mudigang));
                        getprice(itemsIterator, items, myprices);
                        //获取附加费
                        addition addition = additionMapper.get_price(category, items.getUser_id());
                        if (addition == null) {
                            addition addition1 = new addition();
                            addition1.setPrice(0);
                            items.setAddition(addition1);
                        } else items.setAddition(addition);
                    }
                    items.setMudigang(mudigang);
                    items.setLike(Math.toIntExact(dianzanservice.get_count(String.valueOf(items.getUser_id()))));
                }
                if (list.size() > 1) {
                    Collections.sort(list, new Comparator<items>() {
                        @Override
                        public int compare(items u1, items u2) {
                            if (u1.getEtd().before(u2.getEtd())) {
                                return -1;
                            }
                            if (u1.getEtd() == u2.getEtd()) {
                                return 0;
                            }
                            return 1;
                        }
                    });
                    return list;
                } else return list;
                //日本
            } else if (country.equals("亚太")) {
                List<items> list = itemsMapper.get_items_Japan(_items);
                Iterator<items> itemsIterator = list.iterator();
                while (itemsIterator.hasNext()) {
                    items items = itemsIterator.next();
                    if (items.getMethod().equals("海派")) {
                        price_include myprices = priceMapper.get_price1(items.getUser_id(), items.getEtd(), area1(mudigang));
                        getprice(itemsIterator, items, myprices);
                        //获取附加费
                        addition addition = additionMapper.get_price(category, items.getUser_id());
                        if (addition == null) {
                            addition addition1 = new addition();
                            addition1.setPrice(0);
                            items.setAddition(addition1);
                        } else items.setAddition(addition);
                    } else if (items.getMethod().equals("空派")) {
                        price_include myprices = priceMapper.get_price0(items.getUser_id(), items.getEtd(), area1(mudigang));
                        getprice(itemsIterator, items, myprices);
                        //获取附加费
                        addition addition = additionMapper.get_price(category, items.getUser_id());
                        if (addition == null) {
                            addition addition1 = new addition();
                            addition1.setPrice(0);
                            items.setAddition(addition1);
                        } else items.setAddition(addition);
                    } else if (items.getMethod().equals("美森")) {
                        price_include myprices = priceMapper.get_price2(items.getUser_id(), items.getEtd(), area1(mudigang));
                        getprice(itemsIterator, items, myprices);
                        //获取附加费
                        addition addition = additionMapper.get_price(category, items.getUser_id());
                        if (addition == null) {
                            addition addition1 = new addition();
                            addition1.setPrice(0);
                            items.setAddition(addition1);
                        } else items.setAddition(addition);
                    } else if (items.getMethod().equals("空派(直飞)")) {
                        price_include myprices = priceMapper.get_price3(items.getUser_id(), items.getEtd(), area1(mudigang));
                        getprice(itemsIterator, items, myprices);
                        //获取附加费
                        addition addition = additionMapper.get_price(category, items.getUser_id());
                        if (addition == null) {
                            addition addition1 = new addition();
                            addition1.setPrice(0);
                            items.setAddition(addition1);
                        } else items.setAddition(addition);
                    } else if (items.getMethod().equals("空派(带电)")) {
                        //去搜索价格表
                        price_include myprices = priceMapper.get_price4(items.getUser_id(), items.getEtd(), area1(mudigang));
                        getprice(itemsIterator, items, myprices);
                        //获取附加费
                        addition addition = additionMapper.get_price(category, items.getUser_id());
                        if (addition == null) {
                            addition addition1 = new addition();
                            addition1.setPrice(0);
                            items.setAddition(addition1);
                        } else items.setAddition(addition);
                    }
                    items.setMudigang(mudigang);
                    items.setLike(Math.toIntExact(dianzanservice.get_count(String.valueOf(items.getUser_id()))));
                }
                if (list.size() > 1) {
                    Collections.sort(list, new Comparator<items>() {
                        @Override
                        public int compare(items u1, items u2) {
                            if (u1.getEtd().before(u2.getEtd())) {
                                return -1;
                            }
                            if (u1.getEtd() == u2.getEtd()) {
                                return 0;
                            }
                            return 1;
                        }
                    });
                    return list;
                } else return list;
            } else {
                List<items> list = itemsMapper.get_items_Europe(_items);
                Iterator<items> itemsIterator = list.iterator();
                while (itemsIterator.hasNext()) {
                    items items = itemsIterator.next();
                    if (items.getMethod().equals("海派")) {
                        price_include myprices = priceMapper.get_price1(items.getUser_id(), items.getEtd(), area2(mudigang));
                        getprice(itemsIterator, items, myprices);
                        //获取附加费
                        addition addition = additionMapper.get_price(category, items.getUser_id());
                        if (addition == null) {
                            addition addition1 = new addition();
                            addition1.setPrice(0);
                            items.setAddition(addition1);
                        } else items.setAddition(addition);
                    } else if (items.getMethod().equals("空派")) {
                        price_include myprices = priceMapper.get_price0(items.getUser_id(), items.getEtd(), area2(mudigang));
                        getprice(itemsIterator, items, myprices);
                        //获取附加费
                        addition addition = additionMapper.get_price(category, items.getUser_id());
                        if (addition == null) {
                            addition addition1 = new addition();
                            addition1.setPrice(0);
                            items.setAddition(addition1);
                        } else items.setAddition(addition);
                    } else if (items.getMethod().equals("美森")) {
                        price_include myprices = priceMapper.get_price2(items.getUser_id(), items.getEtd(), area2(mudigang));
                        getprice(itemsIterator, items, myprices);
                        //获取附加费
                        addition addition = additionMapper.get_price(category, items.getUser_id());
                        if (addition == null) {
                            addition addition1 = new addition();
                            addition1.setPrice(0);
                            items.setAddition(addition1);
                        } else items.setAddition(addition);
                    } else if (items.getMethod().equals("空派(直飞)")) {
                        price_include myprices = priceMapper.get_price3(items.getUser_id(), items.getEtd(), area2(mudigang));
                        getprice(itemsIterator, items, myprices);
                        //获取附加费
                        addition addition = additionMapper.get_price(category, items.getUser_id());
                        if (addition == null) {
                            addition addition1 = new addition();
                            addition1.setPrice(0);
                            items.setAddition(addition1);
                        } else items.setAddition(addition);
                    } else if (items.getMethod().equals("空派(带电)")) {
                        //去搜索价格表
                        price_include myprices = priceMapper.get_price4(items.getUser_id(), items.getEtd(), area2(mudigang));
                        getprice(itemsIterator, items, myprices);
                        //获取附加费
                        addition addition = additionMapper.get_price(category, items.getUser_id());
                        if (addition == null) {
                            addition addition1 = new addition();
                            addition1.setPrice(0);
                            items.setAddition(addition1);
                        } else items.setAddition(addition);
                    }
                    items.setMudigang(mudigang);
                    items.setLike(Math.toIntExact(dianzanservice.get_count(String.valueOf(items.getUser_id()))));
                }
                if (list.size() > 1) {
                    Collections.sort(list, new Comparator<items>() {
                        @Override
                        public int compare(items u1, items u2) {
                            if (u1.getEtd().before(u2.getEtd())) {
                                return -1;
                            }
                            if (u1.getEtd() == u2.getEtd()) {
                                return 0;
                            }
                            return 1;
                        }
                    });
                    return list;
                } else return list;
            }
        }
        else {
            if (country.equals("美国")) {
                List<items> list = itemsMapper.get_items(_items);
                Iterator<items> itemsIterator = list.iterator();
                while (itemsIterator.hasNext()) {
                    items items = itemsIterator.next();
                //去搜索价格表
                price_haika price_haika = priceMapper.get_price_haika(items.getUser_id(), items.getEtd(), _items.getMudigang(), _items.getQiyungang());
                synchronized (items) {
                    try {
                        items.setPrice(price_haika.getPrice());
                    } catch (NullPointerException e) {
                        itemsIterator.remove();
                    }
                }
                    items.setMudigang(mudigang);
                    items.setLike(Math.toIntExact(dianzanservice.get_count(String.valueOf(items.getUser_id()))));
                }
                if (list.size() > 1) {
                    Collections.sort(list, new Comparator<items>() {
                        @Override
                        public int compare(items u1, items u2) {
                            if (u1.getEtd().before(u2.getEtd())) {
                                return -1;
                            }
                            if (u1.getEtd() == u2.getEtd()) {
                                return 0;
                            }
                            return 1;
                        }
                    });
                    return list;
                } else return list;
            }else if(country.equals("加拿大")){
                List<items> list = itemsMapper.get_items_Canada(_items);
                Iterator<items> itemsIterator = list.iterator();
                while (itemsIterator.hasNext()) {
                    items items = itemsIterator.next();
                    //去搜索价格表
                    price_haika price_haika = priceMapper.get_price_haika(items.getUser_id(), items.getEtd(), _items.getMudigang(), _items.getQiyungang());
                    synchronized (items) {
                        try {
                            items.setPrice(price_haika.getPrice());
                        } catch (NullPointerException e) {
                            itemsIterator.remove();
                        }
                    }
                    items.setMudigang(mudigang);
                    items.setLike(Math.toIntExact(dianzanservice.get_count(String.valueOf(items.getUser_id()))));
                }
                if (list.size() > 1) {
                    Collections.sort(list, new Comparator<items>() {
                        @Override
                        public int compare(items u1, items u2) {
                            if (u1.getEtd().before(u2.getEtd())) {
                                return -1;
                            }
                            if (u1.getEtd() == u2.getEtd()) {
                                return 0;
                            }
                            return 1;
                        }
                    });
                    return list;
                } else return list;
            }else if(country.equals("欧洲")){
                List<items> list = itemsMapper.get_items_Europe(_items);
                Iterator<items> itemsIterator = list.iterator();
                while (itemsIterator.hasNext()) {
                    items items = itemsIterator.next();
                    //去搜索价格表
                    price_haika price_haika = priceMapper.get_price_haika(items.getUser_id(), items.getEtd(), _items.getMudigang(), _items.getQiyungang());
                    synchronized (items) {
                        try {
                            items.setPrice(price_haika.getPrice());
                        } catch (NullPointerException e) {
                            itemsIterator.remove();
                        }
                    }
                    items.setMudigang(mudigang);
                    items.setLike(Math.toIntExact(dianzanservice.get_count(String.valueOf(items.getUser_id()))));
                }
                if (list.size() > 1) {
                    Collections.sort(list, new Comparator<items>() {
                        @Override
                        public int compare(items u1, items u2) {
                            if (u1.getEtd().before(u2.getEtd())) {
                                return -1;
                            }
                            if (u1.getEtd() == u2.getEtd()) {
                                return 0;
                            }
                            return 1;
                        }
                    });
                    return list;
                } else return list;
            }else {
                List<items> list = itemsMapper.get_items_Japan(_items);
                Iterator<items> itemsIterator = list.iterator();
                while (itemsIterator.hasNext()) {
                    items items = itemsIterator.next();
                    //去搜索价格表
                    price_haika price_haika = priceMapper.get_price_haika(items.getUser_id(), items.getEtd(), _items.getMudigang(), _items.getQiyungang());
                    synchronized (items) {
                        try {
                            items.setPrice(price_haika.getPrice());
                        } catch (NullPointerException e) {
                            itemsIterator.remove();
                        }
                    }
                    items.setMudigang(mudigang);
                    items.setLike(Math.toIntExact(dianzanservice.get_count(String.valueOf(items.getUser_id()))));
                }
                if (list.size() > 1) {
                    Collections.sort(list, new Comparator<items>() {
                        @Override
                        public int compare(items u1, items u2) {
                            if (u1.getEtd().before(u2.getEtd())) {
                                return -1;
                            }
                            if (u1.getEtd() == u2.getEtd()) {
                                return 0;
                            }
                            return 1;
                        }
                    });
                    return list;
                } else return list;
            }
        }

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

    //遍历地区
    private String area(items _items) {
        if (_items.getMudigang().equals("美东")) {
            return "east";
        } else if (_items.getMudigang().equals("美西")) {
            return "west";
        } else if (_items.getMudigang().equals("美中")) {
            return "middle";
        }
        return null;
    }
    //遍历地区
    private String area1(String mudigang) {
        switch (mudigang){
            case ("温哥华"):
                return "YVR";
            case ("多伦多"):
                return "YYZ";
            case ("卡尔加多"):
                return "YYC";
            case ("渥太华"):
                return "YOW";
            case ("日本"):
                return "Japan";
        }
        return null;
    }
    //遍历地区
    private String area2(String mudigang) {
        switch (mudigang){
            case ("英国"):
                return "zone1";
            case ("德国"):
                return "zone2";
            case ("法国"):
                return "zone2";
            case ("卢森堡"):
                return "zone2";
            case ("荷兰"):
                return "zone2";
            case ("比利时"):
                return "zone2";
            case ("爱尔兰"):
                return "zone2";
            case ("西班牙"):
                return "zone3";
            case ("意大利"):
                return "zone3";
            case ("奥地利"):
                return "zone3";
            case ("丹麦"):
                return "zone3";
            case ("捷克"):
                return "zone3";
            case ("其他"):
                return "zone3";
        }
        return null;
    }

    //    获取所有订单
    public List<items> get_orders(String unionId) {
        List<items> items = itemsMapper.get_orders(unionId);
        for (com.duogesi.entities.items i : items
        ) {
            order order = i.getOrders().get(0);
            amount amount = amountMapper.get_amount_byid(order.getId());
            if (amount != null) {
                synchronized (amount) {
                    amount.setLocal(amount.getTax().add(amount.getCustomer()).add(amount.getInspect()).add(amount.getAdditional()));
                }
                i.setAmount(amount);
                Swtich swtich = new Swtich();
                swtich.swtich_schdule(i);
//                order.setDest(swtich.switch_mudigang_zhong_ouzhou(order.getDest()));
            }
        }

        return items;
    }

    //获取当前月份总金额
    public amount get_amount_bymount(String unionId) {
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
        amount amount = amountMapper.get_amount(start_data, end_data, unionId);
        if (amount==null) {
            amount amount1=new amount();
            amount1.setTotal(BigDecimal.valueOf(0));
            return amount1;
        } else {
            amount.setTotal(amount.getTotal().add(amount.getAdditional()).add(amount.getCustomer()).add(amount.getInspect()).add(amount.getTax()));
            return amount;
        }

    }

    //特价专区
    public List<items> redis_get() {
        LocalDateTime localDateTime=LocalDateTime.now();
        Date date=new Date();
        List<items> list = itemsMapper.ger_miaosha(date.getdate_eng(localDateTime));
        Iterator<items> itemsIterator = list.iterator();
        while (itemsIterator.hasNext()) {
            items items = itemsIterator.next();
            if (items.getMethod().equals("海派")) {
                price_include myprices = priceMapper.get_price1(items.getUser_id(), items.getEtd(), "west");
                getprice(itemsIterator, items, myprices);
                //获取附加费
                addition addition = additionMapper.get_price("普货", items.getUser_id());
                if (addition == null) {
                    addition addition1 = new addition();
                    addition1.setPrice(0);
                    items.setAddition(addition1);
                } else items.setAddition(addition);
            } else if (items.getMethod().equals("空派")) {
                price_include myprices = priceMapper.get_price0(items.getUser_id(), items.getEtd(), "west");
                getprice(itemsIterator, items, myprices);
                //获取附加费
                addition addition = additionMapper.get_price("普货", items.getUser_id());
                if (addition == null) {
                    addition addition1 = new addition();
                    addition1.setPrice(0);
                    items.setAddition(addition1);
                } else items.setAddition(addition);
            } else if (items.getMethod().equals("美森")) {
                price_include myprices = priceMapper.get_price2(items.getUser_id(), items.getEtd(), "west");
                getprice(itemsIterator, items, myprices);
                //获取附加费
                addition addition = additionMapper.get_price("普货", items.getUser_id());
                if (addition == null) {
                    addition addition1 = new addition();
                    addition1.setPrice(0);
                    items.setAddition(addition1);
                } else items.setAddition(addition);
            } else if (items.getMethod().equals("空派(直飞)")) {
                price_include myprices = priceMapper.get_price3(items.getUser_id(), items.getEtd(), "west");
                getprice(itemsIterator, items, myprices);
                //获取附加费
                addition addition = additionMapper.get_price("普货", items.getUser_id());
                if (addition == null) {
                    addition addition1 = new addition();
                    addition1.setPrice(0);
                    items.setAddition(addition1);
                } else items.setAddition(addition);
            } else if (items.getMethod().equals("空派(带电)")) {
                //去搜索价格表
                price_include myprices = priceMapper.get_price4(items.getUser_id(), items.getEtd(), "west");
                getprice(itemsIterator, items, myprices);
                //获取附加费
                addition addition = additionMapper.get_price("普货", items.getUser_id());
                if (addition == null) {
                    addition addition1 = new addition();
                    addition1.setPrice(0);
                    items.setAddition(addition1);
                } else items.setAddition(addition);
            } else if (items.getMethod().equals("海卡")) {
            }
            items.setDiscount(items.getPrice()-items.getDetails().getDiscount());
            details d =items.getDetails();
            d.setCountDownHour("0");
            d.setCountDownMinute("0");
            d.setCountDownSecond("0");
        }
        if (list.size()>1) {
            Collections.sort(list, new Comparator<items>() {
                @Override
                public int compare(items u1, items u2) {
                    if (u1.getEtd().before(u2.getEtd())) {
                        return -1;
                    }
                    if (u1.getEtd() == u2.getEtd()) {
                        return 0;
                    }
                    return 1;
                }
            });
            return list;
        }else return list;
    }

    //用户搜索小包渠道
    public List<xiaobao> get_xiaobao(String country,float weight){
        switch (country){
            case "美国":
                country= "American";
                break;
            case "韩国":
                country= "Korea";
                break;
            case "日本":
                country= "Japan";
                break;
            case "荷兰":
                country= "Netherlands";
                break;
            case "俄罗斯":
                country= "Russia";
                break;
            case "意大利":
                country= "Italy";
                break;
            case "英国":
                country= "England";
                break;
        }
        List<xiaobao> list=xiaobaoMapper.get_channel_xiaobao(country);
        int index=0;
        //先遍历小包的渠道
        for (int i = 0; i <list.size() ; i++) {
            xiaobao xiaobao=list.get(i);
           int xiaobao_id=xiaobao.getXiaobao_id();
           List<price_xiaobao> price_xiaobaos=xiaobaoMapper.get_price_xiaobao(xiaobao_id);
           //获取等级重
           float[] weights=new float[price_xiaobaos.size()];
            for (int j = 0; j <weights.length ; j++) {
                weights[j]=price_xiaobaos.get(j).getWeight();
            }
            //获取重量所对应的区间
            for (int k = 0; k <weights.length ; k++) {
                if(weights[k]<=weight&&weight<weights[k+1]&&k!=weights.length){
                    index=k;
                    break;
                }
            }
            //获取小包价格对象
            price_xiaobao price_xiaobao= price_xiaobaos.get(index);
            xiaobao.setPrice_xiaobao(price_xiaobao);
            //获取公司的名字
            supplier_company supplier_company=user_infoMapper.get_company_name(xiaobao.getUser_id()).get(0);
            xiaobao.setSupplier_company(supplier_company);
            //缺少判断快递还是小包xiaobao.setMethod("快递");
            xiaobao.setMethod("小包");
            //设置国家
            xiaobao.setCountry(country);
        }
        return list;
    }
}
