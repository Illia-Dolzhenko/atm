package com.dolzhik.atm;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
public class ATMControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldReturnOkForFirstAccount() throws Exception {
        mockMvc.perform(get("/balance?account=123456789&password=1234"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(CoreMatchers.containsString("800")));
    }

    @Test
    public void shouldReturnOkForSecondAccount() throws Exception {
        mockMvc.perform(get("/balance?account=987654321&password=4321"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(CoreMatchers.containsString("1230")));
    }

    @Test
    public void shouldReturnInvalidPass() throws Exception {
        mockMvc.perform(get("/balance?account=123456789&password=invalid"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(CoreMatchers.containsString("Invalid")));
    }

    @Test
    public void shouldReturnInvalidAccount() throws Exception {
        mockMvc.perform(get("/balance?account=invalid&password=1234"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(CoreMatchers.containsString("Invalid")));
    }

    @Test
    public void shouldReturnInvalidPassAndAccount() throws Exception {
        mockMvc.perform(get("/balance?account=invalid&password=invalid"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(CoreMatchers.containsString("Invalid")));
    }

    @Test
    @DirtiesContext
    public void shouldReturnOkWithBanknotes() throws Exception {
        mockMvc.perform(post("/withdraw?account=123456789&password=1234&amount=500"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(CoreMatchers.containsString("successful")));
    }

    @Test
    @DirtiesContext
    public void shouldReturnOkWithNotEnoughMoney() throws Exception {
        mockMvc.perform(post("/withdraw?account=123456789&password=1234&amount=5000"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .string(CoreMatchers.containsString("You don't have enough money on you balance.")));
    }

    @Test
    @DirtiesContext
    public void shouldReturnOkWithNotEnoughBanknotes() throws Exception {
        mockMvc.perform(post("/withdraw?account=123456789&password=1234&amount=1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .string(CoreMatchers.containsString("Not enough cash in the ATM.")));
    }

    @Test
    public void shouldReturnOkWithInvalidAccount() throws Exception {
        mockMvc.perform(post("/withdraw?account=invalid&password=1234&amount=1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .string(CoreMatchers.containsString("Invalid")));
    }

    @Test
    public void shouldReturnOkWithInvalidPass() throws Exception {
        mockMvc.perform(post("/withdraw?account=123456789&password=invalid&amount=1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .string(CoreMatchers.containsString("Invalid")));
    }

    @Test
    public void shouldReturnOkWithInvalidAccountAndPass() throws Exception {
        mockMvc.perform(post("/withdraw?account=invalid&password=invalid&amount=1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .string(CoreMatchers.containsString("Invalid")));
    }

    @Test
    @DirtiesContext
    public void shouldReturnOkForTwoAccounts() throws Exception {
        mockMvc.perform(post("/withdraw?account=123456789&password=1234&amount=1000"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .string(CoreMatchers.containsString("successful")));
        mockMvc.perform(post("/withdraw?account=987654321&password=4321&amount=500"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .string(CoreMatchers.containsString("successful")));
    }
}