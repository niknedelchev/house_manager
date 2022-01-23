package com.housemanager.nn.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

	@GetMapping(path = "/")
	public String showIndexPage(Model model) {
		
		return "index";
	}
}
