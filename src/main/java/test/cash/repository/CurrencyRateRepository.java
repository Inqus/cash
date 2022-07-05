package test.cash.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import test.cash.model.CurrencyRate;

@Repository
public interface CurrencyRateRepository extends JpaRepository<CurrencyRate, Long> {
    CurrencyRate findByFromCurrencyAndToCurrency(Long from, Long to);
}
