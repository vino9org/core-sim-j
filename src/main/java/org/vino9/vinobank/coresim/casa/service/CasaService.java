package org.vino9.vinobank.coresim.casa.service;

import de.huxhorn.sulky.ulid.ULID;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.vino9.vinobank.coresim.casa.api.AccountSchema;
import org.vino9.vinobank.coresim.casa.api.TransferRequestSchema;
import org.vino9.vinobank.coresim.casa.api.TransferSchema;
import org.vino9.vinobank.coresim.casa.data.*;
import org.vino9.vinobank.coresim.exception.NotFoundException;
import org.vino9.vinobank.coresim.exception.ValidationException;

@Service
@Slf4j
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
    public TransferSchema transfer(TransferRequestSchema transfer) throws ValidationException {
        String trxId = ulid.nextULID();
        String refId = transfer.getRefId();

        var accounts = accountRepository.findByAccountsForTransfer(transfer.getDebitAccountNum(), transfer.getCreditAccountNum());
        if (accounts.size() != 2) {
            throw new ValidationException("Invalid debit or credit account");
        }

        var debitAccount = accounts.get(0);
        var creditAccount = accounts.get(1);

        if (debitAccount.getAccountNum().equals(transfer.getCreditAccountNum())) {
            debitAccount = accounts.get(1);
            creditAccount = accounts.get(0);
        }

        var transferAmount = transfer.getAmount();
        if (debitAccount.getAvailBalance().compareTo(transferAmount) < 0) {
            throw new ValidationException("Insufficient funds in debit account");
        }

        debitAccount.setAvailBalance(debitAccount.getAvailBalance().subtract(transferAmount));
        debitAccount.setBalance(debitAccount.getBalance().subtract(transferAmount));
        accountRepository.save(debitAccount);

        Transaction debitTransaction = new Transaction();
        debitTransaction.setRefId(refId);
        debitTransaction.setTrxId(trxId);
        debitTransaction.setTrxDate(transfer.getTrxDate());
        debitTransaction.setCurrency(transfer.getCurrency());
        debitTransaction.setAmount(transferAmount.negate());
        debitTransaction.setMemo(transfer.getMemo());
        debitTransaction.setRunningBalance(debitAccount.getBalance());
        debitTransaction.setAccount(debitAccount);
        transactionRepository.save(debitTransaction);

        creditAccount.setAvailBalance(creditAccount.getAvailBalance().add(transferAmount));
        creditAccount.setBalance(creditAccount.getBalance().add(transferAmount));
        accountRepository.save(creditAccount);

        Transaction creditTransaction = new Transaction();
        creditTransaction.setRefId(refId);
        creditTransaction.setTrxId(trxId);
        creditTransaction.setTrxDate(transfer.getTrxDate());
        creditTransaction.setCurrency(transfer.getCurrency());
        creditTransaction.setAmount(transferAmount);
        creditTransaction.setMemo("from " + transfer.getDebitAccountNum() + ": " + transfer.getMemo());
        creditTransaction.setRunningBalance(creditAccount.getBalance());
        creditTransaction.setAccount(creditAccount);
        transactionRepository.save(creditTransaction);

        Transfer transferObj = new Transfer();
        transferObj.setRefId(refId);
        transferObj.setTrxId(trxId);
        transferObj.setTrxDate(transfer.getTrxDate());
        transferObj.setCurrency(transfer.getCurrency());
        transferObj.setAmount(transferAmount);
        transferObj.setMemo(transfer.getMemo());
        transferObj.setDebitAccountNum(transfer.getDebitAccountNum());
        transferObj.setCreditAccountNum(transfer.getCreditAccountNum());
        transferRepository.save(transferObj);

        log.info("Create transfer for ref_id {} of amount {}", refId, transferAmount);
        return mapper.map(transferObj, TransferSchema.class);
    }
}
