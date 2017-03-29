package com.proptiger.delphi.model.lead;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class LeadDataContainer implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 4112069459303422121L;

    private Integer           startLeadId;
    private Integer           endLeadId;

    private List<LeadData>    leadData         = new ArrayList<LeadData>();

    public List<LeadData> getLeadData() {
        return leadData;
    }

    public void setLeadData(List<LeadData> leadData) {
        this.leadData = leadData;
    }

    public Integer getStartLeadId() {
        return startLeadId;
    }

    public void setStartLeadId(Integer startLeadId) {
        this.startLeadId = startLeadId;
    }

    public Integer getEndLeadId() {
        return endLeadId;
    }

    public void setEndLeadId(Integer endLeadId) {
        this.endLeadId = endLeadId;
    }
}
