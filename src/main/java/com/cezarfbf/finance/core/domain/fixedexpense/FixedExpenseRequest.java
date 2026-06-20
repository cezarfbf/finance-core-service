package com.cezarfbf.finance.core.domain.fixedexpense;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.UUID;

/**
 * Create/update payload for a fixed expense. {@code billingDay} is the day of month (1–31) the
 * commitment falls due; {@code active} defaults to true when omitted.
 */
public record FixedExpenseRequest(
    @NotBlank(message = "name is required") String name,
    @NotNull(message = "amount is required") @Positive(message = "amount must be greater than 0") BigDecimal amount,
    UUID categoryId,
    String currency,
    @NotNull(message = "billingDay is required")
    @Min(value = 1, message = "billingDay must be between 1 and 31")
    @Max(value = 31, message = "billingDay must be between 1 and 31") Integer billingDay,
    Boolean active,
    String notes
) {
}
