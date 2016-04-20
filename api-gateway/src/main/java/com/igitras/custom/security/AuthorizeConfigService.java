package com.igitras.custom.security;

import java.util.List;

/**
 * Authorize configuration Provider.
 *
 * @author mason
 */
public interface AuthorizeConfigService {

    /**
     * Get the authorization configuration
     *
     * @return all the authorization Configurations.
     */
    List<AuthorizeConfig> getAuthorizeConfigs();

}
