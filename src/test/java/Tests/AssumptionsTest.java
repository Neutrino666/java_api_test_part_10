package Tests;


import Work.TestingClass;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumingThat;

public class AssumptionsTest {
    TestingClass testingClass = new TestingClass();


    @Test
    public void test1() {
        System.setProperty("condition", "true");
        assumingThat(System.getProperty("condition").equals("true"), () -> {
            assertEquals(6, testingClass.addition(2, 4));
            System.out.println("Первая строка прошла");
        });

        assertEquals(5, testingClass.addition(2, 3));
        System.out.println("Вторая строка прошла");
    }
}
