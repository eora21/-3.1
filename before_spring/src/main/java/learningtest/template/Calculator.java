package learningtest.template;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Calculator {
    public int calcSum(String filePath) throws IOException {
        return fileReadTemplate(filePath, (BufferedReader bufferedReader) -> {
            int sum = 0;
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                sum += Integer.parseInt(line);
            }

            return sum;
        });
    }

    public int calcMultiply(String filePath) throws IOException {
        return fileReadTemplate(filePath, (BufferedReader bufferedReader) -> {
            int multiply = 1;
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                multiply *= Integer.parseInt(line);
            }

            return multiply;
        });
    }

    public int fileReadTemplate(String filePath, BufferedReaderCallback callback) throws IOException {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath))) {
            return callback.doSomethingWithReader(bufferedReader);
        }
    }

    public int lineReadTemplate(String filePath, LineCallback lineCallback, int initVal) throws IOException {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath))) {
            int result = initVal;
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                result = lineCallback.doSomethingWithLine(line, result);
            }

            return result;
        }
    }
}
