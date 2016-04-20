package com.igitras.custom.security;

import org.springframework.http.HttpMethod;

import java.io.Serializable;

/**
 * @author mason
 */
public interface AuthorizeConfig extends Serializable {

    String getPattern();

    HttpMethod getHttpMethod();

    boolean isCaseSensitive();

    String getExpression();

    String getRequestAttribute();

}
