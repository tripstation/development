package com.manifesto.atm.model;

import org.springframework.data.jpa.repository.JpaRepository;

import com.manifesto.atm.entity.Atm;

public interface AtmRepository extends JpaRepository<Atm, Long>{

}
