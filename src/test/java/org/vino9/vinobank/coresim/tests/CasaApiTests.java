package org.vino9.vinobank.coresim.tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.vino9.vinobank.coresim.casa.data.AccountRepository;
import org.vino9.vinobank.coresim.casa.data.TransactionRepository;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CasaApiTests {
    @Autowired
    MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private TransactionRepository transactionRepository;

    @Test
    void testGetAccountDetail() throws Exception {
        mockMvc.perform(get("/api/casa/accounts/A522190859")).andExpect(status().is2xxSuccessful());
    }

    @Test
    void testGetAccountNotFound() throws Exception {
        mockMvc.perform(get("/api/casa/accounts/bad_account")).andExpect(status().isNotFound());
    }

    @Test
    void testTransferSuccess() throws Exception {
        var debitAccountNum = "A834666497";
        var creditAccountNum = "A522190859";
        var amount = new BigDecimal("35.23");
        var refId = UUID.randomUUID().toString().replace("-", "");
        var payload = objectMapper.writeValueAsString(Map.of(
                "ref_id", refId,
                "trx_date", "2024-06-12",
                "debit_account_num", debitAccountNum,
                "credit_account_num", creditAccountNum,
                "amount", amount,
                "currency", "USD",
                "memo", "Test transfer"
        ));

        var result = accountRepository.findByAccountNum(debitAccountNum);
        if (result.isEmpty()){
            throw new RuntimeException("Debit account not found");
        }
        var prevDebitAccount = result.get();
        var prevDebitAccountTransactionsCount = transactionRepository.findByTransactionsByAccount(prevDebitAccount).size();

        result = accountRepository.findByAccountNum(creditAccountNum);
        if (result.isEmpty()){
            throw new RuntimeException("Debit account not found");
        }
        var prevCreditAccount = result.get();
        var prevCreditAccountTransactionsCount = transactionRepository.findByTransactionsByAccount(prevCreditAccount).size();

        mockMvc.perform(post("/api/casa/transfers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isCreated());

        result = accountRepository.findByAccountNum(debitAccountNum);
        if (result.isEmpty()){
            throw new RuntimeException("Debit account not found");
        }
        var currentDebitAccount = result.get();
        var currentDebitAccountTransactionsCount = transactionRepository.findByTransactionsByAccount(currentDebitAccount).size();

        result = accountRepository.findByAccountNum(creditAccountNum);
        if (result.isEmpty()){
            throw new RuntimeException("Debit account not found");
        }
        var currentCreditAccount = result.get();
        var currentCreditAccountTransactionsCount = transactionRepository.findByTransactionsByAccount(currentCreditAccount).size();

        assertEquals( amount.negate(), currentDebitAccount.getBalance().subtract(prevDebitAccount.getBalance()));
        assertEquals( amount, currentCreditAccount.getBalance().subtract(prevCreditAccount.getBalance()));
        assertEquals(1, currentDebitAccountTransactionsCount - prevDebitAccountTransactionsCount);
        assertEquals(1, currentCreditAccountTransactionsCount - prevCreditAccountTransactionsCount);

    }

    @Test
    void testTransferInvalidPayload() throws Exception {
        var payload = objectMapper.writeValueAsString(Map.of(
                "red_id", "some_very_unique_value",
                "credit_account_num", "A522190860",
                "amount", "100.00",
                "currency", "USD",
                "memo", "Test transfer"
        ));

        mockMvc.perform(post("/api/casa/transfers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testTransferCannotProcess() throws Exception {
        var payload = objectMapper.writeValueAsString(Map.of(
                "red_id", "some_very_unique_value",
                "trx_date", "2024-06-12",
                "debit_account_num", "bad-account",
                "credit_account_num", "A522190860",
                "amount", "100.00",
                "currency", "USD",
                "memo", "Test transfer"
        ));

        mockMvc.perform(post("/api/casa/transfers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isUnprocessableEntity());
    }

}
