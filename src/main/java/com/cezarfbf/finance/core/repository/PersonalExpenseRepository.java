package com.cezarfbf.finance.core.repository;

import com.cezarfbf.finance.core.entity.PersonalExpense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PersonalExpenseRepository extends JpaRepository<PersonalExpense, Long> {

	@Query("SELECT e FROM PersonalExpense e WHERE e.date >= :startDate AND e.date < :endDate ORDER BY e.date")
	List<PersonalExpense> findByYearAndMonth(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
