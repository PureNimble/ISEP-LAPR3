package isep.lapr3.g094.repository;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Time;
import java.util.HashMap;
import java.util.Map;

import isep.lapr3.g094.repository.dataAccess.DatabaseConnection;

import java.sql.SQLException;
import java.sql.Statement;

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

	public void registerFatorDeProducao(double quantidade, int parcelaID, Date dataOperacao, double area,
			int fatorProducaoID, int modoFertilizacaoID)
			throws SQLException {

		CallableStatement callStmt = null;
		try {
			Connection connection = DatabaseConnection.getInstance().getConnection();
			callStmt = connection.prepareCall("{ call registerFatorDeProducao(?,?,?,?,?,?) }");
			callStmt.setDouble(1, quantidade);
			callStmt.setInt(2, parcelaID);
			callStmt.setDate(3, dataOperacao);
			callStmt.setDouble(4, area);
			callStmt.setInt(5, fatorProducaoID);
			callStmt.setInt(6, modoFertilizacaoID);
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

	public Map<String, Integer> getParcelas() throws SQLException {
		CallableStatement callStmt = null;
		Statement stmt = null;
		ResultSet rs = null;
		Map<String, Integer> output = new HashMap<String, Integer>();
		try {
			Connection connection = DatabaseConnection.getInstance().getConnection();
			stmt = connection.createStatement();
			rs = stmt.executeQuery(
					"SELECT E.ID,E.Designacao,E.Area,E.Unidade FROM Parcela P, ESPACO E WHERE P.ESPACOID = E.ID");

			while (rs.next()) {
				output.put(rs.getString(2) + " - " + rs.getDouble(3) + " " + rs.getString(4), rs.getInt(1));
			}
		} finally {
			if (callStmt != null) {
				callStmt.close();
			}
			if (stmt != null) {
				stmt.close();
			}
			if (rs != null) {
				rs.close();
			}
		}
		return output;

	}

	public Map<String, Integer> getPlantacoes(int parcelaID) throws SQLException {
		CallableStatement callStmt = null;
		Statement stmt = null;
		ResultSet rs = null;
		Map<String, Integer> output = new HashMap<String, Integer>();
		try {
			Connection connection = DatabaseConnection.getInstance().getConnection();
			stmt = connection.createStatement();
			rs = stmt.executeQuery(
					"SELECT P.ID,NE.NOMECOMUM || ' ' || CU.VARIEDADE AS Cultura, P.DATAINICIAL FROM Plantacao P, CULTURA CU, NOMEESPECIE NE WHERE P.ParcelaEspacoID = "
							+ parcelaID + " AND P.CULTURAID = CU.ID AND CU.NOMEESPECIEID = NE.ID");

			while (rs.next()) {
				output.put(rs.getString(2) + " | " + rs.getDate(3), rs.getInt(1));
			}
		} finally {
			if (callStmt != null) {
				callStmt.close();
			}
			if (stmt != null) {
				stmt.close();
			}
			if (rs != null) {
				rs.close();
			}
		}
		return output;

	}

	public Map<String, Integer> getCulturas() {
		CallableStatement callStmt = null;
		Statement stmt = null;
		ResultSet rs = null;
		Map<String, Integer> output = new HashMap<String, Integer>();
		try {
			Connection connection = DatabaseConnection.getInstance().getConnection();
			stmt = connection.createStatement();
			rs = stmt.executeQuery(
					"SELECT CU.ID,NE.NOMECOMUM || ' ' || CU.VARIEDADE AS CULTURA FROM CULTURA CU, NOMEESPECIE NE WHERE CU.NOMEESPECIEID = NE.ID AND CU.TIPOCULTURAID = 2");

			while (rs.next()) {
				output.put(rs.getString(2), rs.getInt(1));
			}
		} catch (SQLException e) {
			System.out.println("Erro ao obter lista de culturas");
			System.out.println("Motivo: " + e.getMessage());
		} finally {
			if (callStmt != null) {
				try {
					callStmt.close();
				} catch (SQLException e) {
					System.out.println("Erro ao fechar a ligação à base de dados");
					System.out.println("Motivo: " + e.getMessage());
				}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					System.out.println("Erro ao fechar a ligação à base de dados");
					System.out.println("Motivo: " + e.getMessage());
				}
			}
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					System.out.println("Erro ao fechar a ligação à base de dados");
					System.out.println("Motivo: " + e.getMessage());
				}
			}
		}
		return output;

	}

	public Map<String, Integer> getFatorProducao() {
		CallableStatement callStmt = null;
		Statement stmt = null;
		ResultSet rs = null;
		Map<String, Integer> output = new HashMap<String, Integer>();
		try {
			Connection connection = DatabaseConnection.getInstance().getConnection();
			stmt = connection.createStatement();
			rs = stmt.executeQuery("SELECT ID,DESIGNACAO FROM FATORPRODUCAO");

			while (rs.next()) {
				output.put(rs.getString(2), rs.getInt(1));
			}
		} catch (SQLException e) {
			System.out.println("Erro ao obter lista de fatores de p");
			System.out.println("Motivo: " + e.getMessage());
		} finally {
			if (callStmt != null) {
				try {
					callStmt.close();
				} catch (SQLException e) {
					System.out.println("Erro ao fechar a ligação à base de dados");
					System.out.println("Motivo: " + e.getMessage());
				}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					System.out.println("Erro ao fechar a ligação à base de dados");
					System.out.println("Motivo: " + e.getMessage());
				}
			}
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					System.out.println("Erro ao fechar a ligação à base de dados");
					System.out.println("Motivo: " + e.getMessage());
				}
			}
		}
		return output;

	}

	public Map<String, Integer> getModoFertilizacao() {
		CallableStatement callStmt = null;
		Statement stmt = null;
		ResultSet rs = null;
		Map<String, Integer> output = new HashMap<String, Integer>();
		try {
			Connection connection = DatabaseConnection.getInstance().getConnection();
			stmt = connection.createStatement();
			rs = stmt.executeQuery("SELECT ID,DESIGNACAO FROM MODOFERTILIZACAO");

			while (rs.next()) {
				output.put(rs.getString(2), rs.getInt(1));
			}
		} catch (SQLException e) {
			System.out.println("Erro ao obter lista de modos de fertilização");
			System.out.println("Motivo: " + e.getMessage());
		} finally {
			if (callStmt != null) {
				try {
					callStmt.close();
				} catch (SQLException e) {
					System.out.println("Erro ao fechar a ligação à base de dados");
					System.out.println("Motivo: " + e.getMessage());
				}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					System.out.println("Erro ao fechar a ligação à base de dados");
					System.out.println("Motivo: " + e.getMessage());
				}
			}
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					System.out.println("Erro ao fechar a ligação à base de dados");
					System.out.println("Motivo: " + e.getMessage());
				}
			}
		}
		return output;

	}

	public Map<String, Integer> getProdutos(int parcelaID) {
		CallableStatement callStmt = null;
		Statement stmt = null;
		ResultSet rs = null;
		Map<String, Integer> output = new HashMap<String, Integer>();
		try {
			Connection connection = DatabaseConnection.getInstance().getConnection();
			stmt = connection.createStatement();
			rs = stmt.executeQuery(
					"SELECT CU.ID, NP.DESIGNACAO || ' ' || CU.VARIEDADE FROM PLANTACAO P, CULTURA CU, PRODUTO PO, NOMEPRODUTO NP WHERE P.ParcelaEspacoID = "
							+ parcelaID
							+ " AND P.CULTURAID = CU.ID AND P.CULTURAID = PO.CULTURAID AND PO.NOMEPRODUTOID = NP.ID");

			while (rs.next()) {
				output.put(rs.getString(2), rs.getInt(1));
			}
		} catch (SQLException e) {
			System.out.println("Erro ao obter lista de produtos");
			System.out.println("Motivo: " + e.getMessage());
		} finally {
			if (callStmt != null) {
				try {
					callStmt.close();
				} catch (SQLException e) {
					System.out.println("Erro ao fechar a ligação à base de dados");
					System.out.println("Motivo: " + e.getMessage());
				}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					System.out.println("Erro ao fechar a ligação à base de dados");
					System.out.println("Motivo: " + e.getMessage());
				}
			}
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					System.out.println("Erro ao fechar a ligação à base de dados");
					System.out.println("Motivo: " + e.getMessage());
				}
			}
		}
		return output;

	}

	public Map<String, Integer> getPlantacaoByProduto(int parcelaID, int produtoID) {
		CallableStatement callStmt = null;
		Statement stmt = null;
		ResultSet rs = null;
		Map<String, Integer> output = new HashMap<String, Integer>();
		try {
			Connection connection = DatabaseConnection.getInstance().getConnection();
			stmt = connection.createStatement();
			rs = stmt.executeQuery(
					"SELECT P.ID, NE.NOMECOMUM || ' ' ||CU.VARIEDADE, P.DATAINICIAL FROM PLANTACAO P, CULTURA CU, NOMEESPECIE NE WHERE P.ParcelaEspacoID = "
							+ parcelaID
							+ " AND P.CULTURAID = " + produtoID
							+ " AND P.CULTURAID = CU.ID AND CU.NOMEESPECIEID = NE.ID");

			while (rs.next()) {
				output.put(rs.getString(2) + " | " + rs.getDate(3), rs.getInt(1));
			}
		} catch (SQLException e) {
			System.out.println("Erro ao obter lista de plantações");
			System.out.println("Motivo: " + e.getMessage());
		} finally {
			if (callStmt != null) {
				try {
					callStmt.close();
				} catch (SQLException e) {
					System.out.println("Erro ao fechar a ligação à base de dados");
					System.out.println("Motivo: " + e.getMessage());
				}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					System.out.println("Erro ao fechar a ligação à base de dados");
					System.out.println("Motivo: " + e.getMessage());
				}
			}
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					System.out.println("Erro ao fechar a ligação à base de dados");
					System.out.println("Motivo: " + e.getMessage());
				}
			}
		}
		return output;
	}
}
