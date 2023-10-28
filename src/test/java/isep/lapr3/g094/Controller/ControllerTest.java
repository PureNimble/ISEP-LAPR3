package isep.lapr3.g094.Controller;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Test;
import isep.lapr3.g094.Domain.RegaDiaria;

public class ControllerTest {

    @Test
    public void testSearchCheckOddDays() throws IOException, ParseException {
        Controller controller = new Controller();
        controller.createPlan("01/12/2020");
        Map<RegaDiaria,Integer> output;
        Set<Character> tiposRega = new HashSet<>();
        for(int i = 3; i < 30; i+=2){
            
            output = controller.search(i + "/12/2020", "8:31");
            tiposRega = output.entrySet().stream().map((entry) -> entry.getKey().getTipoRega()).collect(Collectors.toSet());

            assertEquals(true,tiposRega.contains('I') );
        }
    }
    @Test
    public void testSearchCheckEvenDays() throws  IOException, ParseException{
        Controller controller = new Controller();
        controller.createPlan("01/12/2020");
        Map<RegaDiaria,Integer> output;
        Set<Character> tiposRega = new HashSet<>();
        for(int i = 2; i < 30; i+=2){
            int day = i;
            
            output = controller.search(day + "/12/2020", "8:31");
            tiposRega = output.entrySet().stream().map((entry) -> entry.getKey().getTipoRega()).collect(Collectors.toSet());

            assertEquals(true,tiposRega.contains('P') );
            assertEquals(true,tiposRega.contains('T') );
        }
    }
    @Test
    public void testSearchCheck3Days() throws  IOException, ParseException{
        Controller controller = new Controller();
        controller.createPlan("01/12/2020");
        Map<RegaDiaria,Integer> output;
        Set<Character> tiposRega = new HashSet<>();
        for(int i = 3; i < 30; i+=3){
            int day = i;
            
            output = controller.search(day + "/12/2020", "8:31");
            tiposRega = output.entrySet().stream().map((entry) -> entry.getKey().getTipoRega()).collect(Collectors.toSet());

            assertEquals(true,tiposRega.contains('3') );
        }
    }
    @Test
    public void testSeachCheckHour() throws  IOException, ParseException{
        Controller controller = new Controller();
        controller.createPlan("01/12/2020");
        Map<RegaDiaria,Integer> output;
        Set<Character> tiposRega = new HashSet<>();
        for(int i = 2; i < 30; i+=2){
            int day = i;
            
            output = controller.search(day + "/12/2020", "8:31");
            tiposRega = output.entrySet().stream().map((entry) -> entry.getKey().getTipoRega()).collect(Collectors.toSet());

            assertEquals(true,tiposRega.contains('P') );
        }
  
    }
}
