package com.example.bmicalculator;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;


public class BMICalculatorTest {

    

    @Test
    void testCalculateBMI_validInput() {
        float bmi = BMICalculator.calculateBMI(170, 65);
        assertEquals(22.49f, bmi, 0.01f);
    }

    @Test
    void testCalculateBMI_invalidInput_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> BMICalculator.calculateBMI(0, 65));
        assertThrows(IllegalArgumentException.class, () -> BMICalculator.calculateBMI(170, 0));
        assertThrows(IllegalArgumentException.class, () -> BMICalculator.calculateBMI(-170, 65));
    }

    @Test
    void testGetBMICategory() {
        assertEquals("Severe Thinness", BMICalculator.getBMICategory(15.5f));
        assertEquals("Moderate Thinness", BMICalculator.getBMICategory(16.5f));
        assertEquals("Mild Thinness", BMICalculator.getBMICategory(17.5f));
        assertEquals("Normal", BMICalculator.getBMICategory(22.0f));
        assertEquals("Overweight", BMICalculator.getBMICategory(27.0f));
        assertEquals("Obese Class I", BMICalculator.getBMICategory(31.0f));
    }

    @Test
    void testIsValidInput() {
        assertTrue(BMICalculator.isValidInput("Male", 170, 25, 65));
        assertFalse(BMICalculator.isValidInput("0", 170, 25, 65));
        assertFalse(BMICalculator.isValidInput("Female", 0, 25, 65));
        assertFalse(BMICalculator.isValidInput("Female", 170, 0, 65));
        assertFalse(BMICalculator.isValidInput("Female", 170, 25, 0));
    }
    //Parameterized with @CsvSource

    @ParameterizedTest
    @CsvSource({
            "170, 65, 22.49",
            "160, 50, 19.53",
            "180, 90, 27.78"
    })
    void testCalculateBMI_CsvSource(float height, float weight, float expectedBmi) {
        float actual = BMICalculator.calculateBMI(height, weight);
        assertEquals(expectedBmi, actual, 0.01f);
    }

    @ParameterizedTest
    @CsvSource({
            "15.5, Severe Thinness",
            "16.5, Moderate Thinness",
            "17.5, Mild Thinness",
            "22.0, Normal",
            "27.0, Overweight",
            "32.0, Obese Class I"
    })
    void testGetBMICategory_CsvSource(float bmi, String expectedCategory) {
        assertEquals(expectedCategory, BMICalculator.getBMICategory(bmi));
    }

    //Parameterized with @CsvFileSource

    @ParameterizedTest
    @CsvFileSource(resources = "/bmi_test_data.csv", numLinesToSkip = 1)
    void testBMIComputationAndCategory(float height, float weight, float expectedBmi, String expectedCategory) {
        float calculatedBmi = BMICalculator.calculateBMI(height, weight);
        assertEquals(expectedBmi, calculatedBmi, 0.01f);

        String category = BMICalculator.getBMICategory(calculatedBmi);
        assertEquals(expectedCategory,category);
    }

    //Parameterized with @MethodSource

    @ParameterizedTest
    @MethodSource("validInputProvider")
    void testIsValidInput_MethodSource(String gender, int height, int age, int weight, boolean expected) {
        assertEquals(expected, BMICalculator.isValidInput(gender, height, age, weight));
    }

    private static Stream<Arguments> validInputProvider() {
        return Stream.of(
                Arguments.of("Male", 170, 25, 65, true),
                Arguments.of("0", 170, 25, 65, false),
                Arguments.of("Female", 0, 25, 65, false),
                Arguments.of("Female", 170, 0, 65, false),
                Arguments.of("Female", 170, 25, 0, false)
        );
    }


}