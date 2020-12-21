import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BeverageTest {

    @Test
    void getDesc() {
        System.out.println("Bevarage_Desc");
        String expectedResult="unknown beverage";
        BeverageImpl bi=new BeverageImpl();
        String outputResult=bi.getDesc();
        assertEquals(expectedResult,outputResult);
    }

    @Test
    void cost() {
        System.out.println("Bevarage_Cost");
        double expectedResult=0;
        BeverageImpl bi=new BeverageImpl();
        double outputResult=bi.cost();
        assertEquals(expectedResult,outputResult);
    }

    class BeverageImpl extends Beverage {
        public BeverageImpl(){
            super();
        }
    }
}