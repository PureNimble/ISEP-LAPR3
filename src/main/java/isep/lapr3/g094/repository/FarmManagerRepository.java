package isep.lapr3.g094.repository;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;

import isep.lapr3.g094.repository.dataAccess.DatabaseConnection;

public class FarmManagerRepository {

	public boolean registerPoda(int quantidade, int parcelaID, int plantacaoID, Date dataOperacao) throws SQLException {

		CallableStatement callStmt = null;
		try {
			Connection connection = DatabaseConnection.getInstance().getConnection();
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

	public boolean registerFatorDeProducao(int quantidade, int parcelaID, int plantacaoID, Date dataOperacao) {
		// TODO Auto-generated method stub

		return false;
	}

	public boolean registerColheita(int quantidade, int parcelaID, int plantacaoID, Date dataOperacao) throws SQLException{
		CallableStatement callStmt = null;
		try {
			Connection connection = DatabaseConnection.getInstance().getConnection();
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

	public boolean registerSemeadura(int quantidade, int parcelaID, int plantacaoID, Date dataOperacao) {
		// TODO Auto-generated method stub

		return false;
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
		} catch (SQLException e) {
			throw e;
		} finally {
			if (callStmt != null) {
				callStmt.close();
			}
		}
	}

}
