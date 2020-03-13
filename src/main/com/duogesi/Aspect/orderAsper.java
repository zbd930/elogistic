package com.duogesi.Aspect;


import com.duogesi.Mail.Mymail;
import com.duogesi.entities.*;
import com.duogesi.mapper.ItemsMapper;
import com.duogesi.mapper.OrderMapper;
import com.duogesi.mapper.user_infoMapper;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Aspect    //该标签把LoggerAspect类声明为一个切面
@Order(1)  //设置切面的优先级：如果有多个切面，可通过设置优先级控制切面的执行顺序（数值越小，优先级越高）
@Component //该标签把LoggerAspect类放到IOC容器中
public class orderAsper {

    private ItemsMapper itemsMapper;

    private OrderMapper orderMapper;

    private user_infoMapper user_infoMapper;

    private Mymail mymail;

    public Mymail getMymail() {
        return mymail;
    }

    public void setMymail(Mymail mymail) {
        this.mymail = mymail;
    }

    public com.duogesi.mapper.user_infoMapper getUser_infoMapper() {
        return user_infoMapper;
    }

    public void setUser_infoMapper(com.duogesi.mapper.user_infoMapper user_infoMapper) {
        this.user_infoMapper = user_infoMapper;
    }

    public ItemsMapper getItemsMapper() {
        return itemsMapper;
    }

    public void setItemsMapper(ItemsMapper itemsMapper) {
        this.itemsMapper = itemsMapper;
    }

    public OrderMapper getOrderMapper() {
        return orderMapper;
    }

    public void setOrderMapper(OrderMapper orderMapper) {
        this.orderMapper = orderMapper;
    }

    /**
     * 前置通知
     * @param joinPoint
     * 2代表 更新仓位信息成功，3代表失败，4代表仓位不够
     */
    public void beforMethod(JoinPoint joinPoint){
        String methodName = joinPoint.getSignature().getName();
        List<Object> args = Arrays.asList(joinPoint.getArgs());
        System.out.println("this method "+methodName+" begin. param<"+ args+">");
    }
    /**
     * 后置通知（无论方法是否发生异常都会执行,所以访问不到方法的返回值）
     * @param joinPoint
     */
    public void afterMethod(JoinPoint joinPoint){
        String methodName = joinPoint.getSignature().getName();
        System.out.println("this method "+methodName+" end.");
    }



    /**
     * 返回通知（在方法正常结束执行的代码）
     * 返回通知可以访问到方法的返回值！
     * @param joinPoint
     */
    public void afterReturnMethod(JoinPoint joinPoint,Object result){
        String methodName = joinPoint.getSignature().getName();
        System.out.println("this method "+methodName+" end.result<"+result+">");
    }
    /**
     * 异常通知（方法发生异常执行的代码）
     * 可以访问到异常对象；且可以指定在出现特定异常时执行的代码
     * @param joinPoint
     * @param ex
     */
    public void afterThrowingMethod(JoinPoint joinPoint,Exception ex){
        String methodName = joinPoint.getSignature().getName();
        System.out.println("this method "+methodName+" end.ex message<"+ex+">");
    }
    /**
     * 环绕通知(需要携带类型为ProceedingJoinPoint类型的参数)
     * 环绕通知包含前置、后置、返回、异常通知；ProceedingJoinPoin 类型的参数可以决定是否执行目标方法
     * 且环绕通知必须有返回值，返回值即目标方法的返回值
     * @param point
     * 2是更新仓位情况有误，3是仓位不够,4是更新ship_schdule状态有误
     */
    public int aroundMethod(ProceedingJoinPoint point){
        int result = 0;
        String methodName = point.getSignature().getName();
        List<Object> args = Arrays.asList(point.getArgs());
        order order= (com.duogesi.entities.order) args.get(0);
        order_details order_details=(order_details) args.get(2);
        try {
            //前置通知
            System.out.println("The method "+ methodName+" start. param<"+ Arrays.asList(point.getArgs())+">");
//            获取参数
            details details=itemsMapper.get_para(order.getItem_id());
            if(details.getWeight()-order_details.getWeight()>=0 && details.getVolume()-order_details.getVolume()>=0) {
                    //执行目标方法
                result = (int) point.proceed();
            } else result=3;
            //返回通知
            System.out.println("The method "+ methodName+" end. result<"+ result+">");
        } catch (Throwable e) {
            //异常通知
            System.out.println("this method "+methodName+" end.ex message<"+e+">");
            throw new RuntimeException(e);
        }
        //            获取参数
        details details=itemsMapper.get_para(order.getItem_id());
        if(details.getWeight()<=0 && details.getVolume()<=0) {
            //后置通知
            int item_id = order.getItem_id();
            items items =itemsMapper.get_supplier_info(item_id);
            String email=items.getSupplier_companies().get(0).getContact_mail();
            try {
                mymail.send(email, "您发布的拼柜任务已收收到货，请登录查看", "【任务更新】");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("The method "+ methodName+" end.");
        return result;
    }
}
