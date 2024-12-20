package fr.bpifrance.kyc_beneficiaires.model;

import jakarta.persistence.*;

@Entity
@Table(name = "effective")
public class Effectivebeneficiary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Person person;

    @ManyToOne
    private Company company;

    private boolean isDirect;

    private Double totalPercentage;

    public Effectivebeneficiary(Long id, Person person, Company company, boolean isDirect, Double totalPercentage) {
        this.id = id;
        this.person = person;
        this.company = company;
        this.isDirect = isDirect;
        this.totalPercentage = totalPercentage;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public boolean isDirect() {
        return isDirect;
    }

    public void setDirect(boolean direct) {
        isDirect = direct;
    }

    public Double getTotalPercentage() {
        return totalPercentage;
    }

    public void setTotalPercentage(Double totalPercentage) {
        this.totalPercentage = totalPercentage;
    }
}
