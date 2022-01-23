package com.housemanager.nn.controller;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.housemanager.nn.model.Employee;
import com.housemanager.nn.model.Fee;
import com.housemanager.nn.model.ServiceCompany;
import com.housemanager.nn.repository.ServiceCompanyRepository;
import com.housemanager.nn.repository.FeeRepository;

@Controller
public class ServiceCompanyController {

	// 1. CRUD operations for companies
	
	@Autowired
	private ServiceCompanyRepository serviceCompanyRepository;

	//8.a Filter & sort companies by collected fees
	@GetMapping(path = "/companies")
	public String showServiceCompaniesPage(@RequestParam(required = false) String sortBy,
			@RequestParam(required = false) String feesAtleast, Model model) {
		
		List<Object[]> companiesAndCollectedFees;
		Map<ServiceCompany, String> companies = new LinkedHashMap();

		//check if filter applied
		if(feesAtleast!=null)
			companiesAndCollectedFees = serviceCompanyRepository.findAllCompaniesJoinCollectedFeesFilteredBySum(Integer.parseInt(feesAtleast));
		else
			companiesAndCollectedFees = serviceCompanyRepository.findAllCompaniesJoinCollectedFees();
		
		// Map : key->company, value->collectedFees;
		companiesAndCollectedFees.stream().forEach(obj -> {
			ServiceCompany tmpCompany = new ServiceCompany((String) obj[2], null);
			tmpCompany.setId((int) obj[1]);
			BigDecimal tmpRevenue = obj[0]!= null? (BigDecimal) obj[0] : BigDecimal.ZERO.setScale(2);
			companies.put(tmpCompany, tmpRevenue.toString());
		});

		// If sort by revenue is selected, then apply it, ASC
		if (sortBy != null && sortBy.equals("revenue")) {
			Map<ServiceCompany, String> sortedCompanies = companies.entrySet().stream()
					.sorted(new Comparator<Entry<ServiceCompany, String>>() {
						@Override
						public int compare(Entry<ServiceCompany, String> e1, Entry<ServiceCompany, String> e2) {
							BigDecimal c1CollectedFees = new BigDecimal(e1.getValue());
							BigDecimal c2CollectedFees = new BigDecimal(e2.getValue());
							return c1CollectedFees.compareTo(c2CollectedFees);
						}
					}).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
							(oldValue, newValue) -> oldValue, LinkedHashMap::new));

			model.addAttribute("companies", sortedCompanies);
			return "service-company/service-companies";
		}

		model.addAttribute("companies", companies);
		return "service-company/service-companies";
	}

	@GetMapping(path = "/companies/add")
	public String showAddServiceCompanyPage(Model model) {
		model.addAttribute("serviceCompany", new ServiceCompany());

		return "service-company/service-companies-add";
	}

	@PostMapping(path = "/companies/add")
	public String addServiceCompany(@ModelAttribute ServiceCompany serviceCompany) {
		serviceCompanyRepository.save(serviceCompany);

		return "redirect:/companies";
	}

	@GetMapping("/companies/edit/{id}")
	public String showUpdateForm(@PathVariable("id") int id, Model model) {
		ServiceCompany serviceCompany = serviceCompanyRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid company Id:" + id));

		model.addAttribute("serviceCompany", serviceCompany);
		return "service-company/service-companies-edit";
	}

	@PostMapping("/companies/edit/{id}")
	public String updateServiceCompany(@ModelAttribute ServiceCompany serviceCompany) throws Exception {

		ServiceCompany companyInDB = serviceCompanyRepository.findById(serviceCompany.getId()).orElse(null);

		if (companyInDB != null) {
			companyInDB.setName(serviceCompany.getName());
			serviceCompanyRepository.save(companyInDB);
		} else {
			throw new Exception("Company not found");
		}

		return "redirect:/companies";
	}

	@GetMapping("/companies/delete/{id}")
	public String deleteServiceCompanyBrand(@PathVariable("id") int id, Model model) {
		ServiceCompany serviceCompany = serviceCompanyRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid company Id:" + id));
		serviceCompanyRepository.delete(serviceCompany);
		return "redirect:/companies";
	}

}
