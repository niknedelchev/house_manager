package com.housemanager.nn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.housemanager.nn.model.Fee;

@Repository
public interface FeeRepository extends JpaRepository<Fee, Integer> {

}
