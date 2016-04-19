package com.igitras.custom.security;

import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;

import java.util.Collection;

/**
 * Created by mason on 4/19/16.
 */
public class CustomAccessDecisionVoter implements AccessDecisionVoter {
    @Override
    public boolean supports(ConfigAttribute attribute) {
        return false;
    }

    @Override
    public int vote(Authentication authentication, Object object, Collection collection) {
        return 0;
    }

    @Override
    public boolean supports(Class clazz) {
        return true;
    }
}
