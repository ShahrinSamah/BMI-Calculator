package com.example.bmicalculator;

// ==================== STRATEGY PATTERN ====================
interface BMICategoryStrategy {
    String getCategory();
    boolean isInRange(float bmi);
}

class SevereThinnessStrategy implements BMICategoryStrategy {
    @Override public String getCategory() { return "Severe Thinness"; }
    @Override public boolean isInRange(float bmi) { return bmi < 16; }
}

class ModerateThinnessStrategy implements BMICategoryStrategy {
    @Override public String getCategory() { return "Moderate Thinness"; }
    @Override public boolean isInRange(float bmi) { return bmi >= 16 && bmi < 17; }
}

class MildThinnessStrategy implements BMICategoryStrategy {
    @Override public String getCategory() { return "Mild Thinness"; }
    @Override public boolean isInRange(float bmi) { return bmi >= 17 && bmi < 18.5; }
}

class NormalStrategy implements BMICategoryStrategy {
    @Override public String getCategory() { return "Normal"; }
    @Override public boolean isInRange(float bmi) { return bmi >= 18.5 && bmi < 25; }
}

class OverweightStrategy implements BMICategoryStrategy {
    @Override public String getCategory() { return "Overweight"; }
    @Override public boolean isInRange(float bmi) { return bmi >= 25 && bmi < 30; }
}

class ObeseStrategy implements BMICategoryStrategy {
    @Override public String getCategory() { return "Obese Class I"; }
    @Override public boolean isInRange(float bmi) { return bmi >= 30; }
}

// ==================== SIMPLE FACTORY PATTERN ====================
class SimpleBMICategoryFactory {
    public BMICategoryStrategy createStrategy(float bmi) {
        BMICategoryStrategy strategy = null;

        if (bmi < 16) {
            strategy = new SevereThinnessStrategy();
        } else if (bmi >= 16 && bmi < 17) {
            strategy = new ModerateThinnessStrategy();
        } else if (bmi >= 17 && bmi < 18.5) {
            strategy = new MildThinnessStrategy();
        } else if (bmi >= 18.5 && bmi < 25) {
            strategy = new NormalStrategy();
        } else if (bmi >= 25 && bmi < 30) {
            strategy = new OverweightStrategy();
        } else {
            strategy = new ObeseStrategy();
        }
        return strategy;
    }
}

// ==================== MAIN CALCULATOR CLASS (FACADE STYLE) ====================
public class BMICalculator {

    private static final SimpleBMICategoryFactory factory = new SimpleBMICategoryFactory();

    // Public method: Calculate BMI
    public static float calculateBMI(float heightCm, float weightKg) {
        if (heightCm <= 0 || weightKg <= 0) {
            throw new IllegalArgumentException("Height and weight must be positive");
        }
        float heightM = heightCm / 100f;
        return weightKg / (heightM * heightM);
    }

    // Public method: Get category using Strategy + Factory
    public static String getBMICategory(float bmi) {
        if (bmi < 0) {
            throw new IllegalArgumentException("BMI cannot be negative");
        }
        BMICategoryStrategy strategy = factory.createStrategy(bmi);
        return strategy.getCategory();
    }

    // Public method: Full validation
    public static boolean isValidInput(String gender, int height, int age, int weight) {
        return gender != null && !"0".equals(gender)
                && height > 0
                && age > 0
                && weight > 0;
    }
}