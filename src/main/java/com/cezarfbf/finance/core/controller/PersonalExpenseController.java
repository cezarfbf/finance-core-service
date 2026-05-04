package com.cezarfbf.finance.core.controller;

import com.cezarfbf.finance.core.dto.MonthlyExpenseReport;
import com.cezarfbf.finance.core.service.PersonalExpenseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/personal")
@Tag(name = "Personal Expenses", description = "Manage personal expenses and generate reports")
public class PersonalExpenseController {

	private final PersonalExpenseService service;

	public PersonalExpenseController(PersonalExpenseService service) {
		this.service = service;
	}

	@GetMapping("/test")
	@Operation(summary = "Test endpoint", description = "Verify the service is running")
	public ResponseEntity<String> test() {
		return ResponseEntity.ok("Personal expense controller is working!");
	}

	@GetMapping("/monthly/{year}/{month}")
	@Operation(summary = "Get monthly expense report", description = "Retrieve a detailed expense report for the specified month")
	public ResponseEntity<MonthlyExpenseReport> getMonthlyReport(
		@Parameter(description = "Year (e.g., 2026)", required = true)
		@PathVariable int year,
		@Parameter(description = "Month (1-12)", required = true)
		@PathVariable int month
	) {
		if (month < 1 || month > 12) {
			throw new IllegalArgumentException("Month must be between 1 and 12");
		}
		try {
			MonthlyExpenseReport report = service.getMonthlyReport(year, month);
			return ResponseEntity.ok(report);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
}
