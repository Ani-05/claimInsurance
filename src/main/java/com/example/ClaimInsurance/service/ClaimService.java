package com.example.ClaimInsurance.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.example.ClaimInsurance.entity.Claim;
import com.example.ClaimInsurance.entity.ClaimType;
import com.example.ClaimInsurance.entity.Insurer;
import com.example.ClaimInsurance.repository.ClaimRepo;



@Service
public class ClaimService {


    @Autowired
    private ClaimRepo claimRepo;

    public void saveAll(Claim claim){
        String type = claim.getClaimType().toString();
        if (!"CASHLESS".equalsIgnoreCase(type) && !"REIMBURSEMENT".equalsIgnoreCase(type)) {
            throw new IllegalArgumentException("Invalid claimType");
        }
    
        claimRepo.save(claim);
    }

    public List<Claim> getAll(){
        return claimRepo.findAll();
    }

    public Claim updateByNo(String claimNo, Insurer insurer) {
        Claim existingClaim = claimRepo.findById(claimNo)
                .orElseThrow(() -> new RuntimeException("Claim not found with claimNo: " + claimNo));
    
        Insurer existingInsurer = existingClaim.getInsurer();
        if (existingInsurer == null) {
            existingInsurer = new Insurer(); 
        }
    
        existingInsurer.setName(insurer.getName());
        existingInsurer.setAddress(insurer.getAddress());
        existingInsurer.setDob(insurer.getDob());
        existingInsurer.setEmail(insurer.getEmail());
        existingInsurer.setMobile_no(insurer.getMobile_no());
    
        existingClaim.setInsurer(existingInsurer);
    
        return claimRepo.save(existingClaim);
    }
    

    public Optional<Claim> findByNo(String claimNo){
        return claimRepo.findById(claimNo);
    }

    public boolean deleteByNo(String claimNo){
        claimRepo.deleteById(claimNo);
        return true;
    }

    public Claim updateField(String claimNo, Map<String, String> updates) {
        Claim claim = claimRepo.findById(claimNo)
                .orElseThrow(() -> new RuntimeException("Claim not found"));
    
        Insurer insurer = claim.getInsurer();
        if (insurer == null) {
            insurer = new Insurer();
        }
    
        if (updates.containsKey("name")) {
            insurer.setName(updates.get("name"));
        }
        if (updates.containsKey("email")) {
            insurer.setEmail(updates.get("email"));
        }
        if (updates.containsKey("address")) {
            insurer.setAddress(updates.get("address"));
        }
        if (updates.containsKey("mobile_no")) {
            insurer.setMobile_no(updates.get("mobile_no"));
        }
        if (updates.containsKey("dob")) {
            insurer.setDob(updates.get("dob"));
        }
    
        claim.setInsurer(insurer);
        return claimRepo.save(claim);
    }

    public List<Claim> findByClaimType(ClaimType claimType) {
        return claimRepo.findByClaimType(claimType);
    }
    
    public List<Claim> findByPolicyNo(String policyNo) {
        return claimRepo.findByPolicyNo(policyNo);
    }
    
    public List<Claim> findByTypeAndPolicy(ClaimType claimType, String policyNo) {
        return claimRepo.findByClaimTypeAndPolicyNo(claimType, policyNo);
    }

}