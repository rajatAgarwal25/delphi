package com.proptiger.delphi.model.master;

import java.util.List;
import java.util.Properties;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proptiger.delphi.config.DBConnectionConfig;

@Service
public class ReasonStatusMappingService {

    @Autowired
    private SparkSession sparkSession;

    public ReasonStatusMappingContainer getReasonStatusMappingContainer() {
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
        return container;
    }

}
