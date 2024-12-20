package fr.bpifrance.kyc_beneficiaires.controller;

import fr.bpifrance.kyc_beneficiaires.service.CompanyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/companies")
public class CompanyController {

    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping("/{id}/beneficiaries")
    public ResponseEntity<?> getEffectiveBeneficiary(
            @PathVariable Long id,
            @RequestParam(required = false, defaultValue = "all") String type) {

        return null;
    }
}