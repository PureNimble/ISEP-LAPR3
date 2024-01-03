package isep.lapr3.g094.ui;

import java.text.ParseException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang3.time.DateUtils;

import isep.lapr3.g094.application.controller.IrrigationPlanController;
import isep.lapr3.g094.application.controller.ImportController;
import isep.lapr3.g094.domain.Pair;
import isep.lapr3.g094.domain.irrigation.IrrigationDate;
import isep.lapr3.g094.domain.irrigation.IrrigationHour;
import isep.lapr3.g094.domain.irrigation.IrrigationSector;
import isep.lapr3.g094.ui.menu.MenuItem;
import isep.lapr3.g094.ui.utils.Utils;
import net.bytebuddy.asm.Advice.Local;

public class IrrigationUI implements Runnable {

    private IrrigationPlanController irrigationPlanController = new IrrigationPlanController();
    private ImportController importController = new ImportController();
    private static final Pattern PATTERN_HORA = Pattern.compile("^(0[0-9]|1[0-9]|2[0-3]):(0[0-9]|[1-5][0-9])$");

    public void run() {
        List<MenuItem> options = new ArrayList<>();
        int option;
        do {
            options.clear();

            options.add(new MenuItem("Importar plano de rega", this::createPlan));
            if (!irrigationPlanController.getIrrigationSectors().isEmpty()
                    || !irrigationPlanController.getIrrigationHours().isEmpty()) {
                options.add(new MenuItem("Ver plano de rega atual", this::printPlan));
                options.add(new MenuItem("Ver regas em execução", () -> {
                    try {
                        searchWatering();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }));
                options.add(new MenuItem("Executar regas", this::executeWatering));
            }

            option = Utils.showAndSelectIndex(options, "\n=========Interface do Controlador de Rega=========");

            if (option != -1) {
                options.get(option).run();
            }

        } while (option != -1);

    }

    private void createPlan() {

        Date data = Utils.readDateFromConsole("Insira a data de inicio do plano de rega: (dd/mm/yyyy)");

        if (importController.importIrrigationPlan() && irrigationPlanController.createPlan(data)) {
            System.out.println("Plano de rega criado com sucesso");

        } else
            System.out.println("Não foi possivel criar o plano de rega");

    }

    private void searchWatering() throws ParseException {

        Date data = Utils.readDateFromConsole("Insira a data da rega que deseja procurar: (dd/mm/yyyy)");
        String hora = getValidatedHour("\nFormato: hh:mm\n\nInsira a hora: ",
                "Formato de hora invalido. Por favor insira uma hora no formato hh:mm");

        Map<IrrigationSector, Pair<Integer, Boolean>> lista = irrigationPlanController.searchIrrigation(data, hora);
        if (lista != null)
            printResults(lista);
        else
            System.out.println(
                    "Não existem regas para essa data no plano de rega atual, por favor insira uma data válida. (30 dias a partir da data de criação do plano)");
    }

    private String getValidatedHour(String prompt, String errorMessage) {

        String input;
        do {
            try {
                input = Utils.readLineFromConsole(prompt);
                if (!PATTERN_HORA.matcher(input).matches()) {
                    throw new Exception(errorMessage);
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
                continue;
            }
            return input;
        } while (true);

    }

    private void executeWatering() {

        List<String> options = new ArrayList<>();
        options.add("Executar plano de rega");
        options.add("Executar dia");
        Integer option = Utils.showAndSelectIndex(options, "\n=========Interface do Controlador de Rega=========");
        boolean confirm = false;
        if (option == -1)
            return;
        if (option == 0) {
            confirm = irrigationPlanController.executeWatering();
        } else {
            Date date = Utils.readDateFromConsole("Indique o dia: (dd/mm/yyyy)");
            LocalTime time = Utils.readTimeFromConsole("Indique a hora: (hh:mm)");
            confirm = irrigationPlanController.executeDay(date, time);
        }

        if (confirm) {
            System.out.println("Regas registadas com sucesso");
        } else
            System.out.println("Não foi possível registar todas as regas");
    }

    private void printPlan() {
        List<IrrigationSector> irrigationSectors = irrigationPlanController.getIrrigationSectors();
        List<IrrigationHour> irrigationHours = irrigationPlanController.getIrrigationHours();
        List<IrrigationDate> irrigationDates = irrigationPlanController.getIrrigationDates();
        System.out.println("\n===========================Plano de Rega===========================");
        System.out.println("Datas de rega:");
        System.out.println(irrigationDates.get(0) + " a " + irrigationDates.get(irrigationDates.size() - 1));
        System.out.println("\nHoras de rega:");
        irrigationHours.forEach(System.out::println);
        System.out.println("\nSetores a regar:");
        irrigationSectors.forEach(System.out::println);
        System.out.println("===================================================================");
    }

    private void printResults(Map<IrrigationSector, Pair<Integer, Boolean>> lista) {

        if (lista.isEmpty())
            System.out.println("\nNao existem regas nesta hora");
        else
            System.out.println("\nExistem as seguintes regas nesta hora:");
        lista.entrySet().stream().forEach(i -> {
            System.out.print(
                    "\n-> Setor: " + i.getKey().getSector() + " Tempo restante: " + i.getValue().getFirst()
                            + " minutos");
            if (i.getValue().getSecond())
                System.out.print(", Mix: " + i.getKey().getMix());
        });
    }
}