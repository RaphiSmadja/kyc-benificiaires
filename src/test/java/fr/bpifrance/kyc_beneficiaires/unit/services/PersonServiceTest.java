package fr.bpifrance.kyc_beneficiaires.unit.services;

import fr.bpifrance.kyc_beneficiaires.dto.PersonDTO;
import fr.bpifrance.kyc_beneficiaires.mapper.PersonMapper;
import fr.bpifrance.kyc_beneficiaires.model.Person;
import fr.bpifrance.kyc_beneficiaires.repository.PersonRepository;
import fr.bpifrance.kyc_beneficiaires.service.PersonService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PersonServiceTest {

    @Mock
    private PersonRepository personRepository;

    @Mock
    private PersonMapper personMapper;

    @InjectMocks
    private PersonService personService;

    @Test
    void getPersonById_shouldReturnPersonDTO_whenPersonFound() {
        Long personId = 1L;
        Person person = new Person(personId, "Jane");
        PersonDTO personDTO = new PersonDTO(personId, "Jane");

        when(personRepository.findById(personId)).thenReturn(Optional.of(person));
        when(personMapper.toPersonDTO(person)).thenReturn(personDTO);

        PersonDTO result = personService.getPersonById(personId);

        assertNotNull(result);
        assertEquals("Jane", result.getName());
    }

    @Test
    void getPersonById_shouldThrowException_whenPersonNotFound() {
        Long personId = 1L;

        when(personRepository.findById(personId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> personService.getPersonById(personId));

        assertEquals("Person not found", exception.getMessage());
    }

    @Test
    void savePerson_shouldReturnPersonDTO_whenValidPerson() {
        PersonDTO personDTO = new PersonDTO(null, "Raphael");
        Person person = new Person(null, "Raphael");
        Person savedPerson = new Person(1L, "Raphael");
        PersonDTO savedPersonDTO = new PersonDTO(1L, "Raphael");

        when(personMapper.toPerson(personDTO)).thenReturn(person);
        when(personRepository.save(person)).thenReturn(savedPerson);
        when(personMapper.toPersonDTO(savedPerson)).thenReturn(savedPersonDTO);

        PersonDTO result = personService.savePerson(personDTO);

        assertNotNull(result);
        assertEquals("Raphael", result.getName());
    }

    @Test
    void savePerson_shouldThrowException_whenNameIsInvalid() {
        PersonDTO invalidPersonDTO = new PersonDTO(null, "   ");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> personService.savePerson(invalidPersonDTO));

        assertEquals("Person name must not be blank", exception.getMessage());
    }
}
