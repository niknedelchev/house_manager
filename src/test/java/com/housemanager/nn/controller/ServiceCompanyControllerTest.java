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

import com.housemanager.nn.repository.ServiceCompanyRepository;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ServiceCompanyController.class)
class ServiceCompanyControllerTest {

	@MockBean
	private ServiceCompanyRepository serviceCompanyRepository;

	@Autowired
	private MockMvc mockMvc;

	@Test
	void testShowServiceCompaniesPage() throws Exception {
		MvcResult result =	mockMvc.perform(MockMvcRequestBuilders.get("/companies"))
		        .andExpect(status().isOk())
		        .andReturn();
		  
			System.out.println(result.getResponse().getContentAsString());
			String searchedPhrase = "<h1>Service companies</h1>";
			assertThat(result.getResponse().getContentAsString()).containsIgnoringCase(searchedPhrase);
	}

	@Test
	void testShowAddServiceCompanyPage() throws Exception {
		MvcResult result =	mockMvc.perform(MockMvcRequestBuilders.get("/companies/add"))
		        .andExpect(status().isOk())
		        .andReturn();
		  
			System.out.println(result.getResponse().getContentAsString());
			String searchedPhrase = "<h3>add a new service company</h3>";
			assertThat(result.getResponse().getContentAsString()).containsIgnoringCase(searchedPhrase);
	}
//
//	@Test
//	void testAddServiceCompany() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	void testUpdateServiceCompany() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	void testDeleteServiceCompanyBrand() {
//		fail("Not yet implemented");
//	}

}
