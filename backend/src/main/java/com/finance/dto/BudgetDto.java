package com.finance.dto;

import java.math.BigDecimal;

public class BudgetDto {
    private Long categoryId;   // null表示总预算
    private String month;      // YYYY-MM
    private BigDecimal amount;

    public BudgetDto() {}

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}