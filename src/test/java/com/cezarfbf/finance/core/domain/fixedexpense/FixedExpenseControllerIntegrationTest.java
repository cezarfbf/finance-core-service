package com.cezarfbf.finance.core.domain.fixedexpense;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.cezarfbf.finance.core.domain.category.Category;
import com.cezarfbf.finance.core.domain.category.CategoryContext;
import com.cezarfbf.finance.core.domain.category.CategoryRepository;
import com.jayway.jsonpath.JsonPath;
import java.time.OffsetDateTime;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class FixedExpenseControllerIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private FixedExpenseRepository repository;
    @Autowired private CategoryRepository categoryRepository;

    private Category rent;

    @BeforeEach
    void seed() {
        repository.deleteAll();
        categoryRepository.deleteAll();
        rent = saveCategory("Rent");
    }

    @Test
    void createListUpdateDeleteCycle() throws Exception {
        // CREATE
        String created = mockMvc.perform(post("/fixed-expenses")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "name": "Netflix",
                      "amount": 13.99,
                      "categoryId": "%s",
                      "billingDay": 2,
                      "notes": "family plan"
                    }
                    """.formatted(rent.getId())))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.name").value("Netflix"))
            .andExpect(jsonPath("$.amount").value(13.99))
            .andExpect(jsonPath("$.currency").value("EUR"))
            .andExpect(jsonPath("$.context").value("PERSONAL"))
            .andExpect(jsonPath("$.billingDay").value(2))
            .andExpect(jsonPath("$.active").value(true))
            .andExpect(jsonPath("$.category.name").value("Rent"))
            .andReturn().getResponse().getContentAsString();

        String id = JsonPath.read(created, "$.id");

        // LIST (ordered by billing day)
        mockMvc.perform(get("/fixed-expenses"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(1))
            .andExpect(jsonPath("$[0].name").value("Netflix"));

        // UPDATE
        mockMvc.perform(put("/fixed-expenses/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "name": "Netflix Premium",
                      "amount": 17.99,
                      "billingDay": 5,
                      "active": false
                    }
                    """))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Netflix Premium"))
            .andExpect(jsonPath("$.amount").value(17.99))
            .andExpect(jsonPath("$.billingDay").value(5))
            .andExpect(jsonPath("$.active").value(false))
            .andExpect(jsonPath("$.category").doesNotExist());

        // DELETE (hard) then confirm it is gone
        mockMvc.perform(delete("/fixed-expenses/" + id))
            .andExpect(status().isNoContent());

        mockMvc.perform(get("/fixed-expenses"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void createRejectsInvalidBillingDay() throws Exception {
        mockMvc.perform(post("/fixed-expenses")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    { "name": "Bad", "amount": 10.00, "billingDay": 40 }
                    """))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.errors.billingDay").exists());
    }

    @Test
    void deleteUnknownReturnsNotFound() throws Exception {
        mockMvc.perform(delete("/fixed-expenses/" + UUID.randomUUID()))
            .andExpect(status().isNotFound());
    }

    private Category saveCategory(String name) {
        return categoryRepository.save(Category.builder()
            .context(CategoryContext.PERSONAL)
            .name(name)
            .sortOrder(0)
            .createdAt(OffsetDateTime.now())
            .updatedAt(OffsetDateTime.now())
            .build());
    }
}
