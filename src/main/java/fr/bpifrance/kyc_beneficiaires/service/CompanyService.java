package fr.bpifrance.kyc_beneficiaires.service;

import fr.bpifrance.kyc_beneficiaires.model.Company;
import fr.bpifrance.kyc_beneficiaires.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CompanyService {
    @Autowired
    private CompanyRepository companyRepository;

    public Company saveCompany(Company company) {
        return companyRepository.save(company);
    }

    public Optional<Company> getCompanyById(Long id, boolean natural) {
        return companyRepository.findById(id);
    }
}
