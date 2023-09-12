package helpers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class Utils {

    public static String readFile(String pathToFile) {

        BufferedReader reader;
        String line = null;
        StringBuilder stringBuilder = new StringBuilder();
        String ls = System.getProperty("line.separator");

        try {
            reader = new BufferedReader(new FileReader(pathToFile, StandardCharsets.UTF_8));
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
                stringBuilder.append(ls);
            }
            reader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return  stringBuilder.toString();
    }


}