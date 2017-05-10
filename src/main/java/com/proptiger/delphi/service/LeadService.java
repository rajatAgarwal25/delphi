package com.proptiger.delphi.service;

import java.util.List;
import java.util.Map;

import org.apache.spark.mllib.regression.LabeledPoint;

import com.proptiger.delphi.model.lead.LeadData;
import com.proptiger.delphi.service.impl.Pair;

public interface LeadService {

    void fetchLeadsAndSerialize(int maxLeadId);

    Pair<Map<LabeledPoint, Integer>, List<LeadData>> getLeads();
}
