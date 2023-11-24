package isep.lapr3.g094.repository;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.Time;

import isep.lapr3.g094.repository.dataAccess.DatabaseConnection;

import java.sql.SQLException;

public class FarmManagerRepository {

	public boolean registerPoda(int quantidade, int parcelaID, int plantacaoID, Date dataOperacao)
			throws SQLException {

		CallableStatement callStmt = null;
		try {
			Connection connection = DatabaseConnection.getInstance().getConnection();
			callStmt = connection.prepareCall("{ call registerPoda(?,?,?,?) }");
			callStmt.setInt(1, quantidade);
			callStmt.setInt(2, parcelaID);
			callStmt.setInt(3, plantacaoID);
			callStmt.setDate(4, dataOperacao);
			return callStmt.execute();
		} finally {
			if (callStmt != null) {
				callStmt.close();
			}
		}
	}

	public boolean registerFatorDeProducao(int quantidade, int parcelaID, int plantacaoID, Date dataOperacao)
			throws SQLException {
		Connection connection = DatabaseConnection.getInstance().getConnection();

		return false;
	}

	public boolean registerColheita(int quantidade, int parcelaID, int plantacaoID, Date dataOperacao)
			throws SQLException {
		CallableStatement callStmt = null;
		try {
			Connection connection = DatabaseConnection.getInstance().getConnection();
			callStmt = connection.prepareCall("{ call registerColheita(?,?,?,?) }");
			callStmt.setInt(1, plantacaoID);
			callStmt.setInt(2, parcelaID);
			callStmt.setDate(3, dataOperacao);
			callStmt.setInt(4, quantidade);
			return callStmt.execute();
		} finally {
			if (callStmt != null) {
				callStmt.close();
			}
		}
	}

	public boolean registerSemeadura(int quantidade, int parcelaID, int culturaID, Date dataOperacao, double area)
			throws SQLException {
		CallableStatement callStmt = null;
		try {
			Connection connection = DatabaseConnection.getInstance().getConnection();
			callStmt = connection.prepareCall("{ call registerSemeadura(?,?,?,?,?) }");
			callStmt.setInt(1, culturaID);
			callStmt.setInt(2, parcelaID);
			callStmt.setDate(3, dataOperacao);
			callStmt.setInt(4, quantidade);
			callStmt.setDouble(5, area);
			return callStmt.execute();
		} finally {
			if (callStmt != null) {
				callStmt.close();
			}
		}
	}

	public boolean registerMonda(int quantidade, int parcelaID, int plantacaoID, Date dataOperacao)
			throws SQLException {

		CallableStatement callStmt = null;
		try {
			Connection connection = DatabaseConnection.getInstance().getConnection();
			callStmt = connection.prepareCall("{ call registerMonda(?,?,?,?) }");
			callStmt.setInt(1, plantacaoID);
			callStmt.setInt(2, parcelaID);
			callStmt.setDate(3, dataOperacao);
			callStmt.setInt(4, quantidade);
			return callStmt.execute();
		} finally {
			if (callStmt != null) {
				callStmt.close();
			}
		}
	}

	public void registerRega(int quantidade, int setorID, Date dataOperacao, Time hora)
			throws SQLException {

		CallableStatement callStmt = null;
		try {
			Connection connection = DatabaseConnection.getInstance().getConnection();
			callStmt = connection.prepareCall("{ call registerRega(?,?,?,?) }");
			callStmt.setInt(1, quantidade);
			callStmt.setInt(2, setorID);
			callStmt.setDate(3, dataOperacao);
			callStmt.setTime(4, hora);
			callStmt.execute();
			connection.commit();
		} finally {
			if (callStmt != null) {
				callStmt.close();
			}
		}
	}
}
