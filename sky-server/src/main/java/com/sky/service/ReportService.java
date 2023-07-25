package com.sky.service;

import com.sky.vo.TurnoverReportVO;

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
}
