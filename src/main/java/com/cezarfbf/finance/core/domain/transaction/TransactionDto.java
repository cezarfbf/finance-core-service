package com.cezarfbf.finance.core.domain.transaction;

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
	private String category;
	private BigDecimal amount;
	private String classification;
	private String description;

	public static TransactionDto from(Transaction t) {
		return TransactionDto.builder()
			.id(t.getId())
			.date(t.getDate())
			.counterparty(t.getCounterparty())
			.reference(t.getReference())
			.category(t.getCategory())
			.amount(t.getAmount())
			.classification(t.getClassification())
			.description(t.getDescription())
			.build();
	}
}
