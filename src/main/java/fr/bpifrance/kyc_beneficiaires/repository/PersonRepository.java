package fr.bpifrance.kyc_beneficiaires.repository;

import fr.bpifrance.kyc_beneficiaires.model.Person;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonRepository extends CrudRepository<Person, Long> {
    List<Person> findAll();
}
