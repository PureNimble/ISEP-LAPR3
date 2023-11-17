package isep.lapr3.g094.ui;

import isep.lapr3.g094.application.controller.CreateSQLInserts;

public class CreateSQLInsertsUI implements Runnable {

    private final CreateSQLInserts createSQLInserts = new CreateSQLInserts();

    public void run() {

        if (createSQLInserts.createSQLInserts())
            System.out.println("\n" + "-> Ficheiro importado com sucesso" + "\n");
        else
            System.out.println("\n" + "-> Erro ao importar ficheiro" + "\n");

    }
}
