package com.example.ClaimInsurance;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.ClaimInsurance.service.ClaimService;

@Configuration
public class TestConfig {

    @Bean
    public ClaimService claimService() {
        return Mockito.mock(ClaimService.class);
    }
}
