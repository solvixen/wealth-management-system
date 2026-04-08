package com.finance.controller;

import com.finance.dto.LoginDto;
import com.finance.dto.RegisterDto;
import com.finance.dto.Result;
import com.finance.entity.User;
import com.finance.service.UserService;
import com.finance.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private UserService userService;
    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/register")
    public Result register(@Validated @RequestBody RegisterDto dto) {
        if (userService.findByUsername(dto.getUsername()) != null) {
            return Result.error("用户名已存在");
        }
        User user = new User();
        user.setUsername(dto.getUsername());
        String encrypted = DigestUtils.md5DigestAsHex(dto.getPassword().getBytes(StandardCharsets.UTF_8));
        user.setPassword(encrypted);
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        userService.save(user);
        return Result.success("注册成功");
    }

    @PostMapping("/login")
    public Result login(@Validated @RequestBody LoginDto dto) {
        User user = userService.findByUsername(dto.getUsername());
        if (user == null) {
            return Result.error("用户不存在");
        }
        String encrypted = DigestUtils.md5DigestAsHex(dto.getPassword().getBytes(StandardCharsets.UTF_8));
        if (!user.getPassword().equals(encrypted)) {
            return Result.error("密码错误");
        }
        String token = jwtUtils.generateToken(user.getId(), user.getUsername());
        return Result.success(token);
    }
}