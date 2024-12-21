package fr.bpifrance.kyc_beneficiaires.controller;

import fr.bpifrance.kyc_beneficiaires.dto.BeneficiaryDTO;
import fr.bpifrance.kyc_beneficiaires.service.CompanyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/companies")
public class CompanyController {

    private static final Logger logger = LoggerFactory.getLogger(CompanyController.class);

    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @Operation(summary = "Get beneficiaries of a company", description = "Fetches beneficiaries (direct and indirect) of the specified company.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of beneficiaries returned successfully"),
            @ApiResponse(responseCode = "204", description = "No beneficiaries found"),
            @ApiResponse(responseCode = "404", description = "Company not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input parameter")
    })
    @GetMapping("/{id}/beneficiaries")
    public ResponseEntity<List<BeneficiaryDTO>> getEffectiveBeneficiary(
            @Parameter(description = "ID of the company to fetch beneficiaries for")
            @PathVariable Long id,
            @Parameter(description = "Type of beneficiaries to fetch (all, natural, effective)", example = "all")
            @RequestParam(required = false, defaultValue = "all") String type) {
        logger.info("Received request to fetch beneficiaries for company ID: {} with type: {}", id, type);

        try {
            validateType(type);
            List<BeneficiaryDTO> beneficiaries = companyService.getBeneficiaries(id, type);
            if (beneficiaries.isEmpty()) {
                logger.info("No beneficiaries found for company ID: {}", id);
                return ResponseEntity.noContent().build();
            }
            logger.info("Returning {} beneficiaries for company ID: {}", beneficiaries.size(), id);
            return ResponseEntity.ok(beneficiaries);
        } catch (EntityNotFoundException e) {
            logger.error("Company with ID: {} not found. Error: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(List.of(new BeneficiaryDTO(null, e.getMessage(), null, false)));
        }
    }

    private void validateType(String type) {
        List<String> validTypes = List.of("all", "natural", "effective");
        if (!validTypes.contains(type.toLowerCase())) {
            throw new IllegalArgumentException("Invalid type parameter. Allowed values are: all, natural, effective.");
        }
    }
}