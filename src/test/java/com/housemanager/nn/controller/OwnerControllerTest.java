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

import com.housemanager.nn.repository.OwnerRepository;

@ExtendWith(SpringExtension.class)
@WebMvcTest(OwnerController.class)
class OwnerControllerTest {

	@MockBean
	private OwnerRepository ownerRepository;
	@Autowired
	private MockMvc mockMvc;

	@Test
	void testShowOwnersPage() throws Exception {
		MvcResult result =	mockMvc.perform(MockMvcRequestBuilders.get("/owners"))
		        .andExpect(status().isOk())
		        .andReturn();
		  
			System.out.println(result.getResponse().getContentAsString());
			String searchedPhrase = "<h1>owners</h1>";
			assertThat(result.getResponse().getContentAsString()).containsIgnoringCase(searchedPhrase);
	}

	@Test
	void testShowAddOwnersPage() throws Exception {
		MvcResult result =	mockMvc.perform(MockMvcRequestBuilders.get("/owners/add"))
		        .andExpect(status().isOk())
		        .andReturn();
		  
			System.out.println(result.getResponse().getContentAsString());
			String searchedPhrase = "<h3>add a new owner</h3>";
			assertThat(result.getResponse().getContentAsString()).containsIgnoringCase(searchedPhrase);
	}

}
