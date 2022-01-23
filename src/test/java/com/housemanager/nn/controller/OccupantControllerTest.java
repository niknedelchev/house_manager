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

import com.housemanager.nn.repository.ApartmentRepository;
import com.housemanager.nn.repository.BuildingRepository;
import com.housemanager.nn.repository.OccupantRepository;

@ExtendWith(SpringExtension.class)
@WebMvcTest(OccupantController.class)
class OccupantControllerTest {

	@MockBean
	private OccupantRepository occupantRepository;
	@MockBean
	private BuildingRepository buildingRepository;
	@MockBean
	private ApartmentRepository apartmentRepository;

	@Autowired
	private MockMvc mockMvc;

	@Test
	void testShowOccupantsPage() throws Exception {
		MvcResult result =	mockMvc.perform(MockMvcRequestBuilders.get("/occupants"))
		        .andExpect(status().isOk())
		        .andReturn();
		  
			System.out.println(result.getResponse().getContentAsString());
			String searchedPhrase = "<h1>occupants</h1>";
			assertThat(result.getResponse().getContentAsString()).containsIgnoringCase(searchedPhrase);
	}

	@Test
	void testShowAddOccupantsPage() throws Exception {
		MvcResult result =	mockMvc.perform(MockMvcRequestBuilders.get("/occupants/add"))
		        .andExpect(status().isOk())
		        .andReturn();
		  
			System.out.println(result.getResponse().getContentAsString());
			String searchedPhrase = "<h3>add a new occupant</h3>";
			assertThat(result.getResponse().getContentAsString()).containsIgnoringCase(searchedPhrase);
	}

}
