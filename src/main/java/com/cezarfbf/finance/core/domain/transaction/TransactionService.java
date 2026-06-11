package com.cezarfbf.finance.core.domain.transaction;

import com.cezarfbf.finance.core.domain.category.Category;
import com.cezarfbf.finance.core.domain.category.CategoryRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransactionService {

    // No auth yet: all rows belong to the seeded dev user (see V8__unify_transactions.sql).
    private static final UUID DEV_USER_ID = UUID.fromString("00000000-0000-0000-0000-000000000001");

    private final TransactionRepository repository;
    private final CategoryRepository categoryRepository;

    public TransactionService(TransactionRepository repository, CategoryRepository categoryRepository) {
        this.repository = repository;
        this.categoryRepository = categoryRepository;
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

    @Transactional
    public TransactionDto create(TransactionRequest req) {
        Transaction tx = Transaction.builder()
            .userId(DEV_USER_ID)
            .context(req.context())
            .date(req.date())
            .amount(req.amount())
            .currency(req.currency() != null && !req.currency().isBlank() ? req.currency() : "EUR")
            .type(req.type())
            .category(resolveCategory(req.categoryId()))
            .counterparty(req.counterparty())
            .description(req.description())
            .notes(req.notes())
            .source(TransactionSource.MANUAL)
            .build();
        return TransactionDto.from(repository.save(tx));
    }

    @Transactional
    public TransactionDto update(UUID id, TransactionRequest req) {
        Transaction tx = repository.findById(id)
            .filter(t -> t.getDeletedAt() == null)
            .orElseThrow(() -> new TransactionNotFoundException(id));
        tx.setContext(req.context());
        tx.setDate(req.date());
        tx.setAmount(req.amount());
        tx.setCurrency(req.currency() != null && !req.currency().isBlank() ? req.currency() : "EUR");
        tx.setType(req.type());
        tx.setCategory(resolveCategory(req.categoryId()));
        tx.setCounterparty(req.counterparty());
        tx.setDescription(req.description());
        tx.setNotes(req.notes());
        return TransactionDto.from(repository.save(tx));
    }

    @Transactional
    public void delete(UUID id) {
        if (!repository.existsById(id)) {
            throw new TransactionNotFoundException(id);
        }
        repository.deleteById(id);
    }

    private Category resolveCategory(UUID categoryId) {
        if (categoryId == null) {
            return null;
        }
        return categoryRepository.findById(categoryId)
            .orElseThrow(() -> new IllegalArgumentException("Unknown category: " + categoryId));
    }
}
