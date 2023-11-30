package learningtest.template;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Calculator {
    public int calcSum(String filePath) throws IOException {
        return lineReadTemplate(filePath, (String line, Integer result) -> Integer.parseInt(line) + result, 0);
    }

    public int calcMultiply(String filePath) throws IOException {
        return lineReadTemplate(filePath, (String line, Integer result) -> Integer.parseInt(line) * result, 1);
    }

    public String concatenate(String filePath) throws IOException {
        return lineReadTemplate(filePath, (String line, String result) -> result + line, "");
    }

    public int fileReadTemplate(String filePath, BufferedReaderCallback callback) throws IOException {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath))) {
            return callback.doSomethingWithReader(bufferedReader);
        }
    }

    public <T> T lineReadTemplate(String filePath, LineCallback<T> lineCallback, T initVal) throws IOException {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath))) {
            T result = initVal;
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                result = lineCallback.doSomethingWithLine(line, result);
            }

            return result;
        }
    }
}
