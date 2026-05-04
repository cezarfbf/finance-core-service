package com.cezarfbf.finance.core.reports.dto;

import com.cezarfbf.finance.core.domain.transaction.TransactionDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MonthlyReportDto {
	private int year;
	private int month;

	@JsonInclude(JsonInclude.Include.ALWAYS)
	private String motorVersion;

	private int totalProcessed;
	private int unclassified;
	private MonthlyKpisDto kpis;
	private List<TransactionDto> transactions;
}
