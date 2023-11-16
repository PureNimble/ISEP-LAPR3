package isep.lapr3.g094.ui;
import java.io.IOException;
import java.text.ParseException;

import isep.lapr3.g094.Main;
import isep.lapr3.g094.application.controller.CreateSQLInserts;

public class CreateSQLInsertsUI {

    private CreateSQLInserts createSQLInserts;

    public CreateSQLInsertsUI(){
        createSQLInserts = new CreateSQLInserts();
    }
    public void mainUI() throws NumberFormatException, IOException, ParseException{

        if(createSQLInserts.createSQLInserts())
            System.out.println("\n" +"-> Ficheiro importado com sucesso" + "\n");
        else
            System.out.println("\n" +"-> Erro ao importar ficheiro" + "\n");
        
        //Go back to main
        Main.main(null);

    } 
}
