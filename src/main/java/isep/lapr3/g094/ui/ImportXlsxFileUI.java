package isep.lapr3.g094.ui;

import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedHashSet;
import java.util.List;

import isep.lapr3.g094.application.controller.ImportController;
import isep.lapr3.g094.application.controller.TransformXlsxController;

public class ImportXlsxFileUI implements Runnable {

    private TransformXlsxController transformXlsxController = new TransformXlsxController();

    public void run() {

        try {
            if (transformXlsxController.createInserts())
                System.out.println("\n-> Ficheiro .Xlsx importado com sucesso");
            else
                System.out.println("\n-> Erro ao importar ficheiro .Xlsx");
        } catch (ClassNotFoundException | IOException | SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}
