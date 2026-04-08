package com.finance.service;

import java.util.List;
import java.util.Map;

public interface AnalysisService {
    Map<String, Object> getMonthlyTrend(Long userId, int year);
    List<Map<String, Object>> getExpenseByCategory(Long userId, String month);
    List<Map<String, Object>> getIncomeByCategory(Long userId, String month);
}