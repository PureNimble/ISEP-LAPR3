package isep.lapr3.g094.ui;

import isep.lapr3.g094.application.controller.GraphController;
import isep.lapr3.g094.application.controller.ImportController;
import isep.lapr3.g094.domain.Pair;
import isep.lapr3.g094.domain.type.Criteria;
import isep.lapr3.g094.domain.type.FurthestPoints;
import isep.lapr3.g094.domain.type.Location;
import isep.lapr3.g094.gui.GraphVisualizationGUI;
import isep.lapr3.g094.struct.graph.Graph;
import isep.lapr3.g094.ui.menu.MenuItem;
import isep.lapr3.g094.ui.utils.Utils;

import java.io.FileNotFoundException;
import java.util.*;

import javax.swing.SwingUtilities;

public class BasketDistributionUI implements Runnable {

    private ImportController importController = new ImportController();
    private GraphController graphController = new GraphController();

    public void run() {
        List<MenuItem> options = new ArrayList<>();
        int option;

        do {
            options.clear();

            if (graphController.getNumLocations() == 0) {
                options.add(new MenuItem("USEI01 - Importar a rede de distribuição de cabazes",
                        this::buildBasketDistribution));
            } else {
                options.add(
                        new MenuItem("USEI02 - Ver rede de distribuição de cabazes", this::printBasketDistribution));
                options.add(new MenuItem("USEI03 - Determinar os vértices ideais", this::getIdealVertices));
                options.add(
                        new MenuItem("USEI04 - Percurso mínimo possível entre os dois locais mais afastados",
                                this::getMinimal));
                options.add(new MenuItem("USEI05 - Determinar a rede de caminhos mínimos", this::getMinimalPaths));
                options.add(new MenuItem("USEI06 - Dividir a rede em N clusters", this::divideDistribution));
                options.add(new MenuItem("USEI07 - Percursos possivel entre os dois locais, com uma dada autonomia",
                        this::getAllPathsWithAutonomy));
            }

            option = Utils.showAndSelectIndex(options, "\n=========Interface da Rede de Distribuição=========");

            if (option != -1) {
                options.get(option).run();
            }

        } while (option != -1);
    }

    private void buildBasketDistribution() {

        String bigGraphPrint = importController.importToGraph(true) ? "Grafo grande criado com sucesso\n\n"
                : "Erro ao criar o grafo grande\n\n";
        String smallGraphPrint = importController.importToGraph(false) ? "Grafo pequeno criado com sucesso"
                : "Erro ao criar o grafo pequeno";

        System.out.println(bigGraphPrint + smallGraphPrint);

    }

    private void getIdealVertices() {

        Map<Location, Criteria> idealVertices = graphController.getVerticesIdeais();
        // Calculate the maximum length of the IDs
        int maxIdLength = Math.max(idealVertices.keySet().stream()
                .mapToInt(Location -> Location.getId().length())
                .max()
                .orElse(0), "ID".length());
        // Calculate the maximum length of the degrees
        int maxDegreeLength = Math.max(idealVertices.values().stream()
                .mapToInt(criteria -> Integer.toString(criteria.getDegree()).length())
                .max()
                .orElse(0), "Grau".length());
        // Calculate the maximum length of the number of minimum paths
        int maxNumPathsLength = Math.max(idealVertices.values().stream()
                .mapToInt(criteria -> Integer.toString(criteria.getNumberMinimumPaths()).length())
                .max()
                .orElse(0), "Nº Caminhos mínimos".length());

        int maxLength = Math.max(maxIdLength, Math.max(maxDegreeLength, maxNumPathsLength));
        for (Map.Entry<Location, Criteria> entry : idealVertices.entrySet()) {
            System.out.println("\n--------------------------------------------------------------------");
            String formatString = "| ID: %" + maxLength / 3 + "s | Degree: %" + maxLength / 3
                    + "d | Número de Caminhos Mínimos: %" + maxLength / 3 + "d |\n";
            System.out.printf(formatString, entry.getKey().getId(), entry.getValue().getDegree(),
                    entry.getValue().getNumberMinimumPaths());
            printPaths(entry.getKey().getId(), entry.getValue().getPaths(), entry.getValue().getDistances(), maxLength);
            formatString = "| Número de Colaboradores: %" + ((maxLength / 2) - 4) + "d | Horário: %" + (maxLength + 3)
                    + "s |\n";
            int numEmployees = entry.getKey().getNumEmployees();
            String schedule = numEmployees > 105 ? (numEmployees < 216 ? "11h:00 – 16h:00" : "12h:00 – 17h:00")
                    : "9h:00 – 14h:00";
            System.out.printf(formatString, numEmployees, schedule);
            System.out.println("--------------------------------------------------------------------");
        }
    }

    private void printPaths(String id, ArrayList<LinkedList<Location>> arrayList, ArrayList<Integer> distances,
            int maxLength) {
        if (arrayList.size() != (distances.size())) {
            throw new IllegalArgumentException("As duas listas têm de ter o mesmo tamanho");
        }

        for (int i = 0; i < arrayList.size(); i++) {
            LinkedList<Location> path = arrayList.get(i);
            Integer distance = distances.get(i);
            String formatString = "| ID Destino: %" + (maxLength - 1) + "s | Distância: %" + (maxLength - 1)
                    + "d m |\n";
            System.out.println("--------------------------------------------------------------------");
            System.out.printf(formatString, path.getLast().getId(), distance);
        }
        System.out.println("--------------------------------------------------------------------");
    }

    private void getMinimal() {
        int autonomy = Utils.readIntegerFromConsole("Qual a autonomia do veículo?(km)");
        autonomy *= 1000;
        Pair<FurthestPoints, Pair<List<Location>, Integer>> result = graphController.getMinimal(autonomy);
        System.out.println("Localização de Origem: " + result.getFirst().getPair().getFirst().getId());
        System.out.println("Localização de Destino: " + result.getFirst().getPair().getSecond().getId());
        System.out.println("Pontos De Passagem: ");
        int locationsSize = result.getFirst().getLocations().size();
        for (int i = 0; i < locationsSize; i++) {
            System.out.print(result.getFirst().getLocations().get(i).getId());
            if (i < result.getFirst().getDistances().size()) {
                System.out.print("--" + result.getFirst().getDistances().get(i) + "m");
            }
            if (i != locationsSize - 1) {
                System.out.print("-->");
            }
        }
        System.out.println("\nDistância Total: " + result.getSecond().getSecond() + "m");

        System.out.println("Locais de Carregamento: ");
        for (int i = 0; i < result.getSecond().getFirst().size(); i++) {
            System.out.println(result.getSecond().getFirst().get(i).getId());
        }

    }

    /*Determinar a rede que liga todas as localidades com uma distância total mínima.
    Critério de Aceitação: Devolver a rede de ligação mínima: locais, distância entre os locais e
    distância total da rede.
     */

    private void getMinimalPaths() {
        Map<Location, Map<Location, Integer>> map = graphController.getMinimalPaths();
        Integer sumDistance = 0;
        for (Map.Entry<Location, Map<Location, Integer>> entry : map.entrySet()) {
            String locationId = entry.getKey().getId();
            for (Map.Entry<Location, Integer> entry2 : entry.getValue().entrySet()) {
                String location1Id = entry2.getKey().getId();
                int distance = entry2.getValue();
                System.out.println(locationId + " -> " + location1Id + "; Distância: " + distance);
                sumDistance = sumDistance + distance;
            }
        }
        System.out.print("\n");
        System.out.println("Distância total: " + sumDistance);
    }

    private void divideDistribution() {
        Map<Location, Criteria> idealVertices = graphController.getVerticesIdeais();
        // Calculate the maximum length of the IDs
        int maxIdLength = Math.max(idealVertices.keySet().stream()
                .mapToInt(Location -> Location.getId().length())
                .max()
                .orElse(0), "ID".length());
        // Calculate the maximum length of the degrees
        int maxDegreeLength = Math.max(idealVertices.values().stream()
                .mapToInt(criteria -> Integer.toString(criteria.getDegree()).length())
                .max()
                .orElse(0), "Grau".length());
        // Calculate the maximum length of the number of minimum paths
        int maxNumPathsLength = Math.max(idealVertices.values().stream()
                .mapToInt(criteria -> Integer.toString(criteria.getNumberMinimumPaths()).length())
                .max()
                .orElse(0), "Nº Caminhos mínimos".length());

        int maxLength = Math.max(maxIdLength, Math.max(maxDegreeLength, maxNumPathsLength));
        for (Map.Entry<Location, Criteria> entry : idealVertices.entrySet()) {
            System.out.println("--------------------------------------------------------------------");
            String formatString = "| ID: %" + maxLength / 3 + "s | Degree: %" + maxLength / 3
                    + "d | Número de Caminhos Mínimos: %" + maxLength / 3 + "d |\n";
            System.out.printf(formatString, entry.getKey().getId(), entry.getValue().getDegree(),
                    entry.getValue().getNumberMinimumPaths());
        }
        String idSelected;
        List<String> idsSelected = new ArrayList<>();
        do {
            idSelected = Utils.readLineFromConsole(
                    "Escreva o ID to vertice a adicionar (Pressione enter sem responder para continuar):");
            if (!idSelected.isEmpty()) {
                idsSelected.add(idSelected);
            }
        } while ((!idSelected.isEmpty()));
        printClusters(idsSelected);
    }

    private void printBasketDistribution() {
        System.out.println(graphController.getBasketDistribution().toString());
        /* try {
            SwingUtilities.invokeLater(() -> {
                try {
                    new GraphVisualizationGUI(graphController.getBasketDistribution());
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            System.out.println("Erro ao abrir a janela");
        } */
        System.out.println(graphController.getNumLocations() + " Localizações existentes");
        System.out.println(graphController.getNumDistances() + " Distâncias existentes");

    }

    private void printClusters(List<String> idsSelected) {
        List<Graph<Location, Integer>> newList = graphController.divideIntoClusters(idsSelected);
        System.out.println(newList.toString());
        boolean printSC = Utils.confirm("Queres dar print do coeficiente de silhueta? (s/n):");
        if (printSC) {
            System.out.println("coeficiente de silhueta: " + graphController.getCoefSil(newList));
        }
    }

    private void getAllPathsWithAutonomy() {
        String idOrigem = Utils.readLineFromConsole("Escreva o ID da localização de origem:");
        Location idOrigemLocation = new Location(idOrigem);
        String idDestino = Utils.readLineFromConsole("Escreva o ID da localização de destino:");
        Location idDestinoLocation = new Location(idDestino);
        int autonomy = Utils.readIntegerFromConsole("Qual a autonomia do veículo?(km)");
        int velocity = Utils.readIntegerFromConsole("Qual a velocidade do veículo?(km/h)");
        autonomy *= 1000;
        ArrayList<LinkedList<Location>> result = graphController.getAllPathsWithAutonomy(idOrigemLocation,
                idDestinoLocation, autonomy, velocity);
        System.out.println("Localização de Origem: " + idOrigem);
        System.out.println("Localização de Destino: " + idDestino);
        System.out.println("Pontos De Passagem: ");

        for (LinkedList<Location> path : result) {
            path.forEach(location -> System.out.print(location.getId() + " -> "));
        }
    }
}
