package fr.bpifrance.kyc_beneficiaires.unit.controller;


import fr.bpifrance.kyc_beneficiaires.service.CompanyService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CompanyControllerTest {

    @InjectMocks
    private CompanyService companyService;

    @Test
    void testGetEffectiveBeneficiary() {

    }

}
