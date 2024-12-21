package fr.bpifrance.kyc_beneficiaires.unit.controller;


import fr.bpifrance.kyc_beneficiaires.controller.CompanyController;
import fr.bpifrance.kyc_beneficiaires.dto.BeneficiaryDTO;
import fr.bpifrance.kyc_beneficiaires.dto.CompanyDTO;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CompanyControllerTest {

    @Mock
    private CompanyService companyService;

    @InjectMocks
    private CompanyController companyController;

    @Test
    void getEffectiveBeneficiary_shouldReturnOk_whenBeneficiariesFound() {
        Long companyId = 1L;
        String type = "all";
        List<BeneficiaryDTO> beneficiaries = List.of(
                new BeneficiaryDTO(1L, "John Doe", 30.0, true)
        );
        when(companyService.getBeneficiaries(companyId, type)).thenReturn(beneficiaries);

        ResponseEntity<List<BeneficiaryDTO>> response = companyController.getEffectiveBeneficiary(companyId, type);

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

    @Test
    void addBeneficiaryToCompany_shouldReturnCreated_whenBeneficiaryAdded() {
        Long companyId = 1L;
        BeneficiaryDTO beneficiaryDTO = new BeneficiaryDTO(null, "John Doe", 30.0, true);

        doNothing().when(companyService).addBeneficiary(eq(companyId), eq(beneficiaryDTO));

        ResponseEntity<String> response = companyController.addBeneficiaryToCompany(companyId, beneficiaryDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Beneficiary added successfully.", response.getBody());
    }

    @Test
    void addBeneficiaryToCompany_shouldReturnNotFound_whenCompanyNotFound() {
        Long companyId = 1L;
        BeneficiaryDTO beneficiaryDTO = new BeneficiaryDTO(null, "John Doe", 30.0, true);

        doThrow(new EntityNotFoundException("Company not found"))
                .when(companyService).addBeneficiary(eq(companyId), eq(beneficiaryDTO));

        ResponseEntity<String> response = companyController.addBeneficiaryToCompany(companyId, beneficiaryDTO);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Company not found", response.getBody());
    }

    @Test
    void addBeneficiaryToCompany_shouldReturnBadRequest_whenPercentageInvalid() {
        Long companyId = 1L;
        BeneficiaryDTO beneficiaryDTO = new BeneficiaryDTO(null, "John Doe", 150.0, true);

        doThrow(new IllegalArgumentException("Total ownership percentage exceeds 100%"))
                .when(companyService).addBeneficiary(eq(companyId), eq(beneficiaryDTO));

        ResponseEntity<String> response = companyController.addBeneficiaryToCompany(companyId, beneficiaryDTO);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Total ownership percentage exceeds 100%", response.getBody());
    }

    @Test
    void getCompanyById_shouldReturnOk_whenCompanyFound() {
        Long companyId = 1L;
        CompanyDTO companyDTO = new CompanyDTO(companyId, "Company A", List.of());

        when(companyService.getCompanyById(eq(companyId))).thenReturn(companyDTO);

        ResponseEntity<CompanyDTO> response = companyController.getCompanyById(companyId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Company A", response.getBody().getName());
    }

    @Test
    void getCompanyById_shouldReturnNotFound_whenCompanyNotFound() {
        Long companyId = 1L;

        when(companyService.getCompanyById(eq(companyId))).thenThrow(new EntityNotFoundException("Company not found"));

        ResponseEntity<CompanyDTO> response = companyController.getCompanyById(companyId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void createCompany_shouldReturnCreated_whenCompanyCreated() {
        String companyName = "Company X";
        CompanyDTO createdCompany = new CompanyDTO(1L, companyName, List.of());

        when(companyService.saveCompany(eq(companyName))).thenReturn(createdCompany);

        ResponseEntity<Void> response = companyController.createCompany(companyName);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("/companies/1", response.getHeaders().getLocation().toString());
    }

}
