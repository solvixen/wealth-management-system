package com.finance.controller;

import com.finance.dto.BudgetDto;
import com.finance.dto.Result;
import com.finance.service.BudgetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/budgets")
public class BudgetController {
    @Autowired
    private BudgetService budgetService;

    @PostMapping
    public Result setBudget(@RequestBody BudgetDto dto, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        budgetService.setBudget(userId, dto);
        return Result.success("预算设置成功");
    }

    @GetMapping
    public Result list(HttpServletRequest request, @RequestParam String month) {
        Long userId = (Long) request.getAttribute("userId");
        return Result.success(budgetService.listByUser(userId, month));
    }

    @GetMapping("/progress")
    public Result progress(HttpServletRequest request, @RequestParam String month) {
        Long userId = (Long) request.getAttribute("userId");
        Map<String, Object> progress = (Map) budgetService.getBudgetProgress(userId, month);
        return Result.success(progress);
    }
}