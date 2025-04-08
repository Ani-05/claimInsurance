package com.example.ClaimInsurance.controller;



import com.example.ClaimInsurance.entity.Claim;
import com.example.ClaimInsurance.entity.Insurer;
import com.example.ClaimInsurance.service.ClaimService;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;




@RestController
@RequestMapping("/claim")
public class ClaimController {

    @Autowired
    private ClaimService claimService;

    
    public ClaimController(ClaimService claimService) {
        this.claimService = claimService;
    }

    @GetMapping("/entry")
    public List<Claim> getAll() {
        return claimService.getAll();
    }

    @PostMapping("/data")
    public Claim createClaim(@RequestBody Claim claim) {
        claimService.saveAll(claim);
        return claim;
    }

    @GetMapping("/{claimNo}")
    public Optional<Claim> findByNo(@PathVariable Long claimNo) {
        return Optional.of(claimService.findByNo(claimNo).orElse(null));
    }

    @PutMapping("/{claimNo}")
    public Optional<Claim> updateClaim(@PathVariable Long claimNo, @RequestBody Insurer insurer) {
        
        return claimService.updateByNo(claimNo,insurer);
    }

    @PatchMapping("/claim/update-insurer-fields")
public ResponseEntity<?> updateInsurerFields(@RequestBody Map<String, String> request) {
    try {
        if (!request.containsKey("claimNumber")) {
            return ResponseEntity.badRequest().body("Missing claimNumber");
        }

        Long claimNumber = Long.parseLong(request.get("claimNumber"));

        Claim updatedClaim = claimService.updateField(claimNumber, request);
        return ResponseEntity.ok(updatedClaim);

    } catch (NumberFormatException e) {
        return ResponseEntity.badRequest().body("Invalid claimNumber format: " + e.getMessage());
    } catch (Exception e) {
        e.printStackTrace();  
        return ResponseEntity.internalServerError().body("Error updating insurer fields: " + e.getMessage());
    }
}
    

    
    
    
    
}
