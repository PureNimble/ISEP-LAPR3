package isep.lapr3.g094.ui.utils;

import java.io.*;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

public class Utils {

    static public String readLineFromConsole(String prompt) {
        try {
            System.out.println(prompt);

            InputStreamReader converter = new InputStreamReader(System.in);
            BufferedReader in = new BufferedReader(converter);

            return in.readLine();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    static public int readIntegerFromConsole(String prompt) {
        do {
            try {
                String input = readLineFromConsole(prompt);

                int value = Integer.parseInt(input);

                return value;
            } catch (NumberFormatException ex) {
                System.out.println("Invalid input. Please enter a valid integer.");
            }
        } while (true);
    }

    static public double readDoubleFromConsole(String prompt) {
        do {
            try {
                String input = readLineFromConsole(prompt);

                double value = Double.parseDouble(input);

                return value;
            } catch (NumberFormatException ex) {
                System.out.println("input inválido. Por favor escreva um valor válido.");
            }
        } while (true);
    }

    static public Date readDateFromConsole(String prompt) {
        String errorMessage = "Invalid date format. Please enter date in dd/MM/yyyy format.";
        Pattern pattern = Pattern.compile("^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[012])/((19|20)\\d\\d)$");
        while (true) {
            try {
                String strDate = readLineFromConsole(prompt);
                if (!pattern.matcher(strDate).matches()) {
                    throw new Exception(errorMessage);
                }
                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                return df.parse(strDate);
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    public static LocalTime readTimeFromConsole(String string) {
        String errorMessage = "Formato de hora inválido. Por favor insira a hora no formato HH:mm";
        Pattern pattern = Pattern.compile("^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$");
        while (true) {
            try {
                String strTime = readLineFromConsole(string);
                if (!pattern.matcher(strTime).matches()) {
                    throw new Exception(errorMessage);
                }
                return LocalTime.parse(strTime);
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    static public boolean confirm(String message) {
        String input;
        do {
            input = Utils.readLineFromConsole(message + " (S/N): ");
        } while (!input.equalsIgnoreCase("s") && !input.equalsIgnoreCase("n"));

        return input.equalsIgnoreCase("s");
    }

    static public <E> Object showAndSelectOne(List<E> list, String header) {
        showList(list, header);
        return selectsObject(list);
    }

    static public <E> int showAndSelectIndex(List<E> list, String header) {
        showList(list, header);
        return selectsIndex(list);
    }

    static public <E> void showList(List<E> list, String header) {
        System.out.println(header);

        int index = 0;
        for (Object o : list) {
            index++;

            System.out.println(index + ". " + o.toString());
        }
        System.out.println("0. Sair");
    }

    static public <E> Object selectsObject(List<E> list) {
        String input;
        Integer value;
        do {
            input = Utils.readLineFromConsole("Insira uma opção: ");
            value = Integer.valueOf(input);
        } while (value < 0 || value > list.size());

        return value == 0 ? null : list.get(value - 1);

    }

    static public <E> int selectsIndex(List<E> list) {
        String input;
        Integer value;
        do {
            input = Utils.readLineFromConsole("Insira uma opção: ");
            try {
                value = Integer.valueOf(input);
            } catch (NumberFormatException ex) {
                value = -1;
            }
        } while (value < 0 || value > list.size());

        return value - 1;
    }
}
