package com.cezarfbf.finance.core.reports;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.cezarfbf.finance.core.domain.transaction.Transaction;
import com.cezarfbf.finance.core.domain.transaction.TransactionRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class MonthlyReportControllerIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private TransactionRepository repository;

	@BeforeEach
	void seed() {
		repository.deleteAll();
		save("2025-10-02", "KCSIT SA", "4059.00", "Receita");
		save("2025-10-16", "Cezar Vivid", "961.40", "IVA Apurado");
		save("2025-10-16", "Cezar Vivid", "1012.00", "IVA Apurado");
		save("2025-10-16", "Cezar Vivid", "759.00", "IVA Apurado");
		save("2025-10-30", "Consultoria", "1500.00", "Retiradas");
		save("2025-11-04", "KCSIT SA", "5412.00", "Receita");
	}

	@Test
	void getMonthlyReportReturnsKpisAndTransactions() throws Exception {
		mockMvc.perform(get("/reports/monthly/2025/10"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.year").value(2025))
			.andExpect(jsonPath("$.month").value(10))
			.andExpect(jsonPath("$.totalProcessed").value(5))
			.andExpect(jsonPath("$.unclassified").value(0))
			.andExpect(jsonPath("$.kpis.receita").value(4059.00))
			.andExpect(jsonPath("$.kpis.ivaApurado").value(2732.40))
			.andExpect(jsonPath("$.kpis.ivaPago").value(0))
			.andExpect(jsonPath("$.kpis.retiradas").value(1500.00))
			.andExpect(jsonPath("$.transactions.length()").value(5));
	}

	@Test
	void listMonthlyReturnsSummariesForYear() throws Exception {
		mockMvc.perform(get("/reports/monthly").param("year", "2025"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.length()").value(2))
			.andExpect(jsonPath("$[0].month").value(11))
			.andExpect(jsonPath("$[0].totalProcessed").value(1))
			.andExpect(jsonPath("$[0].kpis.receita").value(5412.00))
			.andExpect(jsonPath("$[1].month").value(10))
			.andExpect(jsonPath("$[1].kpis.receita").value(4059.00));
	}

	@Test
	void getTransactionsReturnsAllOrderedByDateDesc() throws Exception {
		mockMvc.perform(get("/transactions"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.length()").value(6))
			.andExpect(jsonPath("$[0].date").value("2025-11-04"))
			.andExpect(jsonPath("$[0].counterparty").value("KCSIT SA"))
			.andExpect(jsonPath("$[0].amount").value(5412.00));
	}

	private void save(String date, String counterparty, String amount, String classification) {
		repository.save(Transaction.builder()
			.date(LocalDate.parse(date))
			.counterparty(counterparty)
			.amount(new BigDecimal(amount))
			.classification(classification)
			.build());
	}
}
