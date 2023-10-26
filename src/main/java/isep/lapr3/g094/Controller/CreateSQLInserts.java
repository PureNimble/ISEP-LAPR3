package isep.lapr3.g094.Controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class CreateSQLInserts {

    private final static String FOLDER_PATH = "src/main/resources/";
    private static LinkedHashSet<String> insertsList = new LinkedHashSet<>();

    public void createSQLInserts() {
        extractXlsx(findXlsxFile());
        writeListToFile();
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
                        //insertsList.addAll(insertExploracaoAgricula(readSheet(sheet)));
                        break;
                    case 3:
                        //insertsList.addAll(insertCulturas(readSheet(sheet)));
                        break;
                    case 4:
                        //insertsList.addAll(insertOperacoes(readSheet(sheet)));
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
        for (int rowIndex = 0; rowIndex <= lastRowNum; rowIndex++) {
            XSSFRow row = sheet.getRow(rowIndex);
            if (row != null) {
                if (rowIndex > 0) {
                    int lastCellNum = row.getLastCellNum();
                    List<String> rowData = new ArrayList<>();
                    for (int cellIndex = 0; cellIndex < lastCellNum; cellIndex++) {
                        XSSFCell cell = row.getCell(cellIndex);
                        if (cell != null) {
                            CellType cellType = cell.getCellType();
                            if (cellType != CellType.BLANK || sheet.getSheetName().contains("Fator")) {
                                if (cellType == CellType.STRING) {
                                    String cellValue = cell.getStringCellValue().replaceAll("'", "''").strip();
                                    rowData.add(cellValue);
                                } else if (cellType == CellType.NUMERIC) {
                                    double cellValue = cell.getNumericCellValue();
                                    String stringValue = String.valueOf(cellValue);
                                    String cleanedValue = stringValue.replaceAll("'", "''").strip();
                                    rowData.add(cleanedValue);
                                }
                            } else rowData.add("NULL");
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
            for (int i = 0; i < rowData.size(); i++) {
                if (rowData.get(i) != "NULL") {
                    rowData.set(i, "'" + rowData.get(i) + "'");
                }
            }

            String especie = rowData.get(0);
            String nomeComum = rowData.get(1);
            String tipoCulturaDesignacao = rowData.get(3);

            if (uniqueValues.add(tipoCulturaDesignacao)) {
                resultList.add("INSERT INTO TipoCultura (ID, Designacao) VALUES (" + tipoCulturaId + ", " + tipoCulturaDesignacao + ");");
                tipoCulturaId++;
            }

            if (uniqueValues.add(especie)) {
                resultList.add("INSERT INTO NomeEspecie (ID, NomeComum, Especie, TipoCulturaID) VALUES (" + nomeEspecieId + ", " + nomeComum + ", " + especie + ", (SELECT ID FROM TipoCultura WHERE UPPER(Designacao) LIKE UPPER(" + tipoCulturaDesignacao + ")));");
                nomeEspecieId++;
            }

            if (resultList.add("INSERT INTO Cultura (ID, Variedade, Poda, Floracao, Colheita, Sementeira, NomeEspecieID) VALUES (" + culturaId + ", " + rowData.get(2) + ", " + rowData.get(5) + ", "+ rowData.get(6) + ", "+ rowData.get(7) + ", "+ rowData.get(4) + ", (SELECT ID FROM NomeEspecie WHERE UPPER(Especie) LIKE UPPER(" + rowData.get(0) + ")));")){
                culturaId++;
            }
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
            resultList.add("INSERT INTO TipoProduto (ID, Designacao) VALUES (" + tipoProdutoId + ", '" + tipoProdutoDesignacao + "');");
            tipoProdutoId++;
        }

        resultList.add("INSERT INTO FatorDeProducao (ID, Designacao, Fabricante, Formato, tipoProdutoId) VALUES (" + fatorDeProducaoId + ", '" 
        + rowData.get(0) + "', '" + rowData.get(1) + "', '"+ rowData.get(2) 
        + "', (SELECT ID FROM TipoProduto WHERE UPPER(Designacao) LIKE UPPER('" + tipoProdutoDesignacao + "')));");
        fatorDeProducaoId++;

        if (aplicacaoDesignacao.contains("+")) {
            String[] values = aplicacaoDesignacao.split("\\+");
            for (String value : values) {
                if (uniqueValues.add(value)){
                    resultList.add("INSERT INTO Aplicacao (ID, Designacao) VALUES (" + aplicacaoId + ", '" + value + "');");
                    aplicacaoId++;
                }
                resultList.add("INSERT INTO AplicacaoProduto (fatorDeProducaoId, aplicacaoId) VALUES ((SELECT ID FROM FatorDeProducao WHERE UPPER(Designacao) LIKE UPPER('" 
                + rowData.get(0) + "')), (SELECT ID FROM Aplicacao WHERE UPPER(Designacao) LIKE UPPER('" + value + "')));");
            }
        } else {
            if (uniqueValues.add(aplicacaoDesignacao)) {
                resultList.add("INSERT INTO Aplicacao (ID, Designacao) VALUES (" + aplicacaoId + ", '" + aplicacaoDesignacao + "');");
                aplicacaoId++;
            }
            resultList.add("INSERT INTO AplicacaoProduto (fatorDeProducaoId, aplicacaoId) VALUES ((SELECT ID FROM FatorDeProducao WHERE UPPER(Designacao) LIKE UPPER('" 
            + rowData.get(0) + "')), (SELECT ID FROM Aplicacao WHERE UPPER(Designacao) LIKE UPPER('" + rowData.get(4) + "')));");
        }

        for (int i = 5; i < rowData.size(); i++) {
            if (i % 2 == 0) {
                resultList.add("INSERT INTO ElementoFicha (fatorDeProducaoId, elementoId, Quantidade) VALUES ((SELECT ID FROM FatorDeProducao WHERE UPPER(Designacao) LIKE UPPER('" 
                + rowData.get(0) + "')), (SELECT ID FROM Elemento WHERE UPPER(Designacao) LIKE UPPER('" 
                + rowData.get(i - 1) + "')), '" + String.format("%.1f", Double.valueOf(rowData.get(i)) * 100) + "%');");
                
            } else {
                if (uniqueValues.add(rowData.get(i))) {
                    resultList.add("INSERT INTO Elemento (ID, Designacao) VALUES (" + elementoId + ", '" + rowData.get(i) + "');");
                    elementoId++;
                }
            }
        }
    }
    return resultList;
}
    

    private static void writeListToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/sql/Inserts.sql", true))) {
            writer.newLine();
            for (String item : insertsList) {
                writer.write(item);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
