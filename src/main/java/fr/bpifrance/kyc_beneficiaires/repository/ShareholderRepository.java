package fr.bpifrance.kyc_beneficiaires.repository;

import fr.bpifrance.kyc_beneficiaires.model.Company;
import fr.bpifrance.kyc_beneficiaires.model.Shareholder;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShareholderRepository extends CrudRepository<Shareholder, Long> {
    List<Shareholder> findAll();
    List<Shareholder> findByCompany(Company company);
}
