import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExpressoTest {

    @Test
    void testGetDesc(){
        System.out.println("Expresso_Desc");
        Expresso ex=new Expresso();
        String expectedResult="Expresso";
        String outputResult=ex.getDesc();
        assertEquals(expectedResult,outputResult);
    }

    @Test
    void cost() {
        System.out.println("Expresso_Cost");
        Expresso ex=new Expresso();
        double expectedResult=1.99;
        double outputResult=ex.cost();
        assertEquals(expectedResult,outputResult);
    }
}