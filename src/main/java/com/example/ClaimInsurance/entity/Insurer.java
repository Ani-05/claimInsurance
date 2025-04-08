package com.example.ClaimInsurance.entity;

//import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
public class Insurer {
    @Id
    @GeneratedValue
    private long id;
private String name;
private String Dob;
private String Address;
private String email;
private String mobile_no;
}
