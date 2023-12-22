package isep.lapr3.g094.application.controller;

import java.sql.SQLException;
import java.text.ParseException;
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
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.time.LocalTime;

public class IrrigationPlanController {

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

            farmManagerRepository = repositories.getFarmManagerRepository();
        }
        return farmManagerRepository;
    }

    public boolean createPlan(Date date) {

        irrigationDateRepository.clearDate();

        List<Date> dates = getDatesBetween(date);

        for (Date data : dates) {
            if (irrigationDateRepository.createIrrigationDate(data).isEmpty())
                return false;
        }
        return true;
    }

    public Map<IrrigationSector, Integer> searchIrrigation(Date dataPesquisa, String hora) throws ParseException {

        if (checkIfDateExists(dataPesquisa)) {

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dataPesquisa);
            int dia = calendar.get(Calendar.DAY_OF_MONTH);
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

    public boolean executeWatering() {
        List<IrrigationSector> sectors = irrigationSectorRepository.getIrrigationSectors();
        sectors.sort(Comparator.comparingInt(IrrigationSector::getDuracao));
        for (IrrigationDate date : irrigationDateRepository.getIrrigationDates()) {
            for (IrrigationHour hour : irrigationHourRepository.getIrrigationHours()) {
                for (IrrigationSector sector : sectors) {
                    java.sql.Date operationDate = new java.sql.Date(date.getDate().getTime());
                    java.sql.Time operationTime = java.sql.Time.valueOf(LocalTime.parse(hour.getHour()));
                    try {
                        farmManagerRepository.registerRega(sector.getDuracao(), sector.getSector(),
                                operationDate,
                                operationTime, 2);
                        System.out.println("Rega registada com sucesso. \nDetalhes: Setor: " + sector.getSector()
                                + " Data: " + operationDate + " Hora: " + operationTime + " Duração: " + sector.getDuracao() + "min\n");
                    } catch (SQLException e) {
                        System.out.println("Não foi possível registar a rega. \nDetalhes: Setor: " + sector.getSector()
                                + " Data: " + operationDate + " Hora: " + operationTime + " Duração: " + sector.getDuracao() + "min\n");
                        System.out.println("Motivo: " + e.getMessage());
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