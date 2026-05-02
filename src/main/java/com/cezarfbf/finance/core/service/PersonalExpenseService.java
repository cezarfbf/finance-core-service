package com.cezarfbf.finance.core.service;

import com.cezarfbf.finance.core.dto.CategorySummary;
import com.cezarfbf.finance.core.dto.ExpenseDTO;
import com.cezarfbf.finance.core.dto.MonthlyExpenseReport;
import com.cezarfbf.finance.core.entity.PersonalExpense;
import com.cezarfbf.finance.core.repository.PersonalExpenseRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PersonalExpenseService {

	private final PersonalExpenseRepository repository;

	public PersonalExpenseService(PersonalExpenseRepository repository) {
		this.repository = repository;
	}

	public MonthlyExpenseReport getMonthlyReport(int year, int month) {
		java.time.LocalDate startDate = java.time.LocalDate.of(year, month, 1);
		java.time.LocalDate endDate = startDate.plusMonths(1);

		List<PersonalExpense> expenses = repository.findByYearAndMonth(startDate, endDate);

		// Group by category and type, summing amounts
		Map<String, CategorySummary> summaryMap = new LinkedHashMap<>();
		for (PersonalExpense expense : expenses) {
			String key = expense.getCategory();
			summaryMap.putIfAbsent(key, new CategorySummary(
				expense.getCategory(),
				expense.getType().name(),
				BigDecimal.ZERO
			));

			CategorySummary existing = summaryMap.get(key);
			summaryMap.put(key, new CategorySummary(
				existing.category(),
				existing.type(),
				existing.amount().add(expense.getAmount())
			));
		}

		List<CategorySummary> summary = new ArrayList<>(summaryMap.values());

		// Convert entities to DTOs
		List<ExpenseDTO> expenseDTOs = expenses.stream()
			.map(e -> new ExpenseDTO(
				e.getId(),
				e.getDate(),
				e.getCategory(),
				e.getType().name(),
				e.getAmount(),
				e.getDescription()
			))
			.collect(Collectors.toList());

		return new MonthlyExpenseReport(year, month, summary, expenseDTOs);
	}
}
