package com.cezarfbf.finance.core.domain.transaction;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    @Operation(
        summary = "List transactions",
        description = "All non-deleted transactions ordered by date desc. Optionally filter by context (PERSONAL or BUSINESS)."
    )
    public ResponseEntity<List<TransactionDto>> getTransactions(
        @Parameter(description = "Filter by context: PERSONAL or BUSINESS (omit for all)")
        @RequestParam(required = false) TransactionContext context
    ) {
        return ResponseEntity.ok(service.findAll(context));
    }

    @GetMapping("/search")
    @Operation(
        summary = "Search and filter transactions",
        description = "Filters by context (required). Optionally narrows by text (description, counterparty, "
            + "notes, externalReference) and/or an inclusive date range (from/to, ISO yyyy-MM-dd)."
    )
    public ResponseEntity<List<TransactionDto>> search(
        @Parameter(description = "Context: PERSONAL or BUSINESS") @RequestParam TransactionContext context,
        @Parameter(description = "Free-text query (optional)") @RequestParam(required = false) String q,
        @Parameter(description = "Start date, inclusive (yyyy-MM-dd, optional)")
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
        @Parameter(description = "End date, inclusive (yyyy-MM-dd, optional)")
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
    ) {
        return ResponseEntity.ok(service.search(context, q, from, to));
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

    @PostMapping
    @Operation(summary = "Create a transaction")
    public ResponseEntity<TransactionDto> create(@Valid @RequestBody TransactionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a transaction")
    public ResponseEntity<TransactionDto> update(
        @PathVariable UUID id,
        @Valid @RequestBody TransactionRequest request
    ) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a transaction (permanent)")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
