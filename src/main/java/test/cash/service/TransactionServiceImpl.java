package test.cash.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import test.cash.model.Transaction;
import test.cash.model.User;
import test.cash.repository.TransactionRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final CurrencyServiceImpl currencyService;
    private final UserDetailsServiceImpl userDetailsService;
    private final CashBoxServiceImpl cashBoxService;

    @Autowired
    public TransactionServiceImpl(TransactionRepository transactionRepository, CurrencyServiceImpl currencyService, UserDetailsServiceImpl userDetailsService, CashBoxServiceImpl cashBoxService) {
        this.transactionRepository = transactionRepository;
        this.currencyService = currencyService;
        this.userDetailsService = userDetailsService;
        this.cashBoxService = cashBoxService;
    }

    @Override
    @Transactional
    public void saveTransaction(Transaction transaction) {

        double transferAmount = currencyService.getConvertedTransferAmount(transaction.getSum(), transaction.getCurrency());
        User loggedUser = userDetailsService.getLoggedUserDetails();
        cashBoxService.debit(loggedUser.getCashBox(), transferAmount);

        String generatedCode = UUID.randomUUID().toString();
        transaction.setCode(generatedCode);
        transaction.setStatus(true);
        transaction.setCashBox(loggedUser.getCashBox());
        transaction.setCashier(loggedUser);
        transactionRepository.save(transaction);
    }

    @Override
    @Transactional
    public void saveTransactionAfterWithdraw(long id, String code) {
        var optionalTransaction = transactionRepository.findById(id);
        if (optionalTransaction.isPresent()) {
            var transaction = optionalTransaction.get();

            if (transaction.getCode().equals(code)) { // checking verification code's equality
                double transferAmount = currencyService.getConvertedTransferAmount(transaction.getSum(), transaction.getCurrency());
                cashBoxService.credit(transaction.getCashBox(), transferAmount);
                transaction.setStatus(false); // withdrawn
                transactionRepository.save(transaction);
                return;
            }
            throw new RuntimeException("Wrong verification code");
        }
        throw new RuntimeException("Transaction not exist");
    }

    @Override
    public Transaction findById(long id) {
        Optional<Transaction> optionalTransaction = transactionRepository.findById(id);
        if (optionalTransaction.isPresent()) {
            return optionalTransaction.get();
        } else {
            throw new RuntimeException("Transaction not found");
        }
    }

    @Override
    @Transactional
    public void deleteTransactionById(long id) {
        transactionRepository.deleteById(id);
    }

    @Override
    public Page<Transaction> findPaginated(int pageNum, int pageSize, String sortField, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(sortField).ascending() :
                Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(pageNum - 1, pageSize, sort);
        return this.transactionRepository.findAll(pageable);
    }
}
