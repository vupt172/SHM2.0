package com.vupt.SHM.security;

import com.vupt.SHM.entity.Account;
import com.vupt.SHM.exceptions.AppException;
import com.vupt.SHM.repositories.AccountRepository;
import com.vupt.SHM.repositories.RoleRepository;
import com.vupt.SHM.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl
        implements UserDetailsService {
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    RoleRepository roleRepository;

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = (Account) this.accountRepository.findAccountWithRolesByUsername(username).orElseThrow(() -> new AppException("Username is incorrect"));

        return (UserDetails) UserDetailsImpl.build(account);
    }
}

