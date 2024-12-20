package fr.bpifrance.kyc_beneficiaires.controller;

import fr.bpifrance.kyc_beneficiaires.dto.BeneficiaryDTO;
import fr.bpifrance.kyc_beneficiaires.service.CompanyService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        try {
            List<BeneficiaryDTO> beneficiaries = companyService.getBenificiaries(id, type);

            if (beneficiaries.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            return ResponseEntity.ok(beneficiaries);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}