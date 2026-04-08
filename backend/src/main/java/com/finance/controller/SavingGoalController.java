package com.finance.controller;

import com.finance.dto.Result;
import com.finance.entity.SavingGoal;
import com.finance.service.SavingGoalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/saving-goals")
public class SavingGoalController {
    @Autowired
    private SavingGoalService savingGoalService;

    @PostMapping
    public Result add(@RequestBody SavingGoal goal, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        goal.setUserId(userId);
        savingGoalService.save(goal);
        return Result.success("储蓄目标添加成功");
    }

    @GetMapping
    public Result list(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        List<SavingGoal> list = savingGoalService.listByUser(userId);
        return Result.success(list);
    }

    @PutMapping("/{id}")
    public Result update(@PathVariable Long id, @RequestBody SavingGoal goal) {
        goal.setId(id);
        savingGoalService.updateById(goal);
        return Result.success("更新成功");
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        savingGoalService.removeById(id);
        return Result.success("删除成功");
    }

    @PostMapping("/refresh-progress")
    public Result refreshProgress(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        savingGoalService.updateProgress(userId);
        return Result.success("进度已刷新");
    }
}