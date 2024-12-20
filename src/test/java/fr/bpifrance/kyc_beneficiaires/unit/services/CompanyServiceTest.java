package fr.bpifrance.kyc_beneficiaires.unit.services;

import fr.bpifrance.kyc_beneficiaires.controller.CompanyController;
import fr.bpifrance.kyc_beneficiaires.service.CompanyService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CompanyServiceTest {
    @InjectMocks
    private CompanyController companyController;

    @Mock
    private CompanyService companyService;

    @Test
    void testGetEffectiveBeneficiary() {

    }
}

