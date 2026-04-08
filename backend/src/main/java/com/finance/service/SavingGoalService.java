package com.finance.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.finance.entity.SavingGoal;
import java.util.List;

public interface SavingGoalService extends IService<SavingGoal> {
    List<SavingGoal> listByUser(Long userId);
    void updateProgress(Long userId);
}