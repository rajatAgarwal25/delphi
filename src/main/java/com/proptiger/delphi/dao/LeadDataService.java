package com.proptiger.delphi.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.spark.mllib.regression.LabeledPoint;

import com.proptiger.delphi.model.lead.LabeledPointFactory;
import com.proptiger.delphi.model.lead.LeadData;
import com.proptiger.delphi.model.lead.LeadDataContainer;
import com.proptiger.delphi.model.lead.LeadDataSerializer;

public class LeadDataService {

    private static LeadDataService _instance = new LeadDataService();

    private LeadDataService() {
    }

    public static LeadDataService getInstance() {
        return _instance;
    }

    public Pair<Map<LabeledPoint, Integer>, List<LeadData>> getLeads() {
        Pair<Map<LabeledPoint, Integer>, List<LeadData>> pair = new Pair<>();
        LeadDataContainer leadDataContainer = LeadDataSerializer.getLeadDataContainer();
        processLeadData(leadDataContainer);

        Map<LabeledPoint, Integer> map = new HashMap<LabeledPoint, Integer>();
        leadDataContainer.getLeadData().forEach(leadData -> {
            map.put(LabeledPointFactory.newInstance(leadData), leadData.getLeadId());
        });

        pair.setFirst(map);
        pair.setSecond(leadDataContainer.getLeadData());
        return pair;
    }

    private void processLeadData(LeadDataContainer leadDataContainer) {
        List<LeadData> presalesVerifiedLead = new ArrayList<>();
        System.out.println("Total leads deserialized = " + leadDataContainer.getLeadData().size());
        for (LeadData l : leadDataContainer.getLeadData()) {
            if (l != null && Boolean.TRUE.equals(l.getIsPresalesVerified())
                    && !Integer.valueOf(3).equals(l.getSaleTypeId())) {
                presalesVerifiedLead.add(l);
            }
        }
        leadDataContainer.setLeadData(presalesVerifiedLead);
        System.out.println("Training on leads count = " + presalesVerifiedLead.size());
    }
}
