package com.cezarfbf.finance.core.domain.fixedexpense;

import com.cezarfbf.finance.core.domain.category.CategoryDto;
import com.cezarfbf.finance.core.domain.transaction.TransactionContext;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FixedExpenseDto {
    private UUID id;
    private UUID userId;
    private TransactionContext context;
    private String name;
    private BigDecimal amount;
    private String currency;
    private CategoryDto category;
    private Integer billingDay;
    private boolean active;
    private String notes;

    public static FixedExpenseDto from(FixedExpense f) {
        CategoryDto category = f.getCategory() != null ? CategoryDto.from(f.getCategory()) : null;
        return FixedExpenseDto.builder()
            .id(f.getId())
            .userId(f.getUserId())
            .context(f.getContext())
            .name(f.getName())
            .amount(f.getAmount())
            .currency(f.getCurrency())
            .category(category)
            .billingDay(f.getBillingDay())
            .active(f.isActive())
            .notes(f.getNotes())
            .build();
    }
}
