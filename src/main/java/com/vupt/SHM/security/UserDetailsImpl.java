package com.vupt.SHM.security;

import com.vupt.SHM.entity.Account;
import com.vupt.SHM.entity.Role;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Data
public class UserDetailsImpl implements UserDetails {
    private long id;
    private String username;
    private String password;
    private boolean isSuspended;
    private Collection<? extends GrantedAuthority> authorities;


    public UserDetailsImpl(long id, String username, String password, boolean isSuspended, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.authorities = authorities;
        this.isSuspended = isSuspended;
    }


    public static UserDetailsImpl build(Account account) {
        List<Role> roles = account.getRoles();
        Set<GrantedAuthority> grantedRoleList = roles.stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role.getCode())).collect(Collectors.toSet());
        Set<GrantedAuthority> grantedAuthoritySet = new HashSet<>();
        grantedAuthoritySet.addAll(grantedRoleList);
        roles.stream().forEach(role -> role.getAuthorities().forEach((appAuthority -> grantedAuthoritySet.add(new SimpleGrantedAuthority(appAuthority.getCode())))));

        return new UserDetailsImpl(account.getId(), account.getUsername(), account.getPassword(), account.isSuspended(), grantedAuthoritySet);
    }


    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }


    public boolean isAccountNonExpired() {
        return true;
    }


    public boolean isAccountNonLocked() {
        return !this.isSuspended;
    }


    public boolean isCredentialsNonExpired() {
        return true;
    }


    public boolean isEnabled() {
        return true;
    }

}

