package com.cezarfbf.finance.core.dto;

import java.math.BigDecimal;

public record CategorySummary(
	String category,
	String type,
	BigDecimal amount
) {}
