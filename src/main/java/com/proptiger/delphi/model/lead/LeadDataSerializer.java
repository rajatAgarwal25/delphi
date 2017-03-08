package com.proptiger.delphi.model.lead;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import com.proptiger.delphi.leadscore.DBConnectionConfig;
import com.proptiger.delphi.model.master.ReasonStatusMappingContainer;
import com.proptiger.delphi.model.master.ReasonStatusMappingModel;
import com.proptiger.delphi.model.master.ReasonStatusMappingSerializer;
import com.proptiger.utils.PropertyUtil;

public class LeadDataSerializer {

    private static final SparkSession sparkSession               = SparkSession.builder().master("local")
                                                                         .appName("Spark2JdbcDs").getOrCreate();

    public static final String        LEADDATA_SERIALIZED_FOLDER = PropertyUtil
                                                                         .getPropertyValue("leads.model.serializedPath");

    private static final Integer      LEADS_MAX_PAGE_SIZE        = 10000;
    private static final Integer      LEADS_TO_FETCH             = LEADS_MAX_PAGE_SIZE * 10 * 10;
    private static final Integer      LEADS_ID_START             = 289638;                                                // 289638
    private static final Properties   connectionProperties       = new Properties();

    public static void main(String[] args) {

        try {
            connectionProperties.put("user", DBConnectionConfig.getInstance().getUserName());
            connectionProperties.put("password", DBConnectionConfig.getInstance().getPassword());

            LeadDataContainer container = new LeadDataContainer();
            loadLeadsData(container);

            System.out.println("Count of models being serialized is " + container.getLeadData().size());

            // FileOutputStream fileOut = new
            // FileOutputStream(LEADDATA_SERIALIZED_PATH);
            // ObjectOutputStream out = new ObjectOutputStream(fileOut);
            // out.writeObject(container);
            // out.close();
            // fileOut.close();
            System.out.println("Serialized data is saved in " + LEADDATA_SERIALIZED_FOLDER);
            System.out.println("Deserialized count = " + getLeadDataContainer().getLeadData().size());
        }
        catch (IOException i) {
            i.printStackTrace();
        }
        finally {
            sparkSession.close();
        }
    }

    private static Map<Integer, ReasonStatusMappingModel> getReasonStatusMappingModelMap() {
        Map<Integer, ReasonStatusMappingModel> map = new HashMap<>();
        ReasonStatusMappingContainer container = ReasonStatusMappingSerializer.getReasonStatusMappingContainer();
        container.getModelList().forEach(model -> {
            map.put(model.getId(), model);
        });
        return map;
    }

    public static LeadDataContainer getLeadDataContainer() {
        LeadDataContainer container = new LeadDataContainer();
        try {
            File folder = new File(LEADDATA_SERIALIZED_FOLDER);
            File[] serializedFiles = folder.listFiles();
            for (File modelFile : serializedFiles) {
                FileInputStream fileIn = new FileInputStream(modelFile);
                ObjectInputStream in = new ObjectInputStream(fileIn);
                LeadDataContainer tc = (LeadDataContainer) in.readObject();
                if (tc != null && tc.getLeadData() != null) {
                    container.getLeadData().addAll(tc.getLeadData());
                }
                in.close();
                fileIn.close();
            }

        }
        catch (IOException i) {
            i.printStackTrace();
        }
        catch (ClassNotFoundException c) {
            System.out.println("LeadDataContainer class not found");
            c.printStackTrace();
        }
        return container;
    }

    private static void loadLeadsData(LeadDataContainer container) throws IOException {

        Map<Integer, ReasonStatusMappingModel> rsmMap = getReasonStatusMappingModelMap();

        int maxLeadId = LEADS_ID_START;
        int minLeadId = LEADS_ID_START - LEADS_MAX_PAGE_SIZE;
        while (container.getLeadData().size() < LEADS_TO_FETCH) {
            System.out.println(minLeadId + " - " + maxLeadId + " - " + LEADS_MAX_PAGE_SIZE);
            String dbQuery = getQuery(minLeadId, maxLeadId);
            System.out.println("Query is " + dbQuery);
            System.out.println("Current size = " + container.getLeadData().size());
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

            System.out.println("Writing " + container.getLeadData().size() + " models. MAX_LEADID is " + maxLeadId);
            File file = new File(LEADDATA_SERIALIZED_FOLDER + "/" + minLeadId + "_" + maxLeadId + ".ser");
            file.createNewFile();
            FileOutputStream fileOut = new FileOutputStream(file);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(tc);
            out.close();
            fileOut.close();

            container.getLeadData().addAll(tc.getLeadData());
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
