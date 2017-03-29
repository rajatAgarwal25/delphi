package com.proptiger.delphi.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.spark.mllib.regression.LabeledPoint;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.proptiger.delphi.config.DBConnectionConfig;
import com.proptiger.delphi.model.lead.LabeledPointFactory;
import com.proptiger.delphi.model.lead.LeadData;
import com.proptiger.delphi.model.lead.LeadDataContainer;
import com.proptiger.delphi.model.lead.LeadDataFactory;
import com.proptiger.delphi.model.master.ReasonStatusMappingContainer;
import com.proptiger.delphi.model.master.ReasonStatusMappingModel;
import com.proptiger.delphi.model.master.ReasonStatusMappingSerializer;
import com.proptiger.delphi.service.LeadService;
import com.proptiger.delphi.service.SerializedLeadInfoService;

@Service
public class LeadServiceImpl implements LeadService {

    private static final Integer      LEADS_MAX_PAGE_SIZE = 10;
    private static final Integer      LEADS_TO_FETCH      = LEADS_MAX_PAGE_SIZE * 2;

    @Autowired
    private SparkSession              sparkSession;

    @Autowired
    private SerializedLeadInfoService serializedLeadInfoService;

    @Async
    @Override
    public void fetchLeadsAndSerialize(int maxLeadId) {
        loadLeadsData(maxLeadId);
    }

    public Pair<Map<LabeledPoint, Integer>, List<LeadData>> getLeads() {
        Pair<Map<LabeledPoint, Integer>, List<LeadData>> pair = new Pair<>();
        LeadDataContainer leadDataContainer = serializedLeadInfoService.get(null);
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

    private static Map<Integer, ReasonStatusMappingModel> getReasonStatusMappingModelMap() {
        Map<Integer, ReasonStatusMappingModel> map = new HashMap<>();
        ReasonStatusMappingContainer container = ReasonStatusMappingSerializer.getReasonStatusMappingContainer();
        container.getModelList().forEach(model -> {
            map.put(model.getId(), model);
        });
        return map;
    }

    private void loadLeadsData(int leadIdToStart) {
        Properties connectionProperties = new Properties();
        connectionProperties.put("user", DBConnectionConfig.getInstance().getUserName());
        connectionProperties.put("password", DBConnectionConfig.getInstance().getPassword());
        connectionProperties.put("driver", "com.mysql.jdbc.Driver");

        Map<Integer, ReasonStatusMappingModel> rsmMap = getReasonStatusMappingModelMap();

        int maxLeadId = leadIdToStart;
        int minLeadId = leadIdToStart - LEADS_MAX_PAGE_SIZE;
        int countLeadsFetched = 0;
        while (countLeadsFetched < LEADS_TO_FETCH) {
            System.out.println(minLeadId + " - " + maxLeadId + " - " + LEADS_MAX_PAGE_SIZE);
            String dbQuery = getQuery(minLeadId, maxLeadId);
            System.out.println("Query is " + dbQuery);
            System.out.println("Current size = " + countLeadsFetched);
            Dataset<Row> jdbcDF = sparkSession.read().jdbc(
                    DBConnectionConfig.getInstance().getUrl(),
                    dbQuery,
                    connectionProperties);
            List<Row> modelRows = jdbcDF.collectAsList();
            LeadDataContainer tc = new LeadDataContainer();
            modelRows.forEach(row -> {
                tc.getLeadData().add(LeadDataFactory.newInstance(row, rsmMap));
            });

            maxLeadId -= LEADS_MAX_PAGE_SIZE;
            minLeadId -= LEADS_MAX_PAGE_SIZE;
            tc.setStartLeadId(minLeadId);
            tc.setEndLeadId(maxLeadId);
            countLeadsFetched += tc.getLeadData().size();
            System.out.println("Writing " + tc.getLeadData().size() + " models. MAX_LEADID is " + maxLeadId);
            serializedLeadInfoService.post(tc);
        }
    }

    private static final String getQuery(Integer minLeadId, Integer maxLeadId) {
        final String dbTable = "(select L.id, L.client_type_id, L.country_id, L.city_id, L.sale_type_id, " + "L.time_frame_id, group_concat(MS.star) as star, group_concat(E.id) as enquiryIds, group_concat(E.upload_type_id) as upload_types, "
                + "min(PR.min_budget) , max(PR.max_budget) as max_budget, group_concat(PR.bedroom) as bedrooms, group_concat(PR.project_id), "
                + "LA.status_id, group_concat(LAH.reason_status_mapping_id) as rsmIds, group_concat(ER.project_id) as enquiry_projects from leads L "
                + "join enquiries E on E.lead_id = L.id and L.id > %d and L.id < %d and "
                + "L.company_id = 499 join master_sources MS on MS.id = E.source_id join property_requirements "
                + "PR on PR.lead_id = L.id join lead_assignments LA on L.latest_lead_assigment = LA.id join lead_assignments "
                + "ALA on ALA.lead_id = L.id join lead_action_history LAH on LAH.lead_assign_id = ALA.id "
                + " join enquiry_requirements ER on ER.enquiry_id = E.id "
                + "group by L.id order by L.id desc limit %d) as leads";

        return String.format(dbTable, minLeadId, maxLeadId, LEADS_MAX_PAGE_SIZE);
    }

}
