package com.housemanager.nn.controller;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.housemanager.nn.model.Building;
import com.housemanager.nn.model.Fee;
import com.housemanager.nn.model.Occupant;
import com.housemanager.nn.repository.ApartmentRepository;
import com.housemanager.nn.repository.BuildingRepository;
import com.housemanager.nn.repository.OccupantRepository;
import com.housemanager.nn.service.FeeService;

@Controller
public class OccupantController {

	// 3.b CRUD operations for occupants

	@Autowired
	private OccupantRepository occupantRepository;
	@Autowired
	private BuildingRepository buildingRepository;
	@Autowired
	private ApartmentRepository apartmentRepository;

	// 8.c Filter & sort occupants by name and age
	@GetMapping(path = "/occupants")
	public String showOccupantsPage(Model model, @RequestParam(required = false) String sortBy,
			@RequestParam(required = false) String minAge, @RequestParam(required = false) String building) {

		List<Occupant> occupants = occupantRepository.findAll();
		List<Building> buildings = buildingRepository.findAll();
		
		// check if filter applied
		if (minAge != null) {
			occupants.removeIf(o -> o.getAge() < Integer.parseInt(minAge));
		}

		// check if filter applied
		if (building != null) {
			occupants.removeIf(o -> o.getApartment().getBuilding().getId() != Integer.parseInt(building));
			model.addAttribute("building", buildingRepository.findById(Integer.parseInt(building)).get());
		}

		
		// if sorting selected, then apply it
		if (sortBy != null && sortBy.equals("name")) {
			occupants.sort(new Comparator<Occupant>() {
				@Override
				public int compare(Occupant o1, Occupant o2) {
					return o1.getName().compareToIgnoreCase(o2.getName());
				}
			});
		}

		model.addAttribute("occupants", occupants);
		model.addAttribute("buildings", buildings);

		return "occupant/occupants";
	}

	@GetMapping(path = "/occupants/add")
	public String showAddOccupantsPage(Model model) {
		model.addAttribute("occupant", new Occupant());
		model.addAttribute("apartments", apartmentRepository.findAll());
		return "occupant/occupants-add";
	}

	@PostMapping(path = "/occupants/add")
	public String addServiceCompany(@ModelAttribute Occupant occupant) {
		occupantRepository.save(occupant);

		return "redirect:/occupants";
	}

	@GetMapping("/occupants/edit/{id}")
	public String showUpdateForm(@PathVariable("id") int id, Model model) {
		Occupant occupant = occupantRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid company Id:" + id));

		model.addAttribute("occupant", occupant);
		model.addAttribute("apartments", apartmentRepository.findAll());
		
		return "occupant/occupants-edit";
	}

	@PostMapping("/occupants/edit/{id}")
	public String updateOccupant(@ModelAttribute Occupant occupant) throws Exception {

		Occupant occupantInDB = occupantRepository.findById(occupant.getId()).orElse(null);
		if (occupantInDB != null) {
			occupantInDB.setAge(occupant.getAge());
			occupantInDB.setApartment(occupant.getApartment());
			occupantInDB.setName(occupant.getName());
			occupantRepository.save(occupantInDB);
		} else {
			throw new Exception("Occupant not found");
		}

		return "redirect:/occupants";
	}

	@GetMapping("/occupants/delete/{id}")
	public String deleteOccupant(@PathVariable("id") int id, Model model) {
		Occupant occupant = occupantRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid occupant Id:" + id));
		occupantRepository.delete(occupant);
		return "redirect:/occupants";
	}

	@GetMapping("/fee-payments")
	public String feePaymentsPage(@RequestParam(required = false) String occupantId, Model model) {
		// List<Occupant> occupants = occupantRepository.findAll();
		List<Object[]> dueFeesByOccupantResultSet = occupantRepository.findAllDueFeesByOccupantNative();

		List<Occupant> occupants = new ArrayList<>();
		List<Fee> dueFees = new ArrayList<>();

		//get only the occupants that have due fees 
		dueFeesByOccupantResultSet.forEach(r -> {
			Occupant occ = new Occupant();
			occ.setId((int) r[0]);
			occ.setAge((int) r[1]);
			occ.setName((String) r[2]);

			occupants.add(occ);
		});

		model.addAttribute("occupants", occupants);

		//if occupantId is received, then proceed with due fees
		if (occupantId != null) {
			dueFeesByOccupantResultSet.forEach(r -> {
				// if occupant id in resultSet matches the received occupantId, then collect all
				// fees in list;
				if ((int) r[0] == Integer.parseInt(occupantId)) {
					Fee fee = new Fee();
					fee.setId((int) r[4]);
					fee.setAmountDue((BigDecimal) r[5]);
					Date issuedDate = (Date) r[7];
					fee.setIssueDate(issuedDate.toLocalDate());
					dueFees.add(fee);
				}

			});

			model.addAttribute("dueFees", dueFees);
		}

		return "fee/fee-payments";
	}

	// 7. Payment of fees by occupants
	@GetMapping("/new-fee-payment")
	public String payFee(
			@RequestParam(name = "occupantId") int id,
			@RequestParam int feeId,
			@RequestParam String datePaid,
			
			 Model model) {
		
		Occupant occupant = occupantRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid occupant Id:" + id));

		Fee unpaidFee = occupant.getApartment().getFees().stream().filter(f -> f.getId() == feeId).findFirst()
				.orElse(null);
		BigDecimal amountReceived = unpaidFee.getAmountDue();
		unpaidFee.setAmountReceived(amountReceived);
		unpaidFee.setAmountDue(unpaidFee.getAmountDue().subtract(amountReceived));
		unpaidFee.setPaymentDate(LocalDate.parse(datePaid));
		occupantRepository.flush();

		// 10. Save received fee to file
		FeeService.saveReceivedFeeToFile(unpaidFee);

		return "redirect:/fees?building="+occupant.getApartment().getBuilding().getId();
	}

}
