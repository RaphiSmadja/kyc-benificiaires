package fr.bpifrance.kyc_beneficiaires.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.ArrayList;
import java.util.List;

@Schema(description = "Represents a company with its details.")
public class CompanyDTO {

    @Schema(description = "ID of the company", example = "1")
    private Long id;

    @Schema(description = "Name of the company", example = "Company A")
    private String name;

    @Schema(description = "List of shareholders")
    private List<ShareholderDTO> shareholders = new ArrayList<>();

    public CompanyDTO(Long id, String name, List<ShareholderDTO> shareholders) {
        this.id = id;
        this.name = name;
        this.shareholders = shareholders;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ShareholderDTO> getShareholders() {
        return shareholders;
    }

    public void setShareholders(List<ShareholderDTO> shareholders) {
        this.shareholders = shareholders;
    }
}
