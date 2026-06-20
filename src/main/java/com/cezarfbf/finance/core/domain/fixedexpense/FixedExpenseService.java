package com.cezarfbf.finance.core.domain.fixedexpense;

import com.cezarfbf.finance.core.domain.category.Category;
import com.cezarfbf.finance.core.domain.category.CategoryRepository;
import com.cezarfbf.finance.core.domain.transaction.TransactionContext;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FixedExpenseService {

    // No auth yet: all rows belong to the seeded dev user (see V8__unify_transactions.sql).
    private static final UUID DEV_USER_ID = UUID.fromString("00000000-0000-0000-0000-000000000001");

    private final FixedExpenseRepository repository;
    private final CategoryRepository categoryRepository;

    public FixedExpenseService(FixedExpenseRepository repository, CategoryRepository categoryRepository) {
        this.repository = repository;
        this.categoryRepository = categoryRepository;
    }

    @Transactional(readOnly = true)
    public List<FixedExpenseDto> findAll(TransactionContext context) {
        List<FixedExpense> expenses = context == null
            ? repository.findAllByOrderByBillingDayAsc()
            : repository.findByContextOrderByBillingDayAsc(context);
        return expenses.stream()
            .map(FixedExpenseDto::from)
            .toList();
    }

    @Transactional
    public FixedExpenseDto create(FixedExpenseRequest req) {
        FixedExpense expense = FixedExpense.builder()
            .userId(DEV_USER_ID)
            .context(TransactionContext.PERSONAL)
            .name(req.name())
            .amount(req.amount())
            .currency(req.currency() != null && !req.currency().isBlank() ? req.currency() : "EUR")
            .category(resolveCategory(req.categoryId()))
            .billingDay(req.billingDay())
            .active(req.active() == null || req.active())
            .notes(req.notes())
            .build();
        return FixedExpenseDto.from(repository.save(expense));
    }

    @Transactional
    public FixedExpenseDto update(UUID id, FixedExpenseRequest req) {
        FixedExpense expense = repository.findById(id)
            .orElseThrow(() -> new FixedExpenseNotFoundException(id));
        expense.setName(req.name());
        expense.setAmount(req.amount());
        expense.setCurrency(req.currency() != null && !req.currency().isBlank() ? req.currency() : "EUR");
        expense.setCategory(resolveCategory(req.categoryId()));
        expense.setBillingDay(req.billingDay());
        expense.setActive(req.active() == null || req.active());
        expense.setNotes(req.notes());
        return FixedExpenseDto.from(repository.save(expense));
    }

    @Transactional
    public void delete(UUID id) {
        if (!repository.existsById(id)) {
            throw new FixedExpenseNotFoundException(id);
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
