package isep.lapr3.g094.Controller;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class CreateSQLInserts {

    private final static String FOLDER_PATH = "src/main/resources/";
    private static LinkedHashSet<String> insertsList = new LinkedHashSet<>();
    public boolean createSQLInserts() {
        extractXlsx(findXlsxFile());
        return writeListToFile();

    }

    private static void extractXlsx(String path) {
        try (XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(path))) {
            int workbookNum = workbook.getNumberOfSheets();

            for (int sheetIndex = 0; sheetIndex < workbookNum; sheetIndex++) {
                XSSFSheet sheet = workbook.getSheetAt(sheetIndex);
                switch (sheetIndex) {
                    case 0:
                        insertsList.addAll(insertPlantas(readSheet(sheet)));
                        break;
                    case 1:
                        insertsList.addAll(insertFatorProducao(readSheet(sheet)));
                        break;
                    case 2:
                        insertsList.addAll(insertExploracaoAgricula(readSheet(sheet)));
                        break;
                    case 3:
                        insertsList.addAll(insertCulturas(readSheet(sheet)));
                        break;
                    case 4:
                        insertsList.addAll(insertOperacoes(readSheet(sheet)));
                        break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static LinkedHashSet<List<String>> readSheet(XSSFSheet sheet) {
        LinkedHashSet<List<String>> sheetData = new LinkedHashSet<>();
        int lastRowNum = sheet.getLastRowNum();
        int lastColumnNum = sheet.getRow(sheet.getFirstRowNum()).getLastCellNum();
        for (int rowIndex = 0; rowIndex <= lastRowNum; rowIndex++) {
            XSSFRow row = sheet.getRow(rowIndex);
            if (row != null) {
                if (rowIndex > 0) {
                    List<String> rowData = new ArrayList<>();
                    for (int cellIndex = 0; cellIndex < lastColumnNum; cellIndex++) {
                        XSSFCell cell = row.getCell(cellIndex);
                        if (cell != null) {
                            String cellValue = "";
                            CellType cellType = cell.getCellType();
                            if (cellType == CellType.STRING) {
                                cellValue = "'" + cell.getStringCellValue().replaceAll("\u00A0", "").replace("'", "''") + "'";
                            } else {
                                if (DateUtil.isCellDateFormatted(cell)) {
                                    Date dateValue = cell.getDateCellValue();
                                    cellValue = "TO_DATE('" + new SimpleDateFormat("MM/dd/yyyy").format(dateValue) + "', 'MM/DD/YYYY')";
                                } else {
                                    double doubleValue = cell.getNumericCellValue();
                                    cellValue = String.valueOf(doubleValue).strip();
                                    if (cellValue.endsWith(".0")) {
                                        cellValue = cellValue.substring(0, cellValue.length() - 2);
                                    }
                                }
                            }
                            rowData.add(cellValue);
                        } else if (!sheet.getSheetName().contains("Fator")) {
                            rowData.add("NULL");
                        }
                    }
                    sheetData.add(rowData);
                }
            }
        }
        return sheetData;
    }
    

    private static LinkedHashSet<String> insertPlantas(LinkedHashSet<List<String>> sheetData) {

        LinkedHashSet<String> resultList = new LinkedHashSet<>();
        int culturaId = 0;
        int nomeEspecieId = 0;
        int tipoCulturaId = 0;
        Set<String> uniqueValues = new HashSet<>();
        for (List<String> rowData : sheetData) {
            String especie = rowData.get(0);
            String nomeComum = rowData.get(1);
            String tipoCulturaDesignacao = rowData.get(3);

            if (uniqueValues.add(tipoCulturaDesignacao)) {
                resultList.add("INSERT INTO TipoCultura (ID, Designacao) VALUES (" + tipoCulturaId + ", " + tipoCulturaDesignacao + ");");
                tipoCulturaId++;
            }

            if (uniqueValues.add(especie)) {
                resultList.add("INSERT INTO NomeEspecie (ID, NomeComum, Especie) VALUES (" + nomeEspecieId + ", " + nomeComum + ", " + especie + ");");
                nomeEspecieId++;
            }

            resultList.add("INSERT INTO Cultura (ID, Variedade, Poda, Floracao, Colheita, Sementeira, NomeEspecieID, TipoCulturaID) VALUES (" + culturaId + ", " + rowData.get(2) + ", " + rowData.get(5) + ", "+ rowData.get(6) + ", "+ rowData.get(7) + ", "+ rowData.get(4) + ", (SELECT ID FROM NomeEspecie WHERE UPPER(Especie) = UPPER(" + especie + ")), (SELECT ID FROM TipoCultura WHERE UPPER(Designacao) = UPPER(" + tipoCulturaDesignacao + ")));");
            culturaId++;
            
        }
        return resultList;
    }

    private static LinkedHashSet<String> insertFatorProducao(LinkedHashSet<List<String>> sheetData) {

        LinkedHashSet<String> resultList = new LinkedHashSet<>();
        int aplicacaoId = 0;
        int elementoId = 0;
        int tipoProdutoId = 0;
        int fatorDeProducaoId = 0;
        Set<String> uniqueValues = new HashSet<>();

        for (List<String> rowData : sheetData) {
            String aplicacaoDesignacao = rowData.get(4);
            String tipoProdutoDesignacao = rowData.get(3);

            if (uniqueValues.add(tipoProdutoDesignacao)) {
                resultList.add("INSERT INTO TipoProduto (ID, Designacao) VALUES (" + tipoProdutoId + ", " + tipoProdutoDesignacao + ");");
                tipoProdutoId++;
            }

            resultList.add("INSERT INTO FatorDeProducao (ID, Designacao, Fabricante, Formato, TipoProdutoId) VALUES (" + fatorDeProducaoId + ", " 
            + rowData.get(0) + ", " + rowData.get(1) + ", "+ rowData.get(2) 
            + ", (SELECT ID FROM TipoProduto WHERE UPPER(Designacao) = UPPER(" + tipoProdutoDesignacao + ")));");
            fatorDeProducaoId++;

            if (aplicacaoDesignacao.contains("+")) {
                String[] values = aplicacaoDesignacao.split("\\+");
                for (String value : values) {
                    value = value.replaceAll("'", "");
                    value = "'" + value + "'";
                    if (uniqueValues.add(value)){
                        resultList.add("INSERT INTO Aplicacao (ID, Designacao) VALUES (" + aplicacaoId + ", " + value + ");");
                        aplicacaoId++;
                    }
                    resultList.add("INSERT INTO AplicacaoProduto (FatorDeProducaoId, AplicacaoId) VALUES ((SELECT ID FROM FatorDeProducao WHERE UPPER(Designacao) = UPPER(" 
                    + rowData.get(0) + ")), (SELECT ID FROM Aplicacao WHERE UPPER(Designacao) = UPPER(" + value + ")));");
                }
            } else {
                if (uniqueValues.add(aplicacaoDesignacao)) {
                    resultList.add("INSERT INTO Aplicacao (ID, Designacao) VALUES (" + aplicacaoId + ", " + aplicacaoDesignacao + ");");
                    aplicacaoId++;
                }
                resultList.add("INSERT INTO AplicacaoProduto (FatorDeProducaoId, AplicacaoId) VALUES ((SELECT ID FROM FatorDeProducao WHERE UPPER(Designacao) = UPPER(" 
                + rowData.get(0) + ")), (SELECT ID FROM Aplicacao WHERE UPPER(Designacao) = UPPER(" + aplicacaoDesignacao + ")));");
            }

            for (int i = 5; i < rowData.size(); i++) {
                if (i % 2 == 0) {
                    resultList.add("INSERT INTO ElementoFicha (FatorDeProducaoId, ElementoId, Quantidade) VALUES ((SELECT ID FROM FatorDeProducao WHERE UPPER(Designacao) = UPPER(" 
                    + rowData.get(0) + ")), (SELECT ID FROM Elemento WHERE UPPER(Designacao) = UPPER(" 
                    + rowData.get(i - 1) + ")), '" + String.format("%.1f", Double.valueOf(rowData.get(i)) * 100).replace(",", ".") + "%');");
                    
                } else {
                    if (uniqueValues.add(rowData.get(i))) {
                        resultList.add("INSERT INTO Elemento (ID, Designacao) VALUES (" + elementoId + ", " + rowData.get(i) + ");");
                        elementoId++;
                    }
                }
            }
        }
        return resultList;
    }

    private static LinkedHashSet<String> insertExploracaoAgricula(LinkedHashSet<List<String>> sheetData) {

        LinkedHashSet<String> resultList = new LinkedHashSet<>();
        int tipoEdificioId = 0;
        Set<String> uniqueValues = new HashSet<>();
        for (List<String> rowData : sheetData) {
            String tipoEdificioDesignacao = rowData.get(1);
            if (uniqueValues.add(tipoEdificioDesignacao)) {
                resultList.add("INSERT INTO TipoEdificio (ID, Designacao) VALUES (" + tipoEdificioId + ", " + tipoEdificioDesignacao + ");");
                tipoEdificioId++;
            }
            if (tipoEdificioDesignacao.equals("'Parcela'")) {
                resultList.add("INSERT INTO Parcela (ID, Designacao, Area, QuintaID) VALUES (" + (int) Double.parseDouble(rowData.get(0)) + ", " + rowData.get(2) + ", " + rowData.get(3) + ", " + 0 + ");");
            } else {
                resultList.add("INSERT INTO Edificio (ID, Designacao, Area, Unidade, TipoEdificioID, QuintaID) VALUES (" + (int) Double.parseDouble(rowData.get(0)) + ", " + rowData.get(2) + ", " + rowData.get(3) + ", "+ rowData.get(4) + ", (SELECT ID FROM TipoEdificio WHERE UPPER(Designacao) = UPPER(" + tipoEdificioDesignacao + ")), " + 0 + ");");
            }
        }
        return resultList;
    }

    private static LinkedHashSet<String> insertCulturas(LinkedHashSet<List<String>> sheetData) {

        LinkedHashSet<String> resultList = new LinkedHashSet<>();
        int id = 0;
        for (List<String> rowData : sheetData) {
            resultList.add("INSERT INTO Plantacao (ID, DataInicial, DataFinal, Quantidade, Unidade, ParcelaID, CulturaID) VALUES (" + id + ", " + rowData.get(4) + ", " + rowData.get(5) + ", " + rowData.get(6) + ", " + rowData.get(7) + ", " + (int) Double.parseDouble(rowData.get(0)) + ", (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER(" + rowData.get(2) + ")));");
            id++;
        }
        return resultList;
    }

    private static LinkedHashSet<String> insertOperacoes(LinkedHashSet<List<String>> sheetData) {

        LinkedHashSet<String> resultList = new LinkedHashSet<>();
        int operacaoID = 0;
        int tipoOperacaoId = 0;
        int modoFertilizacaoID = 0;
        Set<String> uniqueValues = new HashSet<>();
        for (List<String> rowData : sheetData) {
            String tipoOperacaoDesignacao = rowData.get(2);
            String modoFertilizacaoDesignacao = rowData.get(3);
            String culturaDesignacao = rowData.get(4);

            if (uniqueValues.add(tipoOperacaoDesignacao)) {
                resultList.add("INSERT INTO TipoOperacao (ID, Designacao) VALUES (" + tipoOperacaoId + ", " + tipoOperacaoDesignacao + ");");
                tipoOperacaoId++;
            }

            if (!modoFertilizacaoDesignacao.equals("NULL")) {
                if (uniqueValues.add(modoFertilizacaoDesignacao)) {
                resultList.add("INSERT INTO ModoFertilizacao (ID, Designacao) VALUES (" + modoFertilizacaoID + ", " + modoFertilizacaoDesignacao + ");");
                modoFertilizacaoID++;
                }
            }   
            
            if(culturaDesignacao.contains(" ")){

                if (!tipoOperacaoDesignacao.equals("'Plantação'") && !tipoOperacaoDesignacao.equals("'Fertilização'") && !tipoOperacaoDesignacao.equals("'Aplicação fitofármaco'")) {
                    resultList.add("INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (" + operacaoID + ", " + rowData.get(5) + ", " + rowData.get(6) + ", " + rowData.get(7) + ", (SELECT ID FROM Plantacao WHERE ParcelaID = " + rowData.get(0) + " AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER(" + culturaDesignacao + ") AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), " + 0 + ", (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER(" + rowData.get(2) + ")), " + rowData.get(0) + ");");
                    operacaoID++;
                } else if (tipoOperacaoDesignacao.equals("'Plantação'")){
                    resultList.add("INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (" + operacaoID + ", " + rowData.get(5) + ", " + rowData.get(6) + ", " + rowData.get(7) + ", (SELECT ID FROM Plantacao WHERE ParcelaID = " + rowData.get(0) + " AND DataInicial = " + rowData.get(5) + " AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER(" + culturaDesignacao + ") AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), " + 0 + ", (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER(" + rowData.get(2) + ")), " + rowData.get(0) + ");");
                    operacaoID++;
                } else if (tipoOperacaoDesignacao.equals("'Aplicação fitofármaco'")) {
                    resultList.add("INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (" + operacaoID + ", " + rowData.get(5) + ", " + rowData.get(6) + ", " + rowData.get(7) + ", (SELECT ID FROM Plantacao WHERE ParcelaID = " + rowData.get(0) + " AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER(" + culturaDesignacao + ") AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), " + 0 + ", (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER(" + rowData.get(2) + ")), " + rowData.get(0) + ");");
                    resultList.add("INSERT INTO AplicacaoFitofarmaco (OperacaoID, FatorDeProducaoID) VALUES (" + operacaoID + ", (SELECT ID FROM FatorDeProducao WHERE UPPER(Designacao) = UPPER(" + rowData.get(8) + ")));");
                    operacaoID++;
                } else {
                    resultList.add("INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (" + operacaoID + ", " + rowData.get(5) + ", " + rowData.get(6) + ", " + rowData.get(7) + ", (SELECT ID FROM Plantacao WHERE ParcelaID = " + rowData.get(0) + " AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER(" + culturaDesignacao + ")) AND ROWNUM = 1), " + 0 + ", (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER(" + rowData.get(2) + ")), " + rowData.get(0) + ");");
                    resultList.add("INSERT INTO Fertilizacao (OperacaoID, ModoFertilizacaoID, FatorDeProducaoID) VALUES (" + operacaoID + ", (SELECT ID FROM ModoFertilizacao WHERE UPPER(Designacao) = UPPER(" + rowData.get(3) + ")), (SELECT ID FROM FatorDeProducao WHERE UPPER(Designacao) = UPPER(" + rowData.get(8) + ")));");
                    operacaoID++;
                }
            } else {

                if (!tipoOperacaoDesignacao.equals("'Plantação'") && !tipoOperacaoDesignacao.equals("'Fertilização'") && !tipoOperacaoDesignacao.equals("'Aplicação fitofármaco'")) {
                    resultList.add("INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (" + operacaoID + ", " + rowData.get(5) + ", " + rowData.get(6) + ", " + rowData.get(7) + ", (SELECT ID FROM Plantacao WHERE ParcelaID = " + rowData.get(0) + " AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum) = UPPER(" + culturaDesignacao + ") AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), " + 0 + ", (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER(" + rowData.get(2) + ")), " + rowData.get(0) + ");");
                    operacaoID++;
                } else if (tipoOperacaoDesignacao.equals("'Plantação'")){
                    resultList.add("INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (" + operacaoID + ", " + rowData.get(5) + ", " + rowData.get(6) + ", " + rowData.get(7) + ", (SELECT ID FROM Plantacao WHERE ParcelaID = " + rowData.get(0) + " AND DataInicial = " + rowData.get(5) + " AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum) = UPPER(" + culturaDesignacao + ") AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), " + 0 + ", (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER(" + rowData.get(2) + ")), " + rowData.get(0) + ");");
                    operacaoID++;
                } else if (tipoOperacaoDesignacao.equals("'Aplicação fitofármaco'")) {
                    resultList.add("INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (" + operacaoID + ", " + rowData.get(5) + ", " + rowData.get(6) + ", " + rowData.get(7) + ", (SELECT ID FROM Plantacao WHERE ParcelaID = " + rowData.get(0) + " AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER(" + culturaDesignacao + ") AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), " + 0 + ", (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER(" + rowData.get(2) + ")), " + rowData.get(0) + ");");
                    resultList.add("INSERT INTO AplicacaoFitofarmaco (OperacaoID, FatorDeProducaoID) VALUES (" + operacaoID + ", (SELECT ID FROM FatorDeProducao WHERE UPPER(Designacao) = UPPER(" + rowData.get(8) + ")));");
                    operacaoID++;
                } else {
                    resultList.add("INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (" + operacaoID + ", " + rowData.get(5) + ", " + rowData.get(6) + ", " + rowData.get(7) + ", (SELECT ID FROM Plantacao WHERE ParcelaID = " + rowData.get(0) + " AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum) = UPPER(" + culturaDesignacao + ") AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), " + 0 + ", (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER(" + rowData.get(2) + ")), " + rowData.get(0) + ");");
                    resultList.add("INSERT INTO Fertilizacao (OperacaoID, ModoFertilizacaoID, FatorDeProducaoID) VALUES (" + operacaoID + ", (SELECT ID FROM ModoFertilizacao WHERE UPPER(Designacao) = UPPER(" + rowData.get(3) + ")), (SELECT ID FROM FatorDeProducao WHERE UPPER(Designacao) = UPPER(" + rowData.get(8) + ")));");
                    operacaoID++;
                }   
            }
        }
        return resultList;
    }
    
    private String findXlsxFile() {
        Path folder = Paths.get(FOLDER_PATH);
        try (Stream<Path> paths = Files.walk(folder)) {
            return paths
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".xlsx"))
                    .findFirst()
                    .map(Path::toString)
                    .orElse(null);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static boolean writeListToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/sql/Inserts.sql", true))) {
            writer.newLine();
            for (String item : insertsList) {
                writer.write(item);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}