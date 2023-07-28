package com.sky.service;

import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;

/**
 * @author: fosss
 * Date: 2023/7/25
 * Time: 13:35
 * Description:
 */
public interface ReportService {
    /**
     * 营业额统计
     */
    TurnoverReportVO getTurnReportVo(LocalDate begin, LocalDate end);

    /**
     * 用户统计
     */
    UserReportVO getUserReportVO(LocalDate begin, LocalDate end);

    /**
     * 订单统计
     */
    OrderReportVO getOrderReportVo(LocalDate begin, LocalDate end);

    /**
     * 查询销量排名top10
     */
    SalesTop10ReportVO getTop10(LocalDate begin, LocalDate end);

    /**
     * 导出运营数据excel报表
     */
    void export(HttpServletResponse response);
}
