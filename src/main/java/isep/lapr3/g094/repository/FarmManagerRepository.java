package isep.lapr3.g094.repository;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.Time;

import isep.lapr3.g094.repository.dataAccess.DatabaseConnection;

import java.sql.SQLException;

public class FarmManagerRepository {

	public void registerPoda(double quantidade, int parcelaID, int plantacaoID, Date dataOperacao)
			throws SQLException {

		CallableStatement callStmt = null;
		try {
			Connection connection = DatabaseConnection.getInstance().getConnection();
			callStmt = connection.prepareCall("{ call registerPoda(?,?,?,?) }");
			callStmt.setDouble(1, quantidade);
			callStmt.setInt(2, parcelaID);
			callStmt.setInt(3, plantacaoID);
			callStmt.setDate(4, dataOperacao);
			callStmt.execute();
		} finally {
			if (callStmt != null) {
				callStmt.close();
			}
		}
	}

	public void registerFatorDeProducao(double quantidade, int parcelaID, Date dataOperacao,
	int fatorProducaoID, int modoFertilizacaoID)
			throws SQLException {

		CallableStatement callStmt = null;
		try {
			Connection connection = DatabaseConnection.getInstance().getConnection();
			callStmt = connection.prepareCall("{ call registerFatorDeProducao(?,?,?,?,?) }");
			callStmt.setDouble(1, quantidade);
			callStmt.setInt(2, parcelaID);
			callStmt.setDate(3, dataOperacao);
			callStmt.setInt(4, fatorProducaoID);
			callStmt.setInt(5, modoFertilizacaoID);
			callStmt.execute();
		} finally {
			if (callStmt != null) {
				callStmt.close();
			}
		}
	}

	public void registerColheita(double quantidade, int parcelaID, int plantacaoID, Date dataOperacao)
			throws SQLException {
		CallableStatement callStmt = null;
		try {
			Connection connection = DatabaseConnection.getInstance().getConnection();
			callStmt = connection.prepareCall("{ call registerColheita(?,?,?,?) }");
			callStmt.setInt(1, plantacaoID);
			callStmt.setInt(2, parcelaID);
			callStmt.setDate(3, dataOperacao);
			callStmt.setDouble(4, quantidade);
			callStmt.execute();
		} finally {
			if (callStmt != null) {
				callStmt.close();
			}
		}
	}

	public void registerSemeadura(double quantidade, int parcelaID, int culturaID, Date dataOperacao, double area)
			throws SQLException {
		CallableStatement callStmt = null;
		try {
			Connection connection = DatabaseConnection.getInstance().getConnection();
			callStmt = connection.prepareCall("{ call registerSemeadura(?,?,?,?,?) }");
			callStmt.setInt(1, culturaID);
			callStmt.setInt(2, parcelaID);
			callStmt.setDate(3, dataOperacao);
			callStmt.setDouble(4, quantidade);
			callStmt.setDouble(5, area);
			callStmt.execute();
		} finally {
			if (callStmt != null) {
				callStmt.close();
			}
		}
	}

	public void registerMonda(double quantidade, int parcelaID, int plantacaoID, Date dataOperacao)
			throws SQLException {

		CallableStatement callStmt = null;
		try {
			Connection connection = DatabaseConnection.getInstance().getConnection();
			callStmt = connection.prepareCall("{ call registerMonda(?,?,?,?) }");
			callStmt.setInt(1, plantacaoID);
			callStmt.setInt(2, parcelaID);
			callStmt.setDate(3, dataOperacao);
			callStmt.setDouble(4, quantidade);
			callStmt.execute();
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
