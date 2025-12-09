package com.example.bmicalculator;

public class BMICalculator {

    public static float calculateBMI(float heightInCm, float weightInKg) {
        if (heightInCm <= 0 || weightInKg <= 0) {
            throw new IllegalArgumentException("Height and weight must be positive values");
        }

        float heightInMeters = heightInCm / 100f;
        return weightInKg / (heightInMeters * heightInMeters);
    }

    public static String getBMICategory(float bmi) {
        if (bmi < 16) {
            return "Severe Thinness";
        } else if (bmi >= 16 && bmi < 17) {
            return "Moderate Thinness";
        } else if (bmi >= 17 && bmi < 18.5) {
            return "Mild Thinness";
        } else if (bmi >= 18.5 && bmi < 25) {
            return "Normal";
        } else if (bmi >= 25 && bmi < 30) {
            return "Overweight";
        } else {
            return "Obese Class I";
        }
    }

    public static boolean isValidInput(String gender, int height, int age, int weight) {
        return !gender.equals("0") && height > 0 && age > 0 && weight > 0;
    }

}