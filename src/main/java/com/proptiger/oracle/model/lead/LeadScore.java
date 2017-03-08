package com.proptiger.oracle.model.lead;

public class LeadScore implements Comparable<LeadScore> {

    private LeadData leadData;
    private Double   score;

    public LeadData getLeadData() {
        return leadData;
    }

    public void setLeadData(LeadData leadData) {
        this.leadData = leadData;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    @Override
    public int compareTo(LeadScore o) {
        return score.compareTo(o.score);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((leadData == null) ? 0 : leadData.hashCode());
        result = prime * result + ((score == null) ? 0 : score.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        LeadScore other = (LeadScore) obj;
        if (leadData == null) {
            if (other.leadData != null)
                return false;
        }
        else if (!leadData.equals(other.leadData))
            return false;
        if (score == null) {
            if (other.score != null)
                return false;
        }
        else if (!score.equals(other.score))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "LeadScore [leadData=" + leadData.getLeadId() + ", score=" + score + "]";
    }
}
