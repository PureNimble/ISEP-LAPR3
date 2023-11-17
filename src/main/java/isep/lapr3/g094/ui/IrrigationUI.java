package isep.lapr3.g094.ui;

import java.text.ParseException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;
import isep.lapr3.g094.application.controller.CreatePlanController;
import isep.lapr3.g094.application.controller.ImportController;
import isep.lapr3.g094.domain.irrigation.IrrigationDate;
import isep.lapr3.g094.domain.irrigation.IrrigationHour;
import isep.lapr3.g094.domain.irrigation.IrrigationSector;

public class IrrigationUI implements Runnable {

    private CreatePlanController createPlanController = new CreatePlanController();
    private ImportController importController = new ImportController();
    private static final Pattern PATTERN_DATA = Pattern.compile("^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[012])/((19|20)\\d\\d)$");
    private static final Pattern PATTERN_HORA = Pattern.compile("^(0[0-9]|1[0-9]|2[0-3]):(0[0-9]|[1-5][0-9])$");
    private static final Scanner scanner = new Scanner(System.in);

    public void run() {

        int choice = -1;

        do {
            System.out.println("\n============Interface do Controlador============");
            System.out.println("1. Criar plano de rega");
            System.out.println("2. Ver plano de rega atual");
            System.out.println("3. Ver regas em execução");
            System.out.println("0. Voltar ao menu principal\n");
            System.out.print("Insira uma opção: ");
            try {
                choice = scanner.nextInt();
                scanner.nextLine();
                switch (choice) {
                    case 1:
                        createPlan();
                        break;
                    case 2:
                        printPlan();
                        break;
                    case 3:
                        try {
                            searchWatering();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        break;
                    case 0:
                        break;
                    default:
                        System.out.println("Opção Inválida, por favor insira uma opção válida (0 - 3)");
                        break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Opção Inválida, por favor insira uma opção válida (0 - 3)");
                scanner.nextLine();
            }
        } while (choice != 0);

    }

    private void createPlan() {

        String data = getValidatedInput(PATTERN_DATA, "\nFormato: dia/mes/ano\n\nInsira a data: ",
                "Formato de data invalido. Por favor insira uma data no formato dd/mm/yyyy");

        importController.importIrrigationPlan();
        createPlanController.createPlan(data);
        if (createPlanController.getIrrigationSectors().isEmpty() || createPlanController.getIrrigationDates().isEmpty()
                || createPlanController.getIrrigationHours().isEmpty()) {
            System.out.println("Não foi possivel criar o plano de rega");
        } else
            System.out.println("Plano de rega criado com sucesso");

    }

    private void searchWatering() throws ParseException {

        if (!createPlanController.getIrrigationSectors().isEmpty()) {
            String data = getValidatedInput(PATTERN_DATA, "\nFormato: dia/mes/ano\n\nInsira a data: ",
                    "Formato de data invalido. Por favor insira uma data no formato dd/mm/yyyy");
            String hora = getValidatedInput(PATTERN_HORA, "\nFormato: hh:mm\n\nInsira a hora: ",
                    "Formato de hora invalido. Por favor insira uma hora no formato hh:mm");

            Map<IrrigationSector, Integer> lista = createPlanController.searchIrrigation(data, hora);
            if (lista != null)
                printResults(lista);
            else
                System.out.println(
                        "Não existem regas para essa data no plano de rega atual, por favor insira uma data válida. (30 dias a partir da data de criação do plano)");

        } else
            System.out.println("Ainda não existe um plano criado");
    }

    private String getValidatedInput(Pattern pattern, String prompt, String errorMessage) {
        String input;

        do {
            try {
                System.out.print(prompt);
                input = scanner.nextLine();
                if (!pattern.matcher(input).matches()) {
                    throw new Exception(errorMessage);
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
                continue;
            }
            return input;
        } while (true);

    }

    private void printPlan() {
        List<IrrigationSector> irrigationSectors = createPlanController.getIrrigationSectors();
        List<IrrigationHour> irrigationHours = createPlanController.getIrrigationHours();
        List<IrrigationDate> irrigationDates = createPlanController.getIrrigationDates();
        if (irrigationSectors.isEmpty() || irrigationHours.isEmpty())
            System.out.println("Ainda não existe um plano criado");

        else {
            System.out.println("\n=============Plano de Rega=============");
            System.out.println("Datas de rega:");
            System.out.println(irrigationDates.get(0) + " a " + irrigationDates.get(irrigationDates.size() - 1));
            System.out.println("\nHoras de rega:");
            irrigationHours.forEach(System.out::println);
            System.out.println("\nSetores a regar:");
            irrigationSectors.forEach(System.out::println);
            System.out.println("=======================================");
        }
    }

    private void printResults(Map<IrrigationSector, Integer> lista) {

        if (lista.isEmpty())
            System.out.println("Nao existem regas nesta hora");
        else
            System.out.println("Existem as seguintes regas nesta hora:\n");
        lista.entrySet().stream().forEach(i -> {
            System.out.println(
                    "-> Parcela: " + i.getKey().getSector() + " Tempo restante: " + i.getValue() + " minutos");
        });
    }
}