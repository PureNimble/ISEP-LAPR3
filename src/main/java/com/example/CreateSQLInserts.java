package com.example;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
//import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
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
            BufferedReader textFileReader = new BufferedReader(new InputStreamReader(textFileInputStream));

            List<String> resutList = new ArrayList<>();

            XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream("src/main/resources/Legacy_Data_v0.xlsx"));
            FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
            List<String> formulas = new ArrayList<>();

            String line;
            while ((line = textFileReader.readLine()) != null) {
                formulas.add(line);
            }
            
            textFileReader.close();
            textFileInputStream.close();

            XSSFSheet sheet = workbook.getSheetAt(0);

            for (int rowIndex = 0; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                XSSFRow row = sheet.getRow(rowIndex);
                if (row != null) {
                    if (rowIndex > 0) {
                        for (String formula : formulas) {
                            int lastCellNum = row.getLastCellNum();
                            if (lastCellNum == -1) {
                                lastCellNum = 0;
                            }
                            int nextColumnIndex = lastCellNum;

                            XSSFCell formulaCell = row.createCell(nextColumnIndex, CellType.FORMULA);
                            formulaCell.setCellFormula(formula);

                            CellValue cellValue = evaluator.evaluate(formulaCell);

                            if (cellValue.getCellType() == CellType.STRING) {
                                resutList.add(cellValue.getStringValue());
                            } else if (cellValue.getCellType() == CellType.NUMERIC) {
                                resutList.add(String.valueOf(cellValue.getNumberValue()));
                            }
                        }
                    }
                }
            }

            writeListToFile(resutList);
            /*FileOutputStream fileOut = new FileOutputStream("src/main/resources/Legacy_Data_v0.xlsx");
            workbook.write(fileOut);
            fileOut.close();
            workbook.close();*/
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeListToFile(List<String> list) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/sql/Inserts.sql"))) {
            for (String item : list) {
                writer.write(item);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
