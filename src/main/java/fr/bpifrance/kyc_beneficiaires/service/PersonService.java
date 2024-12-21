package fr.bpifrance.kyc_beneficiaires.service;

import fr.bpifrance.kyc_beneficiaires.dto.PersonDTO;
import fr.bpifrance.kyc_beneficiaires.mapper.PersonMapper;
import fr.bpifrance.kyc_beneficiaires.model.Person;
import fr.bpifrance.kyc_beneficiaires.repository.PersonRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class PersonService {

    private final PersonRepository personRepository;
    private final PersonMapper personMapper;

    public PersonService(PersonRepository personRepository, PersonMapper personMapper) {
        this.personRepository = personRepository;
        this.personMapper = personMapper;
    }

    public PersonDTO getPersonById(Long id) {
        Person person = personRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Person not found"));
        return personMapper.toPersonDTO(person);
    }

    public PersonDTO savePerson(PersonDTO personDTO) {
        if (personDTO.getName() == null || personDTO.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Person name must not be blank");
        }
        Person person = personMapper.toPerson(personDTO);
        Person savedPerson = personRepository.save(person);
        return personMapper.toPersonDTO(savedPerson);
    }
}
