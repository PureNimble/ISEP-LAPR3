package isep.lapr3.g094.Controller;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import isep.lapr3.g094.Domain.RegaDiaria;

public class Controller {
    private final static String FILEPATH = "src/main/resources/";
    private final static String FILENAME = "PlanoDeRega.txt";
    private  List<RegaDiaria> planoDeRega;
    private  List<String> horarioDeRega;
    public Controller(){
        planoDeRega = new ArrayList<>();
        horarioDeRega = new ArrayList<>();
    }
    
    public void importData() throws NumberFormatException, IOException{
        //Variables
        BufferedReader textFileReader = new BufferedReader(new FileReader( FILEPATH + FILENAME));
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
                RegaDiaria regaDiaria = new RegaDiaria(lineData[0].charAt(0),Integer.parseInt(lineData[1]), lineData[2].charAt(0));
                planoDeRega.add(regaDiaria);
            }
        }
        textFileReader.close();
    }
    public List<RegaDiaria> search(int dia, String hora) {

        List<RegaDiaria> listaFinal = new ArrayList<>();
        // Remove spaces from the string
        hora = hora.strip();
        //convert hours to minutes
        int minutoPesquisa = convertHoursToMinutes(hora);

        // Iterate over the daily watering and add to the filtered list
        for (RegaDiaria regaDiaria : planoDeRega) {
            if (regaDiaria.getTipoRega() == 'T')
                listaFinal.add(regaDiaria);
            else if (regaDiaria.getTipoRega() == 'P' && dia % 2 == 0) 
                listaFinal.add(regaDiaria);
            else if (regaDiaria.getTipoRega() == 'I' && dia % 2 != 0)
                listaFinal.add(regaDiaria);
            else if (regaDiaria.getTipoRega() == '3' && dia % 3 == 0)
                listaFinal.add(regaDiaria);

            
        }
        List<RegaDiaria> listaFinal2 = new ArrayList<>();
        // Iterate over the watering schedule and filter the list based on the search time
        for (String horarioInicial : horarioDeRega) {
            //convert hours to minutes
            int minutoInicial = convertHoursToMinutes(horarioInicial);
            // Iterate over the filtered list and add to the final list
            listaFinal.stream()
                    .forEach((regaDiaria) -> {
                        int minutoFinal = minutoInicial + regaDiaria.getDuracao();
                        if (minutoInicial <= minutoPesquisa && minutoFinal >= minutoPesquisa) {
                            regaDiaria.setDuracao( minutoFinal - minutoPesquisa);
                            listaFinal2.add(regaDiaria);                     
                        }
                    });
    }
    return listaFinal2;
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
}