package isep.lapr3.g094.ui;
import java.io.IOException;
import java.text.ParseException;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;

import isep.lapr3.g094.Main;
import isep.lapr3.g094.controller.IrrigationController;
import isep.lapr3.g094.domain.DailyIrrigation;
public class IrrigationUI {
    private IrrigationController controller;

    IrrigationUI(){
        controller = new IrrigationController();
    }

    public void mainUI() throws NumberFormatException, IOException, ParseException{
        try (Scanner scanner = new Scanner(System.in)) {
            String input;
            int opcao;
            do {
                System.out.println("============Interface do Controlador============");
                System.out.println("1 - Criar plano de rega");
                System.out.println("2 - Voltar ao menu principal");
                System.out.println("3 - Sair");
                System.out.println("================================================");
                input = scanner.nextLine();
                opcao = Integer.parseInt(input);
            } while(opcao < 1 || opcao > 3);

                switch(opcao){
                    case 1 :
                        String data;
                        Pattern patternData = Pattern.compile("^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])/\\d{4}$");
                        Pattern patternHora = Pattern.compile("^(0[0-9]|1[0-9]|2[0-3]):(0[0-9]|[1-5][0-9])$");
                        do {
                            try {
                                System.out.print("\nFormato: dia/mes/ano\n\nInsira a data: ");
                                input = scanner.nextLine();
                                if (!patternData.matcher(input).matches()) {
                                    throw new Exception("Formato de data invalido. Por favor insira uma data no formato dd/mm/yyyy");
                                }
                            } catch (Exception e) {
                                System.out.println(e.getMessage());
                            }
                        } while (!patternData.matcher(input).matches());

                        data = input; // assign input to data
                        controller.createPlan(data);
                        do {
                            do{
                            System.out.println("============Interface do Controlador============");
                            System.out.println("1 - Pesquisar rega (data/hora)");
                            System.out.println("2 - Voltar ao menu principal");
                            System.out.println("3 - Sair");
                            System.out.println("================================================");
                            input = scanner.nextLine();
                            opcao = Integer.parseInt(input);

                            } while(opcao < 1 || opcao > 3);

                            switch(opcao){
                                case 1:
                                    String hora;

                                    do {
                                        try {
                                            System.out.print("\nFormato: dia/mes/ano\n\nInsira a data: ");
                                            input = scanner.nextLine();
                                            if (patternData.matcher(input).matches()) {
                                                data = input;
                                            } else {
                                                throw new Exception("Formato de data invalido. Por favor insira uma data no formato dd/mm/yyyy");
                                            }
                                        } catch (Exception e) {
                                            System.out.println(e.getMessage());
                                        }
                                    } while (!patternData.matcher(input).matches());

                                    do {
                                        try {
                                            System.out.print("\nFormato: hh:mm\n\nInsira a hora: ");
                                            input = scanner.nextLine();
                                            if (!patternHora.matcher(input).matches()) {
                                                throw new Exception("Formato de hora invalido. Por favor insira uma hora no formato hh:mm");
                                            }
                                        } catch (Exception e) {
                                            System.out.println(e.getMessage());
                                        }
                                    } while (!patternHora.matcher(input).matches());

                                    hora = input;
                                    Map<DailyIrrigation,Integer> lista = controller.search(data, hora);
                                    if(lista != null) printResults(lista);
                                    break;
                                case 2: 
                                    Main.main(null);
                                    break;
                                case 3: 
                                    System.exit(0);
                                    break;
                            }
                        } while (true);
                    case 2 :
                        Main.main(null);
                        break; 
                    case 3 :
                        System.exit(0);
                        break;
            }
        }
    }

    private void printResults(Map<DailyIrrigation,Integer> lista){
        if(lista.isEmpty())
            System.out.println("Nao existem regas nesta hora");
        else
            System.out.println("Existem as seguintes regas nesta hora:\n");
        lista.entrySet().stream().forEach(i ->{
            System.out.println("-> Parcela: " + i.getKey().getParcela() + " Tempo restante: " + i.getValue() + " minutos");
        });

    }
}

