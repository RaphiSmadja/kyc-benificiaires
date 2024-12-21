package fr.bpifrance.kyc_beneficiaires.service;

import fr.bpifrance.kyc_beneficiaires.dto.BeneficiaryDTO;
import fr.bpifrance.kyc_beneficiaires.mapper.BeneficiaryMapper;
import fr.bpifrance.kyc_beneficiaires.model.Company;
import fr.bpifrance.kyc_beneficiaires.model.Person;
import fr.bpifrance.kyc_beneficiaires.model.Shareholder;
import fr.bpifrance.kyc_beneficiaires.repository.CompanyRepository;
import fr.bpifrance.kyc_beneficiaires.repository.ShareholderRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final ShareholderRepository shareholderRepository;
    private final BeneficiaryMapper beneficiaryMapper;

    public CompanyService(CompanyRepository companyRepository, ShareholderRepository shareholderRepository, BeneficiaryMapper beneficiaryMapper) {
        this.companyRepository = companyRepository;
        this.shareholderRepository = shareholderRepository;
        this.beneficiaryMapper = beneficiaryMapper;
    }

    public Company saveCompany(Company company) {
        return companyRepository.save(company);
    }

    public Optional<Company> getCompanyById(Long id, boolean natural) {
        return companyRepository.findById(id);
    }

    public List<BeneficiaryDTO> getBeneficiaries(Long companyId, String type) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new EntityNotFoundException("Company not found"));

        Map<Object, Double> allBeneficiaries = getAllBeneficiaries(company);

        return allBeneficiaries.entrySet().stream()
                .filter(entry -> shouldIncludeBeneficiary(entry.getKey(), type, entry.getValue()))
                .map(entry -> beneficiaryMapper.toBeneficiaryDTO(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    private Map<Object, Double> getAllBeneficiaries(Company company) {
        return calculateBeneficiaries(company, 100.0, new HashSet<>());
    }

    private Map<Object, Double> calculateBeneficiaries(Company company, double parentPercentage, Set<Company> visited) {

        if (visited.contains(company)) {
            return Collections.emptyMap();
        }
        visited.add(company);

        Map<Object, Double> beneficiaries = new HashMap<>();
        List<Shareholder> shareholders = shareholderRepository.findByCompany(company);

        for (Shareholder shareholder : shareholders) {
            if (shareholder.getPerson() != null) {
                addBeneficiary(beneficiaries, shareholder.getPerson(), shareholder.getPercentage() * parentPercentage / 100);
            } else if (shareholder.getShareholderCompany() != null) {
                addBeneficiary(beneficiaries, shareholder.getShareholderCompany(), shareholder.getPercentage() * parentPercentage / 100);


                Map<Object, Double> indirectBeneficiaries = calculateBeneficiaries(
                        shareholder.getShareholderCompany(),
                        shareholder.getPercentage() * parentPercentage / 100,
                        visited
                );

                indirectBeneficiaries.forEach((key, value) -> addBeneficiary(beneficiaries, key, value));
            }
        }

        return beneficiaries;
    }

    private void addBeneficiary(Map<Object, Double> beneficiaries, Object beneficiary, double percentage) {
        beneficiaries.merge(beneficiary, percentage, Double::sum);
    }


    private Map<Object, Double> calculateIndirectBeneficiaries(Company company, double parentPercentage, Set<Company> visited) {
        Map<Object, Double> beneficiaries = new HashMap<>();

        if (visited.contains(company)) {
            return beneficiaries;
        }
        visited.add(company);

        List<Shareholder> shareholders = shareholderRepository.findByCompany(company);

        for (Shareholder shareholder : shareholders) {
            if (shareholder.getPerson() != null) {
                beneficiaries.merge(
                        shareholder.getPerson(),
                        shareholder.getPercentage() * parentPercentage / 100,
                        Double::sum
                );
            } else if (shareholder.getShareholderCompany() != null) {
                beneficiaries.merge(
                        shareholder.getShareholderCompany(),
                        shareholder.getPercentage() * parentPercentage / 100,
                        Double::sum
                );

                Map<Object, Double> indirectBeneficiaries = calculateIndirectBeneficiaries(
                        shareholder.getShareholderCompany(),
                        shareholder.getPercentage() * parentPercentage / 100,
                        visited
                );

                indirectBeneficiaries.forEach((beneficiary, percentage) ->
                        beneficiaries.merge(beneficiary, percentage, Double::sum)
                );
            }
        }

        return beneficiaries;
    }

    private boolean shouldIncludeBeneficiary(Object beneficiary, String type, double percentage) {
        return switch (type.toLowerCase()) {
            case "natural" -> beneficiary instanceof Person;
            case "effective" -> beneficiary instanceof Person && percentage > 25.0;
            default -> true;
        };
    }
}
