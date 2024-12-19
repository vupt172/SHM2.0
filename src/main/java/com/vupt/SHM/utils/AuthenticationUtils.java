package com.vupt.SHM.utils;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class AuthenticationUtils {
    private Authentication authentication;

    public static com.vupt.SHM.utils.AuthenticationUtils getCurrentAuthenticationInfo() {
        return new com.vupt.SHM.utils.AuthenticationUtils(SecurityContextHolder.getContext().getAuthentication());
    }

    public AuthenticationUtils(Authentication authentication) {
        this.authentication = SecurityContextHolder.getContext().getAuthentication();
    }

    public Authentication getAuthentication() {
        return this.authentication;
    }


    public boolean hasAuthority(String authority) {
        List<GrantedAuthority> list1 = (List<GrantedAuthority>) this.authentication.getAuthorities();
        return this.authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(authority));
    }

    public UserDetails getUserDetails() {
        return (this.authentication == null) ? null : (UserDetails) this.authentication.getPrincipal();
    }
}

