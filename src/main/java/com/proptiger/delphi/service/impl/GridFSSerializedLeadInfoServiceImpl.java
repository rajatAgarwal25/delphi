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

    @Autowired
    private GridFsTemplate      gridFSTemplate;

    @Value("${leads.model.serializedPath}")
    public String               LEADDATA_SERIALIZED_FOLDER;

    private static final String COUNT = "count";

    @Override
    public String post(LeadDataContainer leadDataContainer) {
        if (leadDataContainer == null || leadDataContainer.getLeadData() == null
                || leadDataContainer.getLeadData().size() == 0) {
            System.out.println("No leads to serialize.");
            return null;
        }
        System.out.println("Saving " + leadDataContainer.getLeadData().size() + " leads.");
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
            System.err.println("Exception while saving file in mongo" + e.getMessage());

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
                System.err.println("Exception while deserializing gridfs file" + e.getMessage());
            }
        }

        LeadDataContainer ldc = new LeadDataContainer();
        ldc.setLeadData(list);
        return ldc;
    }
}
