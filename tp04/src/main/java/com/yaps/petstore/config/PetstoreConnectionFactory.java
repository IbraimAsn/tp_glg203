package com.yaps.petstore.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


/**
 * This singleton class gives access to all the information needed
 * to connect to the database.
 * 
 * It has default values.
 * Some of them can be overwritten by inline application properties.
 * 
 * Possible properties are :
 * 
 * <ul>
 * <li>dbhost : the database host.
 * </ul>
 */

public class PetstoreConnectionFactory {

    private static final PetstoreConnectionFactory instance = new PetstoreConnectionFactory();

    // ======================================
    // = Static Block =
    // ======================================
    static {
        // Loads the JDBC driver class
        try {
            Class.forName(getInstance().getJDBCDriverName());
        } catch (ClassNotFoundException e) {           
            // no reason to continue :
            throw new RuntimeException(e);
        }
    }

    public static PetstoreConnectionFactory getInstance() {
        return instance;
    }

    /**
     * Private contructor/Singleton.
     */
    private PetstoreConnectionFactory() {
        // NOTHING
    }

    
    /**
     * Default JDBC Driver class to instanciate.
     */
    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";

    /**
     * Default Database host.
     */
    private static final String DB_HOST = "localhost";

    /**
     * Default Database port.
     */
    private static final String DB_PORT = "3306";

    /**
     * Default test database port
     */
    private static final String DB_TEST_PORT = "60307";

    /**
     * Default Database name.
     */
    private static final String DB_NAME = "petstore03";

    /**
     * Default database prefix.
     */
    private static final String URL_PREFIX = "jdbc:mysql://";

    /**
     * Default database suffix.
     */
    private static final String URL_SUFFIX = "?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";

    /**
     * Default Username to access the database.
     */
    private static final String DB_USER = "root";

    /**
     * Default Password to access the database.
     */
    private static final String DB_PASSWORD = "glg203";

    /**
     * Returns the url to connect to the database.
     * <p>
     * The url is built from various properties :
     * <ul>
     * <li>The prefix
     * <li>the host
     * <li>the port
     * <li>the database name
     * <li>the suffix
     * </ul>
     */
    public String getURL() {
        String url = getUrlPrefix() + getDBHost() + ":" + getPort() + "/" + getDBName() + getSuffix();
        return url;
    }

    /**
     * Creates the URL for accessing the test database.
     * <p>Note that this part of the job would belong in the test folder, not in main.
     * But as of today, it's just easier.
     * @see #getURL()
     * @return an url (as a string).
     */
    public String getTestURL() {
        return getUrlPrefix() + getDBHost() + ":" + getTestPort() + "/" + getDBName() + getSuffix();        
    }

    private String getSuffix() {
        return propertyOrDefault("urlsuffix", URL_SUFFIX);
    }

    public String getPort() {
        return propertyOrDefault("dbport", DB_PORT);
    }

    public String getTestPort() {
        return propertyOrDefault("testdbport", DB_TEST_PORT);
    }


    public String getDBName() {
        return propertyOrDefault("dbname", DB_NAME);
    }

    public String getUrlPrefix() {
        return propertyOrDefault("urlprefix", URL_PREFIX);
    }

    public String getJDBCDriverName() {
        return propertyOrDefault("jdbcdriver", JDBC_DRIVER);
    }

    public String getUser() {
        return propertyOrDefault("dbuser", DB_USER);
    }

    public String getPassword() {
        return propertyOrDefault("dbpassword", DB_PASSWORD);
    }

    public String getDBHost() {
        return propertyOrDefault("dbhost", DB_HOST);
    }

    private String propertyOrDefault(String propertyName, String defaultValue) {
        String value = System.getProperty(propertyName);
        return value != null ? value : defaultValue;
    }

    /**
     * Creates and return a new Connection to the database.
     * @return
     * @throws SQLException
     */
    public Connection createConnection() throws SQLException {
        return DriverManager.getConnection(
            getURL(),
            getUser(),
            getPassword());        
    }

    /**
     * Creates and return a new Connection to the test database.
     * <p> The test database default port is supposed to be 60307
     * @return a connection to the test database.
     * @throws SQLException
     */
    public Connection createTestConnection() throws SQLException {
        return DriverManager.getConnection(
            getTestURL(),
            getUser(),
            getPassword());        
    }


}
