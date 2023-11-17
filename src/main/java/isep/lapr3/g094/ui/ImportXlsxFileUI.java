package isep.lapr3.g094.ui;

import isep.lapr3.g094.application.controller.ImportController;

public class ImportXlsxFileUI implements Runnable {

    private ImportController importController = new ImportController();
    public void run() {

        if (importController.importXlsx())
            System.out.println("\n-> Ficheiro .Xlsx importado com sucesso");
        else
            System.out.println("\n-> Erro ao importar ficheiro .Xlsx");

    }
}
