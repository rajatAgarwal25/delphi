package com.proptiger.delphi.service;

import com.proptiger.delphi.dto.LeadScoreDTO;
import com.proptiger.delphi.model.lead.LeadData;

public interface ModelService {

    void trainModel();

    LeadScoreDTO getLeadScore(LeadData leadData);
}