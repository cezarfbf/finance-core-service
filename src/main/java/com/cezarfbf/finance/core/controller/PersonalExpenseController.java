package com.cezarfbf.finance.core.controller;

import com.cezarfbf.finance.core.dto.MonthlyExpenseReport;
import com.cezarfbf.finance.core.service.PersonalExpenseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/personal")
public class PersonalExpenseController {

	private final PersonalExpenseService service;

	public PersonalExpenseController(PersonalExpenseService service) {
		this.service = service;
	}

	@GetMapping("/test")
	public ResponseEntity<String> test() {
		return ResponseEntity.ok("Personal expense controller is working!");
	}

	@GetMapping("/monthly/{year}/{month}")
	public ResponseEntity<MonthlyExpenseReport> getMonthlyReport(
		@PathVariable int year,
		@PathVariable int month
	) {
		try {
			MonthlyExpenseReport report = service.getMonthlyReport(year, month);
			return ResponseEntity.ok(report);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
}
