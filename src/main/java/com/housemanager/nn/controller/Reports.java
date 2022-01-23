package com.housemanager.nn.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.housemanager.nn.model.Apartment;
import com.housemanager.nn.model.Building;
import com.housemanager.nn.model.Employee;
import com.housemanager.nn.model.Fee;
import com.housemanager.nn.model.Occupant;
import com.housemanager.nn.model.ServiceCompany;
import com.housemanager.nn.repository.BuildingRepository;
import com.housemanager.nn.repository.EmployeeRepository;
import com.housemanager.nn.repository.FeeRepository;
import com.housemanager.nn.repository.OccupantRepository;

@Controller
public class Reports {

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private BuildingRepository buildingRepository;

	@Autowired
	private OccupantRepository occupantRepository;

	@Autowired
	private FeeRepository feeRepository;
	
	@GetMapping(path = "/reports")
	public String showAvailableReports() {
		return "reports/reports";
	}

	// 9.a Report: serviced buildings by each employee in each company: Total count & list
	@GetMapping(path = "/reports/employees-buildings")
	public String showServicedBuildingsByEmployeeReport(Model model) {
		List<Employee> employees = employeeRepository.findAll(Sort.by("serviceCompany"));
		model.addAttribute("employees", employees);
		employees.get(0).getBuildings();
		return "reports/reports-employees-buildings";
	}

	// 9.b Report: Apartments in buildings: Total count & list
	@GetMapping(path = "/reports/apartments-buildings")
	public String showApartmentsInBuildingsReport(Model model) {
		List<Building> buildings = buildingRepository.findAll();
		model.addAttribute("buildings", buildings);

		return "reports/reports-apartments-buildings";
	}

	// 9.c Report: Occupants in buildings: Total count & list
	@GetMapping(path = "/reports/occupants-buildings")
	public String showOccupantsInBuildingsReport(Model model) {
		List<Building> buildings = buildingRepository.findAll();

		Map<Building, List<Occupant>> buildingsAndOccupants = new LinkedHashMap();

		for (Building building : buildings) {
			List<Occupant> occupants = new ArrayList();
			for (Apartment apartment : building.getApartments()) {
				occupants.addAll(apartment.getOccupants());
			}

			buildingsAndOccupants.put(building, occupants);
		}

		model.addAttribute("buildingsAndOccupants", buildingsAndOccupants);

		return "reports/reports-occupants-buildings";
	}

	// 9.d Report: Due fees - for each company; for each building; for each employee
	@GetMapping(path = "/reports/due-fees")
	public String showDueFeesReport(@RequestParam(required = false) String reportBy, Model model) {
		List<Fee> fees = feeRepository.findAll();

		if (reportBy == null) {
			Map<Building, BigDecimal> buildingsAndDueFees = dueFeesByKey(fees, Building.class);
			model.addAttribute("buildingsAndDueFees", buildingsAndDueFees);
			return "reports/reports-due-fees-by-buildings";
		} else {
			switch (reportBy) {
			case "building":
				Map<Building, BigDecimal> buildingsAndDueFees = dueFeesByKey(fees, Building.class);
				model.addAttribute("buildingsAndDueFees", buildingsAndDueFees);
				return "reports/reports-due-fees-by-buildings";
			case "company":
				Map<ServiceCompany, BigDecimal> companiesAndDueFees = dueFeesByKey(fees, ServiceCompany.class);
				model.addAttribute("companiesAndDueFees", companiesAndDueFees);
				return "reports/reports-due-fees-by-company";
			case "employee":
				Map<Employee, BigDecimal> employeesAndDueFees = dueFeesByKey(fees, Employee.class);
				model.addAttribute("employeesAndDueFees", employeesAndDueFees);
				return "reports/reports-due-fees-by-employee";
			default: 
				return "reports/reports";
			}

		}
	}
	// 9.e Report: Received fees - for each company; for each building; for each employee
	@GetMapping(path = "/reports/received-fees")
	public String showPaidFeesReport(@RequestParam(required = false) String reportBy, Model model) {
		List<Fee> fees = feeRepository.findAll();

		if (reportBy == null) {
			Map<Building, BigDecimal> buildingsAndReceivedFees = receivedFeesByKey(fees, Building.class);
			model.addAttribute("buildingsAndReceivedFees", buildingsAndReceivedFees);
			return "reports/reports-received-fees-by-building";
		} else {
			switch (reportBy) {
			case "building":
				Map<Building, BigDecimal> buildingsAndReceivedFees = receivedFeesByKey(fees, Building.class);
				model.addAttribute("buildingsAndReceivedFees", buildingsAndReceivedFees);
				return "reports/reports-received-fees-by-building";
			case "company":
				Map<ServiceCompany, BigDecimal> companiesAndReceivedFees = receivedFeesByKey(fees, ServiceCompany.class);
				model.addAttribute("companiesAndReceivedFees", companiesAndReceivedFees);
				return "reports/reports-received-fees-by-company";
			case "employee":
				Map<Employee, BigDecimal> employeesAndReceivedFees = receivedFeesByKey(fees, Employee.class);
				model.addAttribute("employeesAndReceivedFees", employeesAndReceivedFees);
				return "reports/reports-received-fees-by-employee";
			default: 
				return "reports/reports";
			}
		}
	}
	
	// utility functions
	public <K> Map<K, BigDecimal> dueFeesByKey(List<Fee> fees, Class<K> key) {

		Map<K, BigDecimal> keysAndDueFees = new HashMap();

		fees.forEach(f -> {
			K tmpKey;
			
			if(key== Building.class)
				tmpKey = (K) f.getApartment().getBuilding();
			else if(key== ServiceCompany.class)
				tmpKey = (K) f.getEmployee().getServiceCompany();
			else
				tmpKey = (K) f.getEmployee();
			
			if (keysAndDueFees.containsKey(tmpKey)) {
				BigDecimal tmpValue = keysAndDueFees.get(tmpKey);
				keysAndDueFees.put(tmpKey, tmpValue.add(f.getAmountDue()));
			} else {
				keysAndDueFees.put(tmpKey, f.getAmountDue());
			}

		});
		return keysAndDueFees;
	}
	
	public <K> Map<K, BigDecimal> receivedFeesByKey(List<Fee> fees, Class<K> key) {

		Map<K, BigDecimal> keysAndReceivedFees = new HashMap();

		fees.forEach(f -> {
			K tmpKey;
			
			if(key== Building.class)
				tmpKey = (K) f.getApartment().getBuilding();
			else if(key== ServiceCompany.class)
				tmpKey = (K) f.getEmployee().getServiceCompany();
			else
				tmpKey = (K) f.getEmployee();
			
			if (keysAndReceivedFees.containsKey(tmpKey)) {
				BigDecimal tmpValue = keysAndReceivedFees.get(tmpKey);
				keysAndReceivedFees.put(tmpKey, tmpValue.add(f.getAmountReceived()));
			} else {
				keysAndReceivedFees.put(tmpKey, f.getAmountReceived());
			}

		});
		return keysAndReceivedFees;
	}

}
