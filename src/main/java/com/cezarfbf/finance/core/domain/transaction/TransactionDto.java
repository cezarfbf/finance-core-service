package com.cezarfbf.finance.core.domain.transaction;

import com.cezarfbf.finance.core.domain.category.CategoryDto;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDto {
	private Long id;
	private LocalDate date;
	private String counterparty;
	private String reference;
	private CategoryDto category;
	private BigDecimal amount;
	private String classification;
	private String description;

	public static TransactionDto from(Transaction t) {
		CategoryDto category = t.getCategory() != null ? CategoryDto.from(t.getCategory()) : null;
		return TransactionDto.builder()
			.id(t.getId())
			.date(t.getDate())
			.counterparty(t.getCounterparty())
			.reference(t.getReference())
			.category(category)
			.amount(t.getAmount())
			.classification(t.getClassification())
			.description(t.getDescription())
			.build();
	}
}
