package com.example.Controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class CreateSQLInserts {

    public static void createSQLInserts() {

        try {

            FileInputStream textFileInputStream = new FileInputStream("src/main/resources/formulas.txt");
            XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream("src/main/resources/Legacy_Data_v0.xlsx"));
            BufferedReader textFileReader = new BufferedReader(new InputStreamReader(textFileInputStream));
            FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();

            LinkedHashSet<String> resutList = new LinkedHashSet<>();

            List<List<String>> formulas = new ArrayList<>();
            List<String> currentSheetFormulas = new ArrayList<>();
            String line;
            while ((line = textFileReader.readLine()) != null) {
                if (line.startsWith("#")) {
                    if (!currentSheetFormulas.isEmpty()) {
                        formulas.add(currentSheetFormulas);
                    }
                    currentSheetFormulas = new ArrayList<>();
                    continue;
                } else {
                    currentSheetFormulas.add(line);
                }
            }
            if (!currentSheetFormulas.isEmpty()) {
                formulas.add(currentSheetFormulas);
            }
            
            textFileReader.close();
            textFileInputStream.close();

            int workbookNum = workbook.getNumberOfSheets();

            for (int sheetIndex = 0; sheetIndex < workbookNum; sheetIndex++) {
                XSSFSheet sheet = workbook.getSheetAt(sheetIndex);
                List<String> processingFormulas = formulas.get(sheetIndex);
                int sheetLastRowNum = sheet.getLastRowNum();
                for (int rowIndex = 0; rowIndex <= sheetLastRowNum; rowIndex++) {
                    XSSFRow row = sheet.getRow(rowIndex);
                    int lastCellNum = (sheet.getRow(0).getLastCellNum()) + 1;
                    if (row != null) {
                        if (rowIndex > 0) {
                            for (String formula : processingFormulas) {            
                                XSSFCell formulaCell = row.createCell(lastCellNum, CellType.FORMULA);
                                formulaCell.setCellFormula(formula);
            
                                CellValue cellValue = evaluator.evaluate(formulaCell);
                                
                                if (cellValue.getCellType() == CellType.STRING) {
                                    resutList.add(cellValue.getStringValue());
                                } else if (cellValue.getCellType() == CellType.NUMERIC) {
                                    resutList.add(String.valueOf(cellValue.getNumberValue()));
                                }
                                lastCellNum++;
                            }
                        }
                    }
                }
                if (sheetIndex == 1) resutList.addAll(insertFatorProducao(fatorProducaoSheet(sheet)));
            }

            writeListToFile(resutList);
            FileOutputStream fileOut = new FileOutputStream("src/main/resources/Legacy_Data_v0.xlsx");
            workbook.write(fileOut);
            fileOut.close();
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static LinkedHashSet<List<String>> fatorProducaoSheet(XSSFSheet sheet) {
        int lastRowNum = sheet.getLastRowNum();
        LinkedHashSet<List<String>> sheetData = new LinkedHashSet<>();
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
                            if (cellType == CellType.STRING) {
                                String cellValue = cell.getStringCellValue().strip();
                                rowData.add(cellValue);
                            } else if (cellType == CellType.NUMERIC) {
                                double cellValue = cell.getNumericCellValue();
                                rowData.add(String.valueOf(cellValue).strip());
                            }
                        }
                    }
                    sheetData.add(rowData);
                }
            }
        }
        return sheetData;
    }

    private static LinkedHashSet<String> insertFatorProducao(LinkedHashSet<List<String>> sheetData){
        LinkedHashSet<String> resutList = new LinkedHashSet<>();

        for (List<String> rowData : sheetData) {

            if (rowData.get(4).contains("+")){
                String[] values = rowData.get(4).split("\\+");
                for (String value : values) {
                    String insertAplicacao = "INSERT INTO Aplicacao (Designacao) VALUES ('";
                    insertAplicacao += value.trim() + "');";
                    resutList.add(insertAplicacao);
                    String insertAplicacaoProduto = "INSERT INTO AplicacaoProduto (FatorDeProducaoDesignacao, AplicacaoDesignacao) VALUES ('";
                    insertAplicacaoProduto += rowData.get(0) + "', '" + value.trim() + "');";
                    resutList.add(insertAplicacaoProduto);
                }

            } else {    
                String insertAplicacao = "INSERT INTO Aplicacao (Designacao) VALUES ('";
                insertAplicacao += rowData.get(4) + "');";
                resutList.add(insertAplicacao);

                String insertAplicacaoProduto = "INSERT INTO AplicacaoProduto (FatorDeProducaoDesignacao, AplicacaoDesignacao) VALUES ('";
                insertAplicacaoProduto += rowData.get(0) + "', '" + rowData.get(4) + "');";
                resutList.add(insertAplicacaoProduto);
            }
            for (int i = 5; i < rowData.size(); i++) {
                String insert;
                if (i%2 == 0) {
                    insert = "INSERT INTO ElementoFicha (FichaTecnicaFatorDeProducaoDesignacao, ElementoDesignacao, Quantidade) VALUES ('";
                    insert += rowData.get(0) + "', '" + rowData.get(i-1) + "' , '" + String.format("%.1f", Double.valueOf(rowData.get(i)) * 100) + "%" +
                    "');";
                } else {
                    insert = "INSERT INTO Elemento (Designacao) VALUES ('";
                    insert += rowData.get(i) + "');";
                    resutList.add(insert);
                } 
                resutList.add(insert);
            }
        }
        return resutList;
    }

    private static void writeListToFile(LinkedHashSet<String> list) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/sql/Inserts.sql", true))) {
            writer.newLine();
        //try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/sql/Inserts.sql"))) {
            for (String item : list) {
                //if (!item.isEmpty()) {
                    writer.write(item);
                    writer.newLine();
                //}
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
