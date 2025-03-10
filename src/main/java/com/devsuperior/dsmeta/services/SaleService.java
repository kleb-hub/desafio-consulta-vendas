package com.devsuperior.dsmeta.services;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.List;

import com.devsuperior.dsmeta.dto.SalesReportDTO;
import com.devsuperior.dsmeta.dto.SalesSummaryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.devsuperior.dsmeta.dto.SaleMinDTO;
import com.devsuperior.dsmeta.entities.Sale;
import com.devsuperior.dsmeta.repositories.SaleRepository;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SaleService {

	LocalDate today = LocalDate.ofInstant(Instant.now(), ZoneId.systemDefault());
	DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	@Autowired
	private SaleRepository repository;

	@Transactional(readOnly = true)
	public SaleMinDTO findById(Long id) {
		Optional<Sale> result = repository.findById(id);
		Sale entity = result.get();
		return new SaleMinDTO(entity);
	}

	@Transactional(readOnly = true)
	public Page<SalesReportDTO> getReport(String minDate, String maxDate, String name, Pageable pageable) {

		LocalDate dtMinDate, dtMaxDate;

		if (maxDate.isEmpty()) {
			dtMaxDate = today;
		} else {
			dtMaxDate = LocalDate.parse(maxDate, dateFormatter);
		}

		if (minDate.isEmpty()) {
			dtMinDate = dtMaxDate.minusYears(1L);
		} else {
			dtMinDate = LocalDate.parse(minDate, dateFormatter);
		}

		return repository.getSalesReport(dtMinDate, dtMaxDate, name, pageable);
	}

	@Transactional(readOnly = true)
	public List<SalesSummaryDTO> getSummary(String minDate, String maxDate) {

		LocalDate dtMinDate, dtMaxDate;

		if (maxDate.isEmpty()) {
			dtMaxDate = today;
		} else {
			dtMaxDate = LocalDate.parse(maxDate, dateFormatter);
		}

		if (minDate.isEmpty()) {
			dtMinDate = dtMaxDate.minusYears(1L);
		} else {
			dtMinDate = LocalDate.parse(minDate, dateFormatter);
		}

		return repository.getSalesSummary(dtMinDate, dtMaxDate);
	}
}
