package com.example.ClaimInsurance.controller;

import com.example.ClaimInsurance.entity.Claim;
import com.example.ClaimInsurance.entity.ClaimType;
import com.example.ClaimInsurance.entity.Insurer;
import com.example.ClaimInsurance.service.ClaimService;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public ResponseEntity<?> createClaim(@RequestBody Claim claim) {
        try{
        claimService.saveAll(claim);
        return ResponseEntity.status(201).body(claim);}
        catch(IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

  
@GetMapping("/search/by-claimNo/{claimNo}")
    public ResponseEntity<?> getByClaimNo(@PathVariable Long claimNo) {
        Optional<Claim> claim = claimService.findByNo(claimNo);
        if (claim.isPresent()) {
            return ResponseEntity.ok(claim.get());
        } else {
            return ResponseEntity.status(404).body("Claim not found");
        }
    }

    // ✅ GET - Search by Claim Type
    @GetMapping("/search/by-claimType/{claimType}")
    public ResponseEntity<List<Claim>> getByClaimType(@PathVariable String claimType) {
        ClaimType type;
        try {
            type = ClaimType.valueOf(claimType.toUpperCase());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Collections.emptyList());
        }

        return ResponseEntity.ok(claimService.findByClaimType(type));
    }

    // ✅ GET - Search by Policy Number
    @GetMapping("/search/by-policy/{policyNo}")
    public ResponseEntity<List<Claim>> getByPolicy(@PathVariable String policyNo) {
        return ResponseEntity.ok(claimService.findByPolicyNo(policyNo));
    }

    // ✅ GET - Search by both Claim Type and Policy Number
    @GetMapping("/search")
    public ResponseEntity<List<Claim>> getByTypeAndPolicy(
            @RequestParam String claimType,
            @RequestParam String policyNo) {

        ClaimType type;
        try {
            type = ClaimType.valueOf(claimType.toUpperCase());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Collections.emptyList());
        }

        return ResponseEntity.ok(claimService.findByTypeAndPolicy(type, policyNo));
    }

    

    @PutMapping("/{claimNo}")
    public ResponseEntity<?> updateClaim(@PathVariable Long claimNo, @RequestBody Insurer insurer) {
        try {
            Claim updatedClaim = claimService.updateByNo(claimNo, insurer);
            return ResponseEntity.ok(updatedClaim);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @DeleteMapping("/{claimNo}")
    public ResponseEntity<String> deleteClaim(@PathVariable Long claimNo) {
        try {
            claimService.deleteByNo(claimNo);
            return ResponseEntity.ok("Deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
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
