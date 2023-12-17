package isep.lapr3.g094.ui;

import isep.lapr3.g094.application.controller.GraphController;
import isep.lapr3.g094.application.controller.ImportController;
import isep.lapr3.g094.domain.Pair;
import isep.lapr3.g094.domain.type.Criteria;
import isep.lapr3.g094.domain.type.FurthestPoints;
import isep.lapr3.g094.domain.type.Location;
import isep.lapr3.g094.gui.GraphVisualizationGUI;
import isep.lapr3.g094.struct.graph.Graph;
import isep.lapr3.g094.struct.graph.map.MapGraph;
import isep.lapr3.g094.ui.menu.MenuItem;
import isep.lapr3.g094.ui.utils.Utils;
import java.io.IOException;
import java.time.LocalTime;
import java.util.*;


public class BasketDistributionUI implements Runnable {

    private ImportController importController = new ImportController();
    private GraphController graphController = new GraphController();
    private GraphVisualizationGUI graphVisualizationGUI = new GraphVisualizationGUI();

    public void run() {
        List<MenuItem> options = new ArrayList<>();
        int option;

        do {
            options.clear();

            if (graphController.getNumLocations(true) == 0 || graphController.getNumDistances(false) == 0) {
                options.add(new MenuItem("USEI01 - Importar a rede de distribuição de cabazes",
                        this::buildBasketDistribution));
            } else {
                options.add(
                        new MenuItem("USEI01 - Ver rede de distribuição de cabazes", this::printBasketDistribution));
                options.add(new MenuItem("USEI02 - Determinar os vértices ideais", this::getIdealVertices));
                options.add(
                        new MenuItem("USEI03 - Percurso mínimo possível entre os dois locais mais afastados",
                                this::getMinimal));
                options.add(new MenuItem("USEI04 - Determinar a rede de caminhos mínimos", this::getMinimalPaths));
                options.add(new MenuItem("USEI05 - Dividir a rede em N clusters", this::divideDistribution));
                options.add(new MenuItem("USEI06 - Percursos possivel entre os dois locais, com uma dada autonomia",
                        this::getAllPathsWithAutonomy));
                options.add(new MenuItem("USEI11 - Adicionar horários", this::addSchedule));
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
        if (graphController.generateInitialDataCSV(true) && graphController.generateInitialDataCSV(false)){
            System.out.println("CSV's gerados com sucesso");
        } else {
            System.out.println("Erro ao gerar os CSV's");
        };
    }

    private void getIdealVertices() {
        Boolean bigGraph = graphOption();
        if (bigGraph == null) {
        return;
        }
        Map<Location, Criteria> idealVertices = graphController.getVerticesIdeais(bigGraph);
        importController.importOpeningHours("esinf/schedules/horarioFuncionamento.csv", bigGraph);
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
        int totalDistance = entry.getValue().getDistances();
        printPaths(totalDistance, maxLength);
        formatString = "| Número de Colaboradores: %" + ((maxLength / 2) - 4) + "d | Horário: %" + (maxLength + 3)
            + "s |\n";
        int numEmployees = entry.getKey().getNumEmployees();
        LocalTime startHour = entry.getKey().getStartHour();
        LocalTime endHour = entry.getKey().getEndHour();
        String schedule = startHour + " - " + endHour;
        System.out.printf(formatString, numEmployees, schedule);
        System.out.println("--------------------------------------------------------------------");
        }
    }

    private void printPaths(int totalDistance, int maxLength) {
        String formatString = "| Total Distance: %" + ((maxLength * 2) + 8) + "d m |\n";
        System.out.println("--------------------------------------------------------------------");
        System.out.printf(formatString, totalDistance);
        System.out.println("--------------------------------------------------------------------");
    }

    private void getMinimal() {
        Boolean bigGraph = graphOption();
        if (bigGraph == null) {
            return;
        }
        int autonomy = Utils.readIntegerFromConsole("Qual a autonomia do veículo?(km)");
        autonomy *= 1000;
        Pair<FurthestPoints, Pair<List<Location>, Integer>> result = graphController.getMinimal(autonomy, bigGraph);
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

    /*
     * Determinar a rede que liga todas as localidades com uma distância total
     * mínima.
     * Critério de Aceitação: Devolver a rede de ligação mínima: locais, distância
     * entre os locais e
     * distância total da rede.
     */

    private void getMinimalPaths() {
        Boolean bigGraph = graphOption();
        if (bigGraph == null) {
            return;
        }
        Map<Location, Map<Location, Integer>> map = graphController.getMinimalPaths(bigGraph);
        MapGraph<Location, Integer> graph = new MapGraph<>(false);
        if (graphController.convertMapToMapGraph(map, graph)) {
            System.out.println("Grafo criado com sucesso");
        } else {
            System.out.println("Erro ao criar o grafo");
        }
        Utils.confirm("Deseja ver o grafo? (s/n):");
        if (true) {
            graphController.generateDataCSV(graph);
            openGraphViewer();
            graphVisualizationGUI.showGraph();
        }
    }
    
    private void divideDistribution() {
        Boolean bigGraph = graphOption();
        if (bigGraph == null) {
            return;
        }
        Map<Location, Criteria> idealVertices = graphController.getVerticesIdeais(bigGraph);
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
                    "Escreva o ID do vertice a adicionar (Pressione enter sem responder para continuar):");
            if (!idSelected.isEmpty()) {
                idsSelected.add(idSelected);
            }
        } while ((!idSelected.isEmpty()));
        printClusters(idsSelected);
    }

    private void printBasketDistribution() {
        openGraphViewer();
        graphVisualizationGUI.showGraph();
    }

    private void printClusters(List<String> idsSelected) {
        Boolean bigGraph = graphOption();
        if (bigGraph == null) {
            return;
        }
        List<Graph<Location, Integer>> newList = graphController.divideIntoClusters(idsSelected, graphOption());
        System.out.println(newList.toString());
        boolean printSC = Utils.confirm("Queres dar print do coeficiente de silhueta? (s/n):");
        if (printSC) {
            System.out.println("coeficiente de silhueta: " + graphController.getCoefSil(newList, bigGraph));
        }
    }

    private void getAllPathsWithAutonomy() {
        Boolean bigGraph = graphOption();
        if (bigGraph == null) {
            return;
        }
        String idOrigem = Utils.readLineFromConsole("Escreva o ID da localização de origem:");
        Location idOrigemLocation = new Location(idOrigem);
        String idDestino = Utils.readLineFromConsole("Escreva o ID da localização de destino:");
        Location idDestinoLocation = new Location(idDestino);
        int autonomy = Utils.readIntegerFromConsole("Qual a autonomia do veículo?(km)");
        int velocity = Utils.readIntegerFromConsole("Qual a velocidade do veículo?(km/h)");
        autonomy *= 1000;
        ArrayList<LinkedList<Location>> result = graphController.getAllPathsWithAutonomy(idOrigemLocation,
                idDestinoLocation, autonomy, velocity, bigGraph);
        System.out.println("Localização de Origem: " + idOrigem);
        System.out.println("Localização de Destino: " + idDestino);
        System.out.println("Pontos De Passagem: ");

        for (LinkedList<Location> path : result) {
            path.forEach(location -> System.out.print(location.getId() + " -> "));
        }
    }

    private void addSchedule() {
        try {
            Boolean bigGraph = graphOption();
            if (bigGraph == null) {
                return;
            }
            List<String> files = importController.getFilesFromDirectory("esinf/schedules");
            int fileNumber = Utils.showAndSelectIndex(files, "\n=========Lista de ficheiros=========");
            boolean check = importController.importOpeningHours(files.get(fileNumber).toString(), bigGraph);
            if (check) {
                System.out.println("Horários adicionados com sucesso");
            } else {
                System.out.println("Erro ao adicionar todos os horários");
            }

        } catch (IOException e) {
            System.out.println("Ocorreu um erro na pesquisa dos caminhos dos ficheiros: " + e.getMessage());
        }
    }

    private Boolean graphOption() {
        List<String> graphs = new ArrayList<>();
        graphs.add("Grafo Grande");
        graphs.add("Grafo Pequeno");
        int option = Utils.showAndSelectIndex(graphs, "\n=========Escolha do grafo=========");
        if (option >= 0) {
            return (option == 0) ? true : false;
        }
        return null;
    }

     private void openGraphViewer() {
        if (java.awt.Desktop.isDesktopSupported()) {
            java.awt.Desktop desktop = java.awt.Desktop.getDesktop();

            if (desktop.isSupported(java.awt.Desktop.Action.BROWSE)) {
                try {
                    java.net.URI uri = new java.net.URI("https://cosmograph.app/run/");
                    desktop.browse(uri);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                System.err.println("Esta ação não é suportada neste sistema operativo");
            }
        } else {
            System.err.println("Este sistema operativo não suporta esta ação");
        }
    }

}
