package com.housemanager.nn.controller;

import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.housemanager.nn.model.Building;
import com.housemanager.nn.model.Employee;
import com.housemanager.nn.model.ServiceCompany;
import com.housemanager.nn.repository.BuildingRepository;
import com.housemanager.nn.repository.EmployeeRepository;
import com.housemanager.nn.repository.ServiceCompanyRepository;

@Controller
public class BuildingController {

	// 2. CRUD operations for buildings
	
	@Autowired
	private BuildingRepository buildingRepository;
	@Autowired
	private ServiceCompanyRepository serviceCompanyRepository;
	@Autowired
	private EmployeeRepository employeeRepository;


	@GetMapping(path = "/buildings")
	public String showBuildingsPage(Model model, @RequestParam(required = false) String company,  @RequestParam(required = false) String employee) {
		List<Building> buildings = buildingRepository.findAll();

		if(company !=null) {
			buildings.removeIf(b->b.getServiceEmployee().getServiceCompany().getId()!= Integer.parseInt(company));
			model.addAttribute("company",serviceCompanyRepository.findById(Integer.parseInt(company)).get());
		}
		
		if(employee !=null) {
			buildings.removeIf(b->b.getServiceEmployee().getId()!= Integer.parseInt(employee));
			model.addAttribute("employee",employeeRepository.findById(Integer.parseInt(employee)).get());
		}

		
		model.addAttribute("buildings", buildings);

		return "building/buildings";
	}

	@GetMapping(path = "/buildings/add")
	public String showAddBuildingsPage(Model model) {
		model.addAttribute("building", new Building());

		return "building/buildings-add";
	}

	@PostMapping(path = "/buildings/add")
	public String addServiceCompany(@ModelAttribute Building building) {
		buildingRepository.save(building);

		return "redirect:/buildings";
	}

	@GetMapping("/buildings/edit/{id}")
	public String showUpdateForm(@PathVariable("id") int id, Model model) {
		Building building = buildingRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid company Id:" + id));

		model.addAttribute("building", building);
		return "building/buildings-edit";
	}

	@PostMapping("/buildings/edit/{id}")
	public String updateBuilding(@ModelAttribute Building building) throws Exception {

		Building buildingInDB = buildingRepository.findById(building.getId()).orElse(null);
		if (buildingInDB != null) {
			buildingInDB.setAddress(building.getAddress());
			buildingInDB.setTotalFloors(building.getTotalFloors());
			buildingInDB.setCommonAreaSize(building.getCommonAreaSize());
			buildingInDB.setTotalAreaSize(building.getTotalAreaSize() + building.getCommonAreaSize());
			buildingRepository.save(buildingInDB);
		} else {
			throw new Exception("Building not found");
		}

		return "redirect:/buildings";
	}

	@GetMapping("/buildings/delete/{id}")
	public String deleteBuilding(@PathVariable("id") int id, Model model) {
		Building building = buildingRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid building Id:" + id));
		buildingRepository.delete(building);
		return "redirect:/buildings";
	}

	@GetMapping("/service-contract")
	public String serviceContractPage(Model model) {
		List<Building> buildings = buildingRepository.findAll();
		List<ServiceCompany> companies = serviceCompanyRepository.findAll();
		model.addAttribute("buildings", buildings);
		model.addAttribute("companies", companies);
		
		return "service-company/service-contract";
	}
	
	
	// 5. Set who is the servicing employee of the particular building
	// When the new service contract is signed, the employee with the least count of serviced buildings gets it.
	@GetMapping("/new-service-contract")
	public String newServiceContract(
			@RequestParam(name="building") int buildingId,
			@RequestParam(name ="company") int companyId) {
		
		Building building = buildingRepository.findById(buildingId)
				.orElseThrow(() -> new IllegalArgumentException("Invalid building Id:" + buildingId));
		
		List<Employee> employees = serviceCompanyRepository.findById(companyId).get().getEmployees();
		
		employees.sort(new Comparator<Employee>() {
			@Override
			public int compare(Employee e1, Employee e2) {
				return e1.getBuildings().size() - e2.getBuildings().size();
			}
		});
		
		Employee employeeMinBuildings = employees.get(0);
		
		building.setServiceEmployee(employeeMinBuildings);
		buildingRepository.save(building);
		
		return "redirect:/buildings";
	}
	

}
