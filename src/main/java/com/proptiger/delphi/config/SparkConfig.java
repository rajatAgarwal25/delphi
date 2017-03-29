package com.proptiger.delphi.config;

import org.apache.log4j.Logger;
import org.apache.spark.sql.SparkSession;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SparkConfig {
    private static final Logger LOGGER = Logger.getLogger(SparkConfig.class);

    @Bean
    public SparkSession sparkSession() {
        LOGGER.info("Creating SparkContext.  Master= local");
        return SparkSession.builder().master("local").appName("Spark2JdbcDs").getOrCreate();
    }
}