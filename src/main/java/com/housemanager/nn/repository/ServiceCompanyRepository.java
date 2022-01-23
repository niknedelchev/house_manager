package com.housemanager.nn.repository;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.housemanager.nn.model.ServiceCompany;

@Repository
public interface ServiceCompanyRepository extends JpaRepository<ServiceCompany, Integer> {

	@Query(
			  value = "SELECT SUM(amount_received), c.service_company_id, c.name FROM house_manager.service_companies as c" 
					  +" LEFT JOIN house_manager.employees as e "
					  +" ON c.service_company_id = e.service_company_id"
					  +" LEFT JOIN house_manager.fees as f"
					  +" ON f.employee_id = e.employee_id"
					  +" GROUP BY c.service_company_id"
					  +" ORDER BY c.service_company_id;", 
			  nativeQuery = true)
	List<Object[]> findAllCompaniesJoinCollectedFees();
	
	
	@Query(
			  value = "SELECT SUM(amount_received), c.service_company_id, c.name FROM house_manager.service_companies as c" 
					  +" LEFT JOIN house_manager.employees as e "
					  +" ON c.service_company_id = e.service_company_id"
					  +" LEFT JOIN house_manager.fees as f"
					  +" ON f.employee_id = e.employee_id"
					  +" GROUP BY c.service_company_id"
					  +" HAVING SUM(amount_received) >= ?1"
					  +" ORDER BY c.service_company_id;", 
			  nativeQuery = true)
	List<Object[]> findAllCompaniesJoinCollectedFeesFilteredBySum(int amount);
}
