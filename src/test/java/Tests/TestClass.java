package Tests;

import Work.TestingClass;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.temporal.ChronoUnit;
import java.util.EnumSet;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestClass {


    @Test
    public void test1() {
        TestingClass testingClass = new TestingClass();
        assertEquals(4, testingClass.addition(2, 2), "Проверка равенства");
    }

    @Test
    @DisplayName("Тестирование метода сложения")
    public void test2() {
        TestingClass testingClass = new TestingClass();
        assertEquals(4, testingClass.addition(2,2), "Проверка равенства");
    }

    @RepeatedTest(2)
    public void test3() {
        TestingClass testingClass = new TestingClass();
        assertEquals(4, testingClass.addition(2,2), "Проверка равенства");
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3})
    public void test4(int number) {
        TestingClass testingClass = new TestingClass();
        assertEquals(number * 2, testingClass.addition(number,number), "Проверка равенства");
    }

    @ParameterizedTest
    @EnumSource(names = {"DAYS", "HOURS"})
    public void test5(ChronoUnit unit) {
        assertTrue(EnumSet.of(ChronoUnit.DAYS, ChronoUnit.HOURS).contains(unit));
    }

    @ParameterizedTest
    @MethodSource("sourceProvider")
    public void test6(String string, int number) {
        assertTrue(string.length() > 0);
        assertTrue((number == 1 || number == 2));
    }

    static Stream<Arguments> sourceProvider() {
        return Stream.of(
                Arguments.arguments("один", 1),
                Arguments.arguments("два", 2)
        );
    }

    @ParameterizedTest
    @CsvSource({
            "один,1",
            "два,2"
    })
    public void test7(String string, int number) {
        assertTrue(string.length() > 0);
        assertTrue((number == 1 || number == 2));
    }

    @Test
    @Disabled("Отключен для демонстрации")
    public void test8() {
        TestingClass testingClass = new TestingClass();
        assertEquals(4, testingClass.addition(2, 3), "Проверка равенства");
    }

    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    public void test9() {
        long currentTime = System.currentTimeMillis();
        while (System.currentTimeMillis() < currentTime + 4000) {

        }
    }

    @Test
    public void test10(@TempDir Path tempDir) throws IOException {
        Path file = tempDir.resolve(("tempFile.txt"));
        FileWriter fileWriter = new FileWriter(file.toFile());
        fileWriter.write("first\n");
        fileWriter.write("second\n");
        fileWriter.flush();
        fileWriter.close();
        assertTrue(Files.readAllLines(file).contains("first"));
        assertTrue(Files.readAllLines(file).contains("second"));
    }

    @BeforeAll
    public void beforeAll() {
        System.out.println("Метод выполняется 1 раз перед всеми тестами в классе");
    }

    @AfterAll
    public void afterAll() {
        System.out.println("Метод выполняется 1 раз после всех тестов в классе");
    }

    @BeforeEach
    public void beforeEach() {
        System.out.println("Метод выполняется каждый раз перед выполнением теста");
    }

    @AfterEach
    public void afterEach() {
        System.out.println("Метод выполняется каждый раз после выполнения теста");
    }

}
