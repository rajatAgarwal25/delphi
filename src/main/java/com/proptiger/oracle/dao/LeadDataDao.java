package com.proptiger.oracle.dao;

import java.util.List;
import java.util.Properties;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import com.proptiger.oracle.DBConnectionConfig;
import com.proptiger.oracle.LeadTrainer;

public class LeadDataDao {

    private final Properties   connectionProperties = new Properties();

    private static LeadDataDao _instance            = new LeadDataDao();

    private LeadDataDao() {
        connectionProperties.put("user", DBConnectionConfig.getInstance().getUserName());
        connectionProperties.put("password", DBConnectionConfig.getInstance().getPassword());
    }

    public static LeadDataDao getInstance() {
        return _instance;
    }

    public List<Row> getLeads(String query) {
        System.out.println(DBConnectionConfig.getInstance());
        System.out.println(query);

        Dataset<Row> jdbcDF = LeadTrainer.sparkSession.read().jdbc(
                DBConnectionConfig.getInstance().getUrl(),
                query,
                connectionProperties);

        return jdbcDF.collectAsList();
    }

}
