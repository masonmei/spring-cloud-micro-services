package com.igitras.custom.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.FilterInvocation;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;

/**
 * Access Decision Voter with checking for Path Variables, Headers or Request Parameters.
 *
 * @author mason
 */
public class CustomRequestInfoVoter implements AccessDecisionVoter<FilterInvocation> {
    private static final Logger LOG = LoggerFactory.getLogger(CustomRequestInfoVoter.class);

    private static final String HEADER_PREFIX = "H_";
    private static final String PATH_PREFIX = "PV_";
    private static final String PARAM_PREFIX = "RP_";


    private String headerPrefix = HEADER_PREFIX;
    private String pathPrefix = PATH_PREFIX;
    private String paramPrefix = PARAM_PREFIX;

    @Override
    public boolean supports(ConfigAttribute attribute) {
        String attr = attribute.getAttribute();
        return attr != null && (attr.startsWith(headerPrefix) || attr.startsWith(paramPrefix)
                || attr.contains(pathPrefix));
    }

    @Override
    public int vote(Authentication authentication, FilterInvocation fi, Collection<ConfigAttribute> attributes) {
        assert authentication != null;
        assert fi != null;
        assert attributes != null;

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        if (CollectionUtils.isEmpty(authorities)) {
            LOG.debug("No authorities found, voting for denied");
            return ACCESS_DENIED;
        }

        for (ConfigAttribute attribute : attributes) {
            if (!supports(attribute)) {
                LOG.debug("ConfigAttribute {} is not support, ignored for voting.");
                continue;
            }

            String attr = attribute.getAttribute();
            Enumeration<String> attrValue = Collections.emptyEnumeration();
            if (attr.startsWith(headerPrefix)) {
                attrValue = obtainHeaderValue(fi, attr);
            } else if (attr.startsWith(paramPrefix)) {
                attrValue = obtainRequestParamValue(fi, attr);
            } else if (attr.contains(pathPrefix)) {
                attrValue = obtainPathVariableValue(fi, attr);
            }

            if (!attrValue.hasMoreElements()) {
                LOG.debug("Not found the required information for evaluate, voting for denied.");
                return ACCESS_DENIED;
            }

            boolean passed = false;

            while (attrValue.hasMoreElements()) {
                String next = attrValue.nextElement();
                for (GrantedAuthority authority : authorities) {
                    if (authority.getAuthority()
                            .equals(next)) {
                        LOG.debug("Authority meet the requirement found.");
                        passed = true;
                        break;
                    }
                }
                if (passed) {
                    break;
                }
            }

            if (!passed) {
                return ACCESS_DENIED;
            } else {
                return ACCESS_GRANTED;
            }
        }

        return ACCESS_ABSTAIN;
    }

    private Enumeration<String> obtainPathVariableValue(final FilterInvocation fi, final String attr) {
        Assert.hasText(attr, "Path variable must have test.");
        String requestUrl = fi.getRequestUrl();
        AntPathMatcher matcher = new AntPathMatcher();
        if (matcher.match(attr, requestUrl)) {
            Map<String, String> stringStringMap = matcher.extractUriTemplateVariables(attr, requestUrl);
            String authString = StringUtils.collectionToDelimitedString(stringStringMap.values(), ":");
            return Collections.enumeration(Arrays.asList(authString));
        }
        return Collections.emptyEnumeration();
    }

    private Enumeration<String> obtainRequestParamValue(FilterInvocation fi, String attr) {
        Assert.hasText(attr, "Request Param must have text.");

        String headerName = attr.substring(paramPrefix.length());

        String[] parameterValues = fi.getRequest()
                .getParameterValues(headerName);
        if (parameterValues == null) {
            return Collections.emptyEnumeration();
        } else {
            return Collections.enumeration(Arrays.asList(parameterValues));
        }
    }

    private Enumeration<String> obtainHeaderValue(FilterInvocation fi, String attr) {
        Assert.hasText(attr, "Request Header must have text.");
        String headerName = attr.substring(paramPrefix.length());
        return fi.getRequest()
                .getHeaders(headerName);
    }

    @Override
    public boolean supports(Class clazz) {
        return FilterInvocation.class.isAssignableFrom(clazz);
    }

    public String getHeaderPrefix() {
        return headerPrefix;
    }

    public void setHeaderPrefix(String headerPrefix) {
        Assert.hasText(paramPrefix, "Request Header prefix must has text");
        this.headerPrefix = headerPrefix;
    }

    public String getPathPrefix() {
        return pathPrefix;
    }

    public void setPathPrefix(String pathPrefix) {
        Assert.hasText(paramPrefix, "Path variable prefix must has text");
        this.pathPrefix = pathPrefix;
    }

    public String getParamPrefix() {
        return paramPrefix;
    }

    public void setParamPrefix(String paramPrefix) {
        Assert.hasText(paramPrefix, "Request parameter prefix must has text");
        this.paramPrefix = paramPrefix;
    }
}
