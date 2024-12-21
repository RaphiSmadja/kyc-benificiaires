package fr.bpifrance.kyc_beneficiaires.mapper;

import fr.bpifrance.kyc_beneficiaires.dto.PersonDTO;
import fr.bpifrance.kyc_beneficiaires.model.Person;
import org.springframework.stereotype.Service;

@Service
public class PersonMapper {
    public PersonDTO toPersonDTO(Person person) {
        return new PersonDTO(
                person.getId(),
                person.getName()
        );
    }

    public Person toPerson(PersonDTO personDTO) {
        return new Person(
                personDTO.getId(),
                personDTO.getName()
        );
    }
}
