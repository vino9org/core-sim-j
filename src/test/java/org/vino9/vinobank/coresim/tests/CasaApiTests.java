package org.vino9.vinobank.coresim.tests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CasaApiTests {
    @Autowired
    MockMvc mockMvc;

    @Test
    void testGetAccountDetail() throws Exception {
        mockMvc.perform(get("/api/casa/accounts/A522190859")).andExpect(status().is2xxSuccessful());
    }
}
