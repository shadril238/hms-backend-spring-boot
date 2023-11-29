package com.shadril.analyticresearchservice.builderpattern;

import com.shadril.analyticresearchservice.entity.ResearcherEntity;

public class ResearcherBuilder {
    private Long id;
    private String name;
    private String email;
    private String designation;
    private String institute;
    private String purpose;
    private Boolean isValid;
    private Boolean isTaken;

    public ResearcherBuilder id(Long id) {
        this.id = id;
        return this;
    }

    public ResearcherBuilder name(String name) {
        this.name = name;
        return this;
    }

    public ResearcherBuilder email(String email) {
        this.email = email;
        return this;
    }

    public ResearcherBuilder designation(String designation) {
        this.designation = designation;
        return this;
    }

    public ResearcherBuilder institute(String institute) {
        this.institute = institute;
        return this;
    }

    public ResearcherBuilder purpose(String purpose) {
        this.purpose = purpose;
        return this;
    }

    public ResearcherBuilder isValid(Boolean isValid) {
        this.isValid = isValid;
        return this;
    }

    public ResearcherBuilder isTaken(Boolean isTaken) {
        this.isTaken = isTaken;
        return this;
    }

    public ResearcherEntity build() {
        ResearcherEntity researcherEntity = new ResearcherEntity();
        researcherEntity.setId(id);
        researcherEntity.setName(name);
        researcherEntity.setEmail(email);
        researcherEntity.setDesignation(designation);
        researcherEntity.setInstitute(institute);
        researcherEntity.setPurpose(purpose);
        researcherEntity.setIsValid(isValid);
        researcherEntity.setIsTaken(isTaken);
        return researcherEntity;
    }
}
