package isep.lapr3.g094.ui;

import isep.lapr3.g094.application.controller.GraphController;
import isep.lapr3.g094.application.controller.ImportController;
import isep.lapr3.g094.ui.menu.MenuItem;
import isep.lapr3.g094.ui.utils.Utils;
import java.util.ArrayList;
import java.util.List;

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
                options.add(new MenuItem("Determinar os vértices ideais", this::getIdealVertices));
                options.add(new MenuItem("Percurso mínimo possível entre os dois locais mais afastados", this::getMinimal));
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
            System.out.println(graphController.getBasketDistribution().toString());
            System.out.println(graphController.getNumLocations() + " Localizações importadas com sucesso");
            System.out.println(graphController.getNumDistances() + " Distâncias importadas com sucesso");
        } else
            System.out.println("Erro ao criar o grafo");
    }

    private void getIdealVertices() {
    }

    private void getMinimal() {
    }

    private void getMinimalPaths() {

    }

    private void divideDistribution() {

    }

}
