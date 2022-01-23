package com.housemanager.nn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.housemanager.nn.model.Owner;

@Repository
public interface OwnerRepository extends JpaRepository<Owner, Integer> {

}
