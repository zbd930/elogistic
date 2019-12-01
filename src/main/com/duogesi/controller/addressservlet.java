package com.duogesi.controller;

import com.duogesi.Mail.Mymail;
import com.duogesi.Mail.RedisUtil;
import com.duogesi.entities.address;
import com.duogesi.service.addressservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpSession;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Controller
@RequestMapping("/address/")
public class addressservlet {

    @Autowired
    private addressservice addressservice;
    @Autowired
    private HttpSession session;
    @Autowired
    private RedisUtil redisUtil;

    private static final String SYMBOLS = "0123456789"; // 数字

    // 字符串
    // private static final String SYMBOLS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private static final Random RANDOM = new SecureRandom();
    /**
     * 获取长度为 6 的随机数字
     * @return 随机数字
     * @date 修改日志：由 space 创建于 2018-8-2 下午2:43:51
     */
    public static String getNonce_str() {

        // 如果需要4位，那 new char[4] 即可，其他位数同理可得
        char[] nonceChars = new char[5];

        for (int index = 0; index < nonceChars.length; ++index) {
            nonceChars[index] = SYMBOLS.charAt(RANDOM.nextInt(SYMBOLS.length()));
        }

        return new String(nonceChars);
    }

    @RequestMapping(value = "add.do",produces="text/html;charset=UTF-8")
    @ResponseBody
    public String add(address address,String code){
        Map map=redisUtil.hmget(address.getOpenid());
        String email =(String)map.get(0);
        String code1=(String)map.get(1);
        if (email.equals(address.getEmail())) {
            System.out.println(code1);
            if (code.equals(code1)) {
                if (addressservice.add(address)) {
                } else return "Fail";
                return "Success";
            } else return "验证码错误或过期";
        }else return "邮箱错误";
    }

    @RequestMapping("get.do")
    @ResponseBody
    public List<address> get(String openid){
       return addressservice.get(openid);
    }

    @RequestMapping(value = "delete.do",produces="text/html;charset=UTF-8")
    @ResponseBody
    public String delete(int id){
       if (addressservice.delete_address(id)==1){
           return "Success";
       }else return "Fail";

    }

//    验证邮箱的正确性
    @RequestMapping(value = "valid_email.do",produces="text/html;charset=UTF-8")
    @ResponseBody
    public String valid(String openid,String email) {
        System.out.println(email);
        Mymail mymail = new Mymail();
        StringBuilder s = new StringBuilder();
        String code = getNonce_str();
        s.append("您的验证码为" + code + "<br>请在收到邮件的10分钟内操作;");
        Map map = new HashMap();
        map.put("email", email);
        map.put("code", code);
        if (!redisUtil.hasKey(openid)) {
            redisUtil.hmset(openid, map, 600000);
            try {
                mymail.send(email, String.valueOf(s), "【邮箱验证】");
            } catch (Exception e) {
                e.printStackTrace();
                return "send fail";
            }
            return "success";
        } return "10分钟后再试";
    }
}