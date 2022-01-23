package com.housemanager.nn.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.housemanager.nn.model.Apartment;
import com.housemanager.nn.model.Building;
import com.housemanager.nn.model.Employee;
import com.housemanager.nn.model.Occupant;
import com.housemanager.nn.model.Owner;
import com.housemanager.nn.model.ServiceCompany;

@DataJpaTest
class OccupantRepositoryTest {

	@Autowired
	private TestEntityManager testEntityManager ;
	@Autowired
	private OccupantRepository occupantRepository;


	@Test
	void testSaveAndFindAll() {
		ServiceCompany co = new ServiceCompany("company",null);
		testEntityManager.persistAndFlush(co);
		
		Employee emp1 = new Employee("name1",co,null,null);
		testEntityManager.persistAndFlush(emp1);
		
		Building build1 = new Building("address",2,50,null,emp1,250,200);
		Owner owner1 = new Owner("name1",20,null);
		Owner owner2 = new Owner("name2",21,null);
		testEntityManager.persistAndFlush(build1);
		testEntityManager.persistAndFlush(owner1);
		testEntityManager.persistAndFlush(owner2);
		
		Apartment apt1 = new Apartment(1,150,owner1,null,true,build1);
		Apartment apt2 = new Apartment(1,150,owner2,null,true,build1);
		testEntityManager.persistAndFlush(apt1);
		testEntityManager.persistAndFlush(apt2);
	
		
		Occupant oc1= new Occupant("name1",20,apt1);
		Occupant oc2= new Occupant("name2",21,apt2);

		testEntityManager.persistAndFlush(oc1);
		testEntityManager.persistAndFlush(oc2);
		
		assertThat(occupantRepository.findAll().size()).isEqualTo(2);

		
	}

}
