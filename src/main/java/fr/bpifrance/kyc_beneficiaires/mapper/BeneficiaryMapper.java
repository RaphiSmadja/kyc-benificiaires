package fr.bpifrance.kyc_beneficiaires.mapper;

import fr.bpifrance.kyc_beneficiaires.dto.BeneficiaryDTO;
import fr.bpifrance.kyc_beneficiaires.model.Company;
import fr.bpifrance.kyc_beneficiaires.model.Person;
import org.springframework.stereotype.Service;

@Service
public class BeneficiaryMapper {

    public BeneficiaryDTO toBeneficiaryDTO(Object beneficiary, double percentage) {
        if (beneficiary instanceof Person person) {
            return new BeneficiaryDTO(person.getId(), person.getName(), percentage, true);
        } else if (beneficiary instanceof Company company) {
            return new BeneficiaryDTO(company.getId(), company.getName(), percentage, false);
        } else {
            throw new IllegalStateException("Unknown beneficiary type");
        }
    }
}
