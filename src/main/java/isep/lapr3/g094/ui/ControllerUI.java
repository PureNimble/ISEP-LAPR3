package isep.lapr3.g094.ui;
import java.io.IOException;
import java.util.List;
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
public void main() throws NumberFormatException, IOException{
    Scanner scanner = new Scanner(System.in);
    controller.importData();
    String input;
    int opcao;
    do{
    System.out.println("============Interface do Controlador============");
    System.out.println("1 - Pesquisar rega (Data/Hora)");
    System.out.println("2 - Voltar ao menu principal");
    System.out.println("3 - Sair");
    System.out.println("================================================");
    input = scanner.nextLine();
    opcao = Integer.parseInt(input);
    }
    while(opcao < 1 || opcao > 3);
       
    switch(opcao){
        case 1 :
            String hora;
            int dia;
            do{
                System.out.println("Insira o dia");
                input = scanner.nextLine();
                dia = Integer.parseInt(input);
            }while(dia < 1 || dia > 30);
            Pattern pattern = Pattern.compile("^\\d{1,2}:\\d{1,2}$");
            do{
                System.out.println("Insira a hora (Ex: 12:00)");
                hora = scanner.nextLine();
            }while(pattern.matcher(hora).matches() == false);

            printResults(controller.search(dia,hora));
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
private void printResults(List<RegaDiaria> lista){
    if(lista.isEmpty())
        System.out.println("Nao existem regas nesta hora");
    else
        System.out.println("Existem as seguintes regas nesta hora:\n");
    lista.stream().forEach(i ->{
        System.out.println("-> Parcela: " + i.getParcela() + " Tempo restante: " + i.getDuracao());
    });

}
}

