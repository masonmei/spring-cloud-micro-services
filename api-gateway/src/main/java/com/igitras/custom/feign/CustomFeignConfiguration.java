package com.igitras.custom.feign;

import static org.springframework.http.HttpHeaders.ACCEPT;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.igitras.Application;
import feign.Contract;
import feign.Feign;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.netflix.feign.FeignAutoConfiguration;
import org.springframework.cloud.netflix.feign.annotation.PathVariableParameterProcessor;
import org.springframework.cloud.netflix.feign.support.SpringMvcContract;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.security.Principal;

/**
 * @author mason
 */
@Configuration
@ConditionalOnClass(Feign.class)
@AutoConfigureBefore(FeignAutoConfiguration.class)
public class CustomFeignConfiguration {

    @Bean
    public RequestInterceptor headerInterceptor() {
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate requestTemplate) {
                requestTemplate.header(ACCEPT, APPLICATION_JSON_UTF8_VALUE);
                requestTemplate.header(CONTENT_TYPE, APPLICATION_JSON_VALUE);
            }
        };
    }

    @Bean
    public RequestInterceptor customHeaderInterceptor() {
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate template) {
                template.header("test-header", Application.th.get());
            }
        };
    }

    @Bean
    public RequestInterceptor authHeaderInterceptor() {
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate requestTemplate) {
                SecurityContext context = SecurityContextHolder.getContext();
                Object principal = context.getAuthentication().getPrincipal();
                String username;
                if(principal instanceof String){
                    username = (String) principal;
                } else if (principal instanceof Principal){
                    Principal principal1 = (Principal) principal;
                    username = principal1.getName();
                } else {
                    username = principal.toString();
                }

                requestTemplate.header("X-USERNAME", username);
            }
        };
    }

    @Bean
    public Contract feignContract() {
        return new Contract.Default();
    }

}
