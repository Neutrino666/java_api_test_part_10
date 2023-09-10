package helpers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
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
//            e.printStackTrace();
        }
        return  stringBuilder.toString();
    }


}