package com.cezarfbf.finance.core.reports;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.cezarfbf.finance.core.domain.transaction.Transaction;
import com.cezarfbf.finance.core.domain.transaction.TransactionRepository;
import com.cezarfbf.finance.core.reports.dto.MonthlyKpisDto;
import com.cezarfbf.finance.core.reports.dto.MonthlyReportDto;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MonthlyReportServiceTest {

	@Mock
	private TransactionRepository repository;

	@InjectMocks
	private MonthlyReportService service;

	@Test
	void computesKpisGroupedByClassification() {
		List<Transaction> txs = List.of(
			tx("Receita", "4059.00"),
			tx("IVA Apurado", "961.40"),
			tx("IVA Apurado", "1012.00"),
			tx("IVA Apurado", "759.00"),
			tx("Custos Oper.", "9.84"),
			tx("Custos Oper.", "209.97"),
			tx("Custos Oper.", "302.33"),
			tx("Custos Oper.", "153.75"),
			tx("Retiradas", "1440.19"),
			tx("Retiradas", "218.54"),
			tx("Retiradas", "1500.00"),
			tx("Gastos Pessoais", "43.00"),
			tx("Gastos Pessoais", "20.40")
		);

		MonthlyKpisDto kpis = service.computeKpis(txs);

		assertThat(kpis.getReceita()).isEqualByComparingTo("4059.00");
		assertThat(kpis.getIvaApurado()).isEqualByComparingTo("2732.40");
		assertThat(kpis.getIvaPago()).isEqualByComparingTo("0");
		assertThat(kpis.getCustosOperacionais()).isEqualByComparingTo("675.89");
		assertThat(kpis.getRetiradas()).isEqualByComparingTo("3158.73");
		assertThat(kpis.getGastosPessoais()).isEqualByComparingTo("63.40");
	}

	@Test
	void getMonthlyReportCountsUnclassifiedAndTotalProcessed() {
		List<Transaction> txs = List.of(
			tx("Receita", "1000.00"),
			tx("IGNORAR", "10.00"),
			tx("IGNORAR", "20.00")
		);
		when(repository.findByDateGreaterThanEqualAndDateLessThanOrderByDateDesc(any(), any()))
			.thenReturn(txs);

		MonthlyReportDto report = service.getMonthlyReport(2025, 10);

		assertThat(report.getYear()).isEqualTo(2025);
		assertThat(report.getMonth()).isEqualTo(10);
		assertThat(report.getTotalProcessed()).isEqualTo(3);
		assertThat(report.getUnclassified()).isEqualTo(2);
		assertThat(report.getKpis().getReceita()).isEqualByComparingTo("1000.00");
		assertThat(report.getTransactions()).hasSize(3);
	}

	private static Transaction tx(String classification, String amount) {
		return Transaction.builder()
			.date(LocalDate.of(2025, 10, 1))
			.counterparty("X")
			.classification(classification)
			.amount(new BigDecimal(amount))
			.build();
	}
}
