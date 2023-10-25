package isep.lapr3.g094.ui;

import java.io.IOException;
import java.util.Scanner;
import isep.lapr3.g094.Controller.CreateSQLInserts;
public class MenuUI {
    private ControllerUI controllerUI;
    private CreateSQLInserts createSQLInserts;
    public MenuUI(){
        controllerUI = new ControllerUI();

    }
    public void main() throws NumberFormatException, IOException{
        String input;
        int opcao;
        Scanner scanner = new Scanner(System.in);
        do {
            System.out.println("============Interface Principal============");
            System.out.println("1 - Aceder ao controlador do Sistema de Rega");
            System.out.println("2 - Adicionar informação do sistema legacy");
            System.out.println("3 - Sair");
            System.out.println("================================================");
            input = scanner.nextLine();
            opcao = Integer.parseInt(input);
        } while (opcao < 1 || opcao > 3);

    switch(opcao){
        case 1 :
            controllerUI.main();
            break;
        case 2 : 
            createSQLInserts.createSQLInserts();
            break;
        case 3 :
            System.exit(0);
            break;
    }
}
}
