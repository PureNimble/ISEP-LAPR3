package isep.lapr3.g094;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;

import isep.lapr3.g094.repository.dataAccess.DatabaseConnection;
import isep.lapr3.g094.ui.menu.MainMenuUI;

public class Main {

    public static void main(String[] args) {
        try {
            loadProperties();

            String ipAddress = System.getProperty("database.inet");
            @SuppressWarnings("unused")
            InetAddress inet = InetAddress.getByName(ipAddress);

            MainMenuUI menu = new MainMenuUI();
            menu.run();
            DatabaseConnection.getInstance().closeConnection();
        } catch (UnknownHostException e) {
            System.out.println("\nDatabase Server not reachable!");
        } catch (Exception e) {
            System.exit(0);
        }
    }

    private static void loadProperties() throws IOException {
        Properties properties = new Properties(System.getProperties());

        InputStream inputStream = new Main().getClass().getClassLoader().getResourceAsStream("bddad/config.properties");
        properties.load(inputStream);
        inputStream.close();

        System.setProperties(properties);
    }
}
