package tests;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import base.BaseTest;

import io.qameta.allure.Description;

public class DataDriven extends BaseTest {

    @ParameterizedTest
    @MethodSource("csvDataProvider")
    @DisplayName("Data Driven tests")
    @Description("Data driven tests descr")
    public void simpleDataDrivenTest(String screenshotName, String test, String url) {
        page.navigate(url);
        takeScreenshotAndAttachToReport(screenshotName);
    }

    static Stream<Arguments> csvDataProvider() {
        try {
            InputStream input = DataDriven.class.getClassLoader().getResourceAsStream("test-data/testdata.csv");
            if (input == null) {
                throw new FileNotFoundException("CSV file not found in resources!");
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(input));

            List<Arguments> arguments = reader.lines()
                    .skip(1)
                    .map(line -> line.split(",")) // splits all columns
                    .map(data -> Arguments.of((Object[]) Stream.of(data)
                            .map(String::trim)
                            .toArray(String[]::new))) // handles dynamic column count
                    .toList();

            return arguments.stream();

        } catch (Exception e) {
            e.printStackTrace();
            return Stream.empty();
        }
    }

}
