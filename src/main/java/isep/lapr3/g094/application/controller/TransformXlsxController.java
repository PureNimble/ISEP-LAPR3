package isep.lapr3.g094.application.controller;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.sql.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Properties;
import java.util.ArrayList;

public class TransformXlsxController {

    private ImportController importController = new ImportController();
    private SimpleDateFormat format = new SimpleDateFormat("MM-dd-yyyy");

    public boolean createInserts() throws ClassNotFoundException, IOException, SQLException, ParseException {

        // Load the properties file
        Properties properties = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("bddad/config.properties")) {
            properties.load(input);
        }
        Connection conn = createConnection(properties);

        List<LinkedHashSet<List<String>>> xlsxList = importController.importXlsx();
        List<String> culturaList = new ArrayList<>();
        List<String> fatorProducaoList = new ArrayList<>();
        for (int i = 0; i < xlsxList.size(); i++) {
            switch (i) {
                case 0:
                    culturaList = insertCultura(xlsxList.get(i), conn);
                    break;
                case 1:
                    fatorProducaoList = insertFatorDeProducao(xlsxList.get(i), conn);
                    break;
                case 2:
                    insertEspaco(xlsxList.get(i), conn);
                    break;
                case 3:
                    insertPlantacao(xlsxList.get(i), culturaList, conn);
                    break;
                case 4:
                    insertOperacao(xlsxList.get(i), culturaList, fatorProducaoList, conn);
                    break;
                default:
                    continue;
            }
        }

        conn.close();
        return true;
    }

    private List<String> insertCultura(LinkedHashSet<List<String>> pageList, Connection conn)
            throws IOException, ClassNotFoundException, SQLException {

        List<List<String>> insertedDataIndex = new ArrayList<>();
        int tipoCulturaID = 1, nomeEspecieID = 1, culturaID = 1;
        for (int i = 0; i < 3; i++)
            insertedDataIndex.add(new ArrayList<>());
        try {
            conn.setAutoCommit(false);

            try (PreparedStatement pstmtTipoCultura = conn.prepareStatement("INSERT INTO TipoCultura VALUES (?,?)");
                    PreparedStatement pstmtNomeEspecie = conn
                            .prepareStatement("INSERT INTO NomeEspecie VALUES (?,?,?)");
                    PreparedStatement pstmtCultura = conn
                            .prepareStatement("INSERT INTO Cultura VALUES (?, ?, ?, ?, ?, ?, ?, ?)")) {

                for (List<String> rowData : pageList) {
                    String tipoCultura = rowData.get(3); // tipo Cultura
                    String nomeEspecie = rowData.subList(0, 2).toString(); // nomeEspecie

                    if (!insertedDataIndex.get(0).contains(tipoCultura)) { // TipoCultura
                        pstmtTipoCultura.setInt(1, tipoCulturaID); // ID
                        pstmtTipoCultura.setString(2, tipoCultura); // Designação
                        pstmtTipoCultura.addBatch();
                        insertedDataIndex.get(0).add(tipoCultura);
                        tipoCulturaID++;
                    }

                    if (!insertedDataIndex.get(1).contains(nomeEspecie)) { // NomeEspecie
                        pstmtNomeEspecie.setInt(1, nomeEspecieID); // ID
                        pstmtNomeEspecie.setString(2, rowData.get(1)); // Nome comum
                        pstmtNomeEspecie.setString(3, rowData.get(0)); // especie
                        pstmtNomeEspecie.addBatch();
                        insertedDataIndex.get(1).add(nomeEspecie);
                        nomeEspecieID++;
                    }

                    pstmtCultura.setInt(1, culturaID); // ID
                    pstmtCultura.setString(2, rowData.get(2)); // Variedade
                    pstmtCultura.setString(3, rowData.get(5)); // Poda
                    pstmtCultura.setString(4, rowData.get(6)); // Floração
                    pstmtCultura.setString(5, rowData.get(7)); // Colheita
                    pstmtCultura.setString(6, rowData.get(4)); // Plantação
                    pstmtCultura.setInt(7, (insertedDataIndex.get(1).indexOf(nomeEspecie)) + 1); // NomeEspecie
                    pstmtCultura.setInt(8, (insertedDataIndex.get(0).indexOf(tipoCultura)) + 1); // TipoCultura
                    pstmtCultura.addBatch();
                    insertedDataIndex.get(2).add(rowData.get(1).toUpperCase() + " " + rowData.get(2).toUpperCase());
                    culturaID++;
                }

                pstmtTipoCultura.executeBatch();
                pstmtNomeEspecie.executeBatch();
                pstmtCultura.executeBatch();

                conn.commit();

            } catch (SQLException e) {
                conn.rollback();
                throw new RuntimeException("Erro ao inserir na base de dados", e);
            }
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                } catch (SQLException e) {
                    throw new RuntimeException("Erro ao dar reset ao auto-commit", e);
                }
            }
        }
        return insertedDataIndex.get(2);
    }

    private List<String> insertFatorDeProducao(LinkedHashSet<List<String>> pageList, Connection conn)
            throws IOException, ClassNotFoundException, SQLException {
        List<List<String>> insertedDataIndex = new ArrayList<>();
        int tipoProducaoID = 1, aplicacaoID = 1, elementoID = 1, fatorProducaoID = 1;
        for (int i = 0; i < 4; i++)
            insertedDataIndex.add(new ArrayList<>());
        try {
            conn.setAutoCommit(false);

            try (PreparedStatement pstmtTipoProduto = conn.prepareStatement("INSERT INTO TIPOPRODUTO VALUES (?, ?)");
                    PreparedStatement pstmtAplicacao = conn.prepareStatement("INSERT INTO APLICACAO VALUES (?,?)");
                    PreparedStatement pstmtElemento = conn.prepareStatement("INSERT INTO ELEMENTO VALUES (?,?)");
                    PreparedStatement pstmtFatorProducao = conn
                            .prepareStatement("INSERT INTO FatorProducao VALUES (?, ?, ?, ?, ?, ?, ?)");
                    PreparedStatement pstmtAplicacaoProduto = conn
                            .prepareStatement("INSERT INTO AplicacaoProduto VALUES (?, ?)");
                    PreparedStatement pstmtElementoFicha = conn
                            .prepareStatement("INSERT INTO ElementoFicha VALUES (?, ?, ?)")) {

                for (List<String> rowData : pageList) {
                    String tipoProduto = rowData.get(3);
                    String[] aplicacao = rowData.get(4).split("\\+");
                    String fatorProducao = rowData.get(0);

                    if (!insertedDataIndex.get(0).contains(tipoProduto)) { // TipoProduto
                        pstmtTipoProduto.setInt(1, tipoProducaoID); // ID
                        pstmtTipoProduto.setString(2, tipoProduto); // Designação
                        pstmtTipoProduto.addBatch();
                        insertedDataIndex.get(0).add(tipoProduto);
                        tipoProducaoID++;
                    }

                    int i = 0;
                    while (i < aplicacao.length) {
                        if (!insertedDataIndex.get(1).contains(aplicacao[i])) { // Aplicacao
                            pstmtAplicacao.setInt(1, aplicacaoID); // ID
                            pstmtAplicacao.setString(2, aplicacao[i]); // Designação
                            pstmtAplicacao.addBatch();
                            insertedDataIndex.get(1).add(aplicacao[i]);
                            aplicacaoID++;
                        }
                        i++;
                    }

                    i = 5;
                    while (i < rowData.size() - 1) {
                        String elemento = rowData.get(i);
                        if (!insertedDataIndex.get(2).contains(elemento)) { // Elemento
                            pstmtElemento.setInt(1, elementoID); // ID
                            pstmtElemento.setString(2, elemento); // Designação
                            pstmtElemento.addBatch();
                            insertedDataIndex.get(2).add(elemento);
                            elementoID++;
                        }
                        i += 2;
                    }
                    pstmtFatorProducao.setInt(1, fatorProducaoID); // ID
                    pstmtFatorProducao.setString(2, rowData.get(0)); // Designacao
                    pstmtFatorProducao.setString(3, rowData.get(1)); // Fabricante
                    pstmtFatorProducao.setString(4, rowData.get(2)); // Formato
                    pstmtFatorProducao.setNull(5, java.sql.Types.DOUBLE); // MateriaOrganica
                    pstmtFatorProducao.setNull(6, java.sql.Types.DOUBLE); // PH
                    pstmtFatorProducao.setInt(7, (insertedDataIndex.get(0).indexOf(tipoProduto)) + 1); // TipoProduto
                    pstmtFatorProducao.addBatch();
                    insertedDataIndex.get(3).add(fatorProducao);
                    // AplicacaoProduto
                    i = 0;
                    while (i < aplicacao.length) {
                        pstmtAplicacaoProduto.setInt(1, fatorProducaoID); // FatorProducao
                        pstmtAplicacaoProduto.setInt(2, (insertedDataIndex.get(1).indexOf(aplicacao[i])) + 1); // Aplicacao
                        pstmtAplicacaoProduto.addBatch();
                        i++;
                    }
                    // ElementoFicha
                    i = 5;
                    while (i < rowData.size() - 1) {
                        pstmtElementoFicha.setInt(1, fatorProducaoID); // FatorProducao
                        pstmtElementoFicha.setInt(2, (insertedDataIndex.get(2).indexOf(rowData.get(i))) + 1); // Elemento
                        pstmtElementoFicha.setDouble(3, Double.parseDouble(rowData.get(i + 1))); // Quantidade
                        pstmtElementoFicha.addBatch();
                        i += 2;
                    }
                    fatorProducaoID++;
                }

                pstmtTipoProduto.executeBatch();
                pstmtAplicacao.executeBatch();
                pstmtElemento.executeBatch();
                pstmtFatorProducao.executeBatch();
                pstmtAplicacaoProduto.executeBatch();
                pstmtElementoFicha.executeBatch();

                conn.commit();

            } catch (SQLException e) {
                conn.rollback();
                throw new RuntimeException("Erro ao inserir na base de dados", e);
            }
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                } catch (SQLException e) {
                    throw new RuntimeException("Erro ao dar reset ao auto-commit", e);
                }
            }
        }
        return insertedDataIndex.get(3);
    }

    private void insertEspaco(LinkedHashSet<List<String>> pageList, Connection conn) throws SQLException {
        try {
            conn.setAutoCommit(false);

            try (PreparedStatement pstmtQuinta = conn.prepareStatement("INSERT INTO Quinta VALUES (?,?)");
                    PreparedStatement pstmtEspaco = conn.prepareStatement("INSERT INTO Espaco VALUES (?,?,?,?,?,?)");
                    PreparedStatement pstmtParcela = conn.prepareStatement("INSERT INTO Parcela VALUES (?)");
                    PreparedStatement pstmtRega = conn.prepareStatement("INSERT INTO Rega VALUES (?)");
                    PreparedStatement pstmtArmazem = conn.prepareStatement("INSERT INTO ARMAZEM (ESPACOID) VALUES (?)");
                    PreparedStatement pstmtEstabulo = conn.prepareStatement("INSERT INTO Estabulo VALUES (?)");
                    PreparedStatement pstmtGaragem = conn.prepareStatement("INSERT INTO Garagem VALUES (?)");) {

                pstmtQuinta.setInt(1, 1); // QuintaID
                pstmtQuinta.setString(2, "Quinta Do Ângelo v1.2"); // Designação
                for (List<String> rowData : pageList) {

                    int espacoID = Integer.valueOf(rowData.get(0));
                    String tipoEspaco = rowData.get(1);
                    if (tipoEspaco.toLowerCase().charAt(0) == 'm')
                        continue;
                    String designacao = rowData.get(2);
                    Double dimensao = Double.parseDouble(rowData.get(3));
                    String unidade = rowData.get(4);

                    pstmtEspaco.setInt(1, espacoID); // EspacoID
                    pstmtEspaco.setString(2, designacao); // Designação
                    pstmtEspaco.setDouble(3, dimensao); // Dimensao
                    pstmtEspaco.setString(4, unidade); // Unidade
                    pstmtEspaco.setNull(5, java.sql.Types.DATE);
                    pstmtEspaco.setInt(6, 1); // QuintaID
                    pstmtEspaco.addBatch();

                    switch (tipoEspaco.toLowerCase().charAt(0)) {
                        case 'p':
                            pstmtParcela.setInt(1, espacoID);
                            pstmtParcela.addBatch();
                            break;
                        case 'a':
                            pstmtArmazem.setInt(1, espacoID);
                            pstmtArmazem.addBatch();
                            break;
                        case 'g':
                            pstmtGaragem.setInt(1, espacoID);
                            pstmtGaragem.addBatch();
                            break;
                        case 'r':
                            pstmtRega.setInt(1, espacoID);
                            pstmtRega.addBatch();
                            break;
                        default:
                            continue;
                    }
                }
                pstmtQuinta.execute();
                pstmtEspaco.executeBatch();
                pstmtParcela.executeBatch();
                pstmtArmazem.executeBatch();
                pstmtGaragem.executeBatch();
                pstmtRega.executeBatch();

                conn.commit();

            } catch (SQLException e) {
                conn.rollback();
                throw new RuntimeException("Erro ao inserir na base de dados", e);
            }
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                } catch (SQLException e) {
                    throw new RuntimeException("Erro ao dar reset ao auto-commit", e);
                }
            }
        }
    }

    private void insertPlantacao(LinkedHashSet<List<String>> pageList, List<String> culturas,
            Connection conn)
            throws SQLException, ParseException {

        int plantacaoID = 1;
        try {
            conn.setAutoCommit(false);

            try (PreparedStatement pstmtPlantacao = conn
                    .prepareStatement("INSERT INTO Plantacao VALUES (?,?,?,?,?,?,?,?)")) {

                for (List<String> rowData : pageList) {
                    int parcelaID = Integer.parseInt(rowData.get(0)); // ParcelaID
                    String culturaVariedade = rowData.get(2).toUpperCase(); // DataInicial
                    java.util.Date dataInicial = format.parse(rowData.get(4));
                    Double quantidade = Double.parseDouble(rowData.get(6));
                    String unidade = rowData.get(7); // Unidade

                    pstmtPlantacao.setInt(1, plantacaoID);
                    pstmtPlantacao.setDate(2, new Date(dataInicial.getTime()));
                    if (rowData.get(5) != "NULL") {
                        java.util.Date dataFinal = format.parse(rowData.get(5));
                        pstmtPlantacao.setDate(3, new Date(dataFinal.getTime()));
                    } else
                        pstmtPlantacao.setNull(3, java.sql.Types.DATE);
                    pstmtPlantacao.setDouble(4, quantidade);
                    pstmtPlantacao.setString(5, unidade);
                    pstmtPlantacao.setNull(6, java.sql.Types.VARCHAR);
                    pstmtPlantacao.setInt(7, culturas.indexOf(culturaVariedade) + 1);
                    pstmtPlantacao.setInt(8, parcelaID);
                    pstmtPlantacao.addBatch();

                    plantacaoID++;
                }

                pstmtPlantacao.executeBatch();

                conn.commit();

            } catch (SQLException e) {
                conn.rollback();
                throw new RuntimeException("Erro ao inserir na base de dados", e);
            }
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                } catch (SQLException e) {
                    throw new RuntimeException("Erro ao dar reset ao auto-commit", e);
                }
            }
        }
    }

    private void insertOperacao(LinkedHashSet<List<String>> pageList,
            List<String> culturas,
            List<String> fatoresProducao, Connection conn) throws SQLException, ParseException {
        List<List<String>> insertedDataIndex = new ArrayList<>();
        int tipoOperacaoID = 1, operacaoID = 1, ModoFertilizacaoID = 1;
        for (int i = 0; i < 2; i++)
            insertedDataIndex.add(new ArrayList<>());
        try {
            conn.setAutoCommit(false);

            try (PreparedStatement pstmtTipoOperacao = conn.prepareStatement("INSERT INTO TipoOperacao VALUES (?,?)");
                    PreparedStatement pstmtModoFertilizacao = conn
                            .prepareStatement("INSERT INTO ModoFertilizacao VALUES (?,?)");
                    PreparedStatement pstmtOperacao = conn
                            .prepareStatement("INSERT INTO Operacao VALUES (?,?,?,?,?,?)");
                    PreparedStatement pstmtOperacaoFator = conn
                            .prepareStatement("INSERT INTO OperacaoFator VALUES (?, ?)");
                    PreparedStatement pstmtFertilizacao = conn
                            .prepareStatement("INSERT INTO Fertilizacao VALUES (?, ?)");
                    PreparedStatement pstmtOperacaoParcela = conn
                            .prepareStatement("INSERT INTO OperacaoParcela VALUES (?, ?)");
                    PreparedStatement pstmtOperacaoPlantacao = conn
                            .prepareStatement(
                                    "INSERT INTO OperacaoPlantacao VALUES (?, (SELECT ID FROM PLANTACAO WHERE CULTURAID = ? AND PARCELAESPACOID = ? AND ROWNUM = 1))");
                    PreparedStatement pstmtCadernoCampo = conn
                            .prepareStatement("INSERT INTO CadernoCampo VALUES (?, ?)");) {
                pstmtCadernoCampo.setInt(1, 1);
                pstmtCadernoCampo.setInt(2, 1);
                for (List<String> rowData : pageList) {

                    int parcelaID = Integer.valueOf(rowData.get(0));
                    String tipoOperacao = rowData.get(2);
                    String modoOperacao = rowData.get(3);
                    String culturaVariedade = rowData.get(4).toUpperCase();
                    java.util.Date data = format.parse(rowData.get(5));
                    Double quantidade = Double.parseDouble(rowData.get(6));
                    String unidade = rowData.get(7);
                    String fatorProducao = rowData.get(8);

                    if (!insertedDataIndex.get(0).contains(tipoOperacao)) { // TipoOperacao
                        pstmtTipoOperacao.setInt(1, tipoOperacaoID); // ID
                        pstmtTipoOperacao.setString(2, tipoOperacao); // Designação
                        pstmtTipoOperacao.addBatch();
                        insertedDataIndex.get(0).add(tipoOperacao);
                        tipoOperacaoID++;
                    }

                    pstmtOperacao.setInt(1, operacaoID); // ID
                    pstmtOperacao.setDate(2, new Date(data.getTime())); // Data
                    pstmtOperacao.setDouble(3, quantidade); // Quantidade
                    pstmtOperacao.setString(4, unidade); // Unidade
                    pstmtOperacao.setInt(5, insertedDataIndex.get(0).indexOf(tipoOperacao) + 1); // TipoOperacao
                    pstmtOperacao.setInt(6, 1); // CadernoCampo
                    pstmtOperacao.addBatch();

                    if (fatorProducao != "NULL") {
                        pstmtOperacaoFator.setInt(1, operacaoID); // OperacaoID
                        pstmtOperacaoFator.setInt(2, fatoresProducao.indexOf(fatorProducao) + 1); // Fator
                        pstmtOperacaoFator.addBatch();
                    }

                    if (culturaVariedade.equals("NULL") || culturaVariedade.equals("MACIEIRA")
                            || culturaVariedade.equals("VIDEIRA")) {
                        pstmtOperacaoParcela.setInt(1, operacaoID);
                        pstmtOperacaoParcela.setInt(2, parcelaID);
                        pstmtOperacaoParcela.addBatch();
                    } else {
                        pstmtOperacaoPlantacao.setInt(1, operacaoID);
                        pstmtOperacaoPlantacao.setInt(2, (culturas.indexOf(culturaVariedade)) + 1);
                        pstmtOperacaoPlantacao.setInt(3, parcelaID);
                        pstmtOperacaoPlantacao.addBatch();
                    }

                    if (tipoOperacao.charAt(0) == 'F') {
                        if (!insertedDataIndex.get(1).contains(modoOperacao)) {
                            pstmtModoFertilizacao.setInt(1, ModoFertilizacaoID);
                            pstmtModoFertilizacao.setInt(2, operacaoID);
                            pstmtModoFertilizacao.addBatch();
                            insertedDataIndex.get(1).add(modoOperacao);
                            ModoFertilizacaoID++;
                        }
                        pstmtFertilizacao.setInt(1, operacaoID);
                        pstmtFertilizacao.setInt(2, (insertedDataIndex.get(1).indexOf(modoOperacao)) + 1);
                        pstmtFertilizacao.addBatch();
                    }

                    operacaoID++;
                }

                pstmtCadernoCampo.execute();
                pstmtTipoOperacao.executeBatch();
                pstmtOperacao.executeBatch();
                pstmtOperacaoFator.executeBatch();
                pstmtModoFertilizacao.executeBatch();
                pstmtFertilizacao.executeBatch();
                pstmtOperacaoParcela.executeBatch();
                pstmtOperacaoPlantacao.executeBatch();

                conn.commit();

            } catch (SQLException e) {
                conn.rollback();
                throw new RuntimeException("Erro ao inserir na base de dados", e);
            }
        } finally

        {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                } catch (SQLException e) {
                    throw new RuntimeException("Erro ao dar reset ao auto-commit", e);
                }
            }
        }
    }

    private Connection createConnection(Properties properties) throws ClassNotFoundException, SQLException {
        // Load the Oracle JDBC driver
        Class.forName("oracle.jdbc.driver.OracleDriver");

        // Connect to the database
        return DriverManager.getConnection(properties.getProperty("database.url"),
                properties.getProperty("database.user"), properties.getProperty("database.password"));
    }

}
