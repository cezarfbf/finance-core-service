package com.cezarfbf.finance.core.reports.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MonthlyReportSummaryDto {
	private int year;
	private int month;
	private int totalProcessed;
	private MonthlyKpisDto kpis;
}
