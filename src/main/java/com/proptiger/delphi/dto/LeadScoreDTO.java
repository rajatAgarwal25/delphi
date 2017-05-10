package com.proptiger.delphi.dto;

public class LeadScoreDTO {

    private Double                 score;
    private LeadConvertiblityClass leadConvertiblityClass;
    private String                 modelVersion;

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public LeadConvertiblityClass getLeadConvertiblityClass() {
        return leadConvertiblityClass;
    }

    public void setLeadConvertiblityClass(LeadConvertiblityClass leadConvertiblityClass) {
        this.leadConvertiblityClass = leadConvertiblityClass;
    }

    public String getModelVersion() {
        return modelVersion;
    }

    public void setModelVersion(String modelVersion) {
        this.modelVersion = modelVersion;
    }

}
