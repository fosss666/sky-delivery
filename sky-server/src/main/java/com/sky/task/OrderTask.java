package com.sky.task;

import com.sky.constant.MessageConstant;
import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author: fosss
 * Date: 2023/7/24
 * Time: 13:59
 * Description:订单定时处理
 */
@Component
@Slf4j
public class OrderTask {

    @Resource
    private OrderMapper orderMapper;

    /**
     * 处理超时未支付的订单，每分钟触发一次
     */
    @Scheduled(cron = "0 0/1 * * * ? ")
    public void processTimeoutOrder() {
        log.info("处理超时未支付订单：{}", LocalDateTime.now());
        //查询状态为待支付且下单时间超过十五分钟的订单
        LocalDateTime time = LocalDateTime.now().minusMinutes(15);
        List<Orders> ordersList = orderMapper.queryTimeoutAndOrderTimeLT(Orders.PENDING_PAYMENT, time);
        //取消订单
        for (Orders order : ordersList) {
            order.setStatus(Orders.CANCELLED);
            order.setCancelReason(MessageConstant.ORDER_TIMEOUT);
            order.setCancelTime(LocalDateTime.now());
            orderMapper.update(order);
        }
    }

    /**
     * 处理一直处于派送中的订单，每天1点触发一次
     */
    @Scheduled(cron = "0 0 1 * * ? ")
    public void processDeliveryOrder() {
        log.info("处理一直处于派送中的订单：{}", LocalDateTime.now());
        //查询状态为派送中且下单时间为昨天的订单
        LocalDateTime time = LocalDateTime.now().minusMinutes(60);
        List<Orders> ordersList = orderMapper.queryTimeoutAndOrderTimeLT(Orders.DELIVERY_IN_PROGRESS, time);
        //将订单设置为已完成
        for (Orders order : ordersList) {
            order.setStatus(Orders.COMPLETED);
            orderMapper.update(order);
        }
    }
}



















