package com.igitras.custom.security;

import com.google.common.base.MoreObjects;
import org.springframework.http.HttpMethod;

/**
 * Default Authorize configuration.
 *
 * @author mason
 */
public class DefaultAuthorizeConfig implements AuthorizeConfig {
    private static final long serialVersionUID = 3203055617976838289L;

    private String pattern;
    private HttpMethod httpMethod;
    private boolean isCaseSensitive;
    private String expression;
    private String requestAttribute;

    @Override
    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(HttpMethod httpMethod) {
        this.httpMethod = httpMethod;
    }

    @Override
    public boolean isCaseSensitive() {
        return isCaseSensitive;
    }

    public void setCaseSensitive(boolean caseSensitive) {
        isCaseSensitive = caseSensitive;
    }

    @Override
    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    @Override
    public String getRequestAttribute() {
        return requestAttribute;
    }

    public void setRequestAttribute(String requestAttribute) {
        this.requestAttribute = requestAttribute;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("pattern", pattern)
                .add("httpMethod", httpMethod)
                .add("isCaseSensitive", isCaseSensitive)
                .add("expression", expression)
                .add("requestAttribute", requestAttribute)
                .toString();
    }
}
