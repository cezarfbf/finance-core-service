package com.cezarfbf.finance.core.domain.transaction;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Create/update payload for a transaction. {@code amount} is always positive; {@code type}
 * (DEBIT/CREDIT) encodes the direction.
 */
public record TransactionRequest(
    @NotNull(message = "context is required") TransactionContext context,
    @NotNull(message = "date is required") LocalDate date,
    @NotNull(message = "amount is required") @Positive(message = "amount must be greater than 0") BigDecimal amount,
    @NotNull(message = "type is required") TransactionType type,
    UUID categoryId,
    String currency,
    @NotBlank(message = "description is required") String description,
    String counterparty,
    String notes
) {
}
