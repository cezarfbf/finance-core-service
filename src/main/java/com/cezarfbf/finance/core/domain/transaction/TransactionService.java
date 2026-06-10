package com.cezarfbf.finance.core.domain.transaction;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransactionService {

    private final TransactionRepository repository;

    public TransactionService(TransactionRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<TransactionDto> findAll(TransactionContext context) {
        List<Transaction> transactions = context == null
            ? repository.findAllByDeletedAtIsNullOrderByDateDesc()
            : repository.findByContextAndDeletedAtIsNullOrderByDateDesc(context);
        return transactions.stream()
            .map(TransactionDto::from)
            .toList();
    }

    @Transactional(readOnly = true)
    public List<TransactionDto> search(TransactionContext context, String q, LocalDate from, LocalDate to) {
        // Predicates are built dynamically (see TransactionSpecifications), so optional filters that
        // are absent never reach the SQL — avoiding PostgreSQL null-parameter type-inference errors.
        return repository
            .findAll(TransactionSpecifications.search(context, q, from, to), Sort.by(Sort.Direction.DESC, "date"))
            .stream()
            .map(TransactionDto::from)
            .toList();
    }

    @Transactional(readOnly = true)
    public List<TransactionDto> findByYearAndMonth(int year, int month, TransactionContext context) {
        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end = start.plusMonths(1);
        return repository.findByDateGreaterThanEqualAndDateLessThanAndDeletedAtIsNullOrderByDateDesc(start, end)
            .stream()
            .filter(t -> context == null || t.getContext() == context)
            .map(TransactionDto::from)
            .toList();
    }

    @Transactional(readOnly = true)
    public List<Transaction> findByYear(int year) {
        LocalDate start = LocalDate.of(year, 1, 1);
        LocalDate end = start.plusYears(1);
        return repository.findByDateGreaterThanEqualAndDateLessThanAndDeletedAtIsNullOrderByDateDesc(start, end);
    }
}
