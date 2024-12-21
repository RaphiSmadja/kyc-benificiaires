package fr.bpifrance.kyc_beneficiaires.model;

import jakarta.persistence.*;

@Entity
public class Shareholder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Company company;

    @ManyToOne
    private Person person;

    @ManyToOne
    private Company shareholderCompany;

    private Double percentage;

    public Shareholder() {

    }

    public Shareholder(Long id, Company company, Person person, Company shareholderCompany, Double percentage) {
        this.id = id;
        this.company = company;
        this.person = person;
        this.shareholderCompany = shareholderCompany;
        this.percentage = percentage;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Company getShareholderCompany() {
        return shareholderCompany;
    }

    public void setShareholderCompany(Company shareholderCompany) {
        this.shareholderCompany = shareholderCompany;
    }

    public Double getPercentage() {
        return percentage;
    }

    public void setPercentage(Double percentage) {
        this.percentage = percentage;
    }
}
