package com.finance.controller;

import com.finance.dto.Result;
import com.finance.service.AnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/analysis")
public class AnalysisController {
    @Autowired
    private AnalysisService analysisService;

    @GetMapping("/monthly-trend")
    public Result monthlyTrend(HttpServletRequest request, @RequestParam int year) {
        Long userId = (Long) request.getAttribute("userId");
        Map<String, Object> data = analysisService.getMonthlyTrend(userId, year);
        return Result.success(data);
    }

    @GetMapping("/expense-by-category")
    public Result expenseByCategory(HttpServletRequest request, @RequestParam(required = false) String month) {
        Long userId = (Long) request.getAttribute("userId");
        List<Map<String, Object>> data = analysisService.getExpenseByCategory(userId, month);
        return Result.success(data);
    }

    @GetMapping("/advice")
    public Result getAdvice(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        // 简单生成理财建议
        List<String> advice = java.util.Arrays.asList(
                "根据本月消费记录，餐饮支出占比较高，建议适当控制。",
                "预算执行情况良好，继续保持。",
                "储蓄目标进度已过半，加油！"
        );
        return Result.success(advice);
    }
}