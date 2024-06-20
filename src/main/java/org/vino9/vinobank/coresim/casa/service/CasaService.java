package org.vino9.vinobank.coresim.casa.service;

import de.huxhorn.sulky.ulid.ULID;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.vino9.vinobank.coresim.casa.api.AccountSchema;
import org.vino9.vinobank.coresim.casa.api.TransferSchema;
import org.vino9.vinobank.coresim.casa.data.*;
import org.vino9.vinobank.coresim.exception.NotFoundException;
import org.vino9.vinobank.coresim.exception.ValidationException;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class CasaService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final TransferRepository transferRepository;

    private final ModelMapper mapper;

    private final ULID ulid = new ULID();

    public CasaService(AccountRepository accountRepository, TransactionRepository transactionRepository, TransferRepository transferRepository, ModelMapper mapper) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.transferRepository = transferRepository;
        this.mapper = mapper;
    }

    public AccountSchema getAccountDetail(String accountNum) throws NotFoundException {

        var account = accountRepository.findByAccountNum(accountNum);
        if (account.isEmpty()) {
            throw new NotFoundException(String.format("Account %s not found or inactive", accountNum));
        }

        return mapper.map(account.get(), AccountSchema.class);
    }


    @Transactional
    public TransferSchema transfer(TransferSchema transfer) throws ValidationException {
        LocalDateTime nowDt = LocalDateTime.now(); // Replace with your _now_dt_ method

        if (transfer.getRefId() == null || transfer.getRefId().isEmpty()) {
            transfer.setRefId(generateRefId()); // Replace with your ulid.new() equivalent
        }

        Account debitAccount = accountRepository.findByAccountNum(transfer.getDebitAccountNum())
                .orElseThrow(() -> new ValidationException("Invalid debit account"));

        var transferAmount = BigDecimal.valueOf(transfer.getAmount());

        if (debitAccount.getAvailBalance().compareTo(transferAmount) < 0) {
            throw new ValidationException("Insufficient funds in debit account");
        }

        Account creditAccount = accountRepository.findByAccountNum(transfer.getCreditAccountNum())
                .orElseThrow(() -> new ValidationException("Invalid credit account"));

        debitAccount.setAvailBalance(debitAccount.getAvailBalance().subtract(transferAmount));
        debitAccount.setBalance(debitAccount.getBalance().subtract(transferAmount));
        accountRepository.save(debitAccount);

        Transaction debitTransaction = new Transaction();
        debitTransaction.setRefId(transfer.getRefId());
        debitTransaction.setTrxDate(transfer.getTrxDate());
        debitTransaction.setCurrency(transfer.getCurrency());
        debitTransaction.setAmount(transferAmount.negate());
        debitTransaction.setMemo(transfer.getMemo());
        debitTransaction.setAccount(debitAccount);
        debitTransaction.setCreatedAt(nowDt);
        transactionRepository.save(debitTransaction);

        creditAccount.setAvailBalance(creditAccount.getAvailBalance().add(transferAmount));
        creditAccount.setBalance(creditAccount.getBalance().add(transferAmount));
        accountRepository.save(creditAccount);

        Transaction creditTransaction = new Transaction();
        creditTransaction.setRefId(transfer.getRefId());
        creditTransaction.setTrxDate(transfer.getTrxDate());
        creditTransaction.setCurrency(transfer.getCurrency());
        creditTransaction.setAmount(transferAmount);
        creditTransaction.setMemo("from " + transfer.getDebitAccountNum() + ": " + transfer.getMemo());
        creditTransaction.setAccount(creditAccount);
        creditTransaction.setCreatedAt(nowDt);
        transactionRepository.save(creditTransaction);

        Transfer transferObj = new Transfer();
        transferObj.setRefId(transfer.getRefId());
        transferObj.setTrxDate(transfer.getTrxDate());
        transferObj.setCurrency(transfer.getCurrency());
        transferObj.setAmount(transferAmount);
        transferObj.setMemo(transfer.getMemo());
        transferObj.setDebitAccountNum(transfer.getDebitAccountNum());
        transferObj.setCreditAccountNum(transfer.getCreditAccountNum());
        transferObj.setCreatedAt(nowDt);
        transferRepository.save(transferObj);

        return mapper.map(transferObj, TransferSchema.class);
    }

    private String generateRefId() {
        // Implement your ulid.new() equivalent here
        return ulid.nextULID();
    }
}
