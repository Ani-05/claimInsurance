package com.example.ClaimInsurance.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import com.example.ClaimInsurance.entity.Claim;
import com.example.ClaimInsurance.entity.Insurer;
import com.example.ClaimInsurance.repository.ClaimRepo;

@Component
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

    public Claim updateByNo(Long claimNo, Insurer insurer) {
        Claim existingClaim = claimRepo.findById(claimNo)
                .orElseThrow(() -> new RuntimeException("Claim not found with claimNo: " + claimNo));
    
        Insurer existingInsurer = existingClaim.getInsurer();
        if (existingInsurer == null) {
            existingInsurer = new Insurer(); // fallback
        }
    
        existingInsurer.setName(insurer.getName());
        existingInsurer.setAddress(insurer.getAddress());
        existingInsurer.setDob(insurer.getDob());
        existingInsurer.setEmail(insurer.getEmail());
        existingInsurer.setMobile_no(insurer.getMobile_no());
    
        existingClaim.setInsurer(existingInsurer);
    
        return claimRepo.save(existingClaim);
    }
    

    public Optional<Claim> findByNo(Long claimNo){
        return claimRepo.findById(claimNo);
    }

    public boolean deleteByNo(Long claimNo){
        claimRepo.deleteById(claimNo);
        return true;
    }

    public Claim updateField(Long claimNumber, Map<String, String> updates) {
        Claim claim = claimRepo.findById(claimNumber)
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
        if (updates.containsKey("phoneNumber")) {
            insurer.setMobile_no(updates.get("phoneNumber"));
        }
        if (updates.containsKey("dateOfBirth")) {
            insurer.setDob(updates.get("dateOfBirth"));
        }
    
        claim.setInsurer(insurer);
        return claimRepo.save(claim);
    }

}