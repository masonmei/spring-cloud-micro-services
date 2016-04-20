package com.igitras.custom.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParseException;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.access.vote.AuthenticatedVoter;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.access.vote.UnanimousBased;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
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
 * Custom Filter Security Interceptor with support authentication, role based, and request info bases Interceptor.
 *
 * @author mason
 */
@Component
public class CustomFilterSecurityInterceptor extends FilterSecurityInterceptor {

    @Autowired(required = false)
    private SecurityExpressionHandler<FilterInvocation> expressionHandler = new DefaultWebSecurityExpressionHandler();

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private AuthorizeConfigService authorizeConfigService;

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
        decisionVoters.add(new CustomRequestInfoVoter());
        decisionVoters.add(new RoleVoter());
        CustomWebExpressionVoter expressionVoter = new CustomWebExpressionVoter();
        expressionVoter.setExpressionHandler(expressionHandler);
        decisionVoters.add(expressionVoter);
        return new UnanimousBased(decisionVoters);
    }

    private FilterInvocationSecurityMetadataSource securityMetadataSource() {
        LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>> requestMap = loadFromDatabase();
        return new CustomSecurityMetadataSource(requestMap);
    }

    private LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>> loadFromDatabase() {
        LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>> configs = new LinkedHashMap<>();
        ExpressionParser parser = expressionHandler.getExpressionParser();
        List<AuthorizeConfig> authorizeConfigs = authorizeConfigService.getAuthorizeConfigs();
        authorizeConfigs.stream()
                .forEach(authorizeConfig -> {
                    RequestMatcher requestMatcher = new AntPathRequestMatcher(authorizeConfig.getPattern(),
                            authorizeConfig.getHttpMethod()
                                    .name(), authorizeConfig.isCaseSensitive());
                    List<ConfigAttribute> attributeList = new ArrayList<>();
                    attributeList.addAll(SecurityConfig.createList(authorizeConfig.getRequestAttribute()));

                    try {
                        attributeList.add(new CustomWebExpressionConfigAttribute(
                                parser.parseExpression(authorizeConfig.getExpression())));
                    } catch (ParseException e) {
                        throw new IllegalArgumentException(
                                "Failed to parse expression '" + authorizeConfig.getExpression() + "'");
                    }
                    configs.put(requestMatcher, attributeList);
                });
        return configs;
    }
}
