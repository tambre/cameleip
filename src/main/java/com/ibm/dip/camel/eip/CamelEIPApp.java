package com.ibm.dip.camel.eip;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.ibm.dip"})
public class CamelEIPApp
{
    public static void main(String[] args)
    {
        SpringApplication.run(CamelEIPApp.class, args);
    }
}
