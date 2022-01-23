package com.housemanager.nn.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.housemanager.nn.model.Apartment;

@Repository
public interface ApartmentRepository extends JpaRepository<Apartment, Integer> {

	@Query(
			  value = "SELECT a.apartment_id, a.floor, a.size, a.owner_id, a.has_pet, a.building_id "
			  		+ "FROM house_manager.apartments as a INNER JOIN house_manager.buildings as b "
			  		+ "ON a.building_id = b.building_id INNER JOIN house_manager.employees as e "
			  		+ "ON b.employee_id = e.employee_id WHERE e.employee_id = ?;", 
			  nativeQuery = true)
	List<Apartment> findApartmentsByEmployee(int employeeId);
	
}
