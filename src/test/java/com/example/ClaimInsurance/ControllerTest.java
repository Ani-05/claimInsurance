package com.example.ClaimInsurance;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.example.ClaimInsurance.controller.ClaimController;
import com.example.ClaimInsurance.entity.Claim;
import com.example.ClaimInsurance.service.ClaimService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(ClaimController.class)
public class ControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClaimService claimService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGetAllClaims() throws Exception {
        List<Claim> mockClaims = List.of(new Claim(), new Claim());

        when(claimService.getAll()).thenReturn(mockClaims);

        mockMvc.perform(get("/claim/entry"))
               .andExpect(status().isOk());
    }

    @Test
    public void testCreateClaim_Success() throws Exception {
        Claim claim = new Claim();
        claim.setClaimNo("C999");

        mockMvc.perform(post("/claim/data")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(claim)))
                .andExpect(status().isCreated());
    }

    @Test
    public void testDeleteClaim_Success() throws Exception {
        
    when(claimService.deleteByNo("C123")).thenReturn(true);
        mockMvc.perform(delete("/claim/C123"))
        .andExpect(status().isOk())
        .andExpect(content().string("Deleted successfully"));
    }

    @Test
    public void testDeleteClaim_NotFound() throws Exception {
        Mockito.doThrow(new RuntimeException("Claim not found")).when(claimService).deleteByNo("C404");

        mockMvc.perform(delete("/claim/C404"))
               .andExpect(status().isNotFound())
               .andExpect(content().string("Claim not found"));
    }
}
