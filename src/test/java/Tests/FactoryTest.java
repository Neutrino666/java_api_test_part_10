package Tests;


import Work.TestingClass;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

public class FactoryTest {

    TestingClass testingClass = new TestingClass();

    @TestFactory  // должен возвращать collection или string из объектов DynamicTest
    Collection<DynamicTest> dynamicTests1() {
        return Arrays.asList(
                dynamicTest("first dynamicTest", () -> assertEquals(4, testingClass.addition(2, 2))),
                dynamicTest("second dynamicTest", () -> assertEquals(5, testingClass.addition(3, 2))),
                dynamicTest("third dynamicTest", () -> assertEquals(6, testingClass.addition(4, 2)))
        );
    }

    @TestFactory
    Collection<DynamicTest> dynamicTests2() {
        return Arrays.asList(
                dynamicTest("first dynamicTest", () -> assertEquals(4, testingClass.addition(2, 2))),
                dynamicTest("second dynamicTest", () -> assertEquals(8, testingClass.addition(6, 2)))
        );
    }
}
