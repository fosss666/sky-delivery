package com.sky.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.dto.*;
import com.sky.entity.*;
import com.sky.exception.AddressBookBusinessException;
import com.sky.exception.OrderBusinessException;
import com.sky.exception.ShoppingCartBusinessException;
import com.sky.mapper.*;
import com.sky.result.PageResult;
import com.sky.service.OrderService;
import com.sky.utils.WeChatPayUtil;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: fosss
 * Date: 2023/7/20
 * Time: 15:31
 * Description:
 */
@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    @Resource
    private AddressBookMapper addressBookMapper;
    @Resource
    private ShoppingCartMapper shoppingCartMapper;
    @Resource
    private OrderMapper orderMapper;
    @Resource
    private OrderDetailMapper orderDetailMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private WeChatPayUtil weChatPayUtil;

    /**
     * 用户下单
     */
    @Transactional
    @Override
    public OrderSubmitVO submit(OrdersSubmitDTO ordersSubmitDTO) {
        //业务异常处理（地址是否存在、购物车是否存在）
        Long addressBookId = ordersSubmitDTO.getAddressBookId();
        AddressBook addressBook = addressBookMapper.getById(addressBookId);
        if (addressBook == null) {
            //地址不存在
            throw new AddressBookBusinessException(MessageConstant.ADDRESS_BOOK_IS_NULL);
        }

        Long userId = BaseContext.getCurrentId();
        List<ShoppingCart> shoppingCartList = shoppingCartMapper.list(userId);
        if (shoppingCartList == null || shoppingCartList.size() == 0) {
            //不存在购物数据
            throw new ShoppingCartBusinessException(MessageConstant.SHOPPING_CART_IS_NULL);
        }

        //向订单表插入1条数据
        Orders orders = new Orders();
        BeanUtils.copyProperties(ordersSubmitDTO, orders);
        orders.setOrderTime(LocalDateTime.now());
        orders.setPayStatus(Orders.UN_PAID);//支付状态
        orders.setStatus(Orders.PENDING_PAYMENT);//订单状态
        orders.setNumber(String.valueOf(System.currentTimeMillis()));//订单号
        orders.setConsignee(addressBook.getConsignee());//收货人
        orders.setPhone(addressBook.getPhone());
        orders.setUserId(userId);
        orders.setAddress(addressBook.getProvinceName() + addressBook.getCityName() + addressBook.getDistrictName() + addressBook.getDetail());

        orderMapper.insert(orders);
        //向订单明细表插入n条数据
        List<OrderDetail> orderDetailList = shoppingCartList.stream().map(shoppingCart -> {
            OrderDetail orderDetail = new OrderDetail();
            BeanUtils.copyProperties(shoppingCart, orderDetail);
            orderDetail.setOrderId(orders.getId());
            return orderDetail;
        }).collect(Collectors.toList());

        orderDetailMapper.insertBatch(orderDetailList);
        //清空购物车
        shoppingCartMapper.clear(userId);

        //构造返回对象
        OrderSubmitVO orderSubmitVO = new OrderSubmitVO();
        orderSubmitVO.setOrderTime(orders.getOrderTime());
        orderSubmitVO.setOrderAmount(orders.getAmount());
        orderSubmitVO.setOrderNumber(orders.getNumber());

        return orderSubmitVO;
    }

    /**
     * 订单支付
     *
     * @param ordersPaymentDTO
     * @return
     */
    public OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception {
        // 当前登录用户id
        Long userId = BaseContext.getCurrentId();
        User user = userMapper.getById(userId);

        //调用微信支付接口，生成预支付交易单
        JSONObject jsonObject = weChatPayUtil.pay(
                ordersPaymentDTO.getOrderNumber(), //商户订单号
                new BigDecimal(0.01), //支付金额，单位 元
                "苍穹外卖订单", //商品描述
                user.getOpenid() //微信用户的openid
        );

        if (jsonObject.getString("code") != null && jsonObject.getString("code").equals("ORDERPAID")) {
            throw new OrderBusinessException("该订单已支付");
        }

        OrderPaymentVO vo = jsonObject.toJavaObject(OrderPaymentVO.class);
        vo.setPackageStr(jsonObject.getString("package"));

        return vo;
    }

    /**
     * 支付成功，修改订单状态
     *
     * @param outTradeNo
     */
    public void paySuccess(String outTradeNo) {

        // 根据订单号查询订单
        Orders ordersDB = orderMapper.getByNumber(outTradeNo);

        // 根据订单id更新订单的状态、支付方式、支付状态、结账时间
        Orders orders = Orders.builder()
                .id(ordersDB.getId())
                .status(Orders.TO_BE_CONFIRMED)
                .payStatus(Orders.PAID)
                .checkoutTime(LocalDateTime.now())
                .build();

        orderMapper.update(orders);
    }

    /**
     * 历史订单查询
     */
    @Override
    public PageResult queryHistoryOrders(Integer page, Integer pageSize, Integer status) {
        PageHelper.startPage(page, pageSize);
        OrdersPageQueryDTO ordersPageQueryDTO = new OrdersPageQueryDTO();
        ordersPageQueryDTO.setUserId(BaseContext.getCurrentId());
        ordersPageQueryDTO.setStatus(status);

        //查询订单
        Page<Orders> p = orderMapper.queryHistoryOrders(ordersPageQueryDTO);
        List<OrderVO> collect = new ArrayList<>();
        //封装返回结果
        if (p != null && p.getTotal() > 0) {
            collect = p.getResult().stream().map(order -> {
                //封装结果
                OrderVO orderVO = new OrderVO();
                BeanUtils.copyProperties(order, orderVO);
                //查询订单明细
                List<OrderDetail> orderDetailList = orderDetailMapper.getByOrderId(order.getId());
                orderVO.setOrderDetailList(orderDetailList);
                return orderVO;
            }).collect(Collectors.toList());
        }


        return new PageResult(p.getTotal(), collect);
    }

    /**
     * 查询订单详情
     */
    @Override
    public OrderVO queryOrderDetails(Long id) {
        OrderVO orderVO = new OrderVO();

        //根据订单id查询订单
        Orders order = orderMapper.getById(id);
        BeanUtils.copyProperties(order, orderVO);
        //查询订单详情
        List<OrderDetail> orderDetailList = orderDetailMapper.getByOrderId(id);
        orderVO.setOrderDetailList(orderDetailList);
        return orderVO;
    }

    /**
     * 取消订单
     */
    @Override
    public void cancel(Long id) throws Exception {
        //查询订单，根据订单状态进行分别处理
        Orders orders = orderMapper.getById(id);
        if (orders == null) {
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
        }

        ////订单状态 1待付款 2待接单 3已接单 4派送中 5已完成 6已取消
        if (orders.getStatus() > 2) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }

        Orders o = new Orders();
        o.setId(orders.getId());

        // 订单处于待接单状态下取消，需要进行退款
        if (orders.getStatus().equals(Orders.TO_BE_CONFIRMED)) {
            //调用微信支付退款接口
            //TODO 微信退款
            /*
            weChatPayUtil.refund(
                    orders.getNumber(), //商户订单号
                    orders.getNumber(), //商户退款单号
                    new BigDecimal(0.01),//退款金额，单位 元
                    new BigDecimal(0.01));//原订单金额
*/
            //支付状态修改为 退款
            o.setPayStatus(Orders.REFUND);
        }

        // 更新订单状态、取消原因、取消时间
        o.setStatus(Orders.CANCELLED);
        o.setCancelReason("用户取消");
        o.setCancelTime(LocalDateTime.now());
        orderMapper.update(orders);
    }

    /**
     * 再来一单
     */
    @Override
    public void repetition(Long id) {
        //根据订单号查询订单详情
        List<OrderDetail> orderDetailList = orderDetailMapper.getByOrderId(id);
        //封装购物车信息
        Long userId = BaseContext.getCurrentId();
        if (orderDetailList != null && orderDetailList.size() > 0) {
            List<ShoppingCart> collect = orderDetailList.stream().map(orderDetail -> {
                ShoppingCart shoppingCart = new ShoppingCart();
                BeanUtils.copyProperties(orderDetail, shoppingCart);
                shoppingCart.setUserId(userId);
                shoppingCart.setCreateTime(LocalDateTime.now());
                return shoppingCart;
            }).collect(Collectors.toList());
            //批量插入
            shoppingCartMapper.insertBatch(collect);
        }
    }

    /**
     * 订单搜索
     */
    @Override
    public PageResult conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO) {
        PageHelper.startPage(ordersPageQueryDTO.getPage(), ordersPageQueryDTO.getPageSize());
        Page<Orders> page = orderMapper.queryPage(ordersPageQueryDTO);
        List<Orders> ordersList = page.getResult();
        //转成orderVo
        List<OrderVO> collect = ordersList.stream().map(orders -> {
            OrderVO orderVO = new OrderVO();
            BeanUtils.copyProperties(orders, orderVO);
            //获取拼接好的菜品字符串
            String orderDishesStr = getOrderDishesStr(orders.getId());
            orderVO.setOrderDishes(orderDishesStr);
            return orderVO;
        }).collect(Collectors.toList());
        return new PageResult(page.getTotal(), collect);
    }

    private String getOrderDishesStr(Long id) {
        List<OrderDetail> orderDetailList = orderDetailMapper.getByOrderId(id);
        List<String> collect = orderDetailList.stream().map(orderDetail -> {
            return orderDetail.getName() + "*" + orderDetail.getNumber() + "；";
        }).collect(Collectors.toList());
        //将集合拼接成字符串返回
        String res = String.join("", collect);
        return res;
    }

    /**
     * 各个状态的订单数量统计
     */
    @Override
    public OrderStatisticsVO statistics() {
        //查询各状态订单的数量
        int toBeConfirmed = orderMapper.countDeliverStatus(Orders.TO_BE_CONFIRMED);
        int confirmed = orderMapper.countDeliverStatus(Orders.CONFIRMED);
        int deliveryInProgress = orderMapper.countDeliverStatus(Orders.DELIVERY_IN_PROGRESS);
        OrderStatisticsVO orderStatisticsVO = new OrderStatisticsVO();
        orderStatisticsVO.setToBeConfirmed(toBeConfirmed);
        orderStatisticsVO.setConfirmed(confirmed);
        orderStatisticsVO.setDeliveryInProgress(deliveryInProgress);
        return orderStatisticsVO;
    }

    /**
     * 接单
     */
    @Override
    public void confirm(OrdersConfirmDTO ordersConfirmDTO) {
        ordersConfirmDTO.setStatus(Orders.CONFIRMED);
        orderMapper.confirm(ordersConfirmDTO);
    }

    /**
     * 拒单
     */
    @Override
    public void rejection(OrdersRejectionDTO ordersRejectionDTO) throws Exception {
        //查询订单状态，如果是已接单则可以拒单
        Orders order = orderMapper.getById(ordersRejectionDTO.getId());
        if (order == null || !order.getStatus().equals(Orders.TO_BE_CONFIRMED)) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }
        //查询支付状态，如果是已支付，则需要退款
        Integer payStatus = order.getPayStatus();
        if (payStatus.equals(Orders.PAID)) {
            //用户已支付，需要退款
            String refund = weChatPayUtil.refund(
                    order.getNumber(),
                    order.getNumber(),
                    new BigDecimal(0.01),
                    new BigDecimal(0.01));
            log.info("申请退款：{}", refund);
        }

        // 拒单需要退款，根据订单id更新订单状态、拒单原因、取消时间
        Orders orders = new Orders();
        orders.setId(order.getId());
        orders.setStatus(Orders.CANCELLED);//修改状态
        orders.setRejectionReason(ordersRejectionDTO.getRejectionReason());//拒单原因
        orders.setCancelTime(LocalDateTime.now());//订单取消时间

        orderMapper.update(orders);
    }

    /**
     * 取消订单
     */
    @Override
    public void adminCancel(OrdersCancelDTO ordersCancelDTO) throws Exception {
        //查询订单，如果已经支付则需要退款
        Orders order = orderMapper.getById(ordersCancelDTO.getId());
        if (order.getPayStatus().equals(Orders.PAID)) {
            //退款
            String refund = weChatPayUtil.refund(
                    order.getNumber(),
                    order.getNumber(),
                    new BigDecimal(0.01),
                    new BigDecimal(0.01)
            );
            log.info("申请退款：{}", refund);
        }
        //修改订单状态为已取消
        Orders orders = new Orders();
        orders.setId(ordersCancelDTO.getId());
        orders.setCancelReason(ordersCancelDTO.getCancelReason());
        orders.setStatus(Orders.CANCELLED);

        orderMapper.update(orders);
    }

    /**
     * 派送订单
     */
    @Override
    public void delivery(Long id) {
        //查询订单状态
        Orders order = orderMapper.getById(id);
        if (order == null || !order.getStatus().equals(Orders.CONFIRMED)) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }

        Orders orders = new Orders();
        orders.setId(id);
        orders.setStatus(Orders.DELIVERY_IN_PROGRESS);

        orderMapper.update(orders);
    }

    /**
     * 完成订单
     */
    @Override
    public void complete(Long id) {
        Orders order = orderMapper.getById(id);
        if (order == null || !order.getStatus().equals(Orders.DELIVERY_IN_PROGRESS)) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }
        Orders orders = new Orders();
        orders.setId(id);
        orders.setStatus(Orders.COMPLETED);
        orders.setDeliveryTime(LocalDateTime.now());

        orderMapper.update(orders);
    }

}



















