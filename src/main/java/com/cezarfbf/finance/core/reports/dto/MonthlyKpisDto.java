package com.cezarfbf.finance.core.reports.dto;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MonthlyKpisDto {
	private BigDecimal receita;
	private BigDecimal ivaApurado;
	private BigDecimal ivaPago;
	private BigDecimal custosOperacionais;
	private BigDecimal retiradas;
	private BigDecimal gastosPessoais;
}
