package com.finance.controller;

import com.finance.dto.Result;
import com.finance.entity.Category;
import com.finance.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public Result add(@RequestBody Category category, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        category.setUserId(userId);
        categoryService.save(category);
        return Result.success("分类添加成功");
    }

    @GetMapping
    public Result list(HttpServletRequest request,
                       @RequestParam(required = false) Integer type) {
        Long userId = (Long) request.getAttribute("userId");
        List<Category> list = categoryService.listByUser(userId, type);
        return Result.success(list);
    }

    @PutMapping("/{id}")
    public Result update(@PathVariable Long id, @RequestBody Category category) {
        category.setId(id);
        categoryService.updateById(category);
        return Result.success("更新成功");
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        categoryService.removeById(id);
        return Result.success("删除成功");
    }
}