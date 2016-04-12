package com.igitras;

import com.igitras.custom.feign.CustomFeignConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * Created by mason on 4/11/16.
 */
@SpringBootApplication
@EnableFeignClients(defaultConfiguration = {CustomFeignConfiguration.class})
@EnableZuulProxy
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
