package isep.lapr3.g094.Controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
//import java.io.FileOutputStream;
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

    public void createSQLInserts() {

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
                int lastCellNum = (sheet.getRow(0).getLastCellNum()) + 1;
                for (int rowIndex = 0; rowIndex <= sheetLastRowNum; rowIndex++) {
                    XSSFRow row = sheet.getRow(rowIndex);
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

    public static void writeListToFile(LinkedHashSet<String> list) {
        /*try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/sql/Inserts.sql", true))) {
            writer.newLine();*/
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/sql/Inserts.sql"))) {
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
