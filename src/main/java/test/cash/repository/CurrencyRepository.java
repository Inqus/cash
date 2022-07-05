package test.cash.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import test.cash.model.Currency;

@Repository
public interface CurrencyRepository extends JpaRepository<Currency, Long> {

    Currency findByName(String name);
}
