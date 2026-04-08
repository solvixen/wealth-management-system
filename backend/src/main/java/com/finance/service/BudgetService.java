package com.finance.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.finance.entity.Budget;
import com.finance.dto.BudgetDto;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface BudgetService extends IService<Budget> {
    void setBudget(Long userId, BudgetDto dto);
    List<Budget> listByUser(Long userId, String month);
    Map<String, BigDecimal> getBudgetProgress(Long userId, String month);
}