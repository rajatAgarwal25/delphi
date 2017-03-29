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
import com.proptiger.delphi.service.SerializedLeadInfoService;

@Service
public class GridFSSerializedLeadInfoServiceImpl implements SerializedLeadInfoService {

    private static Logger       LOGGER = LoggerFactory.getLogger(GridFSSerializedLeadInfoServiceImpl.class);

    @Autowired
    private GridFsTemplate      gridFSTemplate;

    @Value("${leads.model.serializedPath}")
    public String               LEADDATA_SERIALIZED_FOLDER;

    private static final String COUNT  = "count";

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
    public LeadDataContainer get(Query query) {
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
}
