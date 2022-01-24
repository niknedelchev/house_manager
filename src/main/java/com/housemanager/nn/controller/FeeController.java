package com.housemanager.nn.controller;

import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

import org.apache.tomcat.jni.File;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.housemanager.nn.model.Apartment;
import com.housemanager.nn.model.Employee;
import com.housemanager.nn.model.Fee;
import com.housemanager.nn.repository.ApartmentRepository;
import com.housemanager.nn.repository.BuildingRepository;
import com.housemanager.nn.repository.EmployeeRepository;
import com.housemanager.nn.repository.FeeRepository;
import com.housemanager.nn.repository.OccupantRepository;
import com.housemanager.nn.repository.ServiceCompanyRepository;

enum Fees {
	STANDARD_FEE_FOR_APT_SIZE(0.09), EXTRA_FEE_OVER_7YRS_OLD(0.99), EXTRA_FEE_PET(11.99);

	private double fee;

	Fees(double f) {
		fee = f;
	}

	double getFee() {
		return fee;
	}
}

@Controller
public class FeeController {

	@Autowired
	private FeeRepository feeRepository;
	@Autowired
	private ApartmentRepository apartmentRepository;
	@Autowired
	private EmployeeRepository employeeRepository;
	@Autowired
	private ServiceCompanyRepository serviceCompanyRepository;
	@Autowired
	private BuildingRepository buildingRepository;

	@GetMapping(path = "/fees")
	public String showFeesPage(Model model, 
			@RequestParam(required = false) String company,
			@RequestParam(required = false) String employee,
			@RequestParam(required = false) String building,
			@RequestParam(required = false) String receivedByCompany,
			@RequestParam(required = false) String receivedByEmployee,
			@RequestParam(required = false) String receivedByBuilding) {

		List<Fee> fees = feeRepository.findAll();
		
		if(company !=null) {
			fees.removeIf(f->
			(
			f.getEmployee().getServiceCompany().getId()!= Integer.parseInt(company)
			||
			f.getAmountReceived().compareTo(BigDecimal.ZERO)>0
			));
			model.addAttribute("company",serviceCompanyRepository.findById(Integer.parseInt(company)).get());
		}

		if(employee !=null) {
			fees.removeIf(f->
			(
			f.getEmployee().getId()!= Integer.parseInt(employee)
			||
			f.getAmountReceived().compareTo(BigDecimal.ZERO)>0
			));
			model.addAttribute("employee",employeeRepository.findById(Integer.parseInt(employee)).get());
		}

		if(building !=null) {
			fees.removeIf(f->
			(
			f.getApartment().getBuilding().getId()!= Integer.parseInt(building)
			||
			f.getAmountReceived().compareTo(BigDecimal.ZERO)>0
			));
			model.addAttribute("building",buildingRepository.findById(Integer.parseInt(building)).get());
		}

		if(receivedByCompany !=null) {
			fees.removeIf(f->
			(
			f.getEmployee().getServiceCompany().getId()!= Integer.parseInt(receivedByCompany)
			||
			f.getAmountDue().compareTo(BigDecimal.ZERO)>0
			));
			model.addAttribute("receivedByCompany",serviceCompanyRepository.findById(Integer.parseInt(receivedByCompany)).get());
		}

		if(receivedByEmployee !=null) {
			fees.removeIf(f->
			(
			f.getEmployee().getId()!= Integer.parseInt(receivedByEmployee)
			||
			f.getAmountDue().compareTo(BigDecimal.ZERO)>0
			));
			model.addAttribute("receivedByEmployee",employeeRepository.findById(Integer.parseInt(receivedByEmployee)).get());
		}

		if(receivedByBuilding !=null) {
			fees.removeIf(f->
			(
			f.getApartment().getBuilding().getId()!= Integer.parseInt(receivedByBuilding)
			||
			f.getAmountDue().compareTo(BigDecimal.ZERO)>0
			));
			model.addAttribute("receivedByBuilding",buildingRepository.findById(Integer.parseInt(receivedByBuilding)).get());
		}

		model.addAttribute("fees", fees);

		return "fee/fees";
	}

	@GetMapping(path = "/fees/add")
	public String showAddFeesPage(Model model) {
		model.addAttribute("fee", new Fee());

		return "fee/fees-add";
	}

	@PostMapping(path = "/fees/add")
	public String addFeeCompany(@ModelAttribute Fee fee) {
		feeRepository.save(fee);

		return "redirect:/fees";
	}

	@GetMapping("/fees/edit/{id}")
	public String showUpdateForm(@PathVariable("id") int id, Model model) {
		Fee fee = feeRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid company Id:" + id));

		model.addAttribute("fee", fee);
		return "fee/fees-edit";
	}

	@PostMapping("/fees/edit/{id}")
	public String updateFee(@ModelAttribute Fee fee) throws Exception {

		Fee feeInDB = feeRepository.findById(fee.getId()).orElse(null);
		if (feeInDB != null) {
			feeInDB.setAmountDue(fee.getAmountDue());
			feeInDB.setAmountReceived(fee.getAmountReceived());
			feeInDB.setAppartment(fee.getApartment());
			feeInDB.setIssueDate(fee.getIssueDate());
			feeInDB.setPaymentDate(fee.getPaymentDate());
			feeInDB.setEmployee(fee.getEmployee());
			feeRepository.save(feeInDB);
		} else {
			throw new Exception("Fee not found");
		}

		return "redirect:/fees";
	}

	@GetMapping("/fees/delete/{id}")
	public String deleteFee(@PathVariable("id") int id, Model model) {
		Fee fee = feeRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid fee Id:" + id));
		feeRepository.delete(fee);
		return "redirect:/fees";
	}

	@GetMapping("/fee-accruals")
	public String feeAccrualsPage(Model model) {
		List<Employee> employees = employeeRepository.findAll();
		model.addAttribute("employees", employees);

		return "fee/fee-accruals";
	}

	// 6. Accrue fees to be paid per apartment for the particular month/year
	@GetMapping("/new-fee-accrual")
	public String accrueFees(@RequestParam(name = "employee") int employeeId, @RequestParam int year,
			@RequestParam int month) {
		LocalDate issueDate = LocalDate.of(year, month, 1);
		List<Apartment> apartments = apartmentRepository.findApartmentsByEmployee(employeeId);

		for (Apartment apartment : apartments) {

			double size = apartment.getSize();
			long occupantsOver7yearOldCount = apartment.getOccupants().stream().filter(o -> o.getAge() >= 7).count();
			boolean hasPet = apartment.isHasPet();

			BigDecimal totalFeeDue = BigDecimal.valueOf(Fees.STANDARD_FEE_FOR_APT_SIZE.getFee())
					.multiply(BigDecimal.valueOf(size)).add(BigDecimal.valueOf(Fees.EXTRA_FEE_OVER_7YRS_OLD.getFee()))
					.multiply(BigDecimal.valueOf(occupantsOver7yearOldCount));

			if (hasPet)
				totalFeeDue = totalFeeDue.add(BigDecimal.valueOf(Fees.EXTRA_FEE_PET.getFee()));

			Fee fee = new Fee(totalFeeDue, BigDecimal.ZERO, issueDate, null, apartment,
					apartment.getBuilding().getServiceEmployee());
			feeRepository.save(fee);
		}

		return "redirect:/fees";
	}
}
