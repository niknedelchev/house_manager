package com.housemanager.nn.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.housemanager.nn.model.Apartment;
import com.housemanager.nn.model.Building;
import com.housemanager.nn.model.Employee;
import com.housemanager.nn.model.Fee;
import com.housemanager.nn.model.Owner;
import com.housemanager.nn.model.ServiceCompany;

@DataJpaTest
class FeeRepositoryTest {

	@Autowired
	private TestEntityManager testEntityManager ;
	@Autowired
	private FeeRepository feeRepository;

	@Test
	void testSaveAndFindAll() {
		Building build1 = new Building("address",2,50,null,null,250,200);
		Owner owner = new Owner("name",20,null);
		testEntityManager.persistAndFlush(build1);
		testEntityManager.persistAndFlush(owner);
		
		Apartment apt1 = new Apartment(1,150,owner,null,true,build1);
		testEntityManager.persistAndFlush(apt1);
		
		ServiceCompany co = new ServiceCompany("company",null);
		testEntityManager.persistAndFlush(co);
	
		Employee emp1 = new Employee("name1",co,null,null);
		testEntityManager.persistAndFlush(emp1);
			
		Fee fee1 = new Fee(new BigDecimal("7.50"), BigDecimal.ZERO, LocalDate.now(),null, apt1,emp1);
		Fee fee2 = new Fee(new BigDecimal("8.50"), BigDecimal.ZERO, LocalDate.now(),null, apt1,emp1);
		Fee fee3 = new Fee(new BigDecimal("9.50"), BigDecimal.ZERO, LocalDate.now(),null, apt1,emp1);
		Fee fee4 = new Fee(new BigDecimal("11.50"), BigDecimal.ZERO, LocalDate.now(),null, apt1,emp1);
	
		testEntityManager.persistAndFlush(fee1);
		testEntityManager.persistAndFlush(fee2);
		testEntityManager.persistAndFlush(fee3);
		testEntityManager.persistAndFlush(fee4);

		assertThat(feeRepository.findAll().size()).isEqualTo(4);

	}
	


}
