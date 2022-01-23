package com.housemanager.nn.repository;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.housemanager.nn.model.Employee;
import com.housemanager.nn.model.ServiceCompany;

@DataJpaTest
class EmployeeRepositoryTest {

	@Autowired
	private TestEntityManager testEntityManager ;
	@Autowired
	private EmployeeRepository employeeRepository;

	@Test
	void testSaveAndFindAll() {
		ServiceCompany co = new ServiceCompany("company",null);
		testEntityManager.persistAndFlush(co);
	
		Employee emp1 = new Employee("name1",co,null,null);
		Employee emp2 = new Employee("name1",co,null,null);
		Employee emp3 = new Employee("name1",co,null,null);
		
		testEntityManager.persistAndFlush(emp1);
		testEntityManager.persistAndFlush(emp2);
		testEntityManager.persistAndFlush(emp3);
		
		assertThat(employeeRepository.findAll().size()).isEqualTo(3);

	}


}
