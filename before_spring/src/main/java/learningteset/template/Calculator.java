package learningteset.template;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Calculator {
    public int calcSum(String filePath) throws IOException {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath))) {
            int sum = 0;
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                sum += Integer.parseInt(line);
            }

            return sum;
        }
    }
}
