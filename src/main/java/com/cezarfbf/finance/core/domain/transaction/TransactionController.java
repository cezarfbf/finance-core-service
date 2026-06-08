package com.cezarfbf.finance.core.domain.transaction;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transactions")
@Tag(name = "Transactions", description = "View transactions")
public class TransactionController {

    private final TransactionService service;

    public TransactionController(TransactionService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "List all transactions", description = "All non-deleted transactions ordered by date desc")
    public ResponseEntity<List<TransactionDto>> getTransactions() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{year}/{month}")
    @Operation(
        summary = "List transactions by year and month",
        description = "Returns transactions for the given month. Optionally filter by context (PERSONAL or BUSINESS)."
    )
    public ResponseEntity<List<TransactionDto>> getByYearAndMonth(
        @Parameter(description = "Year (e.g. 2026)") @PathVariable int year,
        @Parameter(description = "Month (1–12)") @PathVariable int month,
        @Parameter(description = "Filter by context: PERSONAL or BUSINESS (omit for all)")
        @RequestParam(required = false) TransactionContext context
    ) {
        if (month < 1 || month > 12) {
            throw new IllegalArgumentException("Month must be between 1 and 12");
        }
        return ResponseEntity.ok(service.findByYearAndMonth(year, month, context));
    }
}
