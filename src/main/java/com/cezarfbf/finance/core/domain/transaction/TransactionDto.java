package com.cezarfbf.finance.core.domain.transaction;

import com.cezarfbf.finance.core.domain.category.CategoryDto;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDto {
    private UUID id;
    private UUID userId;
    private TransactionContext context;
    private LocalDate date;
    private BigDecimal amount;
    private String currency;
    private TransactionType type;
    private CategoryDto category;
    private String counterparty;
    private String externalReference;
    private String description;
    private String notes;
    private TransactionSource source;

    public static TransactionDto from(Transaction t) {
        CategoryDto category = t.getCategory() != null ? CategoryDto.from(t.getCategory()) : null;
        return TransactionDto.builder()
            .id(t.getId())
            .userId(t.getUserId())
            .context(t.getContext())
            .date(t.getDate())
            .amount(t.getAmount())
            .currency(t.getCurrency())
            .type(t.getType())
            .category(category)
            .counterparty(t.getCounterparty())
            .externalReference(t.getExternalReference())
            .description(t.getDescription())
            .notes(t.getNotes())
            .source(t.getSource())
            .build();
    }
}
