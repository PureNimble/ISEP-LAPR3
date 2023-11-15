package isep.lapr3.g094.imports;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import isep.lapr3.g094.domain.Location;

public class Import {

    public List<String> importDistances() {

        InputStream stream = getClass().getClassLoader().getResourceAsStream("esinf/distancias_small.csv");
        List<String> output = new ArrayList<>();

        Scanner sc = new Scanner(stream);
        sc.nextLine();

        while (sc.hasNext()) {
            output.add(sc.nextLine());
        }
        sc.close();

        return output;
    }

    public List<Location> importLocations() {

        InputStream stream = getClass().getClassLoader().getResourceAsStream("esinf/locais_small.csv");
        List<Location> output = new ArrayList<>();

        Scanner sc = new Scanner(stream);
        sc.nextLine();

        while (sc.hasNext()) {
            String[] line = sc.nextLine().split(",");
            output.add(new Location(line[0], Double.parseDouble(line[1].replace(',', '.')),
                    Double.parseDouble(line[2].replace(',', '.')), Integer.parseInt(line[0].substring(2))));
        }
        sc.close();

        return output;
    }

}
