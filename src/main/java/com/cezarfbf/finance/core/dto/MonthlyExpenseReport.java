package com.cezarfbf.finance.core.dto;

import java.util.List;

public record MonthlyExpenseReport(
	int year,
	int month,
	List<CategorySummary> summary,
	List<ExpenseDTO> expenses
) {}
