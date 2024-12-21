package fr.bpifrance.kyc_beneficiaires.unit.services;

import fr.bpifrance.kyc_beneficiaires.dto.BeneficiaryDTO;
import fr.bpifrance.kyc_beneficiaires.mapper.BeneficiaryMapper;
import fr.bpifrance.kyc_beneficiaires.model.Company;
import fr.bpifrance.kyc_beneficiaires.model.Person;
import fr.bpifrance.kyc_beneficiaires.model.Shareholder;
import fr.bpifrance.kyc_beneficiaires.repository.CompanyRepository;
import fr.bpifrance.kyc_beneficiaires.repository.ShareholderRepository;
import fr.bpifrance.kyc_beneficiaires.service.CompanyService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CompanyServiceTest {
    @Mock
    private CompanyRepository companyRepository;

    @Mock
    private ShareholderRepository shareholderRepository;

    @Mock
    private BeneficiaryMapper beneficiaryMapper;

    @InjectMocks
    private CompanyService companyService;

    @Test
    void getBeneficiaries_shouldReturnBeneficiaries_whenFound() {
        Long companyId = 1L;
        Company company = new Company(companyId, "Company A", Collections.emptyList());
        Person person = new Person(1L, "John Doe");
        Shareholder shareholder = new Shareholder(1L, company, person, null, 30.0);

        when(companyRepository.findById(companyId)).thenReturn(Optional.of(company));
        when(shareholderRepository.findByCompany(company)).thenReturn(List.of(shareholder));
        when(beneficiaryMapper.toBeneficiaryDTO(person, 30.0))
                .thenReturn(new BeneficiaryDTO(1L, "John Doe", 30.0, true));

        List<BeneficiaryDTO> beneficiaries = companyService.getBeneficiaries(companyId, "all");

        assertEquals(1, beneficiaries.size());
        assertEquals("John Doe", beneficiaries.get(0).getName());
    }

    @Test
    void getBeneficiaries_shouldThrowEntityNotFoundException_whenCompanyNotFound() {
        Long companyId = 999L;
        when(companyRepository.findById(companyId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                companyService.getBeneficiaries(companyId, "all")
        );
        assertEquals("Company not found", exception.getMessage());
    }

    @Test
    void getBeneficiaries_shouldReturnEmptyList_whenNoBeneficiariesFound() {
        Long companyId = 1L;
        Company company = new Company(companyId, "Company A", Collections.emptyList());

        when(companyRepository.findById(companyId)).thenReturn(Optional.of(company));
        when(shareholderRepository.findByCompany(company)).thenReturn(Collections.emptyList());

        List<BeneficiaryDTO> beneficiaries = companyService.getBeneficiaries(companyId, "all");

        assertTrue(beneficiaries.isEmpty());
    }

    @Test
    void getBeneficiaries_shouldReturnNaturalBeneficiaries_whenTypeIsNatural() {
        Long companyId = 1L;
        Company company = new Company(companyId, "Company A", Collections.emptyList());
        Person person = new Person(1L, "John Doe");
        Shareholder shareholder = new Shareholder(1L, company, person, null, 20.0);

        when(companyRepository.findById(companyId)).thenReturn(Optional.of(company));
        when(shareholderRepository.findByCompany(company)).thenReturn(List.of(shareholder));
        when(beneficiaryMapper.toBeneficiaryDTO(person, 20.0))
                .thenReturn(new BeneficiaryDTO(1L, "John Doe", 20.0, true));

        List<BeneficiaryDTO> beneficiaries = companyService.getBeneficiaries(companyId, "natural");

        assertEquals(1, beneficiaries.size());
        assertEquals("John Doe", beneficiaries.get(0).getName());
    }

    @Test
    void getBeneficiaries_shouldReturnEffectiveBeneficiaries_whenTypeIsEffective() {
        // Arrange
        Long companyId = 1L;
        Company company = new Company(companyId, "Company A", Collections.emptyList());
        Person person = new Person(1L, "John Doe");
        Shareholder shareholder = new Shareholder(1L, company, person, null, 30.0);

        when(companyRepository.findById(companyId)).thenReturn(Optional.of(company));
        when(shareholderRepository.findByCompany(company)).thenReturn(List.of(shareholder));
        when(beneficiaryMapper.toBeneficiaryDTO(person, 30.0))
                .thenReturn(new BeneficiaryDTO(1L, "John Doe", 30.0, true));

        List<BeneficiaryDTO> beneficiaries = companyService.getBeneficiaries(companyId, "effective");

        assertEquals(1, beneficiaries.size());
        assertEquals("John Doe", beneficiaries.get(0).getName());
    }
}

