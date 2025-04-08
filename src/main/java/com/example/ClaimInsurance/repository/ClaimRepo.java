package com.example.ClaimInsurance.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ClaimInsurance.entity.Claim;

public interface ClaimRepo extends JpaRepository<Claim,Long> {
    
}
