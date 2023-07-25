package com.sky.controller.user;

import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author: fosss
 * Date: 2023/7/20
 * Time: 15:28
 * Description:
 */
@RestController("userOrderController")
@RequestMapping("/user/order")
@Api(tags = "用户端订单相关接口")
@Slf4j
public class OrderController {

    @Resource
    private OrderService orderService;

    /**
     * 历史订单查询
     */
    @ApiOperation("历史订单查询")
    @GetMapping("/historyOrders")
    public Result<PageResult> queryHistoryOrders(@RequestParam("page") Integer page,
                                                 @RequestParam("pageSize") Integer pageSize,
                                                 @RequestParam(value = "status", required = false) Integer status) {
        log.info("历史订单查询");
        PageResult pageResult = orderService.queryHistoryOrders(page, pageSize, status);
        return Result.success(pageResult);
    }

    /**
     * 用户下单
     */
    @ApiOperation("用户下单")
    @PostMapping("/submit")
    public Result<OrderSubmitVO> submit(@RequestBody OrdersSubmitDTO ordersSubmitDTO) {
        log.info("用户下单：{}", ordersSubmitDTO);
        OrderSubmitVO orderSubmitVO = orderService.submit(ordersSubmitDTO);
        return Result.success(orderSubmitVO);
    }

    /**
     * 订单支付
     *
     * @param ordersPaymentDTO
     * @return
     */
    @PutMapping("/payment")
    @ApiOperation("订单支付")
    public Result<OrderPaymentVO> payment(@RequestBody OrdersPaymentDTO ordersPaymentDTO) throws Exception {
        log.info("订单支付：{}", ordersPaymentDTO);
        //OrderPaymentVO orderPaymentVO = orderService.payment(ordersPaymentDTO);
        //TODO 申请不到资质，这里封装个假数据
        OrderPaymentVO orderPaymentVO = OrderPaymentVO
                .builder()
                .timeStamp("1670380960")
                .packageStr("wx07104240042328a34b4652a71855300000")
                .paySign("TESTTESTTESTTESTTESTTEST")
                .signType("RSA")
                .nonceStr("94123172860079869972517395812792")
                .build();

        log.info("生成预支付交易单：{}", orderPaymentVO);
        //这里跳过实际调用微信支付过程，直接调用支付成功方法
        orderService.paySuccess(ordersPaymentDTO.getOrderNumber());

        return Result.success(orderPaymentVO);
    }

    /**
     * 查询订单详情
     */
    @ApiOperation("查询订单详情")
    @GetMapping("/orderDetail/{id}")
    public Result<OrderVO> queryOrderDetails(@PathVariable("id") Long id) {
        log.info("查询订单详情：{}", id);
        OrderVO orderVO = orderService.queryOrderDetails(id);
        return Result.success(orderVO);
    }

    /**
     * 取消订单
     */
    @ApiOperation("取消订单")
    @PutMapping("/cancel/{id}")
    public Result cancel(@PathVariable("id") Long id) throws Exception {
        log.info("取消订单：{}", id);
        orderService.cancel(id);
        return Result.success();
    }

    /**
     * 再来一单
     */
    @ApiOperation("再来一单")
    @PostMapping("/repetition/{id}")
    public Result repetition(@PathVariable("id") Long id) {
        log.info("再来一单：{}", id);
        orderService.repetition(id);
        return Result.success();
    }
}


















