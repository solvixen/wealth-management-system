package com.finance.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.finance.entity.Transaction;
import com.finance.mapper.TransactionMapper;
import com.finance.service.TransactionService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TransactionServiceImpl extends ServiceImpl<TransactionMapper, Transaction> implements TransactionService {
    @Override
    public List<Transaction> listByUser(Long userId, String month, Long categoryId) {
        QueryWrapper<Transaction> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        if (month != null && !month.isEmpty()) {
            wrapper.apply("DATE_FORMAT(transaction_date, '%Y-%m') = {0}", month);
        }
        if (categoryId != null) {
            wrapper.eq("category_id", categoryId);
        }
        wrapper.orderByDesc("transaction_date");
        return this.list(wrapper);
    }
}