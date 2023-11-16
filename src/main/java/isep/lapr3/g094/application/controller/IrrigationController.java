package isep.lapr3.g094.application.controller;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import isep.lapr3.g094.domain.DailyIrrigation;

import java.util.Calendar;
import java.util.Date;

public class IrrigationController {

    private final static String FOLDER_PATH = "src/main/resources/lapr3/";
    private final static SimpleDateFormat DATA_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
    private  List<DailyIrrigation> planoDeRega;
    private  List<String> horarioDeRega;
    private  List<Date> diasDeRega;

    public IrrigationController(){
        planoDeRega = new ArrayList<>();
        horarioDeRega = new ArrayList<>();
        diasDeRega = new ArrayList<>();
    }

    public void createPlan (String data) throws NumberFormatException, IOException{
        importData();
        getDatesBetween(data);
    }

    public Map<DailyIrrigation,Integer> search(String dataString, String hora) throws ParseException {
        Date dataPesquisa = DATA_FORMAT.parse(dataString);
        if(checkIfDateExists(dataPesquisa)) {
            int dia = Integer.parseInt(getNumbersBeforeSlash(dataString.toString()));

            List<DailyIrrigation> listaFinal = new ArrayList<>();
            // Remove spaces from the string
            hora = hora.strip();
            //convert hours to minutes
            int minutoPesquisa = convertHoursToMinutes(hora);

            // Iterate over the daily watering and add to the filtered list
            for (DailyIrrigation regaDiaria : planoDeRega) {
                if (regaDiaria.getTipoRega() == 'T')
                    listaFinal.add(regaDiaria);
                else if (regaDiaria.getTipoRega() == 'P' && dia % 2 == 0) 
                    listaFinal.add(regaDiaria);
                else if (regaDiaria.getTipoRega() == 'I' && dia % 2 != 0)
                    listaFinal.add(regaDiaria);
                else if (regaDiaria.getTipoRega() == '3' && dia % 3 == 0)
                    listaFinal.add(regaDiaria);

                
            }
            Map<DailyIrrigation, Integer> resultMap = new HashMap<>();
            // Iterate over the watering schedule and filter the list based on the search time
            for (String horarioInicial : horarioDeRega) {
                //convert hours to minutes
                int minutoInicial = convertHoursToMinutes(horarioInicial);
                // Iterate over the filtered list and add to the final list
                listaFinal.stream()
                        .forEach((regaDiaria) -> {
                            int minutoFinal = minutoInicial + regaDiaria.getDuracao();
                            if (minutoInicial <= minutoPesquisa && minutoFinal > minutoPesquisa) {
                                resultMap.put(regaDiaria, minutoFinal - minutoPesquisa);
                            }
                        });
            }
            return resultMap;
        } else {
            System.out.println("Data não existe no plano de rega, por favor insira uma data válida. (30 dias a partir da data de criação do plano)");
        }
        return null;       
    }

    private void importData() throws NumberFormatException, IOException{
        //Variables
        planoDeRega.clear();
        BufferedReader textFileReader = new BufferedReader(new FileReader(findTxtFile()));
        String line;
        //Read the file
        while((line = textFileReader.readLine()) != null){
            //If the line contains a : it means that it is a time
            if(line.contains(":")){
                String[] lineData = line.split(",");
                for (String string : lineData) {
                    string.strip();
                    horarioDeRega.add(string);
                }
            }else{
                String[] lineData = line.split(",");
                DailyIrrigation regaDiaria = new DailyIrrigation(lineData[0].charAt(0),Integer.parseInt(lineData[1]), lineData[2].charAt(0));
                planoDeRega.add(regaDiaria);
            }
        }
        textFileReader.close();
    }

    private int convertHoursToMinutes(String hora) {
        String[] horas = hora.split(":");
        // Hours and minutes to search
        int hour = Integer.parseInt(horas[0]);
        int minutes = Integer.parseInt(horas[1]);
        //convert hours to minutes
        hour = hour * 60;
        minutes += hour;
        return minutes;
    }

    private String findTxtFile(){
            Path folder = Paths.get(FOLDER_PATH);
            try (Stream<Path> paths = Files.walk(folder)) {
                return paths
                        .filter(Files::isRegularFile)
                        .filter(path -> path.toString().endsWith(".txt"))
                        .findFirst()
                        .map(Path::toString)
                        .orElse(null);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
    }

    private void getDatesBetween(String dateString) {
        try {
            Date startDate = DATA_FORMAT.parse(dateString);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startDate);
            for (int i = 0; i < 30; i++) {
                diasDeRega.add(calendar.getTime());
                calendar.add(Calendar.DATE, 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean checkIfDateExists(Date dataPesquisa) {
        for (Date date : diasDeRega) {
            if (date.equals(dataPesquisa)) {
                return true;
            }
        }
        return false;
    }

    public static String getNumbersBeforeSlash(String str) {
        int index = str.indexOf('/');
        String numbers = str.substring(0, index);
        return numbers.replaceAll("[^\\d]", "");
    }
}