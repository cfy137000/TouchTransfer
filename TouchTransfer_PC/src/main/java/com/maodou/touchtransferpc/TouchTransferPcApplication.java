package com.maodou.touchtransferpc;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class TouchTransferPcApplication {
    public static ConfigurableApplicationContext ctx;

    public static void main(String[] args) {
        SpringApplicationBuilder builder = new SpringApplicationBuilder(TouchTransferPcApplication.class);
        ctx  =builder.headless(false).run(args);
    }
}


