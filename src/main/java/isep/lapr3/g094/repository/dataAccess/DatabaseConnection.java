package isep.lapr3.g094.repository.dataAccess;

import oracle.jdbc.pool.OracleDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;

public class DatabaseConnection {

    public static final int CONNECTION_SUCCESS = 1;
    public static final int CONNECTION_FAILURE = -1;

    private static DatabaseConnection instance;
    private OracleDataSource oracleDataSource;
    private Connection connection;

    public DatabaseConnection() throws SQLException {
        oracleDataSource = new OracleDataSource();
        oracleDataSource.setURL(url());
        connection = oracleDataSource.getConnection(user(), password());
        connection.setAutoCommit(false);
    }

    public Connection getConnection() {
        if (Objects.isNull(connection)) {
            throw new RuntimeException("Connection does not exit");
        }
        return connection;
    }

    public int testConnection() {
        if (Objects.isNull(connection)) {
            return CONNECTION_FAILURE;
        }
        return CONNECTION_SUCCESS;
    }

    public void closeConnection() throws SQLException {
        if (!connection.isClosed())
            connection.close();
    }

    private String url() {
        return System.getProperty("database.url");
    }

    private String user() {
        return System.getProperty("database.user");
    }

    private String password() {
        return System.getProperty("database.password");
    }

    public static DatabaseConnection getInstance() throws SQLException {
        if (Objects.isNull(instance))
            instance = new DatabaseConnection();
        return instance;
    }
}