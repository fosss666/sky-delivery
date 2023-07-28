package com.sky.service.impl;

import com.sky.dto.GoodsSalesDTO;
import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.ReportService;
import com.sky.service.WorkspaceService;
import com.sky.vo.*;
import io.swagger.models.auth.In;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    @Resource
    private UserMapper userMapper;
    @Resource
    private WorkspaceService workspaceService;

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

    /**
     * 用户统计
     */
    @Override
    public UserReportVO getUserReportVO(LocalDate begin, LocalDate end) {
        UserReportVO userReportVO = new UserReportVO();
        //dateList
        List<LocalDate> dateList = new ArrayList<>();
        dateList.add(begin);
        while (!begin.equals(end)) {
            begin = begin.plusDays(1);
            dateList.add(begin);
        }
        String dateListStr = StringUtils.join(dateList, ",");
        userReportVO.setDateList(dateListStr);

        //查询不同日期总用户和当天新增用户
        List<Integer> totalUserList = new ArrayList<>();
        List<Integer> newUserList = new ArrayList<>();

        for (LocalDate date : dateList) {
            //获取这天的开始和结束时间
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);
            Map<String, LocalDateTime> map = new HashMap<>();
            //totalUserList
            map.put("endTime", endTime);
            Integer totalUser = userMapper.countUserByMap(map);
            totalUserList.add(totalUser);

            //newUserList
            map.put("beginTime", beginTime);
            Integer newUser = userMapper.countUserByMap(map);
            newUserList.add(newUser);
        }
        userReportVO.setTotalUserList(StringUtils.join(totalUserList, ","));
        userReportVO.setNewUserList(StringUtils.join(newUserList, ","));

        return userReportVO;
    }

    /**
     * 订单统计
     */
    @Override
    public OrderReportVO getOrderReportVo(LocalDate begin, LocalDate end) {
        OrderReportVO orderReportVO = new OrderReportVO();
        //dateList
        List<LocalDate> dateList = new ArrayList<>();
        dateList.add(begin);
        while (!begin.equals(end)) {
            begin = begin.plusDays(1);
            dateList.add(begin);
        }
        String dateListStr = StringUtils.join(dateList, ",");
        orderReportVO.setDateList(dateListStr);

        //orderCountList validOrderCountList totalOrderCount validOrderCount
        List<Integer> orderCountList = new ArrayList<>();
        List<Integer> validOrderCountList = new ArrayList<>();
        int totalOrderCount = 0;
        int totalValidOrderCount = 0;
        for (LocalDate date : dateList) {
            Map map = new HashMap();
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);
            map.put("beginTime", beginTime);
            map.put("endTime", endTime);
            //查询当日订单总数
            int orderCount = orderMapper.countByMap(map);
            orderCountList.add(orderCount);

            //查询当日有效订单数
            map.put("status", Orders.COMPLETED);//订单状态为已完成
            int validOrderCount = orderMapper.countByMap(map);
            validOrderCountList.add(validOrderCount);

            //计算总订单数
            totalOrderCount += orderCount;

            //计算总有效订单数
            totalValidOrderCount += validOrderCount;

        }
        orderReportVO.setOrderCountList(StringUtils.join(orderCountList, ","));
        orderReportVO.setValidOrderCountList(StringUtils.join(validOrderCountList, ","));
        orderReportVO.setTotalOrderCount(totalOrderCount);
        orderReportVO.setValidOrderCount(totalValidOrderCount);

        //orderCompletionRate 计算订单完成率
        double orderCompletionRate = 0.0;
        if (totalOrderCount != 0) {
            orderCompletionRate = Integer.valueOf(totalValidOrderCount).doubleValue() / totalOrderCount;
        }
        orderReportVO.setOrderCompletionRate(orderCompletionRate);

        return orderReportVO;
    }

    /**
     * 查询销量排名top10
     */
    @Override
    public SalesTop10ReportVO getTop10(LocalDate begin, LocalDate end) {
        List<GoodsSalesDTO> goodsSalesDTOList = orderMapper.getTop10(begin, end, Orders.COMPLETED);
        //封装返回数据
        SalesTop10ReportVO salesTop10ReportVO = new SalesTop10ReportVO();
        List<String> nameList = goodsSalesDTOList.stream().map(item -> item.getName()).collect(Collectors.toList());
        List<Integer> numberList = goodsSalesDTOList.stream().map(item -> item.getNumber()).collect(Collectors.toList());
        String nameListStr = StringUtils.join(nameList, ",");
        String numberListStr = StringUtils.join(numberList, ",");
        salesTop10ReportVO.setNameList(nameListStr);
        salesTop10ReportVO.setNumberList(numberListStr);

        return salesTop10ReportVO;
    }

    /**
     * 导出运营数据excel报表
     */
    @Override
    public void export(HttpServletResponse response) {
        //获取模板输入流
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("template/运营数据报表模板.xlsx");
        //获取概览数据 近三十天
        LocalDate dateBegin = LocalDate.now().minusDays(30);
        LocalDate date = LocalDate.now().minusDays(1);//昨天
        BusinessDataVO businessData = workspaceService.getBusinessData(LocalDateTime.of(dateBegin, LocalTime.MIN), LocalDateTime.of(date, LocalTime.MAX));

        try {
            //写入excel
            XSSFWorkbook excel = new XSSFWorkbook(is);
            XSSFSheet sheet = excel.getSheetAt(0);

            //设置时间区间
            sheet.getRow(1).getCell(1).setCellValue("时间：" + dateBegin + "至" + date);
            //设置概览数据
            //第四行
            XSSFRow row4 = sheet.getRow(3);
            row4.getCell(2).setCellValue(businessData.getTurnover());
            row4.getCell(4).setCellValue(businessData.getOrderCompletionRate());
            row4.getCell(6).setCellValue(businessData.getNewUsers());
            //第五行
            XSSFRow row5 = sheet.getRow(4);
            row5.getCell(2).setCellValue(businessData.getValidOrderCount());
            row5.getCell(4).setCellValue(businessData.getUnitPrice());

            //设置三十天的明细数据
            for (int i = 0; i < 30; i++) {
                //获取当天日期
                LocalDate localDate = dateBegin.plusDays(i);
                //获取数据
                BusinessDataVO bd = workspaceService.getBusinessData(LocalDateTime.of(localDate, LocalTime.MIN), LocalDateTime.of(localDate, LocalTime.MAX));
                //设置各单元格
                XSSFRow row = sheet.getRow(7 + i);//获取每一行
                row.getCell(1).setCellValue(localDate.toString());
                row.getCell(2).setCellValue(bd.getTurnover());
                row.getCell(3).setCellValue(bd.getValidOrderCount());
                row.getCell(4).setCellValue(bd.getOrderCompletionRate());
                row.getCell(5).setCellValue(bd.getUnitPrice());
                row.getCell(6).setCellValue(bd.getNewUsers());

            }

            //写出报表
            excel.write(response.getOutputStream());

            //关闭资源
            is.close();
            excel.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}














