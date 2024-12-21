package fr.bpifrance.kyc_beneficiaires.service;

import fr.bpifrance.kyc_beneficiaires.dto.BeneficiaryDTO;
import fr.bpifrance.kyc_beneficiaires.dto.CompanyDTO;
import fr.bpifrance.kyc_beneficiaires.mapper.BeneficiaryMapper;
import fr.bpifrance.kyc_beneficiaires.mapper.CompanyMapper;
import fr.bpifrance.kyc_beneficiaires.model.Company;
import fr.bpifrance.kyc_beneficiaires.model.Person;
import fr.bpifrance.kyc_beneficiaires.model.Shareholder;
import fr.bpifrance.kyc_beneficiaires.repository.CompanyRepository;
import fr.bpifrance.kyc_beneficiaires.repository.PersonRepository;
import fr.bpifrance.kyc_beneficiaires.repository.ShareholderRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final ShareholderRepository shareholderRepository;
    private final BeneficiaryMapper beneficiaryMapper;
    private final PersonRepository personRepository;
    private final CompanyMapper companyMapper;

    public CompanyService(CompanyRepository companyRepository, ShareholderRepository shareholderRepository,
                          BeneficiaryMapper beneficiaryMapper, PersonRepository personRepository, CompanyMapper companyMapper) {
        this.companyRepository = companyRepository;
        this.shareholderRepository = shareholderRepository;
        this.beneficiaryMapper = beneficiaryMapper;
        this.personRepository = personRepository;
        this.companyMapper = companyMapper;
    }

    public CompanyDTO saveCompany(String companyName) {
        if (companyName == null || companyName.trim().isEmpty()) {
            throw new IllegalArgumentException("Company name must not be blank");
        }
        Company company = new Company();
        company.setName(companyName);
        return companyMapper.toCompanyDTO(companyRepository.save(company));
    }


    public CompanyDTO getCompanyById(Long id) {
        Company company = companyRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Company not found"));
        return companyMapper.toCompanyDTO(company);
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

    @Transactional
    public void addBeneficiary(Long companyId, BeneficiaryDTO beneficiaryDTO) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new EntityNotFoundException("Company not found"));

        double currentPercentage = shareholderRepository.findByCompany(company).stream()
                .mapToDouble(Shareholder::getPercentage)
                .sum();

        if (currentPercentage + beneficiaryDTO.getPercentage() > 100.0) {
            throw new IllegalArgumentException("The total ownership percentage exceeds 100%");
        }

        if (beneficiaryDTO.isNatural()) {
            Person person = personRepository.findById(beneficiaryDTO.getId())
                    .orElseThrow(() -> new EntityNotFoundException("Person not found"));
            Shareholder shareholder = new Shareholder(companyId, company, person, null, beneficiaryDTO.getPercentage());
            shareholderRepository.save(shareholder);
        } else {
            Company shareholderCompany = companyRepository.findById(beneficiaryDTO.getId())
                    .orElseThrow(() -> new EntityNotFoundException("Company not found"));
            Shareholder shareholder = new Shareholder(null, company, null, shareholderCompany, beneficiaryDTO.getPercentage());
            shareholderRepository.save(shareholder);
        }
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
                accumulateBeneficiaryPercentage(beneficiaries, shareholder.getPerson(), shareholder.getPercentage() * parentPercentage / 100);
            } else if (shareholder.getShareholderCompany() != null) {
                accumulateBeneficiaryPercentage(beneficiaries, shareholder.getShareholderCompany(), shareholder.getPercentage() * parentPercentage / 100);


                Map<Object, Double> indirectBeneficiaries = calculateBeneficiaries(
                        shareholder.getShareholderCompany(),
                        shareholder.getPercentage() * parentPercentage / 100,
                        visited
                );

                indirectBeneficiaries.forEach((key, value) -> accumulateBeneficiaryPercentage(beneficiaries, key, value));
            }
        }

        return beneficiaries;
    }

    private void accumulateBeneficiaryPercentage(Map<Object, Double> beneficiaries, Object beneficiary, double percentage) {
        beneficiaries.merge(beneficiary, percentage, Double::sum);
    }

    private boolean shouldIncludeBeneficiary(Object beneficiary, String type, double percentage) {
        return switch (type.toLowerCase()) {
            case "natural" -> beneficiary instanceof Person;
            case "effective" -> beneficiary instanceof Person && percentage > 25.0;
            default -> true;
        };
    }
}
