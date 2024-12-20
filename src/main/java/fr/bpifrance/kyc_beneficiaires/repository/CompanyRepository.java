package fr.bpifrance.kyc_beneficiaires.repository;

import fr.bpifrance.kyc_beneficiaires.model.Company;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyRepository extends CrudRepository<Company, Long> {
    List<Company> findAll();
}
