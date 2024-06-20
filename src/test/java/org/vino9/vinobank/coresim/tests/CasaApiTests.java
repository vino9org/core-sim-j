package org.vino9.vinobank.coresim.tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CasaApiTests {
    @Autowired
    MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

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
        var payload = objectMapper.writeValueAsString(Map.of(
                "trx_date", "2024-06-12",
                "debit_account_num", "A834666497",
                "credit_account_num", "A522190859",
                "amount", 100.0,
                "currency", "USD",
                "memo", "Test transfer"
        ));

        mockMvc.perform(post("/api/casa/transfers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isCreated());
    }

    @Test
    void testTransferInvalidPayload() throws Exception {
        var payload = objectMapper.writeValueAsString(Map.of(
                "credit_account_num", "A522190860",
                "amount", 100.0,
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
                "trx_date", "2024-06-12",
                "debit_account_num", "bad-account",
                "credit_account_num", "A522190860",
                "amount", 100.0,
                "currency", "USD",
                "memo", "Test transfer"
        ));

        mockMvc.perform(post("/api/casa/transfers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isUnprocessableEntity());
    }

}
