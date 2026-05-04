package com.cezarfbf.finance.core.reports;

import com.cezarfbf.finance.core.domain.transaction.Transaction;
import com.cezarfbf.finance.core.domain.transaction.TransactionDto;
import com.cezarfbf.finance.core.domain.transaction.TransactionRepository;
import com.cezarfbf.finance.core.reports.dto.MonthlyKpisDto;
import com.cezarfbf.finance.core.reports.dto.MonthlyReportDto;
import com.cezarfbf.finance.core.reports.dto.MonthlyReportSummaryDto;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MonthlyReportService {

	private static final String CLASSIFICATION_RECEITA = "Receita";
	private static final String CLASSIFICATION_IVA_APURADO = "IVA Apurado";
	private static final String CLASSIFICATION_IVA_PAGO = "IVA Pago";
	private static final String CLASSIFICATION_CUSTOS_OPERACIONAIS = "Custos Oper.";
	private static final String CLASSIFICATION_RETIRADAS = "Retiradas";
	private static final String CLASSIFICATION_GASTOS_PESSOAIS = "Gastos Pessoais";
	private static final String CLASSIFICATION_IGNORAR = "IGNORAR";

	private final TransactionRepository repository;

	public MonthlyReportService(TransactionRepository repository) {
		this.repository = repository;
	}

	@Transactional(readOnly = true)
	public MonthlyReportDto getMonthlyReport(int year, int month) {
		LocalDate start = LocalDate.of(year, month, 1);
		LocalDate end = start.plusMonths(1);
		List<Transaction> transactions =
			repository.findByDateGreaterThanEqualAndDateLessThanOrderByDateDesc(start, end);
		MonthlyKpisDto kpis = computeKpis(transactions);
		int unclassified = (int) transactions.stream()
			.filter(t -> CLASSIFICATION_IGNORAR.equals(t.getClassification()))
			.count();

		return MonthlyReportDto.builder()
			.year(year)
			.month(month)
			.motorVersion(null)
			.totalProcessed(transactions.size())
			.unclassified(unclassified)
			.kpis(kpis)
			.transactions(transactions.stream().map(TransactionDto::from).toList())
			.build();
	}

	@Transactional(readOnly = true)
	public List<MonthlyReportSummaryDto> listSummaries(int year) {
		LocalDate start = LocalDate.of(year, 1, 1);
		LocalDate end = start.plusYears(1);
		List<Transaction> transactions =
			repository.findByDateGreaterThanEqualAndDateLessThanOrderByDateDesc(start, end);

		Map<Integer, List<Transaction>> byMonth = transactions.stream()
			.collect(Collectors.groupingBy(t -> t.getDate().getMonthValue()));

		return byMonth.entrySet().stream()
			.sorted(Map.Entry.<Integer, List<Transaction>>comparingByKey().reversed())
			.map(e -> MonthlyReportSummaryDto.builder()
				.year(year)
				.month(e.getKey())
				.totalProcessed(e.getValue().size())
				.kpis(computeKpis(e.getValue()))
				.build())
			.toList();
	}

	MonthlyKpisDto computeKpis(List<Transaction> transactions) {
		Map<String, BigDecimal> totals = transactions.stream()
			.collect(Collectors.groupingBy(
				Transaction::getClassification,
				Collectors.reducing(BigDecimal.ZERO, Transaction::getAmount, BigDecimal::add)));

		return MonthlyKpisDto.builder()
			.receita(totals.getOrDefault(CLASSIFICATION_RECEITA, BigDecimal.ZERO))
			.ivaApurado(totals.getOrDefault(CLASSIFICATION_IVA_APURADO, BigDecimal.ZERO))
			.ivaPago(totals.getOrDefault(CLASSIFICATION_IVA_PAGO, BigDecimal.ZERO))
			.custosOperacionais(totals.getOrDefault(CLASSIFICATION_CUSTOS_OPERACIONAIS, BigDecimal.ZERO))
			.retiradas(totals.getOrDefault(CLASSIFICATION_RETIRADAS, BigDecimal.ZERO))
			.gastosPessoais(totals.getOrDefault(CLASSIFICATION_GASTOS_PESSOAIS, BigDecimal.ZERO))
			.build();
	}
}
