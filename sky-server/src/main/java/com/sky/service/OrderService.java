package com.sky.service;

import com.sky.dto.OrdersSubmitDTO;
import com.sky.vo.OrderSubmitVO;

/**
 * @author: fosss
 * Date: 2023/7/20
 * Time: 15:31
 * Description:
 */
public interface OrderService {
    /**
     * 用户下单
     */
    OrderSubmitVO submit(OrdersSubmitDTO ordersSubmitDTO);
}
