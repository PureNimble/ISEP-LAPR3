package isep.lapr3.g094.repository;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.List;
import java.text.DateFormatSymbols;
import java.util.Locale;

import isep.lapr3.g094.repository.dataAccess.DatabaseConnection;
import oracle.jdbc.OracleTypes;

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

	public void registerRega(int duracao, int setorID, Date dataOperacao, Time hora, int estado)
			throws SQLException {

		CallableStatement callStmt = null;
		try {
			Connection connection = DatabaseConnection.getInstance().getConnection();
			callStmt = connection.prepareCall("{ call registerRega(?,?,?,?,?) }");
			callStmt.setInt(1, duracao);
			callStmt.setInt(2, setorID);
			callStmt.setDate(3, dataOperacao);
			callStmt.setTime(4, hora);
			callStmt.setInt(5, estado);
			callStmt.execute();
			connection.commit();
		} finally {
			if (callStmt != null) {
				callStmt.close();
			}
		}
	}

	public void registerFertirrega(int duracao, int setorID, Date dataOperacao, Time hora, int estado, int mixID)
			throws SQLException {

		CallableStatement callStmt = null;
		try {
			Connection connection = DatabaseConnection.getInstance().getConnection();
			callStmt = connection.prepareCall("{ call registerFertirrega(?,?,?,?,?,?) }");
			callStmt.setInt(1, duracao);
			callStmt.setInt(2, setorID);
			callStmt.setDate(3, dataOperacao);
			callStmt.setTime(4, hora);
			callStmt.setInt(5, mixID);
			callStmt.setInt(6, estado);

			callStmt.execute();
			connection.commit();
		} finally {
			if (callStmt != null) {
				callStmt.close();
			}
		}
	}

	public int createReceita(String designacao)
			throws SQLException {

		CallableStatement callStmt = null;
		int idReceita;
		try {
			Connection connection = DatabaseConnection.getInstance().getConnection();
			callStmt = connection.prepareCall("{ call registerReceita(?,?) }");
			callStmt.setString(1, designacao);
			callStmt.registerOutParameter(2, OracleTypes.NUMBER);
			callStmt.execute();
			connection.commit();
			idReceita = callStmt.getInt(2);
		} finally {
			if (callStmt != null) {
				callStmt.close();
			}
		}
		return idReceita;
	}

	public void addFatorToReceita(int receitaID, int fatorProducaoID, double quantidade, String unidade)

			throws SQLException {

		CallableStatement callStmt = null;
		try {
			Connection connection = DatabaseConnection.getInstance().getConnection();
			callStmt = connection.prepareCall("{ call addFatorToReceita(?,?,?,?) }");
			callStmt.setInt(1, receitaID);
			callStmt.setInt(2, fatorProducaoID);
			callStmt.setDouble(3, quantidade);
			callStmt.setString(4, unidade);
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
					"SELECT P.ESPACOID AS ID, E.Designacao,(SELECT SUM(E.AREA) FROM ESPACO E WHERE E.ID = P.ESPACOID) - (SELECT NVL(SUM(PL.QUANTIDADE), 0) FROM PLANTACAO PL WHERE P.ESPACOID = PL.PARCELAESPACOID AND PL.UNIDADE = 'ha' AND (PL.DATAFINAL IS NULL OR PL.DATAFINAL > SYSDATE)) AS QUANTIDADE, E.Unidade FROM PARCELA P, Espaco E WHERE P.ESPACOID = E.ID GROUP BY P.ESPACOID, E.Designacao, E.Unidade");
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

	public Map<String, Integer> getPlantacoes(int parcelaID, Date operacaoData) throws SQLException {
		CallableStatement callStmt = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Map<String, Integer> output = new HashMap<String, Integer>();
		try {
			Connection connection = DatabaseConnection.getInstance().getConnection();
			String query = "SELECT P.ID,NE.NOMECOMUM || ' ' || CU.VARIEDADE AS Cultura, P.DATAINICIAL FROM Plantacao P, CULTURA CU, NOMEESPECIE NE WHERE P.ParcelaEspacoID = ? AND P.CULTURAID = CU.ID AND CU.NOMEESPECIEID = NE.ID AND (P.DATAFINAL IS NULL OR P.DATAFINAL >= ?)";
			stmt = connection.prepareStatement(query);
			stmt.setInt(1, parcelaID);
			stmt.setDate(2, operacaoData);
			rs = stmt.executeQuery();
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

	public List<String> getProdutosColhidosList(int parcelaID, Date dataInicial, Date dataFinal) throws SQLException {
		CallableStatement callStmt = null;
		ResultSet resultSet = null;
		List<String> result = null;

		try {
			Connection connection = DatabaseConnection.getInstance().getConnection();
			callStmt = connection.prepareCall("{ ? = call getProdutosColhidosList(?,?,?) }");
			callStmt.registerOutParameter(1, OracleTypes.CURSOR);
			callStmt.setInt(2, parcelaID);
			callStmt.setDate(3, dataInicial);
			callStmt.setDate(4, dataFinal);
			callStmt.execute();
			resultSet = (ResultSet) callStmt.getObject(1);

			result = getProdutosColhidosSet(resultSet);
		} finally {
			if (!Objects.isNull(callStmt)) {
				callStmt.close();
			}
			if (!Objects.isNull(resultSet)) {
				resultSet.close();
			}
		}

		return result;
	}

	private List<String> getProdutosColhidosSet(ResultSet resultSet) throws SQLException {
		List<String> result = new ArrayList<>();
		String previousEspecie = null;
		String previousProduto = null;
		while (resultSet.next()) {
			String parcela = resultSet.getString(1);
			String especie = resultSet.getString(2);
			String produto = resultSet.getString(3);
			if (previousEspecie == null || !previousEspecie.equals(especie)) {
				result.add(" ");
				result.add(" -> Parcela: " + parcela);
				result.add(" -> Especíe: " + especie);
				previousEspecie = especie;
			}
			if (previousProduto == null || !previousProduto.equals(produto)) {
				result.add("\t-> Produto: " + produto);
				previousProduto = produto;
			}
		}
		return result;
	}

	public List<String> getFatoresProducaoList(Date dataInicial, Date dataFinal) throws SQLException {
		CallableStatement callStmt = null;
		ResultSet resultSet = null;
		List<String> result = null;

		try {
			Connection connection = DatabaseConnection.getInstance().getConnection();
			callStmt = connection.prepareCall("{ ? = call getFatorProducaoList(?,?) }");
			callStmt.registerOutParameter(1, OracleTypes.CURSOR);
			callStmt.setDate(2, dataInicial);
			callStmt.setDate(3, dataFinal);
			callStmt.execute();
			resultSet = (ResultSet) callStmt.getObject(1);

			result = getFatorProducaoSet(resultSet);
		} finally {
			if (!Objects.isNull(callStmt)) {
				callStmt.close();
			}
			if (!Objects.isNull(resultSet)) {
				resultSet.close();
			}
		}

		return result;
	}

	private List<String> getFatorProducaoSet(ResultSet rs) throws SQLException {
		List<String> result = new ArrayList<>();
		String previousParcela = null;
		String previousAplicacao = null;
		while (rs.next()) {
			Date data = rs.getDate(1);
			String fatorproducao = rs.getString(2);
			String cultura = rs.getString(3);
			String aplicacao = rs.getString(4);
			String parcela = rs.getString(5);

			if (previousParcela == null || !previousParcela.equals(parcela)) {
				result.add("\nParcela: " + parcela);

				if (aplicacao.equals(previousAplicacao)) {
					result.add("\n\tAplicacao: " + aplicacao + "\n");
				}

				previousParcela = parcela;
			}

			if (previousAplicacao == null || !previousAplicacao.equals(aplicacao)) {
				result.add("\n\tAplicacao: " + aplicacao + "\n");
				previousAplicacao = aplicacao;
			}

			result.add("\t\t" + data + " - " + fatorproducao + " - " + cultura);
		}
		return result;

	}

	public List<String> getFatorProducaoElementosList(int parcelaID, Date dataInicial, Date dataFinal)
			throws SQLException {
		CallableStatement callStmt = null;
		ResultSet resultSet = null;
		List<String> result = null;

		try {
			Connection connection = DatabaseConnection.getInstance().getConnection();
			callStmt = connection.prepareCall("{ ? = call getFatorProducaoElementosList(?,?,?) }");
			callStmt.registerOutParameter(1, OracleTypes.CURSOR);
			callStmt.setDate(2, dataInicial);
			callStmt.setDate(3, dataFinal);
			callStmt.setInt(4, parcelaID);
			callStmt.execute();
			resultSet = (ResultSet) callStmt.getObject(1);

			result = getFatorProducaoElementosSet(resultSet);
		} finally {
			if (!Objects.isNull(callStmt)) {
				callStmt.close();
			}
			if (!Objects.isNull(resultSet)) {
				resultSet.close();
			}
		}

		return result;
	}

	private List<String> getFatorProducaoElementosSet(ResultSet resultSet) throws SQLException {
		List<String> result = new ArrayList<>();
		String previousFatorProducao = null;
		Date previousData = null;
		while (resultSet.next()) {
			String fatorProducao = resultSet.getString(1);
			String quantidade = resultSet.getString(2);
			String elemento = resultSet.getString(3);
			Date data = resultSet.getDate(4);
			if (previousFatorProducao == null || !previousFatorProducao.equals(fatorProducao)) {
				result.add(" ");
				result.add("Fator de Producao: " + fatorProducao);
				previousFatorProducao = fatorProducao;
			}
			if (previousData == null || !previousData.equals(data)) {
				result.add(" ");
				result.add("Data: " + data);
				result.add(" ");
				previousData = data;
			}
			result.add("\t-> Quantidade: " + quantidade + ", Elemento: " + elemento);
		}
		return result;
	}

	public List<String> getRegaMensal(Date dataInicial, Date dataFinal)
			throws SQLException {
		CallableStatement callStmt = null;
		ResultSet resultSet = null;
		List<String> result = null;

		try {
			Connection connection = DatabaseConnection.getInstance().getConnection();
			callStmt = connection.prepareCall("{ ? = call getRegaMensal(?,?) }");
			callStmt.registerOutParameter(1, OracleTypes.CURSOR);
			callStmt.setDate(2, dataInicial);
			callStmt.setDate(3, dataFinal);
			callStmt.execute();
			resultSet = (ResultSet) callStmt.getObject(1);

			result = getRegaMensalSet(resultSet);
		} finally {
			if (!Objects.isNull(callStmt)) {
				callStmt.close();
			}
			if (!Objects.isNull(resultSet)) {
				resultSet.close();
			}
		}

		return result;
	}

	private List<String> getRegaMensalSet(ResultSet resultSet) throws SQLException {
		List<String> result = new ArrayList<>();
		String previousParcela = null;
		Locale locale = Locale.forLanguageTag("pt-PT");
		while (resultSet.next()) {
			String parcela = resultSet.getString(1);
			int year = resultSet.getInt(2);
			int data = resultSet.getInt(3);
			int duracao = resultSet.getInt(4);
			if (previousParcela == null || !previousParcela.equals(parcela)) {
				result.add(" ");
				result.add("Parcela: " + parcela);
				previousParcela = parcela;
			}
			result.add("\t" + new DateFormatSymbols(locale).getMonths()[data - 1] + " de " + year
					+ " -> Duração: " + duracao + "min");
		}
		return result;
	}

	public List<String> GetOperacaoList(int parcelaID, Date dataInicial, Date dataFinal)
			throws SQLException {
		CallableStatement callStmt = null;
		ResultSet resultSet = null;
		List<String> result = null;

		try {
			Connection connection = DatabaseConnection.getInstance().getConnection();
			callStmt = connection.prepareCall("{ ? = call GetOperacaoList(?,?,?) }");
			callStmt.registerOutParameter(1, OracleTypes.CURSOR);
			callStmt.setInt(2, parcelaID);
			callStmt.setDate(3, dataInicial);
			callStmt.setDate(4, dataFinal);
			callStmt.execute();
			resultSet = (ResultSet) callStmt.getObject(1);

			result = GetOperacaoSet(resultSet);
		} finally {
			if (!Objects.isNull(callStmt)) {
				callStmt.close();
			}
			if (!Objects.isNull(resultSet)) {
				resultSet.close();
			}
		}

		return result;
	}

	private List<String> GetOperacaoSet(ResultSet rs) throws SQLException {
		List<String> result = new ArrayList<>();
		String previousTipoOperacao = null;

		while (rs.next()) {
			int operacaoid = rs.getInt(1);
			String tipo_de_operacao = rs.getString(2);
			String data = rs.getString(3);
			String cultura = rs.getString(4);

			if (previousTipoOperacao == null || !previousTipoOperacao.equals(tipo_de_operacao)) {
				result.add("---------------------------------");
				result.add("Tipo de Operação: " + tipo_de_operacao);
				result.add("---------------------------------");
				previousTipoOperacao = tipo_de_operacao;
			}

			result.add("-> Operação: " + operacaoid + " -> Cultura: " + cultura);
			result.add("\t-> Data: " + data + "\n");
		}
		return result;
	}

	public Map<String, Integer> getSetor() {
		CallableStatement callStmt = null;
		Statement stmt = null;
		ResultSet rs = null;
		Map<String, Integer> output = new HashMap<String, Integer>();
		try {
			Connection connection = DatabaseConnection.getInstance().getConnection();
			stmt = connection.createStatement();
			rs = stmt.executeQuery(
					"SELECT ID,DESIGNACAO FROM SETOR");

			while (rs.next()) {
				output.put(rs.getString(2), rs.getInt(1));
			}
		} catch (SQLException e) {
			System.out.println("Erro ao obter lista de setores");
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

	public Map<String, Integer> getMix() {
		CallableStatement callStmt = null;
		Statement stmt = null;
		ResultSet rs = null;
		Map<String, Integer> output = new HashMap<String, Integer>();
		try {
			Connection connection = DatabaseConnection.getInstance().getConnection();
			stmt = connection.createStatement();
			rs = stmt.executeQuery(
					"SELECT ID,DESIGNACAO FROM RECEITA");

			while (rs.next()) {
				output.put(rs.getString(2), rs.getInt(1));
			}
		} catch (SQLException e) {
			System.out.println("Erro ao obter lista de receitas");
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

	public Map<Integer, List<String>> getConsumoByCultura(int year) throws SQLException {
		CallableStatement callStmt = null;
		ResultSet resultSet = null;
		Map<Integer, List<String>> result = new TreeMap<>(Collections.reverseOrder());

		try {
			Connection connection = DatabaseConnection.getInstance().getConnection();
			callStmt = connection.prepareCall("{ ? = call getConsumoRega(?) }");
			callStmt.registerOutParameter(1, OracleTypes.CURSOR);
			callStmt.setInt(2, year);
			callStmt.execute();

			resultSet = (ResultSet) callStmt.getObject(1);
			while (resultSet.next()) {
				String cultura = resultSet.getString(1);
				Integer consumo = resultSet.getInt(2);

				if (result.containsKey(consumo))
					result.get(consumo).add(cultura);
				else {
					List<String> culturas = new ArrayList<>();
					culturas.add(cultura);
					result.put(consumo, culturas);
				}

			}

		} finally {
			if (!Objects.isNull(callStmt)) {
				callStmt.close();
			}
			if (!Objects.isNull(resultSet)) {
				resultSet.close();
			}
		}

		return result;
	}

	public Map<String, List<Integer>> getFatorProducaoYear(int year) throws SQLException {
		CallableStatement callStmt = null;
		ResultSet resultSet = null;
		Map<String, List<Integer>> result = new HashMap<>();

		try {
			Connection connection = DatabaseConnection.getInstance().getConnection();
			callStmt = connection.prepareCall("{ ? = call getFatorProducaoYear(?) }");
			callStmt.registerOutParameter(1, OracleTypes.CURSOR);
			callStmt.setInt(2, year);
			callStmt.execute();
			resultSet = (ResultSet) callStmt.getObject(1);
			String previousFactor = null;
			List<Integer> years = null;
			while (resultSet.next()) {
				String currentFactor = resultSet.getString(1);
				if (previousFactor == null || !previousFactor.equals(currentFactor)) {
					if (years != null) {
						result.put(previousFactor, years);
					}
					years = new ArrayList<>();
					previousFactor = currentFactor;
				}
				years.add(resultSet.getInt(2));
			}
			if (years != null) {
				result.put(previousFactor, years);
			}
		} finally {
			if (!Objects.isNull(callStmt)) {
				callStmt.close();
			}
			if (!Objects.isNull(resultSet)) {
				resultSet.close();
			}
		}

		return result;
	}

	public void cancelOperation(int operacaoID) throws SQLException {
		PreparedStatement pstmt = null;
		try {
			Connection connection = DatabaseConnection.getInstance().getConnection();
			String sql = "UPDATE OPERACAO SET ESTADOID = 3 WHERE ID = ?";
			pstmt = connection.prepareStatement(sql);
			pstmt.setInt(1, operacaoID);
			pstmt.executeUpdate();
			connection.commit();
		} finally {
			if (pstmt != null) {
				pstmt.close();
			}
		}
	}

	public Map<String, Integer> getOperacoes() throws SQLException {
		Statement stmt = null;
		ResultSet rs = null;
		Map<String, Integer> output = new HashMap<>();
		try {
			Connection connection = DatabaseConnection.getInstance().getConnection();
			stmt = connection.createStatement();
			rs = stmt.executeQuery(
					"SELECT ID, DESIGNACAO, DATAOPERACAO, DESIGNACAOPLANTACAO, ESTADO FROM (SELECT OP.ID, TOP.DESIGNACAO, OP.DATAOPERACAO, NE.NOMECOMUM || ' ' || C.VARIEDADE AS DESIGNACAOPLANTACAO, E.DESIGNACAO AS ESTADO, ROW_NUMBER() OVER (PARTITION BY OP.ID ORDER BY OP.DATAOPERACAO DESC) rn FROM OPERACAO OP, TIPOOPERACAO TOP, NOMEESPECIE NE, CULTURA C, PLANTACAO PL, OPERACAOPLANTACAO OPL, ESTADO E, OPERACAOTIPOOPERACAO OTOP WHERE OP.ID = OPL.OPERACAOID AND OPL.PLANTACAOID = PL.ID AND PL.CULTURAID = C.ID AND C.NOMEESPECIEID = NE.ID AND OP.ESTADOID = E.ID AND E.ID = 1 AND OP.ID = OTOP.OPERACAOID AND OTOP.TIPOOPERACAOID = TOP.ID) WHERE rn = 1 UNION SELECT ID, DESIGNACAO, DATAOPERACAO, PARCELA, ESTADO FROM (SELECT OP.ID, TOP.DESIGNACAO, OP.DATAOPERACAO, E.DESIGNACAO AS PARCELA, ET.DESIGNACAO AS ESTADO, ROW_NUMBER() OVER (PARTITION BY OP.ID ORDER BY OP.DATAOPERACAO DESC) rn FROM OPERACAO OP, TIPOOPERACAO TOP, PARCELA P, OPERACAOPARCELA OPAR, ESPACO E, ESTADO ET, OPERACAOTIPOOPERACAO OTOP WHERE OP.ID = OPAR.OPERACAOID AND OPAR.PARCELAESPACOID = P.ESPACOID AND E.ID = P.ESPACOID AND OP.ESTADOID = ET.ID AND ET.ID = 1 AND OP.ID = OTOP.OPERACAOID AND OTOP.TIPOOPERACAOID = TOP.ID) WHERE rn = 1 UNION SELECT ID, DESIGNACAO, DATAOPERACAO, SETOR, ESTADO FROM (SELECT OP.ID, TOP.DESIGNACAO, OP.DATAOPERACAO, S.DESIGNACAO AS SETOR, ET.DESIGNACAO AS ESTADO, ROW_NUMBER() OVER (PARTITION BY OP.ID ORDER BY OP.DATAOPERACAO DESC) rn FROM OPERACAO OP, TIPOOPERACAO TOP, SETOR S, OPERACAOSETOR OS, ESTADO ET, OPERACAOTIPOOPERACAO OTOP WHERE OP.ID = OS.OPERACAOID AND OS.SETORID = S.ID AND OP.ESTADOID = ET.ID AND ET.ID = 1 AND OP.ID = OTOP.OPERACAOID AND OTOP.TIPOOPERACAOID = TOP.ID) WHERE rn = 1");

			while (rs.next()) {
				String key = rs.getString(2) + ", " + rs.getDate(3) + ", " + rs.getString(4) + ", " + rs.getString(5);
				Integer value = rs.getInt(1);
				output.put(key, value);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return output;
	}
}
