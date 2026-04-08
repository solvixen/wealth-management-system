package com.finance.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.finance.entity.SavingGoal;
import com.finance.entity.Transaction;
import com.finance.mapper.SavingGoalMapper;
import com.finance.mapper.TransactionMapper;
import com.finance.service.SavingGoalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;

@Service
public class SavingGoalServiceImpl extends ServiceImpl<SavingGoalMapper, SavingGoal> implements SavingGoalService {
    @Autowired
    private TransactionMapper transactionMapper;

    @Override
    public List<SavingGoal> listByUser(Long userId) {
        return this.list(new QueryWrapper<SavingGoal>().eq("user_id", userId));
    }

    @Override
    public void updateProgress(Long userId) {
        // 简单模拟：累计收入 - 累计支出 = 储蓄金额
        QueryWrapper<Transaction> incomeWrapper = new QueryWrapper<>();
        incomeWrapper.eq("user_id", userId).eq("type", 1);
        BigDecimal totalIncome = transactionMapper.selectList(incomeWrapper)
                .stream().map(Transaction::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        QueryWrapper<Transaction> expenseWrapper = new QueryWrapper<>();
        expenseWrapper.eq("user_id", userId).eq("type", 0);
        BigDecimal totalExpense = transactionMapper.selectList(expenseWrapper)
                .stream().map(Transaction::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal currentSaving = totalIncome.subtract(totalExpense);
        // 更新所有进行中目标的已存金额
        List<SavingGoal> goals = this.listByUser(userId);
        for (SavingGoal goal : goals) {
            if (goal.getStatus() == 0) {
                goal.setSavedAmount(currentSaving.min(goal.getTargetAmount()));
                if (goal.getSavedAmount().compareTo(goal.getTargetAmount()) >= 0) {
                    goal.setStatus(1);
                }
                this.updateById(goal);
            }
        }
    }
}