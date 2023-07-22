package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.Orders;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

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
     * @param ordersPageQueryDTO
     * @return
     */
    Page<Orders> queryPage(OrdersPageQueryDTO ordersPageQueryDTO);
}
