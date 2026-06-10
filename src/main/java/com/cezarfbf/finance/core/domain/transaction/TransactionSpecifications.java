package com.cezarfbf.finance.core.domain.transaction;

import jakarta.persistence.criteria.Predicate;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;

/**
 * Dynamic query predicates for transactions.
 *
 * <p>Built with the JPA Criteria API so each filter is only added to the SQL when its value is
 * present. This avoids emitting optional {@code (:param IS NULL OR ...)} clauses, which trip
 * PostgreSQL's parameter type inference (e.g. "could not determine data type" /
 * "function lower(bytea) does not exist") when a bind is null.
 */
public final class TransactionSpecifications {

    private TransactionSpecifications() {
    }

    /**
     * Active (non-deleted) transactions for the given context, optionally narrowed by a free-text
     * query and/or an inclusive {@code [from, to]} date range. Null/blank arguments are ignored.
     */
    public static Specification<Transaction> search(
        TransactionContext context, String q, LocalDate from, LocalDate to) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("context"), context));
            predicates.add(cb.isNull(root.get("deletedAt")));

            if (q != null && !q.isBlank()) {
                String like = "%" + q.trim().toLowerCase() + "%";
                predicates.add(cb.or(
                    cb.like(cb.lower(root.<String>get("description")), like),
                    cb.like(cb.lower(root.<String>get("counterparty")), like),
                    cb.like(cb.lower(root.<String>get("notes")), like),
                    cb.like(cb.lower(root.<String>get("externalReference")), like)
                ));
            }
            if (from != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.<LocalDate>get("date"), from));
            }
            if (to != null) {
                predicates.add(cb.lessThanOrEqualTo(root.<LocalDate>get("date"), to));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
