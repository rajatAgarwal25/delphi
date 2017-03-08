package com.proptiger.delphi.leadscore;

import com.proptiger.utils.PropertyUtil;

public class DBConnectionConfig {

    private static final String       URL_KEY      = "mysql.connection.url";
    private static final String       USER_KEY     = "mysql.connection.user";
    private static final String       PASSWORD_KEY = "mysql.connection.password";

    private String                    url;
    private String                    userName;
    private String                    password;

    private static DBConnectionConfig _instance    = new DBConnectionConfig();

    private DBConnectionConfig() {
        this.url = PropertyUtil.getPropertyValue(URL_KEY);
        this.userName = PropertyUtil.getPropertyValue(USER_KEY);
        this.password = PropertyUtil.getPropertyValue(PASSWORD_KEY);
    }

    public static DBConnectionConfig getInstance() {
        return _instance;
    }

    public String getUrl() {
        return url;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "DBConnectionConfig [url=" + url + ", userName=" + userName + ", password=" + password + "]";
    }
}
