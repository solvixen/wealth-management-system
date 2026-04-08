package com.finance.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.finance.entity.Budget;
import com.finance.entity.Transaction;
import com.finance.mapper.BudgetMapper;
import com.finance.mapper.TransactionMapper;
import com.finance.service.BudgetService;
import com.finance.dto.BudgetDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BudgetServiceImpl extends ServiceImpl<BudgetMapper, Budget> implements BudgetService {
    @Autowired
    private TransactionMapper transactionMapper;

    @Override
    public void setBudget(Long userId, BudgetDto dto) {
        // 先删除旧预算
        QueryWrapper<Budget> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId)
                .eq("month", dto.getMonth());
        if (dto.getCategoryId() == null) {
            wrapper.isNull("category_id");
        } else {
            wrapper.eq("category_id", dto.getCategoryId());
        }
        this.remove(wrapper);
        // 新增
        Budget budget = new Budget();
        budget.setUserId(userId);
        budget.setCategoryId(dto.getCategoryId());
        budget.setMonth(dto.getMonth());
        budget.setAmount(dto.getAmount());
        this.save(budget);
    }

    @Override
    public List<Budget> listByUser(Long userId, String month) {
        QueryWrapper<Budget> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId).eq("month", month);
        return this.list(wrapper);
    }

    @Override
    public Map<String, BigDecimal> getBudgetProgress(Long userId, String month) {
        Map<String, BigDecimal> result = new HashMap<>();
        // 总预算进度
        QueryWrapper<Budget> budgetWrapper = new QueryWrapper<>();
        budgetWrapper.eq("user_id", userId).eq("month", month).isNull("category_id");
        Budget totalBudget = this.getOne(budgetWrapper);
        if (totalBudget != null) {
            // 当月总支出
            QueryWrapper<Transaction> expenseWrapper = new QueryWrapper<>();
            expenseWrapper.eq("user_id", userId)
                    .eq("type", 0)
                    .apply("DATE_FORMAT(transaction_date, '%Y-%m') = {0}", month);
            BigDecimal totalExpense = transactionMapper.selectList(expenseWrapper)
                    .stream().map(Transaction::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
            result.put("totalBudget", totalBudget.getAmount());
            result.put("totalExpense", totalExpense);
        }
        return result;
    }
}