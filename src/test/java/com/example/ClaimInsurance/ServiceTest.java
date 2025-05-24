package com.example.ClaimInsurance;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.example.ClaimInsurance.entity.Claim;
import com.example.ClaimInsurance.entity.ClaimType;
import com.example.ClaimInsurance.entity.Insurer;
import com.example.ClaimInsurance.repository.ClaimRepo;
import com.example.ClaimInsurance.service.ClaimService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ServiceTest {

    @Mock
    private ClaimRepo claimRepo;

    @InjectMocks
    private ClaimService claimService;

    // ---------- Save Tests ----------

    @Test
    void testSaveClaim_WithValidClaimTypes() {
        Claim cashlessClaim = new Claim();
        cashlessClaim.setClaimType(ClaimType.CASHLESS);

        Claim reimbursementClaim = new Claim();
        reimbursementClaim.setClaimType(ClaimType.REIMBURSEMENT);

        claimService.saveAll(cashlessClaim);
        claimService.saveAll(reimbursementClaim);

        verify(claimRepo, times(2)).save(any(Claim.class));
    }

    @Test
    void testSaveClaim_NullClaim() {
        assertThrows(NullPointerException.class, () -> claimService.saveAll(null));
        verify(claimRepo, never()).save(any());
    }

    @Test
void testSaveClaim_InvalidClaimType() {
    Claim claim = new Claim(); // claimType is null
    assertThrows(IllegalArgumentException.class, () -> claimService.saveAll(claim));
    verify(claimRepo, never()).save(any());
}


    // ---------- UpdateByNo Tests ----------

    @Test
    void testUpdateByNo_SuccessfulUpdate() {
        Claim existingClaim = new Claim();
        existingClaim.setClaimNo("C1");
        existingClaim.setInsurer(new Insurer());

        Insurer newInsurer = new Insurer();
        newInsurer.setName("John");

        when(claimRepo.findById("C1")).thenReturn(Optional.of(existingClaim));
        when(claimRepo.save(any())).thenReturn(existingClaim);

        Claim updated = claimService.updateByNo("C1", newInsurer);
        assertEquals("John", updated.getInsurer().getName());
    }

    @Test
    void testUpdateByNo_ClaimNotFound() {
        when(claimRepo.findById("C999")).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> 
            claimService.updateByNo("C999", new Insurer())
        );

        assertEquals("Claim not found with claimNo: C999", ex.getMessage());
    }

    @Test
    void testUpdateByNo_NullInsurer() {
        Claim claim = new Claim();
        claim.setClaimNo("C2");
        claim.setInsurer(new Insurer());

        when(claimRepo.findById("C2")).thenReturn(Optional.of(claim));

        assertThrows(NullPointerException.class, () -> claimService.updateByNo("C2", null));
        verify(claimRepo, never()).save(any());
    }

    @Test
    void testUpdateByNo_FieldsSetToNull() {
        Claim claim = new Claim();
        claim.setClaimNo("C3");
        claim.setInsurer(new Insurer());

        Insurer update = new Insurer(); // All fields null

        when(claimRepo.findById("C3")).thenReturn(Optional.of(claim));
        when(claimRepo.save(any())).thenReturn(claim);

        Claim updated = claimService.updateByNo("C3", update);

        assertNull(updated.getInsurer().getName());
        assertNull(updated.getInsurer().getEmail());
    }

    // ---------- UpdateField Tests ----------

    @Test
    void testUpdateField_AllFieldsPresent() {
        Claim claim = new Claim();
        claim.setInsurer(new Insurer());

        when(claimRepo.findById("C4")).thenReturn(Optional.of(claim));
        when(claimRepo.save(any())).thenReturn(claim);

        Map<String, String> updates = Map.of(
                "name", "Alice",
                "email", "alice@example.com",
                "address", "Wonderland",
                "dob", "1995-05-05",
                "mobile_no", "1234567890"
        );

        Claim updated = claimService.updateField("C4", updates);

        assertEquals("Alice", updated.getInsurer().getName());
        assertEquals("alice@example.com", updated.getInsurer().getEmail());
        assertEquals("Wonderland", updated.getInsurer().getAddress());
    }

    @Test
    void testUpdateField_ClaimWithNoInsurer() {
        Claim claim = new Claim();
        claim.setInsurer(null);

        when(claimRepo.findById("C5")).thenReturn(Optional.of(claim));
        when(claimRepo.save(any())).thenReturn(claim);

        Map<String, String> updates = Map.of("name", "Bob");

        Claim updated = claimService.updateField("C5", updates);
        assertEquals("Bob", updated.getInsurer().getName());
    }

    @Test
    void testUpdateField_EmptyUpdates() {
        Claim claim = new Claim();
        claim.setInsurer(new Insurer());

        when(claimRepo.findById("C6")).thenReturn(Optional.of(claim));
        when(claimRepo.save(any())).thenReturn(claim);

        Map<String, String> updates = Map.of();

        Claim updated = claimService.updateField("C6", updates);
        assertNull(updated.getInsurer().getName());
    }

    @Test
    void testUpdateField_EmptyOrNullClaimNo() {
        assertThrows(RuntimeException.class, () -> claimService.updateField("", Map.of("name", "Bob")));
        assertThrows(RuntimeException.class, () -> claimService.updateField(null, Map.of("name", "Bob")));
    }

   

    @Test
    void testGetAllClaims() {
        when(claimRepo.findAll()).thenReturn(List.of(new Claim(), new Claim()));
        List<Claim> all = claimService.getAll();
        assertEquals(2, all.size());
        verify(claimRepo).findAll();
    }

    @Test
    void testFindByClaimNo() {
        Claim claim = new Claim();
        claim.setClaimNo("C7");
        when(claimRepo.findById("C7")).thenReturn(Optional.of(claim));

        Optional<Claim> result = claimService.findByNo("C7");

        assertTrue(result.isPresent());
        assertEquals("C7", result.get().getClaimNo());
    }

    @Test
void testFindByTypeAndPolicy() {
    ClaimType type = ClaimType.CASHLESS;
    String policyNo = "P123";
    Claim claim = new Claim();
    claim.setClaimType(type);
    claim.setPolicyNo(policyNo);

    when(claimRepo.findByClaimTypeAndPolicyNo(type, policyNo)).thenReturn(List.of(claim));

    List<Claim> result = claimService.findByTypeAndPolicy(type, policyNo);

    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals(type, result.get(0).getClaimType());
    assertEquals(policyNo, result.get(0).getPolicyNo());
}
    
    @Test
    void testFindByClaimType() {
        when(claimRepo.findByClaimType(ClaimType.CASHLESS)).thenReturn(List.of(new Claim()));
        List<Claim> claims = claimService.findByClaimType(ClaimType.CASHLESS);
        assertEquals(1, claims.size());
    }

    @Test
    void testFindByPolicyNo() {
        when(claimRepo.findByPolicyNo("P123")).thenReturn(List.of(new Claim()));
        List<Claim> result = claimService.findByPolicyNo("P123");
        assertEquals(1, result.size());
    }

    @Test
    void testDeleteByClaimNo() {
        doNothing().when(claimRepo).deleteById("C8");
        boolean deleted = claimService.deleteByNo("C8");
        assertTrue(deleted);
        verify(claimRepo).deleteById("C8");
    }
}
