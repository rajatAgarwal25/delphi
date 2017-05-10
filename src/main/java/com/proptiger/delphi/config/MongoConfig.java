package com.proptiger.delphi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.WriteConcern;

@Configuration
public class MongoConfig extends AbstractMongoConfiguration {

    @Value("${mongo.db.uri}")
    private String  uri;

    @Value("${mongo.db.name}")
    private String  database;

    @Value("${mongo.db.auth.name}")
    private String  authDatabase;    // NOPMD

    @Value("${mongo.db.user}")
    private String  username;        // NOPMD

    @Value("${mongo.db.pass}")
    private String  password;        // NOPMD

    @Value("${mongo.write.concern}")
    private Integer writeConcern = 1;

    @Override
    protected String getDatabaseName() {
        return this.database;
    }

    @Override
    public Mongo mongo() throws Exception {
        return new MongoClient(new MongoClientURI(uri));
    }

    @Override
    public @Bean MongoDbFactory mongoDbFactory() throws Exception {
        return new SimpleMongoDbFactory(new MongoClientURI(uri));
    }

    @Override
    public @Bean MongoTemplate mongoTemplate() throws Exception {
        MongoTemplate mongoTemplate = new MongoTemplate(mongoDbFactory());
        mongoTemplate.setWriteConcern(new WriteConcern(writeConcern));
        return mongoTemplate;
    }

    @Bean
    public GridFsTemplate gridFsTemplate() throws Exception {
        return new GridFsTemplate(mongoDbFactory(), mappingMongoConverter());
    }

}