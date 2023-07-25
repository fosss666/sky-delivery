package com.sky.service;

import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;

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
}
