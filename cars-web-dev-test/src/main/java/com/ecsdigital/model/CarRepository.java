package com.ecsdigital.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ecsdigital.entity.Car;

@Repository
public interface CarRepository extends JpaRepository<Car, Long>{
	Car findById(@Param("id") long id);
	long deleteByModel(@Param("model") String model);
}
