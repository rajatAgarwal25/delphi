package com.proptiger.delphi.service;

import org.springframework.data.mongodb.core.query.Query;

import com.proptiger.delphi.model.lead.LeadDataContainer;

public interface SerializedLeadInfoService {

    String serialize(LeadDataContainer leadDataContainer);

    LeadDataContainer get(Query query);

}
