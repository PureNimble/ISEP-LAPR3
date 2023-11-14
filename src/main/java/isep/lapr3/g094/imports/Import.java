package isep.lapr3.g094.imports;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Import {

    public List<String> importDistances() {

        InputStream stream = getClass().getClassLoader().getResourceAsStream("distancias_small.csv");
        List<String> output = new ArrayList<>();

        Scanner sc = new Scanner(stream);
        sc.nextLine();

        while (sc.hasNext()) {
            output.add(sc.nextLine());
        }
        sc.close();

        return output;
    }

    public List<String> importLocations() {

        InputStream stream = getClass().getClassLoader().getResourceAsStream("locais_small.csv");
        List<String> output = new ArrayList<>();

        Scanner sc = new Scanner(stream);
        sc.nextLine();

        while (sc.hasNext()) {
            output.add(sc.nextLine());
        }
        sc.close();

        return output;
    }

}
