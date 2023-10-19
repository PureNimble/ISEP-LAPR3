package com.example;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
//import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
//import java.util.Scanner;
import java.util.Set;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.DateUtil;

public class CreateSQLInserts {

    public static void createSQLInserts() {

        String filePath = searchFile();
        List<String> tables = readTablesFile();

        if (filePath != null && tables != null) {
            Map<String, Set<List<String>>> sheetDataMap = new HashMap<>();
    
            if (filePath.endsWith(".xlsx")) {
                sheetDataMap = readFromXLSX(filePath);
            } else {
                //readFromCSV(filePath);
            }

            createInsertStatements(tables, sheetDataMap);
        }
    }

    /*private static void readFromCSV(String filePath) {

        try (Scanner scanner = new Scanner(new File(filePath))) {
    
            Set<List<String>> setOfLists = new HashSet<>();

            if (scanner.hasNextLine()) {
                scanner.nextLine();
            }
            
            while (scanner.hasNextLine()) {
                List<String> values = Arrays.asList(scanner.nextLine().split(","));
                setOfLists.add(values);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

    private static Map<String, Set<List<String>>> readFromXLSX(String filePath) {

        Map<String, Set<List<String>>> sheetDataMap = new HashMap<>();
        try (FileInputStream file = new FileInputStream(new File(filePath))) {
            try (XSSFWorkbook workbook = new XSSFWorkbook(file)) {

                for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                    XSSFSheet sheet = workbook.getSheetAt(i);
                    String sheetName = sheet.getSheetName();

                    Set<List<String>> setOfLists = new HashSet<>();

                    boolean isFirstRow = true;
                    for (Row row : sheet) {
                        if (isFirstRow) {
                            isFirstRow = false;
                            continue;
                        }
                        List<String> values = new ArrayList<>();
                        for (Cell cell : row) {
                            String value = getCellValueAsString(cell);
                            values.add(value);
                        }
                        setOfLists.add(values);
                    }

                    sheetDataMap.put(sheetName, setOfLists);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sheetDataMap;
    }

    private static String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return "";
        }

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                }
                return String.valueOf(cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            default:
                return "";
        }
    }

    private static void createInsertStatements(List<String> tables, Map<String, Set<List<String>>> sheetDataMap) {

        try (PrintWriter writer = new PrintWriter(new FileWriter("src/main/sql/Inserts.txt"))) {
            Iterator<String> tableIterator = tables.iterator();
            for (Map.Entry<String, Set<List<String>>> entry : sheetDataMap.entrySet()) {

                Set<List<String>> setOfLists = entry.getValue();
                String table = tableIterator.next();
                if (!tableIterator.hasNext()) {
                    tableIterator = tables.iterator();
                }

                for (List<String> list : setOfLists) {
                    StringBuilder sql = new StringBuilder();
                    sql.append("INSERT INTO ");
                    sql.append(table);
                    sql.append(" VALUES (");
                    for (int i = 0; i < list.size(); i++) {
                        sql.append("'");
                        sql.append(list.get(i));
                        sql.append("'");
                        if (i < list.size() - 1) {
                            sql.append(", ");
                        }
                    }
                    sql.append(");");
                    writer.println(sql.toString());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String searchFile() {

        String caminhoInicial = "src/main/resources/";
        File receivedFolder = new File(caminhoInicial);
        
        if (receivedFolder.exists() && receivedFolder.isDirectory()) {

            File[] files = receivedFolder.listFiles();
    
            for (File file : files) {
                String fileName = file.getName();
                if (fileName.endsWith(".csv") || fileName.endsWith(".xlsx")) {

                    return caminhoInicial + fileName;
                }
            }
    
            System.out.println("Nenhum ficheiro suportado encontrado na pasta (ficheiros suportados: .csv, .xlsx)");

        } else System.out.println("Pasta não encontrada");
    
        return null;
    }

    private static List<String> readTablesFile() {

        String filePath = "src/main/resources/tables.txt";
        List<String> values = new ArrayList<>();
    
        File file = new File(filePath);
        if (!file.exists()) {
            System.out.println("Ficheiro não encontrado: " + filePath);
            return null;
        }
    
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine();
            String[] items = line.split(",");
            for (String item : items) {
                values.add(item.trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    
        return values;
    }
}
