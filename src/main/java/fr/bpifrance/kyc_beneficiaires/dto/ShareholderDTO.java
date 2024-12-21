package fr.bpifrance.kyc_beneficiaires.dto;

import fr.bpifrance.kyc_beneficiaires.model.Company;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Represents a ShareHolder with its details.")
public class ShareholderDTO {
    @Schema(description = "ID of the shareholder", example = "1")
    private Long id;
    @Schema(description = "name")
    private String name;
    @Schema(description = "percentage", example = "10.0")
    private Double percentage;
    @Schema(description = "natural", example = "true")
    private boolean isNatural;

    public ShareholderDTO(Long id, String name, Double percentage, boolean isNatural) {
        this.id = id;
        this.name = name;
        this.percentage = percentage;
        this.isNatural = isNatural;
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

    public Double getPercentage() {
        return percentage;
    }

    public void setPercentage(Double percentage) {
        this.percentage = percentage;
    }

    public boolean isNatural() {
        return isNatural;
    }

    public void setNatural(boolean natural) {
        isNatural = natural;
    }
}
