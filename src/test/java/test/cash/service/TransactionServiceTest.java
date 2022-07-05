package test.cash.service;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import test.cash.model.CashBox;
import test.cash.model.Currency;
import test.cash.model.Transaction;
import test.cash.model.User;
import test.cash.repository.TransactionRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@SpringBootTest
public class TransactionServiceTest {

    @Autowired
    private TransactionService service;

    @MockBean
    private TransactionRepository repository;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @MockBean
    private CashBoxServiceImpl cashBoxService;

    @Test
    @DisplayName("Test findById Success")
    void testFindById() {
        Transaction transaction = new Transaction();
        transaction.setId(1l);
        doReturn(Optional.of(transaction)).when(repository).findById(1l);

        Transaction returnedTransaction = service.findById(1l);

        Assertions.assertNotNull(returnedTransaction, "Widget was not found");
        Assertions.assertSame(returnedTransaction, transaction, "The widget returned was not the same as the mock");
    }

    @Test
    @DisplayName("Test findById Not Found")
    void testFindByIdNotFound() {

        final long ID = 1L;
        doReturn(Optional.empty()).when(repository).findById(ID);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            Transaction returnedTransaction = service.findById(ID);
        });

        String expectedMessage = "Transaction not found";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    @DisplayName("Test findAll Paginated")
    void testFindAll() {
        Transaction transaction1 = new Transaction();
        Transaction transaction2 = new Transaction();
        List<Transaction> listOfTransactions = Arrays.asList(transaction1, transaction2);

        Page<Transaction> page = new PageImpl<>(listOfTransactions);
        Pageable pageable = PageRequest.of(0, 3, Sort.by("id").ascending());

        doReturn(page).when(repository).findAll(pageable);
        var pages =  service.findPaginated(1,3,"id", "asc");
        Assertions.assertEquals(2, pages.getContent().size(), "findAll should return 2 widgets");
    }


    @Test
    @DisplayName("Test save transaction")
    void testSave() {
        double transactionSum = 1000;
        Transaction transaction = new Transaction();
        transaction.setId(1L);
        transaction.setSum(transactionSum);
        transaction.setCurrency(new Currency(1L, "KGS"));

        CashBox cashBox = new CashBox(100L, 100000.0);
        User loggedUser = new User(100L, "fio", "user", "pass", "USER", cashBox);
        doReturn(loggedUser).when(userDetailsService).getLoggedUserDetails();
        doNothing().when(cashBoxService).debit(cashBox, transactionSum);
        service.saveTransaction(transaction);

        verify(repository,times(1)).save(transaction);
    }

}
