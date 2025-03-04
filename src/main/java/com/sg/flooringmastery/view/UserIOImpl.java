package com.sg.flooringmastery.view;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class UserIOImpl implements UserIO{
    Scanner sc = new Scanner(System.in);
    public int min;
    public int max;
    public boolean intChecker = false;
    public boolean doubleChecker = false;
    public boolean floatChecker = false;
    public boolean longChecker = false;
    public boolean bigdecimalChecker = false;

    @Override
    public void print(String message) {
        System.out.println(message);
    }

    /**
     * Reads a string input from the user.
     * @param prompt the prompt for the input
     * @return the string input
     */
    @Override
    public String readString(String prompt) {
        System.out.println(prompt);
        return sc.nextLine();
    }

    /**
     * Reads an int input from the user.
     * @param prompt the prompt for the input
     * @return the int input
     */
    @Override
    public int readInt(String prompt) {
        System.out.println(prompt);
        return Integer.parseInt(sc.nextLine());
    }

    /**
     * Reads an int input from the user between two values.
     * @param prompt the prompt for the input
     * @param min minimum accepted int
     * @param max maximum accepted int
     * @return the integer input from the user
     */
    @Override
    public int readInt(String prompt, int min, int max) {
        while (!intChecker) {
            System.out.println(prompt);
            int input = Integer.parseInt(sc.nextLine());
            if (input >= min && input <= max) {
                return input;
            } else {
                intChecker = false;
            }
        }
        return 0;
    }

    /**
     * Reads a double input from the user
     * @param prompt the prompt for the input
     * @return the double input from the user
     */
    @Override
    public double readDouble(String prompt) {
        System.out.println(prompt);
        return Double.parseDouble(sc.nextLine());
    }

    /**
     * Reads a double input from the user between two values.
     * @param prompt the prompt for the input
     * @param min the minimum accepted double
     * @param max the maximum accepted double
     * @return the double input from the user
     */
    @Override
    public double readDouble(String prompt, double min, double max) {
        while (!doubleChecker) {
            System.out.println(prompt);
            double input = Double.parseDouble(sc.nextLine());
            if (input >= min && input <= max) {
                return input;
            } else {
                doubleChecker = false;
            }
        }
        return 0;
    }

    /**
     * Reads a float input from the user.
     * @param prompt the prompt for the input
     * @return the float input from the user
     */
    @Override
    public float readFloat(String prompt) {
        System.out.println(prompt);
        return Float.parseFloat(sc.nextLine());
    }

    /**
     * Reads a float input from the user between two values.
     * @param prompt the prompt for the input
     * @param min the minimum accepted float
     * @param max the maximum accepted float
     * @return the float input from the user
     */
    @Override
    public float readFloat(String prompt, float min, float max) {
        while (!floatChecker) {
            System.out.println(prompt);
            float input = Float.parseFloat(sc.nextLine());
            if (input >= min && input <= max) {
                return input;
            } else {
                floatChecker = false;
            }
        }
        return 0;
    }

    /**
     * Reads a long input from the user.
     * @param prompt the prompt for the input
     * @return the long input from the user
     */
    @Override
    public long readLong(String prompt) {
        System.out.println(prompt);
        return Long.parseLong(sc.nextLine());
    }

    /**
     * Reads a long input from the user between two values.
     * @param prompt the prompt for the input
     * @param min the minimum accepted long
     * @param max the maximum accepted long
     * @return the long input from the user
     */
    @Override
    public long readLong(String prompt, long min, long max) {
        while (!longChecker) {
            System.out.println(prompt);
            long input = Long.parseLong(sc.nextLine());
            if (input >= min && input <= max) {
                return input;
            } else {
                longChecker = false;
            }
        }
        return 0;
    }

    /**
     * Reads a BigDecimal input from the user.
     * @param prompt the prompt for the input
     * @return the BigDecimal input from the user
     */
    @Override
    public BigDecimal readBigDecimal(String prompt) {
        System.out.println(prompt);
        while (true) {
            try {
                return new BigDecimal(sc.nextLine());
            } catch (NumberFormatException e) {
                this.print("Input error. Please try again.");
            }
        }
    }

    /**
     * Reads a BigDecimal input from the user between two values.
     * @param prompt the prompt for the input
     * @param min the minimum accepted BigDecimal
     * @param max the maximum accepted BigDecimal
     * @return the BigDecimal input from the user
     */
    @Override
    public BigDecimal readBigDecimal(String prompt, BigDecimal min, BigDecimal max) {
        while (!bigdecimalChecker) {
            System.out.println(prompt);
            BigDecimal input = new BigDecimal(sc.nextLine());
            if (input.compareTo(min) < 0 && input.compareTo(max) > 0) {
                return input;
            } else {
                bigdecimalChecker = false;
            }
        }
        return new BigDecimal("0.0");
    }

    /**
     * Reads a LocalDate input from the user.
     * @param prompt the prompt for the input
     * @param formatter the DateTimeFormatter that represents the input format
     * @return the LocalDate input from the user
     */
    @Override
    public LocalDate readLocalDate(String prompt, DateTimeFormatter formatter) {
        return LocalDate.parse(readString(prompt), formatter);
    }

}