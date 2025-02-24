package org.example.utils;

import org.apache.commons.dbcp2.BasicDataSource;

import javax.sql.DataSource;

public class ConnectionPoolManager {

    private static final String URL_KEY = "db.url";
    private static final String USER_KEY = "db.user";
    private static final String PASSWORD_KEY = "db.password";

    private static final BasicDataSource ds = new BasicDataSource();



    static {
        ds.setUrl(PropertiesUtil.get(URL_KEY));
        ds.setUsername( PropertiesUtil.get(USER_KEY));
        ds.setPassword(PropertiesUtil.get(PASSWORD_KEY));
        ds.setMinIdle(5);   // Минимальное количество соединений в пуле
        ds.setMaxIdle(10);  // Максимальное количество "простаивающих" соединений
        ds.setMaxOpenPreparedStatements(100); // Максимальное количество открытых подготовленных запросов
    }

    public static DataSource getDataSource() {
        return ds;
    }
}
