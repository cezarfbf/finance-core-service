package com.cezarfbf.finance.core.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

	@GetMapping
	public ResponseEntity<List<Map<String, Object>>> getTransactions() {
		List<Map<String, Object>> transactions = List.of(
				Map.of("id", 1L, "description", "Supermercado", "amount", 250.75),
				Map.of("id", 2L, "description", "Salario", "amount", 5000.00));

		return ResponseEntity.ok(transactions);
	}
}
