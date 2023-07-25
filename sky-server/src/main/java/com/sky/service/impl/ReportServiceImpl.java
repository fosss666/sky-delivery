package com.sky.service.impl;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.service.ReportService;
import com.sky.vo.TurnoverReportVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: fosss
 * Date: 2023/7/25
 * Time: 13:35
 * Description:
 */
@Service
public class ReportServiceImpl implements ReportService {

    @Resource
    private OrderMapper orderMapper;

    /**
     * 营业额统计
     */
    @Override
    public TurnoverReportVO getTurnReportVo(LocalDate begin, LocalDate end) {
        TurnoverReportVO turnoverReportVO = new TurnoverReportVO();

        //构造变量dateList,获取开始结束之间的所有时间，然后拼接字符串
        List<LocalDate> dateList = new ArrayList<>();
        dateList.add(begin);
        //注意用equals比较，不要用==
        while (!begin.equals(end)) {
            begin = begin.plusDays(1);
            dateList.add(begin);
        }
        String date = StringUtils.join(dateList, ",");
        turnoverReportVO.setDateList(date);

        //统计每天的营业额
        List<Double> turnoverList = dateList.stream().map(localDate -> {
            //构造区间
            LocalDateTime beginTime = LocalDateTime.of(localDate, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(localDate, LocalTime.MAX);
            Double turnover = orderMapper.queryTurnoverOfDayAndStatus(beginTime, endTime, Orders.COMPLETED);
            //处理营业额为空时
            turnover = turnover == null ? 0 : turnover;
            return turnover;
        }).collect(Collectors.toList());
        //转成字符串
        String turnover = StringUtils.join(turnoverList, ",");
        turnoverReportVO.setTurnoverList(turnover);

        return turnoverReportVO;
    }
}














