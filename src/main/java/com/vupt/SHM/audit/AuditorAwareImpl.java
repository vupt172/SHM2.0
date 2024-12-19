package com.vupt.SHM.audit;

import com.vupt.SHM.utils.AuthenticationUtils;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;


public class AuditorAwareImpl
        implements AuditorAware<String> {
    public Optional<String> getCurrentAuditor() {
        AuthenticationUtils authenticationUtils = AuthenticationUtils.getCurrentAuthenticationInfo();
        Authentication authentication = authenticationUtils.getAuthentication();
        String username = (authentication == null) ? "" : authenticationUtils.getUserDetails().getUsername();
        return Optional.of(username);
    }
}

