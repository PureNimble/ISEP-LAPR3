package isep.lapr3.g094.ui;

import isep.lapr3.g094.application.controller.GraphController;
import isep.lapr3.g094.application.controller.ImportController;
import isep.lapr3.g094.domain.type.Criteria;
import isep.lapr3.g094.ui.menu.MenuItem;
import isep.lapr3.g094.ui.utils.Utils;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BasketDistributionUI implements Runnable {

    private ImportController importController = new ImportController();
    private GraphController graphController = new GraphController();

    public void run() {
        List<MenuItem> options = new ArrayList<>();
        int option;

        do {
            options.clear();

            if (graphController.getNumLocations() == 0) {
                options.add(new MenuItem("Importar a rede de distribuição de cabazes", this::buildBasketDistribution));
            } else {
                options.add(new MenuItem("Ver rede de distribuição de cabazes", this::printBasketDistribution));
                options.add(new MenuItem("Determinar os vértices ideais", this::getIdealVertices));
                options.add(
                        new MenuItem("Percurso mínimo possível entre os dois locais mais afastados", this::getMinimal));
                options.add(new MenuItem("Determinar a rede de caminhos mínimos", this::getMinimalPaths));
                options.add(new MenuItem("Dividir a rede em N clusters", this::divideDistribution));
            }

            option = Utils.showAndSelectIndex(options, "\n=========Interface da Rede de Distribuição=========");

            if (option != -1) {
                options.get(option).run();
            }

        } while (option != -1);
    }

    private void buildBasketDistribution() {
        if (importController.importToGraph()) {
            System.out.println("Grafo criado com sucesso");
        } else
            System.out.println("Erro ao criar o grafo");
    }

    private void getIdealVertices() {
        Map<String, Criteria> idealVertices = graphController.getEveryLocationDegree();

        // Calculate the maximum length of the IDs
        int maxIdLength = Math.max(idealVertices.keySet().stream()
                .mapToInt(String::length)
                .max()
                .orElse(0), "ID".length());

        // Calculate the maximum length of the degrees
        int maxDegreeLength = Math.max(idealVertices.values().stream()
                .mapToInt(criteria -> Integer.toString(criteria.getDegree()).length())
                .max()
                .orElse(0), "Grau".length());

        // Calculate the maximum length of the paths
        int maxPathLength = Math.max(idealVertices.values().stream()
                .mapToInt(criteria -> criteria.getPaths().toString().length())
                .max()
                .orElse(0), "Caminhos mínimos (origem)".length());

        // Calculate the maximum length of the number of minimum paths
        int maxNumPathsLength = Math.max(idealVertices.values().stream()
                .mapToInt(criteria -> Integer.toString(criteria.getNumberMinimumPaths()).length())
                .max()
                .orElse(0), "Nº Caminhos mínimos".length());

        String formatString = "| %-" + maxIdLength + "s | %" + maxDegreeLength + "d | %-" + maxPathLength + "s | %"
                + maxNumPathsLength + "d |\n";

        printLine(maxIdLength, maxDegreeLength, maxPathLength, maxNumPathsLength);
        System.out.printf("| %" + maxIdLength + "s | %" + maxDegreeLength + "s | %" + maxPathLength + "s | %"
                + maxNumPathsLength + "s |\n", "ID", "Grau", "Caminhos mínimos (origem)", "Nº Caminhos mínimos");
        printLine(maxIdLength, maxDegreeLength, maxPathLength, maxNumPathsLength);
        for (Map.Entry<String, Criteria> entry : idealVertices.entrySet()) {
            System.out.printf(formatString, entry.getKey(), entry.getValue().getDegree(), entry.getValue().getPaths(),
                    entry.getValue().getNumberMinimumPaths());
            printLine(maxIdLength, maxDegreeLength, maxPathLength, maxNumPathsLength); // print a line after each entry
        }
    }

    private void printLine(int idLength, int degreeLength, int pathLength, int numPathsLength) {
        System.out.println("|" + "-".repeat(idLength + 2) + "|" + "-".repeat(degreeLength + 2) + "|"
                + "-".repeat(pathLength + 2) + "|" + "-".repeat(numPathsLength + 2) + "|");
    }

    private void getMinimal() {
    }

    private void getMinimalPaths() {

    }

    private void divideDistribution() {

    }

    private void printBasketDistribution() {
        System.out.println(graphController.getBasketDistribution().toString());
        System.out.println(graphController.getNumLocations() + " Localizações existentes");
        System.out.println(graphController.getNumDistances() + " Distâncias existentes");
    }

}
