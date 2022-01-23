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
import com.housemanager.nn.repository.FeeRepository;
import com.housemanager.nn.repository.OccupantRepository;

@ExtendWith(SpringExtension.class)
@WebMvcTest(Reports.class)
class ReportsTest {

	@MockBean
	private EmployeeRepository employeeRepository;
	@MockBean
	private BuildingRepository buildingRepository;
	@MockBean
	private OccupantRepository occupantRepository;
	@MockBean
	private FeeRepository feeRepository;

	@Autowired
	private MockMvc mockMvc;

	@Test
	void testShowAvailableReports() throws Exception {
		MvcResult result =	mockMvc.perform(MockMvcRequestBuilders.get("/reports"))
		        .andExpect(status().isOk())
		        .andReturn();
		  
			System.out.println(result.getResponse().getContentAsString());
			String searchedPhrase = "<h1>reports</h1>";
			assertThat(result.getResponse().getContentAsString()).containsIgnoringCase(searchedPhrase);
	}

//	@Test
//	void testShowServicedBuildingsByEmployeeReport() throws Exception {
//		MvcResult result =	mockMvc.perform(MockMvcRequestBuilders.get("/reports/employees-buildings"))
//		        .andExpect(status().isOk())
//		        .andReturn();
//		  
//			System.out.println(result.getResponse().getContentAsString());
//			String searchedPhrase = "<h1>Reports: Serviced buildings by each employee</h1>";
//			assertThat(result.getResponse().getContentAsString()).containsIgnoringCase(searchedPhrase);	
//	}
//
//	@Test
//	void testShowApartmentsInBuildingsReport() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	void testShowOccupantsInBuildingsReport() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	void testShowDueFeesReport() {
//		fail("Not yet implemented");
//	}

//	@Test
//	void testShowPaidFeesReport() {
//		fail("Not yet implemented");
//	}

}
