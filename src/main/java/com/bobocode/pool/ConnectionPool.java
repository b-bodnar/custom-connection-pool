package com.bobocode.pool;

import com.bobocode.pool.exception.LoadPropertiesException;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ConnectionPool {
    private static final Logger logger =
            Logger.getLogger(ConnectionPool.class.getCanonicalName());

    private final Queue<PooledConnection> pool;
    private int initialPoolSize;
    private String url;
    private String username;
    private String password;
    private String driverClassName;

    public ConnectionPool(PoolConfig config) throws ClassNotFoundException, SQLException {
        this.pool = new ConcurrentLinkedDeque<>();
        this.initialPoolSize = config.getInitialPoolSize();
        this.url = config.getUrl();
        this.username = config.getUsername();
        this.password = config.getPassword();
        this.driverClassName = config.getDriverClassName();
        validateInitialPoolSize(initialPoolSize);
        initPooledConnections(driverClassName);
    }

    public ConnectionPool() throws SQLException, ClassNotFoundException {
        this.pool = new ConcurrentLinkedDeque<>();
        parsePropertiesToInitializePoolConnection();
        initPooledConnections(driverClassName);
    }

    private void initPooledConnections(String driverClassName) throws ClassNotFoundException, SQLException {
        Class.forName(driverClassName);
        for (int i = 0; i < initialPoolSize; i++) {
            openPoolConnection();
        }
        logger.log(Level.INFO, "Created pool connection, poolSize={0}",  new Object[]{initialPoolSize});
    }

    private void validateInitialPoolSize(int initialPoolSize) {
        if (initialPoolSize < 1) {
            throw new IllegalArgumentException("Invalid pool size parameters");
        }
    }

    private void parsePropertiesToInitializePoolConnection() {
        var properties = loadProperties();
        this.url = properties.getProperty("url");
        this.username = properties.getProperty("username");
        this.password = properties.getProperty("password");
        this.initialPoolSize = Integer.parseInt(properties.getProperty("initialPoolSize"));
        this.driverClassName = properties.getProperty("driverClassName");
        validateInitialPoolSize(initialPoolSize);
    }

    private Properties loadProperties() {
        var properties = new Properties();
        var propFileInputStream = PooledConnection.class.getClassLoader()
                .getResourceAsStream("application.properties");
        try {
            properties.load(propFileInputStream);
        } catch (IOException e) {
            throw new LoadPropertiesException("Load properties file error!", e);
        }
        return properties;
    }

    private synchronized void openPoolConnection() throws SQLException {
        Connection physicalConnection = DriverManager.getConnection(url, username, password);
        pool.offer(new PooledConnection(physicalConnection, this));
    }

    public Connection getConnection() {
        return pool.poll();
    }

    public void surrenderConnection(PooledConnection conn) {
        pool.offer(conn);
    }
}
