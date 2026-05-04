package com.cezarfbf.finance.core.reports;

import com.cezarfbf.finance.core.reports.dto.MonthlyReportDto;
import com.cezarfbf.finance.core.reports.dto.MonthlyReportSummaryDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reports/monthly")
@Tag(name = "Monthly Reports", description = "Monthly accounting reports")
public class MonthlyReportController {

	private final MonthlyReportService service;

	public MonthlyReportController(MonthlyReportService service) {
		this.service = service;
	}

	@GetMapping
	@Operation(summary = "List monthly summaries for a year",
		description = "Returns one summary per month that has data for the given year")
	public ResponseEntity<List<MonthlyReportSummaryDto>> listMonthly(
		@Parameter(description = "Year (e.g. 2025)", required = true)
		@RequestParam("year") int year
	) {
		return ResponseEntity.ok(service.listSummaries(year));
	}

	@GetMapping("/{year}/{month}")
	@Operation(summary = "Get monthly report",
		description = "Full monthly report with KPIs and transactions")
	public ResponseEntity<MonthlyReportDto> getMonthly(
		@Parameter(description = "Year (e.g. 2025)", required = true)
		@PathVariable int year,
		@Parameter(description = "Month (1-12)", required = true)
		@PathVariable int month
	) {
		return ResponseEntity.ok(service.getMonthlyReport(year, month));
	}
}
