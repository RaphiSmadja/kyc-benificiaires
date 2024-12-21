package fr.bpifrance.kyc_beneficiaires.controller;

import fr.bpifrance.kyc_beneficiaires.dto.PersonDTO;
import fr.bpifrance.kyc_beneficiaires.service.PersonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/persons")
public class PersonController {

    private static final Logger logger = LoggerFactory.getLogger(PersonController.class);

    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @Operation(summary = "Get a person by ID", description = "Fetches the details of a physical person by their ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Person details returned successfully"),
            @ApiResponse(responseCode = "400", description = "Person not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<PersonDTO> getPersonById(
            @Parameter(description = "ID of the person to fetch") @PathVariable Long id) {
        logger.info("Received request to fetch person with ID: {}", id);

        try {
            PersonDTO person = personService.getPersonById(id);
            logger.info("Person with ID: {} found", id);
            return ResponseEntity.ok(person);
        } catch (EntityNotFoundException e) {
            logger.error("Person with ID: {} not found. Error: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @Operation(summary = "Create a new person", description = "Creates a new physical person with the provided details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Person created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping
    public ResponseEntity<Void> createPerson(@Valid @RequestBody PersonDTO personDTO) {
        logger.info("Received request to create a new person with name: {}", personDTO.getName());

        if (personDTO.getName() == null || personDTO.getName().trim().isEmpty()) {
            logger.error("Invalid person name: {}", personDTO.getName());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        PersonDTO createdPerson = personService.savePerson(personDTO);

        URI location = URI.create(String.format("/persons/%d", createdPerson.getId()));
        logger.info("Person created with ID: {}", createdPerson.getId());
        return ResponseEntity.created(location).build();
    }
}
