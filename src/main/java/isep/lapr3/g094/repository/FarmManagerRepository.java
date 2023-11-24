package isep.lapr3.g094.repository;

import java.io.IOException;
import java.io.InputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.Time;
import java.util.Properties;
import java.sql.SQLException;

public class FarmManagerRepository {

	public boolean registerPoda(int quantidade, int parcelaID, int plantacaoID, Date dataOperacao) throws SQLException, ClassNotFoundException {

		CallableStatement callStmt = null;
		try {
        	Connection connection = createConnection();
			callStmt = connection.prepareCall("{ call registerPoda(?,?,?,?) }");
			callStmt.setInt(1, plantacaoID);
			callStmt.setInt(2, parcelaID);
			callStmt.setDate(3, dataOperacao);
			callStmt.setInt(4, quantidade);
			return callStmt.execute();
		} catch (SQLException e) {
			throw e;
		} finally {
			if (callStmt != null) {
				callStmt.close();
			}
		}
	}

	public boolean registerFatorDeProducao(int quantidade, int parcelaID, int plantacaoID, Date dataOperacao) throws ClassNotFoundException, SQLException {
        	Connection connection = createConnection();

		return false;
	}

	public boolean registerColheita(int quantidade, int parcelaID, int plantacaoID, Date dataOperacao) throws SQLException, ClassNotFoundException{
		CallableStatement callStmt = null;
		try {
        	Connection connection = createConnection();
			callStmt = connection.prepareCall("{ call registerColheita(?,?,?,?) }");
			callStmt.setInt(1, plantacaoID);
			callStmt.setInt(2, parcelaID);
			callStmt.setDate(3, dataOperacao);
			callStmt.setInt(4, quantidade);
			return callStmt.execute();
		} catch (SQLException e) {
			throw e;
		} finally {
			if (callStmt != null) {
				callStmt.close();
			}
		}
	}

	public boolean registerSemeadura(int quantidade, int parcelaID, int culturaID, Date dataOperacao, int area) throws ClassNotFoundException, SQLException {
		CallableStatement callStmt = null;
        try {
        	Connection connection = createConnection();
			callStmt = connection.prepareCall("{ call registerSemeadura(?,?,?,?,?) }");
			callStmt.setInt(1, culturaID);
			callStmt.setInt(2, parcelaID);
			callStmt.setDate(3, dataOperacao);
			callStmt.setInt(4, quantidade);
			callStmt.setInt(5, area);
			return callStmt.execute();
		} catch (SQLException e) {
			throw e;
		} finally {
			if (callStmt != null) {
				callStmt.close();
			}
		}
	}

	public boolean registerMonda(int quantidade, int parcelaID, int plantacaoID, Date dataOperacao)
			throws SQLException, ClassNotFoundException {

		CallableStatement callStmt = null;
		try {
        	Connection connection = createConnection();
			callStmt = connection.prepareCall("{ call registerMonda(?,?,?,?) }");
			callStmt.setInt(1, plantacaoID);
			callStmt.setInt(2, parcelaID);
			callStmt.setDate(3, dataOperacao);
			callStmt.setInt(4, quantidade);
			return callStmt.execute();
		} catch (SQLException e) {
			throw e;
		} finally {
			if (callStmt != null) {
				callStmt.close();
			}
		}
	}

	public boolean registerRega(int quantidade, int setorID, Date dataOperacao, Time hora)
			throws SQLException, ClassNotFoundException {

		CallableStatement callStmt = null;
		try {
        	Connection connection = createConnection();
			callStmt = connection.prepareCall("{ call registerRega(?,?,?,?) }");
			callStmt.setInt(1, quantidade);
			callStmt.setInt(2, setorID);
			callStmt.setDate(3, dataOperacao);
			callStmt.setTime(4, hora);
			return callStmt.execute();
		} catch (SQLException e) {
			throw e;
		} finally {
			if (callStmt != null) {
				callStmt.close();
			}
		}
	}

	private Connection createConnection() throws ClassNotFoundException, SQLException {
		Properties properties = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("bddad/config.properties")) {
            properties.load(input);
        } catch (IOException e) {
			e.printStackTrace();
		}

        // Load the Oracle JDBC driver
        Class.forName("oracle.jdbc.driver.OracleDriver");

        // Connect to the database
        return DriverManager.getConnection(properties.getProperty("database.url"),
                properties.getProperty("database.user"), properties.getProperty("database.password"));
    }

}
