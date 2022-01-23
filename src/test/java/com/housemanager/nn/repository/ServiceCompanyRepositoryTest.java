package com.housemanager.nn.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.housemanager.nn.model.ServiceCompany;

@DataJpaTest
class ServiceCompanyRepositoryTest {

	@Autowired
	private TestEntityManager testEntityManager ;
	@Autowired
	private ServiceCompanyRepository serviceCompanyRepository;

	@Test
	void testSaveAndFindAll() {
		ServiceCompany co = new ServiceCompany("company",null);
		testEntityManager.persistAndFlush(co);

		ServiceCompany co2 = new ServiceCompany("company2",null);
		testEntityManager.persistAndFlush(co2);

		assertThat(serviceCompanyRepository.findAll().size()).isEqualTo(2);

	}

}
