package fr.bpifrance.kyc_beneficiaires.unit.controller;

import fr.bpifrance.kyc_beneficiaires.controller.PersonController;
import fr.bpifrance.kyc_beneficiaires.dto.PersonDTO;
import fr.bpifrance.kyc_beneficiaires.service.PersonService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PersonControllerTest {

    @Mock
    private PersonService personService;

    @InjectMocks
    private PersonController personController;

    @Test
    void getPersonById_shouldReturnOk_whenPersonFound() {
        Long personId = 1L;
        PersonDTO personDTO = new PersonDTO(personId, "Raphael SMADJA");

        when(personService.getPersonById(personId)).thenReturn(personDTO);

        ResponseEntity<PersonDTO> response = personController.getPersonById(personId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Raphael SMADJA", response.getBody().getName());
    }

    @Test
    void getPersonById_shouldReturnBadRequest_whenPersonNotFound() {
        Long personId = 1L;

        when(personService.getPersonById(personId)).thenThrow(new EntityNotFoundException("Person not found"));

        ResponseEntity<PersonDTO> response = personController.getPersonById(personId);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void createPerson_shouldReturnCreated_whenPersonCreated() {
        PersonDTO personDTO = new PersonDTO(null, "Raphael SMADJA");
        PersonDTO createdPersonDTO = new PersonDTO(1L, "Raphael SMADJA");

        when(personService.savePerson(personDTO)).thenReturn(createdPersonDTO);

        ResponseEntity<Void> response = personController.createPerson(personDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("/persons/1", response.getHeaders().getLocation().toString());
    }

    @Test
    void createPerson_shouldReturnBadRequest_whenPersonNameIsInvalid() {
        PersonDTO invalidPersonDTO = new PersonDTO(null, "     ");

        ResponseEntity<Void> response = personController.createPerson(invalidPersonDTO);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }


}
