package isep.lapr3.g094.Controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Test;

import isep.lapr3.g094.controller.IrrigationController;
import isep.lapr3.g094.domain.DailyIrrigation;

public class IrrigationTest {

    @Test
    public void testSearchCheckOddDays() throws IOException, ParseException {
        IrrigationController controller = new IrrigationController();
        controller.createPlan("01/12/2020");
        Map<DailyIrrigation,Integer> output;
        Set<Character> tiposRega = new HashSet<>();
        for(int i = 1; i < 30; i+=2){
            
            output = controller.search(i + "/12/2020", "8:31");
            tiposRega = output.entrySet().stream().map((entry) -> entry.getKey().getTipoRega()).collect(Collectors.toSet());

            assertEquals(true,tiposRega.contains('I'));
            assertEquals(true,tiposRega.contains('T'));
        }
    }
    @Test
    public void testSearchCheckEvenDays() throws  IOException, ParseException{
        IrrigationController controller = new IrrigationController();
        controller.createPlan("01/12/2020");
        Map<DailyIrrigation,Integer> output;
        Set<Character> tiposRega = new HashSet<>();
        for(int i = 2; i < 30; i+=2){
            
            output = controller.search(i + "/12/2020", "8:31");
            tiposRega = output.entrySet().stream().map((entry) -> entry.getKey().getTipoRega()).collect(Collectors.toSet());

            assertEquals(true,tiposRega.contains('P'));
            assertEquals(true,tiposRega.contains('T'));
        }
    }
    @Test
    public void testSearchCheck3Days() throws  IOException, ParseException{
        IrrigationController controller = new IrrigationController();
        controller.createPlan("01/12/2020");
        Map<DailyIrrigation,Integer> output;
        Set<Character> tiposRega = new HashSet<>();
        for(int i = 3; i < 30; i+=3){
            
            output = controller.search(i + "/12/2020", "8:31");
            tiposRega = output.entrySet().stream().map((entry) -> entry.getKey().getTipoRega()).collect(Collectors.toSet());

            assertEquals(true,tiposRega.contains('3'));
            assertEquals(true,tiposRega.contains('T'));
        }
    }

    @Test
    public void testSearchCheck30PlusDays() throws  IOException, ParseException{
        IrrigationController controller = new IrrigationController();
        controller.createPlan("01/11/2020");
        Map<DailyIrrigation,Integer> output;            
        output = controller.search("25/12/2020", "8:31");
        assertNull(output);
    }

    @Test
    public void testSeachCheckHour() throws  IOException, ParseException{
        IrrigationController controller = new IrrigationController();
        controller.createPlan("01/12/2020");
        Map<DailyIrrigation,Integer> output;
        Set<Character> tiposRega = new HashSet<>();
        for(int i = 2; i < 30; i+=2){

            output = controller.search(i + "/12/2020", "8:31");
            tiposRega = output.entrySet().stream().map((entry) -> entry.getKey().getTipoRega()).collect(Collectors.toSet());

            assertEquals(true,tiposRega.contains('P'));
        }
  
    }

    @Test
    public void testSeachCheckNonExistentHours() throws  IOException, ParseException{
        IrrigationController controller = new IrrigationController();
        controller.createPlan("01/12/2020");
        Map<DailyIrrigation,Integer> output;
        Set<Character> tiposRega = new HashSet<>();
        for(int i = 1; i < 30; i++){            
            output = controller.search(i + "/12/2020", "9:31");
            tiposRega = output.entrySet().stream().map((entry) -> entry.getKey().getTipoRega()).collect(Collectors.toSet());

            assertEquals(true,tiposRega.isEmpty());
        }
  
    }
}
