package com.housemanager.nn.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.housemanager.nn.model.Owner;
import com.housemanager.nn.repository.OwnerRepository;

@Controller
public class OwnerController {

	// 3.a CRUD operations for owners
	
	@Autowired
	private OwnerRepository ownerRepository;

	@GetMapping(path = "/owners")
	public String showOwnersPage(Model model) {

		List<Owner> owners = ownerRepository.findAll();
		model.addAttribute("owners", owners);

		return "owner/owners";
	}
	
	@GetMapping(path = "/owners/add")
	public String showAddOwnersPage(Model model) {
		model.addAttribute("owner", new Owner());
		
		return "owner/owners-add";
	}
	
	@PostMapping(path = "/owners/add")
	public String addServiceCompany(@ModelAttribute Owner owner) {
		ownerRepository.save(owner);
		
		return "redirect:/owners";
	}
	
	@GetMapping("/owners/edit/{id}")
	public String showUpdateForm(@PathVariable("id") int id, Model model) {
	    Owner owner = ownerRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid company Id:" + id));
	    
	    model.addAttribute("owner", owner);
	    return "owner/owners-edit";
	}
	
	@PostMapping("/owners/edit/{id}")
	public String updateOwner(@ModelAttribute Owner owner) throws Exception {
	
		Owner ownerInDB = ownerRepository.findById(owner.getId()).orElse(null);
		if (ownerInDB != null) {
			ownerInDB.setAge(owner.getAge());
			ownerInDB.setName(owner.getName());
			ownerRepository.save(ownerInDB);
		} else {
			throw new Exception("Owner not found");
		}
		
	    return "redirect:/owners";
	}
	    
	@GetMapping("/owners/delete/{id}")
	public String deleteOwner(@PathVariable("id") int id, Model model) {
	    Owner owner = ownerRepository.findById(id)
	      .orElseThrow(() -> new IllegalArgumentException("Invalid owner Id:" + id));
	    ownerRepository.delete(owner);
	    return "redirect:/owners";
	}
	
}
