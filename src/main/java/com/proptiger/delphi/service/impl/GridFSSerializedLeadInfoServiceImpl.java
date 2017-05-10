package com.proptiger.delphi.service.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.spark.mllib.tree.model.DecisionTreeModel;
import org.apache.spark.sql.SparkSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.gridfs.GridFSDBFile;
import com.proptiger.delphi.model.lead.LeadData;
import com.proptiger.delphi.model.lead.LeadDataContainer;
import com.proptiger.delphi.service.SerializationService;

@Service
public class GridFSSerializedLeadInfoServiceImpl implements SerializationService {

    private static Logger       LOGGER              = LoggerFactory
                                                            .getLogger(GridFSSerializedLeadInfoServiceImpl.class);

    @Autowired
    private GridFsTemplate      gridFSTemplate;

    @Autowired
    private SparkSession        sparkSession;

    @Value("${leads.model.serializedPath}")
    public String               LEADDATA_SERIALIZED_FOLDER;

    @Value("${ml.model.serializedPath}")
    public String               MODELS_SERIALIZED_FOLDER;

    private static final String COUNT               = "count";
    private static final String TIME_STAMP          = "time_stamp";

    private static final String SERIALIZED_TYPE_STR = "SERIALIZED_TYPE";
    private DecisionTreeModel   decisionTreeModel;

    @Override
    public String serialize(LeadDataContainer leadDataContainer) {
        if (leadDataContainer == null || leadDataContainer.getLeadData() == null
                || leadDataContainer.getLeadData().size() == 0) {
            System.out.println("No leads to serialize.");
            return null;
        }
        System.out.println("Saving leads count " + leadDataContainer.getLeadData().size());
        try {
            File file = new File(LEADDATA_SERIALIZED_FOLDER + ".ser");
            file.createNewFile();
            FileOutputStream fileOut = new FileOutputStream(file);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(leadDataContainer);
            out.close();
            fileOut.close();

            DBObject metaData = new BasicDBObject();
            metaData.put(COUNT, leadDataContainer.getLeadData().size());
            metaData.put(SERIALIZED_TYPE_STR, SERIALIZED_TYPE.LEADS.name());

            InputStream inputStream = new FileInputStream(file);
            return gridFSTemplate.store(inputStream, file.getName(), "application/octet-stream", metaData).getId()
                    .toString();
        }
        catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("Exception while saving file in mongo", e);

        }
        return null;
    }

    @Override
    public LeadDataContainer getLeadDataContainer(Query query) {
        if (query == null) {
            query = getStarQueryForType(SERIALIZED_TYPE.LEADS);
        }
        List<GridFSDBFile> files = gridFSTemplate.find(query);

        List<LeadData> list = new ArrayList<>();
        for (GridFSDBFile file : files) {
            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                file.writeTo(baos);

                ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
                ObjectInputStream in = new ObjectInputStream(bais);
                LeadDataContainer tc = (LeadDataContainer) in.readObject();
                if (tc != null && tc.getLeadData() != null) {
                    list.addAll(tc.getLeadData());
                }
                in.close();
                bais.close();
                baos.close();
            }
            catch (Exception e) {
                e.printStackTrace();
                LOGGER.error("Exception while deserializing gridfs file", e);
            }
        }

        LeadDataContainer ldc = new LeadDataContainer();
        ldc.setLeadData(list);
        return ldc;
    }

    @Override
    public String serialize(DecisionTreeModel decisionTreeModel) {
        try {
            String currentTime = String.valueOf(System.currentTimeMillis());

            File file = new File(MODELS_SERIALIZED_FOLDER + File.pathSeparator + currentTime + ".ser");
            file.createNewFile();
            FileOutputStream fileOut = new FileOutputStream(file);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(decisionTreeModel);
            out.close();
            fileOut.close();

            DBObject metaData = new BasicDBObject();
            metaData.put(TIME_STAMP, currentTime);
            metaData.put(SERIALIZED_TYPE_STR, SERIALIZED_TYPE.MODELS.name());

            InputStream inputStream = new FileInputStream(file.getAbsolutePath());
            return gridFSTemplate.store(inputStream, file.getName(), "application/octet-stream", metaData).getId()
                    .toString();
        }
        catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("Exception while saving file in mongo", e);
        }
        return "";
    }

    @Override
    public synchronized DecisionTreeModel getModel(Query query) {
        if (decisionTreeModel != null) {
            return decisionTreeModel;
        }
        try {
            if (query == null) {
                query = getStarQueryForType(SERIALIZED_TYPE.MODELS);
            }
            List<GridFSDBFile> files = gridFSTemplate.find(query);
            for (GridFSDBFile file : files) {
                try {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    file.writeTo(baos);

                    ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
                    ObjectInputStream in = new ObjectInputStream(bais);
                    DecisionTreeModel decisionTreeModel = (DecisionTreeModel) in.readObject();
                    in.close();
                    bais.close();
                    baos.close();

                    if (decisionTreeModel != null) {
                        this.decisionTreeModel = decisionTreeModel;
                        return decisionTreeModel;
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return decisionTreeModel;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private Query getStarQueryForType(SERIALIZED_TYPE type) {
        return new Query(Criteria.where("metadata." + SERIALIZED_TYPE_STR).is(type));
    }
}

enum SERIALIZED_TYPE {
    LEADS,
    MODELS
}
