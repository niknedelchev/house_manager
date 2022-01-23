package com.housemanager.nn.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.housemanager.nn.model.Occupant;

@Repository
public interface OccupantRepository extends JpaRepository<Occupant, Integer> {

	@Query(value = "SELECT * FROM house_manager.occupants AS o LEFT JOIN house_manager.fees AS f ON o.apartment_id = f.appartment_id WHERE amount_due>0;", nativeQuery = true)
	List<Object[]> findAllDueFeesByOccupantNative();
}
