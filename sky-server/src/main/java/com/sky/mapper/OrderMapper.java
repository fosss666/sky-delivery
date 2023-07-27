package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.OrdersConfirmDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.Orders;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * @author: fosss
 * Date: 2023/7/20
 * Time: 15:44
 * Description:
 */
@Mapper
public interface OrderMapper {
    /**
     * 新增订单
     *
     * @param orders
     */
    void insert(Orders orders);

    /**
     * 根据订单号查询订单
     *
     * @param orderNumber
     */
    @Select("select * from sky_take_out.orders where number = #{orderNumber}")
    Orders getByNumber(String orderNumber);

    /**
     * 修改订单信息
     *
     * @param orders
     */
    void update(Orders orders);

    /**
     * 查询历史订单
     */
    Page<Orders> queryHistoryOrders(OrdersPageQueryDTO ordersPageQueryDTO);

    @Select("select * from sky_take_out.orders where id=#{id}")
    Orders getById(Long id);

    /**
     * 条件查询
     *
     * @param ordersPageQueryDTO
     * @return
     */
    Page<Orders> queryPage(OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     * 查询各状态订单的数量
     */
    int countDeliverStatus(Integer status);

    /**
     * 接单
     */
    void confirm(OrdersConfirmDTO ordersConfirmDTO);

    /**
     * 查询状态为待支付且下单时间超过十五分钟的订单
     */
    @Select("select * from sky_take_out.orders where status=#{status} and order_time < #{time}")
    List<Orders> queryTimeoutAndOrderTimeLT(Integer status, LocalDateTime time);

    /**
     * 根据时间区间和订单状态查询
     */
    Double queryTurnoverOfDayAndStatus(@Param("beginTime") LocalDateTime beginTime,
                                       @Param("endTime") LocalDateTime endTime,
                                       @Param("status") Integer status);

    /**
     * 查询订单数
     */
    int countByMap(Map map);
}
