package com.cezarfbf.finance.core.domain.transaction;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

    List<Transaction> findAllByDeletedAtIsNullOrderByDateDesc();

    List<Transaction> findByDateGreaterThanEqualAndDateLessThanAndDeletedAtIsNullOrderByDateDesc(
        LocalDate startInclusive, LocalDate endExclusive);

    @Query("""
        SELECT t FROM Transaction t
        WHERE t.userId = :userId
          AND t.context = :context
          AND t.deletedAt IS NULL
        ORDER BY t.date DESC
        """)
    List<Transaction> findActiveByUserAndContext(
        @Param("userId") UUID userId,
        @Param("context") TransactionContext context
    );
}
