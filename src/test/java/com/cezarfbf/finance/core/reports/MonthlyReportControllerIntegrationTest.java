package com.cezarfbf.finance.core.reports;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.cezarfbf.finance.core.domain.category.Category;
import com.cezarfbf.finance.core.domain.category.CategoryContext;
import com.cezarfbf.finance.core.domain.category.CategoryRepository;
import com.cezarfbf.finance.core.domain.transaction.Transaction;
import com.cezarfbf.finance.core.domain.transaction.TransactionContext;
import com.cezarfbf.finance.core.domain.transaction.TransactionRepository;
import com.cezarfbf.finance.core.domain.transaction.TransactionType;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;
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

    private static final UUID USER_ID = UUID.fromString("00000000-0000-0000-0000-000000000001");

    @Autowired private MockMvc mockMvc;
    @Autowired private TransactionRepository repository;
    @Autowired private CategoryRepository categoryRepository;

    private Category receita;
    private Category ivaApurado;
    private Category retiradas;

    @BeforeEach
    void seed() {
        repository.deleteAll();
        categoryRepository.deleteAll();

        receita     = saveCategory("Receita");
        ivaApurado  = saveCategory("IVA Apurado");
        retiradas   = saveCategory("Retiradas");

        save("2025-10-02", "KCSIT SA",     "4059.00", receita);
        save("2025-10-16", "Cezar Vivid",  "961.40",  ivaApurado);
        save("2025-10-16", "Cezar Vivid",  "1012.00", ivaApurado);
        save("2025-10-16", "Cezar Vivid",  "759.00",  ivaApurado);
        save("2025-10-30", "Consultoria",  "1500.00", retiradas);
        save("2025-11-04", "KCSIT SA",     "5412.00", receita);
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

    private Category saveCategory(String name) {
        return categoryRepository.save(Category.builder()
            .context(CategoryContext.BUSINESS)
            .name(name)
            .sortOrder(0)
            .createdAt(OffsetDateTime.now())
            .updatedAt(OffsetDateTime.now())
            .build());
    }

    private void save(String date, String counterparty, String amount, Category category) {
        repository.save(Transaction.builder()
            .userId(USER_ID)
            .context(TransactionContext.BUSINESS)
            .date(LocalDate.parse(date))
            .counterparty(counterparty)
            .type(TransactionType.CREDIT)
            .currency("EUR")
            .amount(new BigDecimal(amount))
            .category(category)
            .build());
    }
}
