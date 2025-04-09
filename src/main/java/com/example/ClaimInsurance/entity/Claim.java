package com.example.ClaimInsurance.entity;

import java.util.UUID;

import jakarta.persistence.CascadeType;
//import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class Claim {
    @Id
    private String claimNo=UUID.randomUUID().toString();;
    @Enumerated(EnumType.STRING)
    private ClaimType claimType;
    private double claimAmount;
    private String policyNo;
    
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "insurer_id", referencedColumnName = "id")
    private Insurer insurer;

}
