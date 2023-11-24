package isep.lapr3.g094.ui;

import java.sql.SQLException;
import java.text.ParseException;

import isep.lapr3.g094.application.controller.TransformXlsxController;

public class ImportXlsxFileUI implements Runnable {

    private TransformXlsxController transformXlsxController = new TransformXlsxController();

    public void run() {
        try {
            transformXlsxController.createInserts();
            System.out.println("\n-> Ficheiro .Xlsx importado com sucesso");
        } catch (SQLException | ParseException e) {
            System.out.println("\n-> Erro ao importar ficheiro .Xlsx");
            e.getMessage();
        }
    }
}
