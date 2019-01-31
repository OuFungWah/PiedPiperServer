package com.crazywah;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.crazywah.dao")
public class PiedPiperApplication {

    public static void main(String[] args) {
        SpringApplication.run(PiedPiperApplication.class,args);
    }

}
