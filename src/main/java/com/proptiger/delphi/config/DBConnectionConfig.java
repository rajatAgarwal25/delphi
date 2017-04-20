package com.proptiger.delphi.config;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DBConnectionConfig {

    @Value("${mysql.connection.url}")
    private String url;

    @Value("${mysql.connection.user}")
    private String userName;

    @Value("${mysql.connection.password}")
    private String password;
    private String driver = "com.mysql.jdbc.Driver";

    public Properties getConnectionProperties() {
        Properties connectionProperties = new Properties();
        connectionProperties.put("user", userName);
        connectionProperties.put("password", password);
        connectionProperties.put("driver", "com.mysql.jdbc.Driver");

        return connectionProperties;
    }

    public String getUrl() {
        return url;
    }

    public String getDriver() {
        return driver;
    }

    @Override
    public String toString() {
        return "DBConnectionConfig [url=" + url + ", userName=" + userName + ", password=" + password + "]";
    }
}
