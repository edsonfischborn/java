package handlelibrary.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author Edson
 */
public class Db {
    private boolean connectionStatus;
    private String errorMessage;
    private Connection connection;
    private String driverName;
    private String serverName;
    private String serverPort;
    private String userName;
    private String password;
    private String dbName;
    private String connectionUrl;

    public Db(String serverName, String serverPort, String userName, String password, String dbName) {
        connectionStatus = false;
        errorMessage = "";
        driverName = "com.mysql.jdbc.Driver";

        this.serverName = serverName;
        this.serverPort = serverPort;
        this.userName = userName;
        this.password = password;
        this.dbName = dbName;

        this.connectionUrl = "jdbc:mysql://" + this.serverName + ":" + this.serverPort + "/" + this.dbName + "?autoReconnect=true&useUnicode=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getServerPort() {
        return serverPort;
    }

    public void setServerPort(String serverPort) {
        this.serverPort = serverPort;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public boolean getConnectionStatus() {
        return connectionStatus;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public Connection getConnection() {
        return connection;
    }

    public void connect() {
        try {
            Class.forName(this.driverName);
            this.connection = DriverManager.getConnection(this.connectionUrl, this.userName, this.password);
            connectionStatus = true;
        } catch (ClassNotFoundException e) {
            this.errorMessage = "Driver not found " + e.toString();
        } catch (SQLException e) {
            this.connectionStatus = false;
            this.errorMessage = "Connection error " + e.toString();
        }
    }

    public void closeConnect() {
        try {
            this.connection.close();
            this.connectionStatus = false;
        } catch (SQLException e) {
            this.errorMessage = "Disconnect error " + e.toString();
        }
    }


}
