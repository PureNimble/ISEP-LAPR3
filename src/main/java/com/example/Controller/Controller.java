package com.example.Controller;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.example.Domain.RegaDiaria;

public class Controller {
    private final static String FILEPATH = "src/main/resources/";
    private final static String FILENAME = "PlanoDeRega.txt";
    private List<RegaDiaria> planoDeRega;
    private List<String> horarioDeRega;

    public Controller(){
        planoDeRega = new ArrayList<>();
        horarioDeRega = new ArrayList<>();
    }

    public void importData() throws NumberFormatException, IOException{
        //Variables
        BufferedReader textFileReader = new BufferedReader(new FileReader(FILEPATH + FILENAME));
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
    public List<RegaDiaria> search(int dia, String hora){
        List<RegaDiaria> listaFiltrada = new ArrayList<>();
        List<RegaDiaria> listaFinal = new ArrayList<>();
        String[] horas = hora.split(":");
        //Hours and minutes to search
        int hourSearch = Integer.parseInt(horas[0]);
        int minuteSearch = Integer.parseInt(horas[1]);
        //Remove spaces from the string
        hora.strip();
        //Add all the daily watering
        listaFiltrada.addAll(planoDeRega.stream().filter(regaDiaria -> regaDiaria.getTipoRega() == 'T').collect(Collectors.toList()));
        //Add all the watering that is done on even days
        if(dia % 2 == 0)
            listaFiltrada.addAll(planoDeRega.stream().filter(regaDiaria -> regaDiaria.getTipoRega() == 'P').collect(Collectors.toList()));
        else
            listaFiltrada.addAll(planoDeRega.stream().filter(regaDiaria -> regaDiaria.getTipoRega() == 'I').collect(Collectors.toList()));
        //Add all the watering that is done on odd days
        if(dia % 3 == 0)
            listaFiltrada.addAll(planoDeRega.stream().filter(regaDiaria -> regaDiaria.getTipoRega() == '3').collect(Collectors.toList()));

        //Split the string into hours and minutes
        horarioDeRega.stream().forEach(horarioInicial -> {
            String[] x = horarioInicial.split(":");
            int horaInicial = Integer.valueOf(x[0]);
            int minutoInicial = Integer.valueOf(x[1]);
            listaFinal.addAll(listaFiltrada.stream()
                    .filter(regaDiaria -> {
                        if(horaInicial <= hourSearch && 
                            (minutoInicial <= minuteSearch &&
                             minutoInicial + regaDiaria.getDuracao()> minuteSearch)){
                            int tempoRestante = regaDiaria.getDuracao() + minutoInicial - minuteSearch;
                            regaDiaria.setDuracao(tempoRestante);
                            return true;
                            }
                        else
                            return false;
                    }).collect(Collectors.toList()));
        });

        return listaFinal;
    }
    
}
