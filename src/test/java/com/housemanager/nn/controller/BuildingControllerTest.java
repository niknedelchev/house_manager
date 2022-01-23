package com.housemanager.nn.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.housemanager.nn.repository.BuildingRepository;
import com.housemanager.nn.repository.EmployeeRepository;
import com.housemanager.nn.repository.ServiceCompanyRepository;


@ExtendWith(SpringExtension.class)
@WebMvcTest(BuildingController.class)
class BuildingControllerTest {
	@MockBean
	private BuildingRepository buildingRepository;
	@MockBean
	private ServiceCompanyRepository serviceCompanyRepository;
	@MockBean
	private EmployeeRepository employeeRepository;

	@Autowired
	private MockMvc mockMvc;


	@Test
	void testShowBuildingsPage() throws Exception {

		MvcResult result =	mockMvc.perform(MockMvcRequestBuilders.get("/buildings"))
		        .andExpect(status().isOk())
		        .andReturn();
		  
			System.out.println(result.getResponse().getContentAsString());
			String searchedPhrase = "<h1>buildings</h1>";
			assertThat(result.getResponse().getContentAsString()).containsIgnoringCase(searchedPhrase);

	}

	@Test
	void testShowAddBuildingsPage() throws Exception {
		MvcResult result =	mockMvc.perform(MockMvcRequestBuilders.get("/buildings/add"))
		        .andExpect(status().isOk())
		        .andReturn();
		  
			System.out.println(result.getResponse().getContentAsString());
			String searchedPhrase = "<form action=\"/buildings/add\" class=\"w-50\" method=\"post\">";
			assertThat(result.getResponse().getContentAsString()).containsIgnoringCase(searchedPhrase);
	}


	@Test
	void testServiceContractPage() throws Exception {
		MvcResult result =	mockMvc.perform(MockMvcRequestBuilders.get("/service-contract"))
		        .andExpect(status().isOk())
		        .andReturn();
		  
			System.out.println(result.getResponse().getContentAsString());
			String searchedPhrase = "<h1>service contract</h1>";
			assertThat(result.getResponse().getContentAsString()).containsIgnoringCase(searchedPhrase);
	}

}
