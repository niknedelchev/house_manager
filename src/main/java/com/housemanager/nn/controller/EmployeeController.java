package com.housemanager.nn.controller;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Supplier;
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
import com.housemanager.nn.model.ServiceCompany;
import com.housemanager.nn.repository.EmployeeRepository;
import com.housemanager.nn.repository.ServiceCompanyRepository;

@Controller
public class EmployeeController {
	
	// 4. CRUD operations for employees

	@Autowired
	private EmployeeRepository employeeRepository;
	@Autowired
	private ServiceCompanyRepository serviceCompanyRepository;

	// 8.b Filter & sort employees data
	@GetMapping(path = "/employees")
	public String showEmployeesPage(Model model, @RequestParam(required = false) String sortBy,
			@RequestParam(required = false) String minBuildingsCount,
			@RequestParam(required = false) String company) {

		List<Employee> employees = employeeRepository.findAll();
		
		Comparator<ServiceCompany> byId = Comparator.comparingInt(ServiceCompany::getId);
		Supplier<TreeSet<ServiceCompany>> co = () -> new TreeSet<ServiceCompany>(byId);
		TreeSet<ServiceCompany> companies = employees.stream().map(e->e.getServiceCompany()).collect(Collectors.toCollection(co));
		
		// check for filter and apply it
		if (minBuildingsCount != null) {
			employees.removeIf(e -> e.getBuildings().size() < Integer.parseInt(minBuildingsCount));
		}
		
		// check for filter and apply it
		if (company != null) {
			employees.removeIf(e -> e.getServiceCompany().getId() != Integer.parseInt(company));
		}

		// if sorting selected, then apply it
		if (sortBy != null && sortBy.equals("buildings")) {
			employees.sort(new Comparator<Employee>() {
				@Override
				public int compare(Employee e1, Employee e2) {
					return e1.getBuildings().size() - e2.getBuildings().size();
				}
			});
		} else if (sortBy != null && sortBy.equals("name")) {
			employees.sort(new Comparator<Employee>() {
				@Override
				public int compare(Employee e1, Employee e2) {
					return e1.getName().compareTo(e2.getName());
				}
			});
		} else if (sortBy != null && sortBy.equals("nameAndBuldings")) {
			employees.sort(Comparator.comparing(Employee::getName)
					.thenComparing((e1, e2) -> e1.getBuildings().size() - e2.getBuildings().size()));
		}

		model.addAttribute("employees", employees);
		model.addAttribute("companies", companies);

		return "employee/employees";
	}

	@GetMapping(path = "/employees/add")
	public String showAddEmployeesPage(Model model) {
		model.addAttribute("employee", new Employee());
		model.addAttribute("companies",serviceCompanyRepository.findAll());
		return "employee/employees-add";
	}

	@PostMapping(path = "/employees/add")
	public String addServiceCompany(@ModelAttribute Employee employee) {
		employeeRepository.save(employee);

		return "redirect:/employees";
	}

	@GetMapping("/employees/edit/{id}")
	public String showUpdateForm(@PathVariable("id") int id, Model model) {
		Employee employee = employeeRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid company Id:" + id));

		model.addAttribute("employee", employee);
		model.addAttribute("companies",serviceCompanyRepository.findAll());
		return "employee/employees-edit";
	}

	@PostMapping("/employees/edit/{id}")
	public String updateEmployee(@ModelAttribute Employee employee) throws Exception {

		Employee employeeInDB = employeeRepository.findById(employee.getId()).orElse(null);
		if (employeeInDB != null) {
			employeeInDB.setName(employee.getName());
			employeeInDB.setServiceCompany(employee.getServiceCompany());
			employeeRepository.save(employeeInDB);
		} else {
			throw new Exception("Employee not found");
		}

		return "redirect:/employees";
	}

	@GetMapping("/employees/delete/{id}")
	public String deleteEmployee(@PathVariable("id") int id, Model model) {
		Employee employee = employeeRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid employee Id:" + id));
		employeeRepository.delete(employee);
		return "redirect:/employees";
	}
}
