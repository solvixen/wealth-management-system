package com.finance.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.finance.entity.Transaction;
import java.util.List;

public interface TransactionService extends IService<Transaction> {
    List<Transaction> listByUser(Long userId, String month, Long categoryId);
}