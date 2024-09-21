package isep.lapr3.g094.ui;

import isep.lapr3.g094.application.controller.GraphController;
import isep.lapr3.g094.application.controller.ImportController;
import isep.lapr3.g094.domain.Pair;
import isep.lapr3.g094.domain.type.Criteria;
import isep.lapr3.g094.domain.type.FurthestPoints;
import isep.lapr3.g094.domain.type.Location;
import isep.lapr3.g094.struct.graph.Graph;
import isep.lapr3.g094.struct.graph.map.MapGraph;
import isep.lapr3.g094.ui.menu.MenuItem;
import isep.lapr3.g094.ui.utils.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalTime;
import java.util.*;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class BasketDistributionUI implements Runnable {

    private ImportController importController = new ImportController();
    private GraphController graphController = new GraphController();

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
                options.add(new MenuItem("USEI03 - Percurso mínimo possível entre os dois locais mais afastados",
                        this::getMinimal));
                options.add(new MenuItem("USEI04 - Determinar a rede de caminhos mínimos", this::getMinimalPaths));
                options.add(new MenuItem("USEI05 - Dividir a rede em N clusters", this::divideDistribution));
                options.add(new MenuItem("USEI06 - Percursos possivel entre os dois locais, com uma dada " +
                        "autonomia", this::getAllPathsWithAutonomy));
                options.add(new MenuItem("USEI07 - Percurso de entrega que maximiza o número de hubs pelo " +
                        "qual passa", this::maximizedPath));
                options.add(new MenuItem("USEI08 - Percurso de entrega de N hubs com maior número de " +
                        "colaboradores", this::deliveryCircuitPath));
                options.add(new MenuItem("USEI09 - Organizar as localidades do grafo", this::getClusters));
                options.add(new MenuItem("USEI10 - Rede que permita transportar o número máximo de " +
                        "cabazes", this::maximumCapacity));
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
        if (graphController.generateInitialDataCSV(true) && graphController.generateInitialDataCSV(false)) {
            System.out.println("CSV's gerados com sucesso");
        } else {
            System.out.println("Erro ao gerar os CSV's");
        }
        ;
    }

    private void getIdealVertices() {
        Boolean bigGraph = graphOption();
        if (bigGraph == null) {
            return;
        }
        List<String> orders = new ArrayList<>();
        orders.add("Influência e proximidade");
        orders.add("Centralidade");
        int order = Utils.showAndSelectIndex(orders, "\n=========Escolha do critério=========");
        if (order == -1) {
            return;
        }
        int numberOfHubs = Utils.readIntegerFromConsole("Qual o número de hubs?");
        Map<Location, Criteria> idealVertices = graphController.getVerticesIdeais(order, numberOfHubs, bigGraph);
        int maxIdLength = Math.max(idealVertices.keySet().stream()
                .mapToInt(Location -> Location.getId().length())
                .max()
                .orElse(0), "ID".length());
        int maxDegreeLength = Math.max(idealVertices.values().stream()
                .mapToInt(criteria -> Integer.toString(criteria.getDegree()).length())
                .max()
                .orElse(0), "Grau".length());
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
        importController.importOpeningHours("esinf/schedules/horarioFuncionamento.csv", bigGraph);
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
        Utils.confirm("Deseja ver o grafo?:");
        if (true) {
            graphController.generateDataCSV(graph);
            String filePath = graphController.getLatestFileFromDirectory("./output/");
            openGraphViewer(filePath);
        }
    }

    private void divideDistribution() {
        Boolean bigGraph = graphOption();
        if (bigGraph == null) {
            return;
        }
        int numberOfHubs = Utils.readIntegerFromConsole("Qual o número de hubs?");
        Map<Location, Criteria> idealVertices = graphController.getVerticesIdeais(1, numberOfHubs, bigGraph);
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
        printClusters(bigGraph, idsSelected);
    }

    private void printBasketDistribution() {
        Boolean bigGraph = graphOption();
        if (bigGraph == null) {
            return;
        }
        String filePath = bigGraph ? "BigGraph.csv" : "SmallGraph.csv";
        openGraphViewer(filePath);
    }

    private void printClusters(Boolean bigGraph, List<String> idsSelected) {
        List<Graph<Location, Integer>> newList = graphController.divideIntoClusters(idsSelected, bigGraph);
        System.out.println(newList.toString());
        boolean printSC = Utils.confirm("Queres dar print do coeficiente de silhueta? (s/n):");
        if (printSC) {
            System.out.println("coeficiente de silhueta: " + graphController.getCoefSil(newList, bigGraph));
        }
    }

    private void getClusters() {
        Boolean bigGraph = graphOption();
        if (bigGraph == null) {
            return;
        }
        int numberOfClusters = Utils.readIntegerFromConsole("Qual o número de clusters?");
        Map<Location, Criteria> idealVertices = graphController.getVerticesIdeais(1, numberOfClusters, bigGraph);

        Set<Location> listHubs = new LinkedHashSet<>();
        int i = 0;
        for (Map.Entry<Location, Criteria> entry : idealVertices.entrySet()) {
            listHubs.add(entry.getKey());
            i++;
            if (i >= numberOfClusters)
                break;
        }

        Map<Location, LinkedList<Location>> clusters = graphController.getClusters(bigGraph, numberOfClusters,
                listHubs);
        if (clusters != null) {
            System.out.println("Clusters criados com sucesso");
            selectCluster(clusters);
        } else {
            System.out.println("Erro a criar clusters!");
        }
    }

    private void selectCluster(Map<Location, LinkedList<Location>> clusters) {

        List<Location> hubList = new ArrayList<>(clusters.keySet());

        int option;

        do {
            System.out.println("Clusters:");
            for (int i = 0; i < hubList.size(); i++) {
                System.out.printf("%d - Hub %s\n", i + 1, hubList.get(i).getId());
            }

            option = Utils.readIntegerFromConsole("Selecione o cluster que quer ver (0 para sair do menu)");
            if (option > 0 && option <= hubList.size()) {
                Location selectedHub = hubList.get(option - 1);
                printCluster(selectedHub, clusters.get(selectedHub));
            }
        } while (option != 0);
    }

    private void printCluster(Location hub, LinkedList<Location> locations) {
        System.out.printf("Cluster do hub %s\n", hub.getId());

        System.out.println("Locations:");
        for (Location location : locations) {
            System.out.println(location.getId());
        }
        System.out.println();
    }

    private void getAllPathsWithAutonomy() {
        Boolean bigGraph = graphOption();
        if (bigGraph == null) {
            return;
        }
        String idOrigem = null;
        String idDestino = null;
        do {
            idOrigem = Utils.readLineFromConsole("Escreva o ID da localização de origem: (CT**)").toUpperCase();

        } while ((!idOrigem.contains("CT")));
        do {
            idDestino = Utils.readLineFromConsole("Escreva o ID da localização de destino: (CT**)").toUpperCase();

        } while ((!idDestino.contains("CT")));
        int autonomy = Utils.readIntegerFromConsole("Qual a autonomia do veículo?(km)");
        int velocity = Utils.readIntegerFromConsole("Qual a velocidade do veículo?(km/h)");

        Map<List<Pair<Location, Integer>>, Integer> paths = graphController.allPathsWithLimit(idOrigem,
                idDestino, autonomy * 1000, velocity, bigGraph);
        if (paths == null) {
            System.out.println("--------------------------");
            System.out.println("| Localização invalida ! |");
            System.out.println("--------------------------");
            return;
        }
        if (paths.isEmpty()) {
            System.out.println("--------------------------");
            System.out.println("| Não existem caminhos ! |");
            System.out.println("--------------------------");
            return;
        }
        System.out.println("Localização de Origem: " + idOrigem);
        System.out.println("Localização de Destino: " + idDestino);
        System.out.println("Caminhos Possiveis: \n");
        final String dest = idDestino;

        paths.forEach((path, pathDistance) -> {

            path.forEach(locations -> {
                Location location = locations.getFirst();
                String locString = location.getId();
                if (location.isHub())
                    locString += " (Hub)";
                int distance = locations.getSecond();
                if (location.getId().equals(dest))
                    System.out.print(locString + "\n");
                else
                    System.out.print(locString + " -> " + distance + "m -> ");
            });
            double totalDistanceDouble = pathDistance / 1000;
            double time = totalDistanceDouble / velocity;
            int hours = (int) time;
            int minutes = (int) ((time - hours) * 60);
            int seconds = (int) (((time - hours) * 60 - minutes) * 60);

            String timeFormatted = String.format("%02d:%02d:%02d", hours, minutes, seconds);
            System.out.println("\nDistância Total: " + pathDistance / 1000.0 + "km");
            System.out.println("Tempo Total: " + timeFormatted + "\n");

        });
    }

    private void maximizedPath() {
        Boolean bigGraph = graphOption();
        if (bigGraph == null) {
            return;
        }

        MapGraph<Location, Integer> graph = graphController.getGraph(bigGraph);
        boolean check = false;
        for (Location location : graph.vertices()) {
            if (location.isHub()) {
                check = true;
                break;
            }
        }

        if (!check) {
            System.out.println("Não existem Hubs neste grafo! Por favor execute a USEI02 primeiro!");
            return;
        }

        String idOrigem;
        LocalTime time;
        do {
            idOrigem = Utils.readLineFromConsole("Escreva o ID da localização de origem: (CT**)").toUpperCase();
        } while ((!idOrigem.contains("CT")) || (!idExists(idOrigem, bigGraph)));
        do {
            time = Utils.readTimeFromConsole("Escreva a hora de partida: (HH:MM)");
        } while (checkHours(time, bigGraph));
        int autonomy = Utils.readIntegerFromConsole("Qual a autonomia do veículo?(km)");
        int velocity = Utils.readIntegerFromConsole("Qual a velocidade média do veículo?(km/h)");
        int numCharges = 0;
        LinkedList<Location> topPath = new LinkedList<>();
        LinkedList<LocalTime> topArriveTimes = new LinkedList<>();
        LinkedList<LocalTime> topDepartTimes = new LinkedList<>();
        LinkedList<LocalTime> topAfterChargeTimes = new LinkedList<>();
        LinkedList<LocalTime> topDescargaTimes = new LinkedList<>();
        int topDistance = graphController.maximizedPath(idOrigem, time, autonomy * 1000, velocity, bigGraph, topPath,
                topArriveTimes,
                topDepartTimes, topAfterChargeTimes, topDescargaTimes);
        System.out.println("Localização de Origem: " + idOrigem);
        System.out.println("Hora de Partida: " + time);
        System.out.println("Autonomia: " + autonomy + "km");
        System.out.println("Velocidade Média: " + velocity + "km/h");
        for (int i = 0; i < topPath.size(); i++) {
            System.out.println("+-----------------+");
            System.out.println("| ID: " + topPath.get(i).getId() + "    |");
            System.out.println("| Arrive: " + topArriveTimes.get(i) + " |");
            if (topPath.get(i).isHub() && !topPath.get(i).getId().equals(idOrigem)) {
                System.out.println("| Depart: " + topDepartTimes.get(i) + " |");
            }
            System.out.println("+-----------------+");
            if (i < topPath.size() - 1) {
                System.out.println("  |");
                System.out.println("  v");
            }
        }
        Duration chargeDuration = Duration.ZERO;
        Duration travDuration = Duration.ZERO;
        for (int i = 1; i < topAfterChargeTimes.size(); i++) {
            if (!topAfterChargeTimes.get(i).equals(topDepartTimes.get(i - 1)) && topDistance > autonomy * 1000) {
                numCharges++;
                Duration duration = Duration.between(topDepartTimes.get(i - 1), topAfterChargeTimes.get(i));
                chargeDuration = chargeDuration.plus(duration);
            }
        }

        System.out.println("\nNúmero de Carregamentos: " + numCharges);
        System.out.println("\nDistância Total: " + topDistance + "m");
        System.out.println("Tempo de carregamento: " + formatDuration(chargeDuration) + "\n");
        for (int i = 1; i < topDescargaTimes.size(); i++) {
            if (!topDescargaTimes.get(i).equals(topArriveTimes.get(i))) {
                Duration duration = Duration.between(topArriveTimes.get(i), topDescargaTimes.get(i));
                System.out.println("Tempo de descarga dos cestos no hub " + topPath.get(i).getId() + ": "
                        + formatDuration(duration));
            }
        }
        for (int i = 1; i < topArriveTimes.size(); i++) {
            Duration duration = Duration.ZERO;
            if (!topAfterChargeTimes.get(i).equals(topDepartTimes.get(i - 1))) {
                duration = Duration.between(topDepartTimes.get(i - 1), topAfterChargeTimes.get(i));
            }
            travDuration = travDuration
                    .plus(Duration.between(topDepartTimes.get(i - 1), topArriveTimes.get(i)).minus(duration));
        }
        System.out.println("\nTempo de viagem: " + formatDuration(travDuration));
        System.out.println("Tempo Total: " + formatDuration(Duration.between(time, topArriveTimes.getLast())));
    }

    /**Encontrar para um produtor o circuito de entrega que parte de um local origem, passa por N hubs com maior número
    de colaboradores uma só vez e volta ao local origem minimizando a distância total percorrida. Considere como número
    de hubs: 5, 6 e 7.
    
    Critério de Aceitação: Devolver o local de origem do circuito, os locais de passagem (sendo um hub, indicar o
    respetivo número de colaboradores), a distância entre todos os locais do percurso, a distância total, o número de
    carregamentos e o tempo total do circuito (discriminando o tempo afeto aos carregamentos do veículo,
    ao percurso e ao tempo de descarga dos cestos em cada hub).
    
     Critérios principais: Passar por ponto de origem: ponto final --> ponto inicial sem repetir caminhos ✔️
                           Passar por N hubs uma unica vez ✔️
                           Maior numero de colaboradores ✔️
                           Minimizar a distância total percorrida ✔️
     Critérios secundários: Tempo total do circuito
                            Número de carregamentos
    */
    private void deliveryCircuitPath() {
        Boolean bigGraph = graphOption();
        if (bigGraph == null) {
            return;
        }
        String idOrigem;
        int nHubs;
        do {
            idOrigem = Utils.readLineFromConsole("Escreva o ID da localização de origem: (CT**)").toUpperCase();
        } while ((!idOrigem.contains("CT")) || (!idExists(idOrigem, bigGraph)));
        do {
            nHubs = Utils.readIntegerFromConsole("Escreva o número de hubs (5, 6 e 7): ");
        } while (nHubs < 5 || nHubs > 7);
        int autonomy = Utils.readIntegerFromConsole("Qual a autonomia do veículo?(km)");
        int velocity = Utils.readIntegerFromConsole("Qual a velocidade média do veículo?(km/h)");
        int numCharges = 0;
        List<Location> bestPath = graphController.deliveryCircuitPath(idOrigem, nHubs, bigGraph);
        System.out.println("Localização de Origem: " + idOrigem);
        System.out.println("Número de Hubs: " + nHubs);
        int totalDistance = 0;
        int numCollaborations = 0;
        for (int i = 0; i < bestPath.size(); i++) {
            System.out.println("+--------------------+");
            System.out.println("| ID: " + bestPath.get(i).getId() + "            |");
            System.out.println("| Colaboradores: " + bestPath.get(i).getNumEmployees() + "   |");
            if (i < bestPath.size() - 1) {
                System.out.println("| Distância: " + graphController.getDistance(bestPath.get(i), bestPath.get(i + 1),
                        bigGraph) + " m |");
                Integer distance = graphController.getDistance(bestPath.get(i), bestPath.get(i + 1), bigGraph);
                if (distance == null) {
                    System.out.println("Não existe caminho entre " + bestPath.get(i).getId() + " e "
                            + bestPath.get(i + 1).getId());
                    return;
                }
                totalDistance += distance;
            }
            System.out.println("+--------------------+");
            numCollaborations += bestPath.get(i).getNumEmployees();
            if (i < bestPath.size() - 1) {
                System.out.println("  |");
                System.out.println("  v");
            }
        }
        Pair<Duration, Integer> charge = graphController.getChargeDuration(bestPath, bigGraph, autonomy * 1000);
        Duration chargeDuration = charge.getFirst();
        numCharges = charge.getSecond();
        Duration travDuration = graphController.getTravDuration(bestPath, bigGraph, velocity);
        Duration fullDuration = chargeDuration.plus(travDuration);
        System.out.println("\nNúmero de Colaboradores: " + numCollaborations);
        System.out.println("\nDistância Total: " + totalDistance + "m");
        System.out.println("Número de Carregamentos: " + numCharges);
        System.out.println("Tempo Total: " + formatDuration(fullDuration) + "\n");
        System.out.println("Tempo de Carregamento: " + formatDuration(chargeDuration) + "\n");

    }

    private String formatDuration(Duration duration) {
        long hours = duration.toHours();
        int minutes = duration.toMinutesPart();
        int seconds = duration.toSecondsPart();
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    private boolean checkHours(LocalTime time, Boolean bigGraph) {
        return graphController.checkHours(time, bigGraph);
    }

    private boolean idExists(String idOrigem, Boolean bigGraph) {
        return graphController.idExists(idOrigem, bigGraph);
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

    private void openGraphViewer(String filePath) {
        try {
            InputStream in = getClass().getClassLoader().getResourceAsStream("chromedriver.exe");
            if (in != null) {
                File tempFile = File.createTempFile("chromedriver", ".exe");
                tempFile.deleteOnExit();
                try (FileOutputStream out = new FileOutputStream(tempFile)) {
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = in.read(buffer)) != -1) {
                        out.write(buffer, 0, bytesRead);
                    }
                }
                String path = tempFile.getAbsolutePath();
                System.out.println("ChromeDriver path: " + path);
                System.setProperty("webdriver.chrome.driver", path);
            } else {
                System.out.println("Could not find chromedriver.exe");
                return;
            }

            WebDriver driver = new ChromeDriver();
            driver.manage().window().maximize();
            driver.get("https://cosmograph.app/run/");
            WebDriverWait wait = new WebDriverWait(driver, 10);
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div")));

            // Find all div elements in the webpage
            List<WebElement> divElements = driver.findElements(By.cssSelector("div"));
            WebElement div = divElements.get(8);
            div.click();

            divElements = driver.findElements(By.cssSelector("div"));
            div = divElements.get(10);

            // Find the input element
            WebElement input = div.findElement(By.cssSelector("input[type=file]"));

            // get a path of the file in resources folder
            if (!(filePath == null)) {
                String path = Paths.get("./output/", filePath).toAbsolutePath().toString();
                try {
                    // Decode the path
                    path = URLDecoder.decode(path, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                // Remove leading slash on Windows
                if (System.getProperty("os.name").startsWith("Windows") && path.startsWith("/")) {
                    path = path.substring(1);
                }

                System.out.println("Path: " + path);
                input.sendKeys(path);
                List<WebElement> buttons = driver.findElements(By.cssSelector("button"));

                buttons.getLast().click();
            }
            
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                    if (driver != null) {
                        driver.quit();
                    }
                }));

        } catch (

        Exception e) {
            System.out.println("Ocorreu um erro ao abrir o GraphViewer: " + e.getMessage());
        }
    }

    private void maximumCapacity() {
        Boolean bigGraph = graphOption();
        if (bigGraph == null) {
            return;
        }
        MapGraph<Location, Integer> graph = graphController.getGraph(bigGraph);
        MapGraph<Location, Integer> hubGraph = graphController.filterGraph(graph);
        if (hubGraph.vertices().isEmpty()) {
            System.out.println("Erro ao filtrar o grafo");
            return;
        }

        if (Utils.confirm("Deseja ver o grafo?:")) {
            graphController.generateDataCSV(hubGraph);
            String filePath = graphController.getLatestFileFromDirectory("./output/");
            openGraphViewer(filePath);
        }

        String idOrigem;
        String idDestino;
        do {
            idOrigem = Utils.readLineFromConsole("Escreva o ID do HUB de origem: (CT**)").toUpperCase();
        } while ((!idOrigem.contains("CT")) || (!hubGraph.validVertex(new Location(idOrigem))));
        do {
            idDestino = Utils.readLineFromConsole("Escreva o ID do HUB de destino: (CT**)").toUpperCase();
        } while ((!idDestino.contains("CT")) || (!hubGraph.validVertex(new Location(idDestino))));

        Pair<Integer, MapGraph<Location, Integer>> result = graphController.maximumCapacity(hubGraph,
                new Location(idOrigem), new Location(idDestino));
        System.out.println("Hub de Origem: " + idOrigem);
        System.out.println("Hub de Destino: " + idDestino);
        if (result == null || result.getFirst() == 0) {
            System.out.println("----------------------");
            System.out.println("| Não existe fluxo ! |");
            System.out.println("----------------------");
            return;
        }
        System.out.println("Capacidade Máxima: " + result.getFirst());
        if (Utils.confirm("Deseja ver o grafo?:")) {
            graphController.generateDataCSV(result.getSecond());
            String filePath = graphController.getLatestFileFromDirectory("./output/");
            openGraphViewer(filePath);
        }
    }
}