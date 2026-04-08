package com.finance;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.finance.mapper")
public class WealthManagementApplication {
    public static void main(String[] args) {
        SpringApplication.run(WealthManagementApplication.class, args);
    }
}