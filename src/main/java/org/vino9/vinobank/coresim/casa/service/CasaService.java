package org.vino9.vinobank.coresim.casa.service;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.vino9.vinobank.coresim.casa.api.AccountSchema;
import org.vino9.vinobank.coresim.casa.data.Account;
import org.vino9.vinobank.coresim.casa.data.AccountRepository;
import org.vino9.vinobank.coresim.exception.NotFoundException;

@Service
public class CasaService {

    private final AccountRepository accountRepository;

    private final ModelMapper mapper;

    public CasaService(AccountRepository accountRepository, ModelMapper mapper) {
        this.accountRepository = accountRepository;
        this.mapper = mapper;
    }

    public AccountSchema getAccountDetail(String accountNum) throws NotFoundException {

        var account = accountRepository.findByAccountNum(accountNum, Account.Status.active);
        if (account.isEmpty()) {
            throw new NotFoundException(String.format("Account %s not found or inactive", accountNum));
        }

        return mapper.map(account.get(), AccountSchema.class);
    }
}
