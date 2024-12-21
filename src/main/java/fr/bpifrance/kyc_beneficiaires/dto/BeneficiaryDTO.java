package fr.bpifrance.kyc_beneficiaires.dto;

public class BeneficiaryDTO {
    private Long id;
    private String name;
    private Double percentage;
    private boolean isNatural;

    public BeneficiaryDTO(Long id, String name, Double percentage, boolean isNatural) {
        this.id = id;
        this.name = name;
        this.percentage = percentage;
        this.isNatural = isNatural;
    }

    public BeneficiaryDTO() {

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