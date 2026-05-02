package com.cezarfbf.finance.core.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ExpenseDTO(
	Long id,
	LocalDate date,
	String category,
	String type,
	BigDecimal amount,
	String description
) {}
