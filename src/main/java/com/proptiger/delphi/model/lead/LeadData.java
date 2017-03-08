package com.proptiger.delphi.model.lead;

import java.io.Serializable;

public class LeadData implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 8804379444343690749L;

    private Integer           leadId;
    private Boolean           meetingDone;
    private Boolean           svDone;
    private Boolean           closed;
    private Boolean           isPresalesVerified;

    private Integer           clientTypeId;
    private Integer           cityId;
    private Integer           saleTypeId;
    private Integer           timeFrameId;
    private Boolean           isStar;
    private Integer           additionalEnquiryCount;
    private Integer           statusId;
    private Integer           enquiryLevelProjectCount;
    private Boolean           projectLevelLead;
    private Integer           uploadTypeId;
    private Integer           numCallAtPresales;
    private Boolean           confCallVerified;
    private Integer           bedrooms;
    private Integer           countryId;
    private Integer           budget;

    public Integer getClientTypeId() {
        return clientTypeId;
    }

    public void setClientTypeId(Integer clientTypeId) {
        this.clientTypeId = clientTypeId;
    }

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public Integer getSaleTypeId() {
        return saleTypeId;
    }

    public void setSaleTypeId(Integer saleTypeId) {
        this.saleTypeId = saleTypeId;
    }

    public Integer getTimeFrameId() {
        return timeFrameId;
    }

    public void setTimeFrameId(Integer timeFrame) {
        this.timeFrameId = timeFrame;
    }

    public Boolean getIsStar() {
        return isStar;
    }

    public void setIsStar(Boolean isStar) {
        this.isStar = isStar;
    }

    public Integer getAdditionalEnquiryCount() {
        return additionalEnquiryCount;
    }

    public void setAdditionalEnquiryCount(Integer additionalEnquiryCount) {
        this.additionalEnquiryCount = additionalEnquiryCount;
    }

    public Integer getStatusId() {
        return statusId;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }

    public Boolean getProjectLevelLead() {
        return projectLevelLead;
    }

    public void setProjectLevelLead(Boolean projectLevelLead) {
        this.projectLevelLead = projectLevelLead;
    }

    public Integer getUploadTypeId() {
        return uploadTypeId;
    }

    public void setUploadTypeId(Integer uploadTypeId) {
        this.uploadTypeId = uploadTypeId;
    }

    public Integer getNumCallAtPresales() {
        return numCallAtPresales;
    }

    public void setNumCallAtPresales(Integer numCallAtPresales) {
        this.numCallAtPresales = numCallAtPresales;
    }

    public Boolean getConfCallVerified() {
        return confCallVerified;
    }

    public void setConfCallVerified(Boolean confCallVerified) {
        this.confCallVerified = confCallVerified;
    }

    public Integer getBedrooms() {
        return bedrooms;
    }

    public void setBedrooms(Integer bedrooms) {
        this.bedrooms = bedrooms;
    }

    public Integer getCountryId() {
        return countryId;
    }

    public void setCountryId(Integer countryId) {
        this.countryId = countryId;
    }

    public Integer getLeadId() {
        return leadId;
    }

    public void setLeadId(Integer leadId) {
        this.leadId = leadId;
    }

    public Boolean getClosed() {
        return closed;
    }

    public void setClosed(Boolean closed) {
        this.closed = closed;
    }

    public Integer getBudget() {
        return budget;
    }

    public void setBudget(Integer budget) {
        this.budget = budget;
    }

    public Boolean getIsPresalesVerified() {
        return isPresalesVerified;
    }

    public void setIsPresalesVerified(Boolean isPresalesVerified) {
        this.isPresalesVerified = isPresalesVerified;
    }

    public Boolean getMeetingDone() {
        return meetingDone;
    }

    public void setMeetingDone(Boolean meetingDone) {
        this.meetingDone = meetingDone;
    }

    public Boolean getSvDone() {
        return svDone;
    }

    public void setSvDone(Boolean svDone) {
        this.svDone = svDone;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((additionalEnquiryCount == null) ? 0 : additionalEnquiryCount.hashCode());
        result = prime * result + ((bedrooms == null) ? 0 : bedrooms.hashCode());
        result = prime * result + ((budget == null) ? 0 : budget.hashCode());
        result = prime * result + ((cityId == null) ? 0 : cityId.hashCode());
        result = prime * result + ((clientTypeId == null) ? 0 : clientTypeId.hashCode());
        result = prime * result + ((closed == null) ? 0 : closed.hashCode());
        result = prime * result + ((confCallVerified == null) ? 0 : confCallVerified.hashCode());
        result = prime * result + ((countryId == null) ? 0 : countryId.hashCode());
        result = prime * result + ((isPresalesVerified == null) ? 0 : isPresalesVerified.hashCode());
        result = prime * result + ((isStar == null) ? 0 : isStar.hashCode());
        result = prime * result + ((leadId == null) ? 0 : leadId.hashCode());
        result = prime * result + ((meetingDone == null) ? 0 : meetingDone.hashCode());
        result = prime * result + ((numCallAtPresales == null) ? 0 : numCallAtPresales.hashCode());
        result = prime * result + ((enquiryLevelProjectCount == null) ? 0 : enquiryLevelProjectCount.hashCode());
        result = prime * result + ((projectLevelLead == null) ? 0 : projectLevelLead.hashCode());
        result = prime * result + ((saleTypeId == null) ? 0 : saleTypeId.hashCode());
        result = prime * result + ((statusId == null) ? 0 : statusId.hashCode());
        result = prime * result + ((svDone == null) ? 0 : svDone.hashCode());
        result = prime * result + ((timeFrameId == null) ? 0 : timeFrameId.hashCode());
        result = prime * result + ((uploadTypeId == null) ? 0 : uploadTypeId.hashCode());
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
        LeadData other = (LeadData) obj;
        if (additionalEnquiryCount == null) {
            if (other.additionalEnquiryCount != null)
                return false;
        }
        else if (!additionalEnquiryCount.equals(other.additionalEnquiryCount))
            return false;
        if (bedrooms == null) {
            if (other.bedrooms != null)
                return false;
        }
        else if (!bedrooms.equals(other.bedrooms))
            return false;
        if (budget == null) {
            if (other.budget != null)
                return false;
        }
        else if (!budget.equals(other.budget))
            return false;
        if (cityId == null) {
            if (other.cityId != null)
                return false;
        }
        else if (!cityId.equals(other.cityId))
            return false;
        if (clientTypeId == null) {
            if (other.clientTypeId != null)
                return false;
        }
        else if (!clientTypeId.equals(other.clientTypeId))
            return false;
        if (closed == null) {
            if (other.closed != null)
                return false;
        }
        else if (!closed.equals(other.closed))
            return false;
        if (confCallVerified == null) {
            if (other.confCallVerified != null)
                return false;
        }
        else if (!confCallVerified.equals(other.confCallVerified))
            return false;
        if (countryId == null) {
            if (other.countryId != null)
                return false;
        }
        else if (!countryId.equals(other.countryId))
            return false;
        if (isPresalesVerified == null) {
            if (other.isPresalesVerified != null)
                return false;
        }
        else if (!isPresalesVerified.equals(other.isPresalesVerified))
            return false;
        if (isStar == null) {
            if (other.isStar != null)
                return false;
        }
        else if (!isStar.equals(other.isStar))
            return false;
        if (leadId == null) {
            if (other.leadId != null)
                return false;
        }
        else if (!leadId.equals(other.leadId))
            return false;
        if (meetingDone == null) {
            if (other.meetingDone != null)
                return false;
        }
        else if (!meetingDone.equals(other.meetingDone))
            return false;
        if (numCallAtPresales == null) {
            if (other.numCallAtPresales != null)
                return false;
        }
        else if (!numCallAtPresales.equals(other.numCallAtPresales))
            return false;
        if (enquiryLevelProjectCount == null) {
            if (other.enquiryLevelProjectCount != null)
                return false;
        }
        else if (!enquiryLevelProjectCount.equals(other.enquiryLevelProjectCount))
            return false;
        if (projectLevelLead == null) {
            if (other.projectLevelLead != null)
                return false;
        }
        else if (!projectLevelLead.equals(other.projectLevelLead))
            return false;
        if (saleTypeId == null) {
            if (other.saleTypeId != null)
                return false;
        }
        else if (!saleTypeId.equals(other.saleTypeId))
            return false;
        if (statusId == null) {
            if (other.statusId != null)
                return false;
        }
        else if (!statusId.equals(other.statusId))
            return false;
        if (svDone == null) {
            if (other.svDone != null)
                return false;
        }
        else if (!svDone.equals(other.svDone))
            return false;
        if (timeFrameId == null) {
            if (other.timeFrameId != null)
                return false;
        }
        else if (!timeFrameId.equals(other.timeFrameId))
            return false;
        if (uploadTypeId == null) {
            if (other.uploadTypeId != null)
                return false;
        }
        else if (!uploadTypeId.equals(other.uploadTypeId))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "LeadData [leadId=" + leadId
                + ", meetingDone="
                + meetingDone
                + ", svDone="
                + svDone
                + ", closed="
                + closed
                + ", isPresalesVerified="
                + isPresalesVerified
                + ", clientTypeId="
                + clientTypeId
                + ", cityId="
                + cityId
                + ", saleTypeId="
                + saleTypeId
                + ", timeFrameId="
                + timeFrameId
                + ", isStar="
                + isStar
                + ", additionalEnquiryCount="
                + additionalEnquiryCount
                + ", statusId="
                + statusId
                + ", enquiryLevelProjectCount="
                + enquiryLevelProjectCount
                + ", projectLevelLead="
                + projectLevelLead
                + ", uploadTypeId="
                + uploadTypeId
                + ", numCallAtPresales="
                + numCallAtPresales
                + ", confCallVerified="
                + confCallVerified
                + ", bedrooms="
                + bedrooms
                + ", countryId="
                + countryId
                + ", budget="
                + budget
                + "]";
    }

    public Integer getEnquiryLevelProjectCount() {
        return enquiryLevelProjectCount;
    }

    public void setEnquiryLevelProjectCount(Integer enquiryLevelProjectCount) {
        this.enquiryLevelProjectCount = enquiryLevelProjectCount;
    }
}
