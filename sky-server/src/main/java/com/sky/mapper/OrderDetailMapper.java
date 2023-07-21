package com.sky.mapper;

import com.sky.entity.OrderDetail;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author: fosss
 * Date: 2023/7/20
 * Time: 15:57
 * Description:
 */
@Mapper
public interface OrderDetailMapper {
    /**
     * 批量添加订单详情
     *
     * @param orderDetailList
     */
    void insertBatch(@Param("orderDetailList") List<OrderDetail> orderDetailList);

    @Select("select * from sky_take_out.order_detail where order_id=#{id}")
    List<OrderDetail> getByOrderId(Long id);
}












