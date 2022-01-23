package com.housemanager.nn.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.housemanager.nn.model.Apartment;
import com.housemanager.nn.model.Building;
import com.housemanager.nn.model.Owner;


@DataJpaTest
class ApartmentRepositoryTest {
	
	@Autowired
	private TestEntityManager testEntityManager ;
	@Autowired
	private ApartmentRepository apartmentRepository;

	@Test
	void testSaveAndFindAll() {
		Building build1 = new Building("address",2,50,null,null,250,200);
		Owner owner = new Owner("name",20,null);
		testEntityManager.persistAndFlush(build1);
		testEntityManager.persistAndFlush(owner);
		
		Apartment apt1 = new Apartment(1,150,owner,null,true,build1);
		Apartment apt2 = new Apartment(2,120,owner,null,false,build1);
		
		testEntityManager.persistAndFlush(apt1);
		testEntityManager.persistAndFlush(apt2);
		List<Apartment> tmp = apartmentRepository.findAll();
		assertThat(apartmentRepository.findAll().size()).isEqualTo(2);
	}

}
