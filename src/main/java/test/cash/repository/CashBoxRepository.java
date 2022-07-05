package test.cash.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import test.cash.model.CashBox;

@Repository
public interface CashBoxRepository extends JpaRepository<CashBox, Long> {
}
