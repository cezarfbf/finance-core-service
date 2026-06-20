package com.cezarfbf.finance.core.domain.fixedexpense;

import com.cezarfbf.finance.core.domain.transaction.TransactionContext;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
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
@RequestMapping("/fixed-expenses")
@Tag(name = "Fixed expenses", description = "Recurring monthly commitments")
public class FixedExpenseController {

    private final FixedExpenseService service;

    public FixedExpenseController(FixedExpenseService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(
        summary = "List fixed expenses",
        description = "All fixed expenses ordered by billing day. Optionally filter by context (PERSONAL or BUSINESS)."
    )
    public ResponseEntity<List<FixedExpenseDto>> getFixedExpenses(
        @Parameter(description = "Filter by context: PERSONAL or BUSINESS (omit for all)")
        @RequestParam(required = false) TransactionContext context
    ) {
        return ResponseEntity.ok(service.findAll(context));
    }

    @PostMapping
    @Operation(summary = "Create a fixed expense")
    public ResponseEntity<FixedExpenseDto> create(@Valid @RequestBody FixedExpenseRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a fixed expense")
    public ResponseEntity<FixedExpenseDto> update(
        @PathVariable UUID id,
        @Valid @RequestBody FixedExpenseRequest request
    ) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a fixed expense (permanent)")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
