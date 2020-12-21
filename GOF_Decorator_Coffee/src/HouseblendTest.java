import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HouseblendTest {
    @Test
    void testGetDesc(){
        System.out.println("HouseBlend_Desc");
        Houseblend hb=new Houseblend();
        String expectedResult="Houseblend";
        String outputResult=hb.getDesc();
        assertEquals(expectedResult,outputResult);
    }

    @Test
    void testCost() {
        System.out.println("HouseBlend_Cost");
        Houseblend hb=new Houseblend();
        double expectedResult=0.99;
        double outputResult=hb.cost();
        assertEquals(expectedResult,outputResult);
    }
}