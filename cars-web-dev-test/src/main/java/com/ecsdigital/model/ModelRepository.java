package com.ecsdigital.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ecsdigital.entity.Model;

@Repository
public interface ModelRepository extends JpaRepository<Model, Long> {
	long deleteByModel(@Param("model") String model);
}
