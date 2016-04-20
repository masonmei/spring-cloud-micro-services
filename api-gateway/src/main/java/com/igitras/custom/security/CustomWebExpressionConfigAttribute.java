package com.igitras.custom.security;

import org.springframework.expression.Expression;
import org.springframework.security.access.ConfigAttribute;

/**
 * @author mason
 */
public class CustomWebExpressionConfigAttribute implements ConfigAttribute {
    private final Expression authorizeExpression;

    public CustomWebExpressionConfigAttribute(Expression authorizeExpression) {
        this.authorizeExpression = authorizeExpression;
    }

    Expression getAuthorizeExpression() {
        return authorizeExpression;
    }

    public String getAttribute() {
        return null;
    }

    @Override
    public String toString() {
        return authorizeExpression.getExpressionString();
    }
}
