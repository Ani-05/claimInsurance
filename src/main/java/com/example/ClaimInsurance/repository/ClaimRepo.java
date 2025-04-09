package com.example.ClaimInsurance.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ClaimInsurance.entity.Claim;
import com.example.ClaimInsurance.entity.ClaimType;

import java.util.List;


public interface ClaimRepo extends JpaRepository<Claim,String> {
    boolean existsByClaimType(ClaimType claimType);
    List<Claim> findByClaimType(ClaimType claimType);
    List<Claim> findByPolicyNo(String policyNo);
    List<Claim> findByClaimTypeAndPolicyNo(ClaimType claimType, String policyNo);
}
