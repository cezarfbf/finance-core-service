package com.cezarfbf.finance.core.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(TransactionController.class)
class TransactionControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void shouldReturnTransactionsList() throws Exception {
		mockMvc.perform(get("/transactions"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.length()").value(2))
				.andExpect(jsonPath("$[0].id").value(1))
				.andExpect(jsonPath("$[0].description").value("Supermercado"))
				.andExpect(jsonPath("$[0].amount").value(250.75))
				.andExpect(jsonPath("$[1].id").value(2))
				.andExpect(jsonPath("$[1].description").value("Salario"))
				.andExpect(jsonPath("$[1].amount").value(5000.00));
	}
}
