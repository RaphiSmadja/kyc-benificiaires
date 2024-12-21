package fr.bpifrance.kyc_beneficiaires.mapper;

import fr.bpifrance.kyc_beneficiaires.dto.CompanyDTO;
import fr.bpifrance.kyc_beneficiaires.dto.ShareholderDTO;
import fr.bpifrance.kyc_beneficiaires.model.Company;
import fr.bpifrance.kyc_beneficiaires.model.Shareholder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CompanyMapper {
    public CompanyDTO toCompanyDTO(Company company) {
        return new CompanyDTO(company.getId(), company.getName(),
                company.getShareholders().stream()
                        .map(this::toShareholderDTO)
                        .toList());
    }

    public ShareholderDTO toShareholderDTO(Shareholder shareholder) {
        String name = shareholder.getPerson() != null
                ? shareholder.getPerson().getName()
                : shareholder.getShareholderCompany().getName();

        boolean isNatural = shareholder.getPerson() != null;

        return new ShareholderDTO(
                shareholder.getId(),
                name,
                shareholder.getPercentage(),
                isNatural
        );
    }

    public Company toCompany(CompanyDTO companyDTO) {
        return new Company(companyDTO.getId(), companyDTO.getName(), new ArrayList<>());
    }

}
