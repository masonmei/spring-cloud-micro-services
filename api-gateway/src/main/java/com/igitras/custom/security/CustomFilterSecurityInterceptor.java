package com.igitras.custom.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.access.vote.AuthenticatedVoter;
import org.springframework.security.access.vote.UnanimousBased;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.access.expression.ExpressionBasedFilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.expression.WebExpressionVoter;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import javax.annotation.PostConstruct;

/**
 * Created by mason on 4/19/16.
 */
@Component
public class CustomFilterSecurityInterceptor extends FilterSecurityInterceptor {

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostConstruct
    public void postConstruct() {
        setObserveOncePerRequest(false);
        setAuthenticationManager(authenticationManager);
        setAccessDecisionManager(accessDecisionManager());
        setSecurityMetadataSource(securityMetadataSource());
    }

    private AccessDecisionManager accessDecisionManager() {
        List<AccessDecisionVoter<? extends Object>> decisionVoters = new ArrayList<>();
        decisionVoters.add(new AuthenticatedVoter());
        WebExpressionVoter voter = new WebExpressionVoter();
        decisionVoters.add(voter);
        return new UnanimousBased(decisionVoters);
    }

    private FilterInvocationSecurityMetadataSource securityMetadataSource() {
        LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>> requestMap = loadFromDatabase();
        SecurityExpressionHandler<FilterInvocation> expressionHandler = new DefaultWebSecurityExpressionHandler();
        return new ExpressionBasedFilterInvocationSecurityMetadataSource(requestMap, expressionHandler);
    }

    private LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>> loadFromDatabase() {
        RequestMatcher requestMatcher = new AntPathRequestMatcher("/api/users/**", HttpMethod.GET.name(), false);

        List<ConfigAttribute> list = SecurityConfig.createList("IN_PRODUCT");

        LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>> configs = new LinkedHashMap<>();
        configs.put(requestMatcher, list);
        return configs;
    }
}
