package com.finance.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.finance.entity.Category;
import com.finance.entity.Transaction;
import com.finance.mapper.CategoryMapper;
import com.finance.mapper.TransactionMapper;
import com.finance.service.AnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.*;

@Service
public class AnalysisServiceImpl implements AnalysisService {
    @Autowired
    private TransactionMapper transactionMapper;
    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public Map<String, Object> getMonthlyTrend(Long userId, int year) {
        List<Map<String, Object>> trend = new ArrayList<>();
        for (int month = 1; month <= 12; month++) {
            String start = year + "-" + String.format("%02d", month) + "-01";
            String end = year + "-" + String.format("%02d", month) + "-31";
            QueryWrapper<Transaction> incomeWrapper = new QueryWrapper<>();
            incomeWrapper.eq("user_id", userId).eq("type", 1)
                    .ge("transaction_date", start).le("transaction_date", end);
            BigDecimal income = transactionMapper.selectList(incomeWrapper).stream()
                    .map(Transaction::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
            QueryWrapper<Transaction> expenseWrapper = new QueryWrapper<>();
            expenseWrapper.eq("user_id", userId).eq("type", 0)
                    .ge("transaction_date", start).le("transaction_date", end);
            BigDecimal expense = transactionMapper.selectList(expenseWrapper).stream()
                    .map(Transaction::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
            Map<String, Object> monthData = new HashMap<>();
            monthData.put("month", month);
            monthData.put("income", income);
            monthData.put("expense", expense);
            trend.add(monthData);
        }
        Map<String, Object> result = new HashMap<>();
        result.put("trend", trend);
        return result;
    }

    @Override
    public List<Map<String, Object>> getExpenseByCategory(Long userId, String month) {
        // 获取用户所有支出分类
        QueryWrapper<Category> catWrapper = new QueryWrapper<>();
        catWrapper.eq("user_id", userId).eq("type", 0);
        List<Category> categories = categoryMapper.selectList(catWrapper);
        List<Map<String, Object>> result = new ArrayList<>();
        for (Category cat : categories) {
            QueryWrapper<Transaction> wrapper = new QueryWrapper<>();
            wrapper.eq("user_id", userId).eq("type", 0).eq("category_id", cat.getId());
            if (month != null) {
                wrapper.apply("DATE_FORMAT(transaction_date, '%Y-%m') = {0}", month);
            }
            BigDecimal total = transactionMapper.selectList(wrapper).stream()
                    .map(Transaction::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
            Map<String, Object> item = new HashMap<>();
            item.put("categoryName", cat.getName());
            item.put("amount", total);
            result.add(item);
        }
        return result;
    }

    @Override
    public List<Map<String, Object>> getIncomeByCategory(Long userId, String month) {
        // 类似支出，略
        return new ArrayList<>();
    }
}