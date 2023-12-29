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

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
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
                options.add(new MenuItem("USEI06 - Percursos possivel entre os dois locais, com uma dada autonomia",
                        this::getAllPathsWithAutonomy));
                options.add(new MenuItem("USEI07 - Percurso de entrega que maximiza o número de hubs pelo qual passa",
                        this::maximizedPath));
                options.add(new MenuItem("USEI09 - Organizar as localidades do grafo", this::getClusters));
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
        int numberOfHubs = Utils.readIntegerFromConsole("Qual o número de hubs?");
        Map<Location, Criteria> idealVertices = graphController.getVerticesIdeais(order, numberOfHubs, bigGraph);
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
            String filePath = "output/" + graphController.getLatestFileFromDirectory("esinf/output/");
            openGraphViewer(filePath);
        }
    }

    private void divideDistribution() {
        Boolean bigGraph = graphOption();
        if (bigGraph == null) {
            return;
        }
        int numberOfHubs = Utils.readIntegerFromConsole("Qual o número de hubs?");
        Map<Location, Criteria> idealVertices = graphController.getVerticesIdeais(1,numberOfHubs, bigGraph);
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
        String filePath = bigGraph ? "distancias_big.csv" : "distancias_small.csv";
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

    private void getClusters(){
        Boolean bigGraph = graphOption();
        if (bigGraph == null) {
            return;
        }
        int numberOfClusters = Utils.readIntegerFromConsole("Qual o número de clusters?");
        Map<Location, Criteria> idealVertices = graphController.getVerticesIdeais(1, numberOfClusters, bigGraph);

        Set<Location> listHubs = new LinkedHashSet<>();
        int i = 0;
        for(Map.Entry<Location, Criteria> entry : idealVertices.entrySet()){
            listHubs.add(entry.getKey());
            i++;
            if(i >= numberOfClusters) break;
        }

        Map<Location, Map<Location, Integer>> clusters = graphController.getClusters(bigGraph, listHubs, numberOfClusters);
        MapGraph<Location, Integer> graph = new MapGraph<>(false);
        if (graphController.convertMapToMapGraph(clusters, graph)) {
            System.out.println("Grafo criado com sucesso");
        } else {
            System.out.println("Erro ao criar o grafo");
        }
        Utils.confirm("Deseja ver o grafo? (s/n):");
        if (true) {
            graphController.generateDataCSV(graph);
            String filePath = "output/" + graphController.getLatestFileFromDirectory("esinf/output/");
            openGraphViewer(filePath);
        }
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
        String idOrigem;
        LocalTime time;
        do {
            idOrigem = Utils.readLineFromConsole("Escreva o ID da localização de origem: (CT**)").toUpperCase();
        } while ((!idOrigem.contains("CT")) || (!idExists(idOrigem, bigGraph)));
        time = Utils.readTimeFromConsole("Escreva a hora de partida: (HH:MM)");
        double autonomy = Utils.readDoubleFromConsole("Qual a autonomia do veículo?(km)");
        int velocity = Utils.readIntegerFromConsole("Qual a velocidade média do veículo?(km/h)");
        graphController.maximizedPath(idOrigem, time, autonomy * 1000, velocity, bigGraph);
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
            URL url = getClass().getClassLoader().getResource("chromedriver.exe");
            if (url != null) {
                String path = url.getPath();
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
                String path = getClass().getClassLoader().getResource("esinf/" + filePath).getPath();
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

        } catch (

        Exception e) {
            System.out.println("Ocorreu um erro ao abrir o GraphViewer: " + e.getMessage());
        }
    }

}
