package com.housemanager.nn.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.housemanager.nn.model.Apartment;
import com.housemanager.nn.model.Building;
import com.housemanager.nn.model.Owner;
import com.housemanager.nn.repository.ApartmentRepository;
import com.housemanager.nn.repository.BuildingRepository;
import com.housemanager.nn.repository.OwnerRepository;

@Controller
public class ApartmentController {

	@Autowired
	private ApartmentRepository apartmentRepository;
	@Autowired
	private BuildingRepository buildingRepository;
	@Autowired
	private OwnerRepository ownerRepository;
	
	@GetMapping(path = "/apartments")
	public String showApartmentsPage(Model model, @RequestParam(required = false) String building) {
		
		List<Apartment> apartments = apartmentRepository.findAll();

		if(building !=null) {
			apartments.removeIf(a-> a.getBuilding().getId()!= Integer.parseInt(building));
			model.addAttribute("building",buildingRepository.findById(Integer.parseInt(building)).get());
		}

		model.addAttribute("apartments", apartments);

		return "apartment/apartments";
	}
	
	@GetMapping(path = "/apartments/add")
	public String showAddApartmentsPage(Model model) {
		model.addAttribute("apartment", new Apartment());
		
		List<Integer> floors=Arrays.asList(1, 2, 3, 4);
		model.addAttribute("floors", floors);

		List<Building> buildings = buildingRepository.findAll();
		model.addAttribute("buildings", buildings);
		
		List<Owner> owners = ownerRepository.findAll();
		model.addAttribute("owners", owners);
		
		return "apartment/apartments-add";
	}
	
	@PostMapping(path = "/apartments/add")
	public String addServiceCompany(@ModelAttribute Apartment apartment) {
		updateBuildingTotalArea(apartment, false);
		apartmentRepository.save(apartment);
		
		return "redirect:/apartments";
	}
	
	@GetMapping("/apartments/edit/{id}")
	public String showUpdateForm(@PathVariable("id") int id, Model model) {
	    Apartment apartment = apartmentRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid company Id:" + id));

		List<Integer> floors=Arrays.asList(1, 2, 3, 4);
		model.addAttribute("floors", floors);

		List<Owner> owners = ownerRepository.findAll();
		model.addAttribute("owners", owners);

		model.addAttribute("apartment", apartment);
	    return "apartment/apartments-edit";
	}
	
	@PostMapping("/apartments/edit/{id}")
	public String updateApartment(@ModelAttribute Apartment apartment) throws Exception {
	
		Apartment apartmentInDB = apartmentRepository.findById(apartment.getId()).orElse(null);
		if (apartmentInDB != null) {
			apartmentInDB.setFloor(apartment.getFloor());
			apartmentInDB.setBuilding(apartment.getBuilding());
			apartmentInDB.setHasPet(apartment.isHasPet());
			apartmentInDB.setOwner(apartment.getOwner());
			apartmentInDB.setSize(apartment.getSize());
			updateBuildingTotalArea(apartment, false);
			apartmentRepository.save(apartmentInDB);
		} else {
			throw new Exception("Apartment not found");
		}
		
	    return "redirect:/apartments";
	}
	    
	@GetMapping("/apartments/delete/{id}")
	public String deleteApartment(@PathVariable("id") int id, Model model) {
	    Apartment apartment = apartmentRepository.findById(id)
	      .orElseThrow(() -> new IllegalArgumentException("Invalid apartment Id:" + id));
	    updateBuildingTotalArea(apartment, true); 
	    apartmentRepository.delete(apartment);
		
	    return "redirect:/apartments";
	}
	
	
	public void updateBuildingTotalArea(Apartment apartment, boolean isDelete) {
		List<Apartment> apartments = apartment.getBuilding().getApartments();
		double totalApartmentsSize = 0;
		
		for(Apartment apt : apartments) {
			totalApartmentsSize += apt.getSize();
		}
		
		double buildingCommonAreaSize = apartment.getBuilding().getCommonAreaSize();
		double totalBuildingAreaSize = totalApartmentsSize + buildingCommonAreaSize;
		
		if(isDelete) {
			totalApartmentsSize -= apartment.getSize();
			totalBuildingAreaSize -= apartment.getSize();
		}
			
		
		apartment.getBuilding().setApartmentsAreaSize(totalApartmentsSize);
		apartment.getBuilding().setTotalAreaSize(totalBuildingAreaSize);
		
		
	}
	
}
