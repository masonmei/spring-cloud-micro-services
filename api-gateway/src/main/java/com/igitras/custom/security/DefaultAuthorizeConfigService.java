package com.igitras.custom.security;

import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * Default Authorize config service implementation.
 *
 * @author mason
 */
@Component
public class DefaultAuthorizeConfigService implements AuthorizeConfigService {

    @Override
    public List<AuthorizeConfig> getAuthorizeConfigs() {
        DefaultAuthorizeConfig authorizeConfig = new DefaultAuthorizeConfig();
        authorizeConfig.setPattern("/api/users/**");
        authorizeConfig.setHttpMethod(HttpMethod.GET);
        authorizeConfig.setRequestAttribute("/api/users/{PV_USERNAME}/actions/**");
        authorizeConfig.setExpression("hasAnyRole('ROLE_ANONYMOUS', 'ROLE_USER')");
        return Arrays.asList(authorizeConfig);
    }
}
