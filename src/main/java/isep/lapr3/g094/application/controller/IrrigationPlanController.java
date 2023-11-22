package isep.lapr3.g094.application.controller;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import isep.lapr3.g094.domain.irrigation.IrrigationDate;
import isep.lapr3.g094.domain.irrigation.IrrigationHour;
import isep.lapr3.g094.domain.irrigation.IrrigationSector;
import isep.lapr3.g094.repository.FarmManagerRepository;
import isep.lapr3.g094.repository.Repositories;
import isep.lapr3.g094.repository.irrigation.IrrigationDateRepository;
import isep.lapr3.g094.repository.irrigation.IrrigationHourRepository;
import isep.lapr3.g094.repository.irrigation.IrrigationSectorRepository;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.time.LocalTime;

public class IrrigationPlanController {

    private final static SimpleDateFormat DATA_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
    private IrrigationSectorRepository irrigationSectorRepository;
    private IrrigationDateRepository irrigationDateRepository;
    private IrrigationHourRepository irrigationHourRepository;
    private FarmManagerRepository farmManagerRepository;

    public IrrigationPlanController() {
        getIrrigationSectorRepository();
        getIrrigationDateRepository();
        getIrrigationHourRepository();
        getFarmManagerRepository();
    }

    private IrrigationSectorRepository getIrrigationSectorRepository() {
        if (irrigationSectorRepository == null) {
            Repositories repositories = Repositories.getInstance();

            irrigationSectorRepository = repositories.getIrrigationSectorRepository();
        }
        return irrigationSectorRepository;
    }

    private IrrigationDateRepository getIrrigationDateRepository() {
        if (irrigationDateRepository == null) {
            Repositories repositories = Repositories.getInstance();

            irrigationDateRepository = repositories.getIrrigationDateRepository();
        }
        return irrigationDateRepository;
    }

    private IrrigationHourRepository getIrrigationHourRepository() {
        if (irrigationHourRepository == null) {
            Repositories repositories = Repositories.getInstance();

            irrigationHourRepository = repositories.getIrrigationHourRepository();
        }
        return irrigationHourRepository;
    }

    private FarmManagerRepository getFarmManagerRepository() {
        if (farmManagerRepository == null) {
            Repositories repositories = Repositories.getInstance();

            farmManagerRepository = repositories.getGestorAgricolaRepository();
        }
        return farmManagerRepository;
    }

    public boolean createPlan(String date) {

        irrigationDateRepository.clearDate();
        Date startDate = null;

        try {
            startDate = DATA_FORMAT.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        List<Date> dates = getDatesBetween(startDate);

        for (Date data : dates) {
            if (irrigationDateRepository.createIrrigationDate(data).isEmpty())
                return false;
        }
        return true;
    }

    public Map<IrrigationSector, Integer> searchIrrigation(String dataString, String hora) throws ParseException {

        Date dataPesquisa = DATA_FORMAT.parse(dataString);

        if (checkIfDateExists(dataPesquisa)) {

            int dia = Integer.parseInt(getNumbersBeforeSlash(dataString.toString()));
            List<IrrigationSector> planoDeRega = irrigationSectorRepository.getIrrigationSectors();
            List<IrrigationHour> horarioDeRega = irrigationHourRepository.getIrrigationHours();
            List<IrrigationSector> listaFinal = new ArrayList<>();
            hora = hora.strip();
            int minutoPesquisa = convertHoursToMinutes(hora);

            for (IrrigationSector regaDiaria : planoDeRega) {
                if (regaDiaria.getPeriodicidade() == 'T')
                    listaFinal.add(regaDiaria);
                else if (regaDiaria.getPeriodicidade() == 'P' && dia % 2 == 0)
                    listaFinal.add(regaDiaria);
                else if (regaDiaria.getPeriodicidade() == 'I' && dia % 2 != 0)
                    listaFinal.add(regaDiaria);
                else if (regaDiaria.getPeriodicidade() == '3' && dia % 3 == 0)
                    listaFinal.add(regaDiaria);

            }
            Map<IrrigationSector, Integer> resultMap = new HashMap<>();

            for (IrrigationHour horarioInicial : horarioDeRega) {
                int minutoInicial = convertHoursToMinutes(horarioInicial.getHour());
                listaFinal.stream()
                        .forEach((regaDiaria) -> {
                            int minutoFinal = minutoInicial + regaDiaria.getDuracao();
                            if (minutoInicial <= minutoPesquisa && minutoFinal > minutoPesquisa) {
                                resultMap.put(regaDiaria, minutoFinal - minutoPesquisa);
                            }
                        });
            }
            return resultMap;
        }
        return null;
    }

    public List<Date> getDatesBetween(Date startDate) {
        List<Date> diasDeRega = new ArrayList<>();
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startDate);
            for (int i = 0; i < 30; i++) {
                diasDeRega.add(calendar.getTime());
                calendar.add(Calendar.DATE, 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return diasDeRega;
    }

    private int convertHoursToMinutes(String hora) {

        String[] horas = hora.split(":");
        int hour = Integer.parseInt(horas[0]);
        int minutes = Integer.parseInt(horas[1]);
        hour = hour * 60;
        minutes += hour;
        return minutes;
    }

    private boolean checkIfDateExists(Date dataPesquisa) {

        List<IrrigationDate> diasDeRega = irrigationDateRepository.getIrrigationDates();
        for (IrrigationDate date : diasDeRega) {
            if (date.getDate().equals(dataPesquisa)) {
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

    public boolean executeWatering() {
        for (IrrigationDate date : irrigationDateRepository.getIrrigationDates()) {
            for (IrrigationHour hour : irrigationHourRepository.getIrrigationHours()) {
                for (IrrigationSector sector : irrigationSectorRepository.getIrrigationSectors()) {
                    try {
                        java.sql.Date operationDate = new java.sql.Date(date.getDate().getTime());
                        java.sql.Time operationTime = java.sql.Time.valueOf(LocalTime.parse(hour.getHour()));
                        try {
                            farmManagerRepository.registerRega(sector.getDuracao(), sector.getSector(), operationDate,
                                    operationTime);
                            System.out.println(
                                    "Rega: " + sector.getSector() + " " + operationDate + " registada com sucesso!");
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                            System.out.println(
                                    "Rega: " + sector.getSector() + " " + operationDate + " não foi registada!");
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public List<IrrigationSector> getIrrigationSectors() {
        return irrigationSectorRepository.getIrrigationSectors();
    }

    public List<IrrigationDate> getIrrigationDates() {
        return irrigationDateRepository.getIrrigationDates();
    }

    public List<IrrigationHour> getIrrigationHours() {
        return irrigationHourRepository.getIrrigationHours();
    }
}