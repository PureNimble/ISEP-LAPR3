package isep.lapr3.g094.domain.imports;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Import {

    public List<String> importTxtFile(String filePath, boolean skip) {

        InputStream stream = getClass().getClassLoader().getResourceAsStream(filePath);
        List<String> output = new ArrayList<>();
        Scanner sc = new Scanner(stream);

        if (skip)
            sc.nextLine();
        while (sc.hasNext()) {
            output.add(sc.nextLine());
        }
        sc.close();

        return output;
    }

    public List<LinkedHashSet<List<String>>> importXlsxFile(String filePath) {

        InputStream stream = getClass().getClassLoader().getResourceAsStream(filePath);
        List<LinkedHashSet<List<String>>> xlsxData = new ArrayList<>();
        try (XSSFWorkbook workbook = new XSSFWorkbook(stream)) {
            int workbookNum = workbook.getNumberOfSheets();

            for (int sheetIndex = 0; sheetIndex < workbookNum; sheetIndex++) {
                XSSFSheet sheet = workbook.getSheetAt(sheetIndex);
                xlsxData.add(importSheet(sheet));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return xlsxData;
    }

    private LinkedHashSet<List<String>> importSheet(XSSFSheet sheet) {

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
                        String processedCell = processCell(cell, sheet.getSheetName());
                        if (processedCell != null) {
                            rowData.add(processedCell);
                        }
                    }
                    sheetData.add(rowData);
                }
            }
        }
        return sheetData;
    }

    private String processCell(XSSFCell cell, String sheetName) {
        if (cell == null) {
            return sheetName.contains("Fator") ? null : "NULL";
        }
        String cellValue = "";
        CellType cellType = cell.getCellType();
        if (cellType == CellType.STRING) {
            cellValue = cell.getStringCellValue().replaceAll("\u00A0", "").replace("'", "''");
        } else {
            if (DateUtil.isCellDateFormatted(cell)) {
                Date dateValue = cell.getDateCellValue();
                cellValue = new SimpleDateFormat("MM-dd-yyyy").format(dateValue);
            } else {
                double doubleValue = cell.getNumericCellValue();
                cellValue = String.valueOf(doubleValue).strip();
                if (cellValue.endsWith(".0")) {
                    cellValue = cellValue.substring(0, cellValue.length() - 2);
                }
            }
        }
        return cellValue;
    }

    public List<String> importFilesNames(String folderPath) {
        URL url = getClass().getClassLoader().getResource(folderPath);
        File folder = new File(url.getPath());
        String[] fileNames = folder.list();
        return Arrays.stream(fileNames).map(fileName -> folderPath + "/" + fileName).collect(Collectors.toList());
    }

}
