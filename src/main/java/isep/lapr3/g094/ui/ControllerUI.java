package isep.lapr3.g094.ui;
import java.io.IOException;
import java.text.ParseException;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;

import isep.lapr3.g094.Main;
import isep.lapr3.g094.Controller.Controller;
import isep.lapr3.g094.Domain.RegaDiaria;
public class ControllerUI {
    private Controller controller;
    ControllerUI(){
        controller = new Controller();
    }
public void mainUI() throws NumberFormatException, IOException, ParseException{
    Scanner scanner = new Scanner(System.in);
    String input;
    int opcao;
    do{
    System.out.println("============Interface do Controlador============");
    System.out.println("1 - Criar plano de rega");
    System.out.println("2 - Voltar ao menu principal");
    System.out.println("3 - Sair");
    System.out.println("================================================");
    input = scanner.nextLine();
    opcao = Integer.parseInt(input);
    }
    while(opcao < 1 || opcao > 3);
       
    switch(opcao){
        case 1 :
            String data;
            Pattern patternData = Pattern.compile("^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])/\\d{4}$");
            Pattern patternHora = Pattern.compile("^(0[0-9]|1[0-9]|2[0-3]):(0[0-9]|[1-5][0-9])$");
            do {
                System.out.print("Insira a data: ");
                input = scanner.nextLine();

            } while (patternData.matcher(input).matches() == false);

            data = input; // assign input to data
            controller.createPlan(data);

            do{
            System.out.println("============Interface do Controlador============");
            System.out.println("1 - Pesquisar rega (data/hora)");
            System.out.println("2 - Voltar ao menu principal");
            System.out.println("3 - Sair");
            System.out.println("================================================");
            input = scanner.nextLine();
            opcao = Integer.parseInt(input);

            }while(opcao < 1 || opcao > 3);

            switch(opcao){
                case 1:
                    String hora;
                    do{
                        System.out.print("\nFormato: dia/mes/ano\n\nInsira a data: ");
                        input = scanner.nextLine();
                        data = input;
                    }while(patternData.matcher(input).matches() == false);
                    do{
                        System.out.print("\nFormato: horas:minutos\n\nInsira a hora: ");
                        input = scanner.nextLine();
                        hora = input;
                    }while(patternHora.matcher(input).matches() == false);
                    Map<RegaDiaria,Integer> lista = controller.search(data, hora);
                    printResults(lista);
                    break;
                case 2: 
                    Main.main(null);
                    break;
                case 3: 
                    System.exit(0);
                    break;
            }
            break;
        case 2 :
            Main.main(null);
            break; 
        case 3 :
            System.exit(0);
            break;
    }
    scanner.close();
}
private void printResults(Map<RegaDiaria,Integer> lista){
    if(lista.isEmpty())
        System.out.println("Nao existem regas nesta hora");
    else
        System.out.println("Existem as seguintes regas nesta hora:\n");
    lista.entrySet().stream().forEach(i ->{
        System.out.println("-> Parcela: " + i.getKey().getParcela() + " Tempo restante: " + i.getValue() + " minutos");
    });

}
}

