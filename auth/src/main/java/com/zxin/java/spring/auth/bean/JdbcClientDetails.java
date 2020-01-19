package com.zxin.java.spring.auth.bean;

import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.provider.ClientDetails;

import java.util.*;

@Data
@Builder
public class JdbcClientDetails implements ClientDetails {

    private String clientId;

    private String clientSecret;

    private Set<String> scope = Collections.emptySet();

    private Set<String> resourceIds = Collections.emptySet();

    private Set<String> authorizedGrantTypes = Collections.emptySet();

    private Set<String> registeredRedirectUris;

    private Set<String> autoApproveScopes;

    private List<GrantedAuthority> authorities = Collections.emptyList();

    private Integer accessTokenValiditySeconds;

    private Integer refreshTokenValiditySeconds;

    private Map<String, Object> additionalInformation = new LinkedHashMap<String, Object>();


    @Override
    public boolean isSecretRequired() {
        return this.clientSecret != null;
    }

    @Override
    public boolean isScoped() {
        return this.scope != null && !this.scope.isEmpty();
    }

    @Override
    public Set<String> getRegisteredRedirectUri() {
        return registeredRedirectUris;
    }

    @Override
    public boolean isAutoApprove(String scope) {
        if (autoApproveScopes == null) {
            return false;
        }
        for (String auto : autoApproveScopes) {
            if (auto.equals("true") || scope.matches(auto)) {
                return true;
            }
        }
        return false;
    }


    public static ClientBuilder builder(){
        return new ClientBuilder();
    }

}
