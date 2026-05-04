package com.cezarfbf.finance.core.domain.transaction;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

	List<Transaction> findAllByOrderByDateDesc();

	List<Transaction> findByDateGreaterThanEqualAndDateLessThanOrderByDateDesc(
		LocalDate startInclusive, LocalDate endExclusive);
}
