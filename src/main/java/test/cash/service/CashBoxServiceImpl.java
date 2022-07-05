package test.cash.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import test.cash.model.CashBox;
import test.cash.repository.CashBoxRepository;

@Service
public class CashBoxServiceImpl {

    private final CashBoxRepository cashBoxRepository;

    public CashBoxServiceImpl(CashBoxRepository cashBoxRepository) {
        this.cashBoxRepository = cashBoxRepository;
    }

    @Transactional
    public void credit(CashBox cashBox, double amount) {
        cashBox.setBalance(cashBox.getBalance() + amount);
        cashBoxRepository.save(cashBox);
    }

    @Transactional
    public void debit(CashBox cashBox, double amount) {
        if (cashBox.getBalance() < amount) {
            throw new ArithmeticException("Need to add error for wrong balance value");
        }
        cashBox.setBalance(cashBox.getBalance() - amount);
        cashBoxRepository.save(cashBox);
    }

}
