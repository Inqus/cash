package test.cash.service;

import org.springframework.data.domain.Page;
import test.cash.model.Transaction;

public interface TransactionService {

    void saveTransaction(Transaction transaction);

    void saveTransactionAfterWithdraw(long id, String code);

    Transaction findById(long id);

    void deleteTransactionById(long id);

    Page<Transaction> findPaginated(int pageNum, int pageSize,
                                    String sortField,
                                    String sortDirection);
}
