package com.housemanager.nn.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.Assert;

import com.housemanager.nn.repository.ApartmentRepository;
import com.housemanager.nn.repository.BuildingRepository;
import com.housemanager.nn.repository.OwnerRepository;


@ExtendWith(SpringExtension.class)
@WebMvcTest(ApartmentController.class)
class ApartmentControllerTest {
	
	@MockBean
	private ApartmentRepository apartmentRepository;
	@MockBean
	private BuildingRepository buildingRepository;
	@MockBean
	private OwnerRepository ownerRepository;

	@Autowired
	private MockMvc mockMvc;

	@Test
	void showApartmentsPageTest() throws Exception {
		MvcResult result =	mockMvc.perform(MockMvcRequestBuilders.get("/apartments"))
	        .andExpect(status().isOk())
	        .andReturn();
	  
		System.out.println(result.getResponse().getContentAsString());
		String searchedPhrase = "<h1>apartments</h1>";
		assertThat(result.getResponse().getContentAsString()).containsIgnoringCase(searchedPhrase);

			
	}

}
