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

@ExtendWith(SpringExtension.class)
@WebMvcTest(IndexController.class)
class IndexControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void testShowIndexPage() throws Exception {
		MvcResult result =	mockMvc.perform(MockMvcRequestBuilders.get("/"))
		        .andExpect(status().isOk())
		        .andReturn();
		  
			System.out.println(result.getResponse().getContentAsString());
			String searchedPhrase = "<p>This app facilitates the management activities";
			assertThat(result.getResponse().getContentAsString()).containsIgnoringCase(searchedPhrase);
	}

}
