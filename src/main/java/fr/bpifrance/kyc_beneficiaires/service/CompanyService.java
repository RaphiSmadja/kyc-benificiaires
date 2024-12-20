package fr.bpifrance.kyc_beneficiaires.service;

import fr.bpifrance.kyc_beneficiaires.dto.BeneficiaryDTO;
import fr.bpifrance.kyc_beneficiaires.model.Company;
import fr.bpifrance.kyc_beneficiaires.model.Shareholder;
import fr.bpifrance.kyc_beneficiaires.repository.CompanyRepository;
import fr.bpifrance.kyc_beneficiaires.repository.ShareholderRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final ShareholderRepository shareholderRepository;

    public CompanyService(CompanyRepository companyRepository, ShareholderRepository shareholderRepository) {
        this.companyRepository = companyRepository;
        this.shareholderRepository = shareholderRepository;
    }

    public Company saveCompany(Company company) {
        return companyRepository.save(company);
    }

    public Optional<Company> getCompanyById(Long id, boolean natural) {
        return companyRepository.findById(id);
    }

    public List<BeneficiaryDTO> getBenificiaries(Long companyId, String type) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new EntityNotFoundException("Company not found"));

        List<Shareholder> shareholders = shareholderRepository.findByCompany(company);

        return shareholders.stream()
                .filter(holder -> filterByType(holder, type))
                .map(holder -> {
                    Long id = holder.getId();
                    String name = (holder.getPerson() != null)
                            ? holder.getPerson().getName()
                            : holder.getShareholderCompany().getName();
                    Double percentage = holder.getPercentage();
                    boolean isNatural = holder.getPerson() != null;

                    return new BeneficiaryDTO(id, name, percentage, isNatural);
                })
                .collect(Collectors.toList());
    }

    private boolean filterByType(Shareholder shareholder, String type) {
        return switch (type.toLowerCase()) {
            case "natural" -> shareholder.getPerson() != null;
            case "effective" -> shareholder.getPercentage() > 25.0;
            default -> true;
        };
    }
}
