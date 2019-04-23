package com.crazywah;

import com.crazywah.service.UserService;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.SQLException;

@SpringBootApplication
@MapperScan("com.crazywah.dao")
public class PiedPiperApplication {

    private static UserService service;

    public PiedPiperApplication(UserService service){
        PiedPiperApplication.service = service;
    }

    public static void main(String[] args) {
        SpringApplication.run(PiedPiperApplication.class,args);
        //启动数据库链接池，避免使用时再链接
        try {
            service.getUsers();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            service = null;
        }
    }

}
