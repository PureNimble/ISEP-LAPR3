package isep.lapr3.g094.Controller;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Test;
import isep.lapr3.g094.Domain.RegaDiaria;

public class ControllerTest {

@Test
public void testSearchCheckOddDays() throws IOException {
    Controller controller = new Controller();
    controller.importData();
    int dia;
    String[] horas = {"8:31", "17:01"}; 
    Set<Character> tiposRega = new HashSet<>();
    //Check all the odd days
    for(String hora : horas){
        dia = 3;
        List<RegaDiaria> output = controller.search(dia, hora);
        
            tiposRega = output.stream().map(i -> i.getTipoRega()).collect(Collectors.toSet());

            System.out.println(tiposRega);
            System.out.println(dia);
            assertEquals(true, tiposRega.contains('I'));
            assertEquals(false, tiposRega.contains('P'));
        
    }
}
@Test
public void testSearchCheckEvenDays() throws NumberFormatException, IOException{
    Controller controller = new Controller();
    controller.importData();
    int dia;
    String[] horas = {"08:31", "17:01"}; 
    Set<Character> tiposRega = new HashSet<>();

    for(String hora : horas){
        dia = 2;
        List<RegaDiaria> output = controller.search(dia, hora);
        
            tiposRega = output.stream().map(i -> i.getTipoRega()).collect(Collectors.toSet());

            System.out.println(tiposRega);
            System.out.println(dia);
            assertEquals(true, tiposRega.contains('P'));
            assertEquals(false, tiposRega.contains('I'));
        }
    }

}
