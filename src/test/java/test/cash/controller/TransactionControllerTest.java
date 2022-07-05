package test.cash.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import test.cash.model.Transaction;
import test.cash.model.User;
import test.cash.service.TransactionService;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TransactionControllerTest {

    @MockBean
    private TransactionService service;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("GET Transactions success")
    @WithMockUser
    void testGetWidgetsSuccess() throws Exception {
        // Setup our mocked service
        Transaction transaction1 = new Transaction();
        Transaction transaction2 = new Transaction();
        List<Transaction> listOfTransactions = Arrays.asList(transaction1, transaction2);
        Page<Transaction> page = new PageImpl<>(listOfTransactions);
        doReturn(page).when(service).findPaginated(1,3,"id", "asc");

        // Execute the GET request
        mockMvc.perform(get("/"))
                // Validate the response code and content type
                .andExpect(status().isOk());

    }

    @Test
    @DisplayName("Try withdraw non existing transaction - Not Found")
    @WithMockUser
    void testGetTransactionByIdNotFound() throws Exception {
        doReturn(null).when(service).findById(1l);

        mockMvc.perform(get("/withdraw/{id}", 1L))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @WithMockUser
    void test() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(authentication.getPrincipal());

        Assertions.assertTrue(true);
    }
}
