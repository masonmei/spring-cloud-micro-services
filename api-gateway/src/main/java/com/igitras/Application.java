package com.igitras;

import com.igitras.custom.feign.CustomFeignConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.core.NamedInheritableThreadLocal;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by mason on 4/11/16.
 */
@SpringBootApplication
@EnableFeignClients(defaultConfiguration = CustomFeignConfiguration.class)
@EnableZuulProxy
public class Application {

    public static final ThreadLocal<String> th = new NamedInheritableThreadLocal<>("tes");

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public HiddenHttpMethodFilter methodFilter(){
        return new HiddenHttpMethodFilter();
    }

    @Bean
    public OncePerRequestFilter addHeaderFilter(){
        return new OncePerRequestFilter() {
            @Override
            protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                    FilterChain filterChain) throws ServletException, IOException {
                try {
                    th.set("test");
                    filterChain.doFilter(request, response);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ServletException e) {
                    e.printStackTrace();
                } finally {
                    th.remove();
                }
            }
        };
    }
}
