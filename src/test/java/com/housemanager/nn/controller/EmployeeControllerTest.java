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

import com.housemanager.nn.repository.EmployeeRepository;
import com.housemanager.nn.repository.ServiceCompanyRepository;

@ExtendWith(SpringExtension.class)
@WebMvcTest(EmployeeController.class)
class EmployeeControllerTest {

	@MockBean
	private EmployeeRepository employeeRepository;
	@MockBean
	private ServiceCompanyRepository serviceCompanyRepository;
	
	@Autowired
	private MockMvc mockMvc;

	@Test
	void testShowEmployeesPage() throws Exception {
		MvcResult result =	mockMvc.perform(MockMvcRequestBuilders.get("/employees"))
		        .andExpect(status().isOk())
		        .andReturn();
		  
			System.out.println(result.getResponse().getContentAsString());
			String searchedPhrase = "<h1>Employees</h1>";
			assertThat(result.getResponse().getContentAsString()).containsIgnoringCase(searchedPhrase);
	}

	@Test
	void testShowAddEmployeesPage() throws Exception {
	MvcResult result =	mockMvc.perform(MockMvcRequestBuilders.get("/employees/add"))
	        .andExpect(status().isOk())
	        .andReturn();
	  
		System.out.println(result.getResponse().getContentAsString());
		String searchedPhrase = "<h3>Add a new employee</h3>";
		assertThat(result.getResponse().getContentAsString()).containsIgnoringCase(searchedPhrase);
	}

}
