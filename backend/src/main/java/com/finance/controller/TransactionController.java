package com.finance.controller;

import com.finance.dto.Result;
import com.finance.entity.Transaction;
import com.finance.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    @PostMapping
    public Result add(@RequestBody Transaction transaction, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        transaction.setUserId(userId);
        transactionService.save(transaction);
        return Result.success("添加成功");
    }

    @GetMapping
    public Result list(HttpServletRequest request,
                       @RequestParam(required = false) String month,
                       @RequestParam(required = false) Long categoryId) {
        Long userId = (Long) request.getAttribute("userId");
        List<Transaction> list = transactionService.listByUser(userId, month, categoryId);
        return Result.success(list);
    }

    @PutMapping("/{id}")
    public Result update(@PathVariable Long id, @RequestBody Transaction transaction) {
        transaction.setId(id);
        transactionService.updateById(transaction);
        return Result.success("更新成功");
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        transactionService.removeById(id);
        return Result.success("删除成功");
    }
}