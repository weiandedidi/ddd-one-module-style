package com.ddd.example;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * 需要事务的时候打开
 */
@SpringBootApplication
//@EnableTransactionManagement
@EnableWebMvc
public class OneModuleApplication {
    public static void main(String[] args) {
        SpringApplication.run(OneModuleApplication.class, args);
    }
}