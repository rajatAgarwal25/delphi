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

import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.mllib.tree.model.DecisionTreeModel;
import org.apache.spark.sql.SparkSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    private static Logger       LOGGER     = LoggerFactory.getLogger(GridFSSerializedLeadInfoServiceImpl.class);

    @Autowired
    private GridFsTemplate      gridFSTemplate;

    @Autowired
    private SparkSession        sparkSession;

    @Autowired
    private SparkContext        sparkContext;

    @Value("${leads.model.serializedPath}")
    public String               LEADDATA_SERIALIZED_FOLDER;

    @Value("${ml.model.serializedPath}")
    public String               MODELS_SERIALIZED_FOLDER;

    private static final String OUTPUT     = "output";
    private static final String COUNT      = "count";
    private static final String TIME_STAMP = "time_stamp";

    @Override
    public String serialize(LeadDataContainer leadDataContainer) {
        if (leadDataContainer == null || leadDataContainer.getLeadData() == null
                || leadDataContainer.getLeadData().size() == 0) {
            LOGGER.debug("No leads to serialize.");
            return null;
        }
        LOGGER.debug("Saving {} leads.", leadDataContainer.getLeadData().size());
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

            InputStream inputStream = new FileInputStream(file);
            return gridFSTemplate.store(inputStream, file.getName(), "application/octet-stream").getId().toString();
        }
        catch (Exception e) {
            LOGGER.error("Exception while saving file in mongo", e);

        }
        return null;
    }

    @Override
    public LeadDataContainer getLeadDataContainer(Query query) {
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
            String path = MODELS_SERIALIZED_FOLDER + currentTime + ".ser";
            File file = new File(path);
            // XXX Bug
            JavaSparkContext jsc = new JavaSparkContext(sparkSession.sparkContext());
            decisionTreeModel.save(jsc.sc(), path);

            DBObject metaData = new BasicDBObject();
            metaData.put(TIME_STAMP, currentTime);

            InputStream inputStream = new FileInputStream(file);
            return gridFSTemplate.store(inputStream, file.getName(), "application/octet-stream").getId().toString();
        }
        catch (Exception e) {
            LOGGER.error("Exception while saving file in mongo", e);

        }
        return null;
    }

    @Override
    public DecisionTreeModel getModel(Query query) {
        try {
            GridFSDBFile file = gridFSTemplate.findOne(query);

            String currentTime = String.valueOf(System.currentTimeMillis());
            String path = MODELS_SERIALIZED_FOLDER + OUTPUT + currentTime + ".ser";
            File f = new File(path);

            FileOutputStream faos = new FileOutputStream(f);
            file.writeTo(faos);
            faos.close();

            return DecisionTreeModel.load(sparkContext, path);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
