package com.cezarfbf.finance.core.domain.fixedexpense;

import com.cezarfbf.finance.core.domain.transaction.TransactionContext;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FixedExpenseRepository extends JpaRepository<FixedExpense, UUID> {

    List<FixedExpense> findAllByOrderByBillingDayAsc();

    List<FixedExpense> findByContextOrderByBillingDayAsc(TransactionContext context);
}
