package com.vupt.SHM.services;

import com.vupt.SHM.dto.AccountDto;
import com.vupt.SHM.dto.AccountExDto;
import com.vupt.SHM.dto.AccountPasswordChangingDto;
import com.vupt.SHM.entity.Account;
import com.vupt.SHM.exceptions.AppException;
import com.vupt.SHM.mapstruct.mapper.MapstructMapper;
import com.vupt.SHM.repositories.AccountRepository;
import com.vupt.SHM.specifications.AccountSpecification;
import com.vupt.SHM.utils.DisplayTextUtils;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class AccountService {
    @Autowired
    AccountRepository accountRepo;
    @Autowired
    MapstructMapper mapstructMapper;
    @Autowired
    PasswordEncoder passwordEncoder;

    public List<AccountDto> findAll() {
        return (List<AccountDto>) this.accountRepo.findAll().stream()
                .map(account -> this.mapstructMapper.accountToAccountDto(account))
                .collect(Collectors.toList());
    }


    public Account findById(long id) {
        return (Account) this.accountRepo.findById(Long.valueOf(id))
                .orElseThrow(() -> new AppException(DisplayTextUtils.getNotFoundMessage("Tài khoản", "id", Long.valueOf(id))));
    }

    public Account findByUsername(String username) {
        return (Account) this.accountRepo.findByUsername(username)
                .orElseThrow(() -> new AppException(DisplayTextUtils.getNotFoundMessage("Tài khoản", "username", username)));
    }

    public Account findAccountWithRolesById(long id) {
        return (Account) this.accountRepo.findAccountWithRolesById(id)
                .orElseThrow(() -> new AppException(DisplayTextUtils.getNotFoundMessage("Tài khoản", "id", Long.valueOf(id))));
    }

    public void save(AccountDto accountDTO) {
        if (accountDTO.getId() == 0L) {
            Account account = this.mapstructMapper.accountDtoToAccount(accountDTO);
            account.setPassword(this.passwordEncoder.encode(account.getPassword()));
            this.accountRepo.save(account);
        } else {
            Account curAccount = findById(accountDTO.getId());
            this.mapstructMapper.accountDtoToSelectedAccount(accountDTO, curAccount);
            this.accountRepo.save(curAccount);
        }
    }

    public void updateRoles(AccountExDto accountExDto) {
        Account curAccount = findById(accountExDto.getId());
        curAccount.setRoles(accountExDto.getRoles());
        this.accountRepo.save(curAccount);
    }

    public void updatePassword(AccountPasswordChangingDto accountPasswordChangingDto) {
        Account curAccount = findById(accountPasswordChangingDto.getId());
        String encodedNewPassword = this.passwordEncoder.encode(accountPasswordChangingDto.getNewPassword());
        if (!this.passwordEncoder.matches(accountPasswordChangingDto.getOldPassword(), curAccount.getPassword()))
            throw new AppException("Mật khẩu cũ không chính xác");
        if (this.passwordEncoder.matches(accountPasswordChangingDto.getNewPassword(), curAccount.getPassword()))
            throw new AppException("Mật khẩu mới trùng mật khẩu cũ!");
        if (accountPasswordChangingDto.getNewPassword().length() < 3) {
            throw new AppException("Mật khẩu mới phải ít nhất 3 kí tự!");
        }
        curAccount.setPassword(encodedNewPassword);
        this.accountRepo.save(curAccount);
    }

    public List<AccountDto> search(String fullName) {
        return this.mapstructMapper.accountsToAccountDtos(this.accountRepo.findAll(AccountSpecification.filterSearch(fullName)));
    }

    public void resetPassword(AccountDto selectedAccount) {
        Account curAccount = findById(selectedAccount.getId());
        curAccount.setPassword(this.passwordEncoder.encode("123"));
        this.accountRepo.save(curAccount);
    }
}

