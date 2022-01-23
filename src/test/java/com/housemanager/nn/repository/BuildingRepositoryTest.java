package com.housemanager.nn.repository;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.housemanager.nn.model.Building;


@DataJpaTest
class BuildingRepositoryTest {

	@Autowired
	private TestEntityManager testEntityManager ;
	@Autowired
	private BuildingRepository buildingRepository;

	@Test
	void testSaveAndFindAll() {
		Building build1 = new Building("address1",2,50,null,null,250,200);
		Building build2 = new Building("address2",2,150,null,null,350,200);
		Building build3 = new Building("address3",2,250,null,null,450,200);
		
		testEntityManager.persistAndFlush(build1);
		testEntityManager.persistAndFlush(build2);
		testEntityManager.persistAndFlush(build3);
		
		assertThat(buildingRepository.findAll().size()).isEqualTo(3);
	}


}
