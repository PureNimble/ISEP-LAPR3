package isep.lapr3.g094.application.controller;

import java.sql.SQLException;
import java.sql.Time;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import isep.lapr3.g094.domain.Pair;
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
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

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

    public Map<IrrigationSector, Pair<Integer, Boolean>> searchIrrigation(Date dataPesquisa, String hora)
            throws ParseException {

        if (checkIfDateExists(dataPesquisa)) {

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dataPesquisa);
            int dia = calendar.get(Calendar.DAY_OF_MONTH);
            List<IrrigationDate> diasDeRega = irrigationDateRepository.getIrrigationDates();
            List<IrrigationSector> planoDeRega = irrigationSectorRepository.getIrrigationSectors();
            List<IrrigationHour> horarioDeRega = irrigationHourRepository.getIrrigationHours();
            List<IrrigationSector> listaFinal = new ArrayList<>();
            IrrigationDate startDay = diasDeRega.getFirst();
            hora = hora.strip();
            LocalDate startDate = startDay.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate currentDate = dataPesquisa.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            long daysBetween = ChronoUnit.DAYS.between(startDate, currentDate);

            int dayNumber = (int) (daysBetween + 1);
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
            Map<IrrigationSector, Pair<Integer, Boolean>> resultMap = new HashMap<>();

            for (IrrigationHour horarioInicial : horarioDeRega) {
                int minutoInicial = convertHoursToMinutes(horarioInicial.getHour());
                listaFinal.stream()
                        .forEach((regaDiaria) -> {
                            int minutoFinal = minutoInicial + regaDiaria.getDuracao();
                            if (minutoInicial <= minutoPesquisa && minutoFinal > minutoPesquisa) {
                                if (dayNumber == regaDiaria.getRecorrencia()) {
                                    resultMap.put(regaDiaria, new Pair<>(minutoFinal - minutoPesquisa, true));
                                } else {
                                    resultMap.put(regaDiaria, new Pair<>(minutoFinal - minutoPesquisa, false));
                                }
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

    public boolean registerOperation(IrrigationSector sector, int dayNum, java.sql.Date operationDate,
            Time operationTime) {
        try {
            if (sector.getRecorrencia() != dayNum) {
                farmManagerRepository.registerRega(sector.getDuracao(), sector.getSector(),
                        operationDate,
                        operationTime, 2);
                System.out
                        .println("Rega registada com sucesso. \nDetalhes: Setor: " + sector.getSector()
                                + " Data: " + operationDate + " Hora: " + operationTime + " Duração: "
                                + sector.getDuracao() + "min\n");
            } else {
                farmManagerRepository.registerFertirrega(sector.getDuracao(), sector.getSector(),
                        operationDate,
                        operationTime, 2, sector.getMix());
                System.out
                        .println("Fertirrega registada com sucesso. \nDetalhes: Setor: "
                                + sector.getSector()
                                + " Data: " + operationDate + " Hora: " + operationTime + " Duração: "
                                + sector.getDuracao() + "min, " + "Mix: " + sector.getMix() + "\n");
            }
        } catch (SQLException e) {
            System.out.println(
                    "Não foi possível registar a rega. \nDetalhes: Setor: " + sector.getSector()
                            + " Data: " + operationDate + " Hora: " + operationTime + " Duração: "
                            + sector.getDuracao() + "min\n");
            System.out.println("Motivo: " + e.getMessage());
            return false;
        }
        return true;
    }

    public boolean executeDay(Date date, LocalTime time) {
        List<IrrigationSector> sectors = irrigationSectorRepository.getIrrigationSectors();
        sectors.sort(Comparator.comparingInt(IrrigationSector::getDuracao));
        List<IrrigationDate> diasDeRega = irrigationDateRepository.getIrrigationDates();
        List<IrrigationHour> irrigationHours = irrigationHourRepository.getIrrigationHours();
        IrrigationDate startDay = diasDeRega.getFirst();
        LocalDate startDate = startDay.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        boolean confirm = false;
        if (!diasDeRega.contains(new IrrigationDate(date)))
            return confirm;

        for (int i = 0; i < irrigationHours.size(); i++) {
            LocalTime irrigationHour = LocalTime.parse(irrigationHours.get(i).getHour());
            Time operationTime = Time.valueOf(LocalTime.parse(irrigationHours.get(i).getHour()));
            for (IrrigationSector sector : sectors) {
                if ((irrigationHour.plusMinutes(sector.getDuracao())).isAfter(time)) {
                    confirm = true;
                    char periodicidade = sector.getPeriodicidade();
                    boolean shouldRegister = (periodicidade == 'T') ||
                            (periodicidade == 'P' && i % 2 == 0) ||
                            (periodicidade == 'I' && i % 2 != 0) ||
                            (periodicidade == '3' && i % 3 == 0);
                    int dayNum = (int) ChronoUnit.DAYS.between(startDate,
                            date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()) + 1;

                    if (shouldRegister) {
                        registerOperation(sector, dayNum, new java.sql.Date(date.getTime()), operationTime);
                    }
                }
            }
        }

        return confirm;
    }

    public boolean executeWatering() {
        List<IrrigationSector> sectors = irrigationSectorRepository.getIrrigationSectors();
        sectors.sort(Comparator.comparingInt(IrrigationSector::getDuracao));
        List<IrrigationDate> diasDeRega = irrigationDateRepository.getIrrigationDates();
        List<IrrigationHour> irrigationHours = irrigationHourRepository.getIrrigationHours();
        IrrigationDate startDay = diasDeRega.getFirst();
        LocalDate startDate = startDay.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        for (IrrigationDate date : diasDeRega) {
            LocalDate currentDate = date.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            int dia = currentDate.getDayOfMonth();
            java.sql.Date operationDate = java.sql.Date.valueOf(currentDate);

            for (IrrigationHour hour : irrigationHours) {
                Time operationTime = Time.valueOf(LocalTime.parse(hour.getHour()));
                for (IrrigationSector sector : sectors) {
                    char periodicidade = sector.getPeriodicidade();
                    boolean shouldRegister = (periodicidade == 'T') ||
                            (periodicidade == 'P' && dia % 2 == 0) ||
                            (periodicidade == 'I' && dia % 2 != 0) ||
                            (periodicidade == '3' && dia % 3 == 0);
                    int dayNum = (int) ChronoUnit.DAYS.between(startDate, currentDate) + 1;

                    if (shouldRegister) {
                        registerOperation(sector, dayNum, operationDate, operationTime);
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