package com.mpw;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Hello world!
 *
 */
@SpringBootApplication
@MapperScan("com.mpw.model.**.mapper")
public class RouteApplication
{
    public static void main( String[] args )
    {
        SpringApplication.run(RouteApplication.class, args);
    }
}
