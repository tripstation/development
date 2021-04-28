package com.ecsdigital.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ecsdigital.entity.Make;

@Repository
public interface MakeRepository extends JpaRepository<Make, Long>{
	Make findById(@Param("id") long id);
	long findByMake(@Param("make")String make);
}
