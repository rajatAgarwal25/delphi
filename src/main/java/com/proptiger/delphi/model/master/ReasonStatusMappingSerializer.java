package com.proptiger.delphi.model.master;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.Properties;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import com.proptiger.delphi.leadscore.DBConnectionConfig;
import com.proptiger.utils.PropertyUtil;

public class ReasonStatusMappingSerializer {

    private static final SparkSession sparkSession                      = SparkSession.builder().master("local")
                                                                                .appName("Spark2JdbcDs").getOrCreate();

    public static final String        RSM_MODELS_SERIALIZED_MODELS_PATH = PropertyUtil
                                                                                .getPropertyValue("rsm.model.serializedPath");

    public static void main(String[] args) {
        final Properties connectionProperties = new Properties();
        connectionProperties.put("user", DBConnectionConfig.getInstance().getUserName());
        connectionProperties.put("password", DBConnectionConfig.getInstance().getPassword());

        final String dbTable = "(select RSM.id, MLAS.is_presales_status, MLAS.is_sales_status, " + "MLAS.is_homeloan_status, MR.id as commentId, MR.reason from reason_status_mapping RSM "
                + "join master_reasons MR on RSM.reason_id = MR.id join master_lead_assign_status "
                + "MLAS on MLAS.id = RSM.current_status_id) as rsmmodels";

        // Load MySQL query result as Dataset
        Dataset<Row> jdbcDF = sparkSession.read().jdbc(
                DBConnectionConfig.getInstance().getUrl(),
                dbTable,
                connectionProperties);

        ReasonStatusMappingContainer container = new ReasonStatusMappingContainer();
        List<Row> modelRows = jdbcDF.collectAsList();
        modelRows.forEach(row -> {
            container.getModelList().add(ReasonStatusMappingModelFactory.newInstance(row));
        });

        System.out.println("Count of models being serialized is " + container.getModelList().size());

        try {
            FileOutputStream fileOut = new FileOutputStream(RSM_MODELS_SERIALIZED_MODELS_PATH);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(container);
            out.close();
            fileOut.close();
            System.out.println("Serialized data is saved in " + RSM_MODELS_SERIALIZED_MODELS_PATH);
        }
        catch (IOException i) {
            i.printStackTrace();
        }

        // System.out.println("Deserialized count = " +
        // getReasonStatusMappingContainer().getModelList().size());

        sparkSession.close();
    }

    public static ReasonStatusMappingContainer getReasonStatusMappingContainer() {
        ReasonStatusMappingContainer container = null;
        try {
            FileInputStream fileIn = new FileInputStream(RSM_MODELS_SERIALIZED_MODELS_PATH);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            container = (ReasonStatusMappingContainer) in.readObject();
            in.close();
            fileIn.close();
        }
        catch (IOException i) {
            i.printStackTrace();
        }
        catch (ClassNotFoundException c) {
            System.out.println("ReasonStatusMappingContainer class not found");
            c.printStackTrace();
        }
        return container;
    }

}
