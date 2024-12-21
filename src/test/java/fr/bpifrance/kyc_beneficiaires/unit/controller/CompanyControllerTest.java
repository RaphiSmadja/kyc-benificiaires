package fr.bpifrance.kyc_beneficiaires.unit.controller;


import fr.bpifrance.kyc_beneficiaires.controller.CompanyController;
import fr.bpifrance.kyc_beneficiaires.dto.BeneficiaryDTO;
import fr.bpifrance.kyc_beneficiaires.service.CompanyService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CompanyControllerTest {

    @Mock
    private CompanyService companyService;

    @InjectMocks
    private CompanyController companyController;

    @Test
    void getEffectiveBeneficiary_shouldReturnOk_whenBeneficiariesFound() {
        // Arrange
        Long companyId = 1L;
        String type = "all";
        List<BeneficiaryDTO> beneficiaries = List.of(
                new BeneficiaryDTO(1L, "John Doe", 30.0, true)
        ); // Liste non vide
        when(companyService.getBeneficiaries(companyId, type)).thenReturn(beneficiaries);

        // Act
        ResponseEntity<List<BeneficiaryDTO>> response = companyController.getEffectiveBeneficiary(companyId, type);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("John Doe", response.getBody().get(0).getName());
    }

    @Test
    void getEffectiveBeneficiary_shouldReturnNoContent_whenNoBeneficiariesFound() {
        Long companyId = 1L;
        String type = "all";
        when(companyService.getBeneficiaries(eq(companyId), eq(type))).thenReturn(List.of());

        ResponseEntity<List<BeneficiaryDTO>> response = companyController.getEffectiveBeneficiary(companyId, type);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void getEffectiveBeneficiary_shouldReturnNotFound_whenCompanyNotFound() {
        Long companyId = 999L;
        String type = "all";
        String errorMessage = "Company not found";
        when(companyService.getBeneficiaries(eq(companyId), eq(type))).thenThrow(new EntityNotFoundException(errorMessage));

        ResponseEntity<List<BeneficiaryDTO>> response = companyController.getEffectiveBeneficiary(companyId, type);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals(errorMessage, response.getBody().get(0).getName());
    }

    @Test
    void getEffectiveBeneficiary_shouldThrowIllegalArgumentException_whenInvalidType() {
        Long companyId = 1L;
        String invalidType = "invalid";

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                companyController.getEffectiveBeneficiary(companyId, invalidType)
        );

        assertEquals("Invalid type parameter. Allowed values are: all, natural, effective.", exception.getMessage());
    }

    @Test
    void getEffectiveBeneficiary_shouldReturnOk_whenEffectiveBeneficiariesFound() {
        Long companyId = 1L;
        String type = "effective";
        List<BeneficiaryDTO> beneficiaries = List.of(
                new BeneficiaryDTO(1L, "John Doe", 30.0, true)
        );

        when(companyService.getBeneficiaries(eq(companyId), eq(type))).thenReturn(beneficiaries);

        ResponseEntity<List<BeneficiaryDTO>> response = companyController.getEffectiveBeneficiary(companyId, type);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("John Doe", response.getBody().get(0).getName());
    }

    @Test
    void getEffectiveBeneficiary_shouldReturnOk_whenNaturalBeneficiariesFound() {
        Long companyId = 1L;
        String type = "natural";
        List<BeneficiaryDTO> beneficiaries = List.of(
                new BeneficiaryDTO(1L, "Jane Doe", 20.0, true)
        );

        when(companyService.getBeneficiaries(eq(companyId), eq(type))).thenReturn(beneficiaries);

        ResponseEntity<List<BeneficiaryDTO>> response = companyController.getEffectiveBeneficiary(companyId, type);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("Jane Doe", response.getBody().get(0).getName());
    }

}
