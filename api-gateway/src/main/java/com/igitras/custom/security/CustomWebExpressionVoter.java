package com.igitras.custom.security;

import org.springframework.expression.EvaluationContext;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.expression.ExpressionUtils;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.access.expression.WebExpressionVoter;

import java.util.Collection;

/**
 * @author mason
 */
public class CustomWebExpressionVoter extends WebExpressionVoter {
    private SecurityExpressionHandler<FilterInvocation> expressionHandler = new DefaultWebSecurityExpressionHandler();

    public int vote(Authentication authentication, FilterInvocation fi,
            Collection<ConfigAttribute> attributes) {
        assert authentication != null;
        assert fi != null;
        assert attributes != null;

        CustomWebExpressionConfigAttribute weca = findConfigAttribute(attributes);

        if (weca == null) {
            return ACCESS_ABSTAIN;
        }

        EvaluationContext ctx = expressionHandler.createEvaluationContext(authentication,
                fi);

        return ExpressionUtils.evaluateAsBoolean(weca.getAuthorizeExpression(), ctx) ? ACCESS_GRANTED
                : ACCESS_DENIED;
    }

    private CustomWebExpressionConfigAttribute findConfigAttribute(
            Collection<ConfigAttribute> attributes) {
        for (ConfigAttribute attribute : attributes) {
            if (attribute instanceof CustomWebExpressionConfigAttribute) {
                return (CustomWebExpressionConfigAttribute) attribute;
            }
        }
        return null;
    }

    public boolean supports(ConfigAttribute attribute) {
        return attribute instanceof CustomWebExpressionConfigAttribute;
    }

    public boolean supports(Class<?> clazz) {
        return FilterInvocation.class.isAssignableFrom(clazz);
    }

    public void setExpressionHandler(
            SecurityExpressionHandler<FilterInvocation> expressionHandler) {
        this.expressionHandler = expressionHandler;
    }

}
