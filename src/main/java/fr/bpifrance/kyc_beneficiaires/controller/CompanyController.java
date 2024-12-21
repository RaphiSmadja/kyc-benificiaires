package fr.bpifrance.kyc_beneficiaires.controller;

import fr.bpifrance.kyc_beneficiaires.dto.BeneficiaryDTO;
import fr.bpifrance.kyc_beneficiaires.dto.CompanyDTO;
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

import java.net.URI;
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

    @Operation(summary = "Add a beneficiary to a company", description = "Adds a new beneficiary (person or company) to the specified company.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Beneficiary added successfully"),
            @ApiResponse(responseCode = "404", description = "Company or beneficiary not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input or percentage exceeds 100%")
    })
    @PostMapping("/{companyId}/beneficiaries")
    public ResponseEntity<String> addBeneficiaryToCompany(
            @Parameter(description = "ID of the company to which the beneficiary is being added")
            @PathVariable Long companyId,
            @RequestBody BeneficiaryDTO beneficiaryDTO) {
        logger.info("Received request to add beneficiary to company ID: {}", companyId);

        try {
            companyService.addBeneficiary(companyId, beneficiaryDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body("Beneficiary added successfully.");
        } catch (EntityNotFoundException e) {
            logger.error("Error adding beneficiary to company ID: {}. {}", companyId, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("Invalid request for company ID: {}. {}", companyId, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @Operation(summary = "Get a company by ID", description = "Fetches the details of a company by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Company details returned successfully"),
            @ApiResponse(responseCode = "404", description = "Company not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CompanyDTO> getCompanyById(
            @Parameter(description = "ID of the company to fetch")
            @PathVariable Long id) {
        logger.info("Received request to fetch company with ID: {}", id);
        try {
            CompanyDTO company = companyService.getCompanyById(id);
            return ResponseEntity.ok(company);
        } catch (EntityNotFoundException e) {
            logger.error("Company with ID: {} not found. Error: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @Operation(summary = "Create a new company", description = "Creates a new company with the given name.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Company created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input (e.g., empty name)")
    })
    @PostMapping
    public ResponseEntity<Void> createCompany(
            @Parameter(description = "Name of the company to be created", example = "Company A")
            @RequestBody String companyName) {
        logger.info("Received request to create a new company with name: {}", companyName);

        CompanyDTO createdCompany = companyService.saveCompany(companyName);

        URI location = URI.create(String.format("/companies/%d", createdCompany.getId()));

        logger.info("Company created with ID: {}", createdCompany.getId());
        return ResponseEntity.created(location).build();
    }

    private void validateType(String type) {
        List<String> validTypes = List.of("all", "natural", "effective");
        if (!validTypes.contains(type.toLowerCase())) {
            throw new IllegalArgumentException("Invalid type parameter. Allowed values are: all, natural, effective.");
        }
    }
}