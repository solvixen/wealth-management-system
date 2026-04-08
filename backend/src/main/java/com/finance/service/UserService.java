package com.finance.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.finance.entity.User;

public interface UserService extends IService<User> {
    User findByUsername(String username);
}