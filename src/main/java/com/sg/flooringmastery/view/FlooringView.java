package com.sg.flooringmastery.view;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;

public class FlooringView {

    private UserIO io = new UserIOImpl();

    /**
     * Displays a welcome banner to be shown every time the menu
     * is shown.
     */
    public void displayWelcomeBanner() {
        io.print("* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * ");
        io.print("* * * * * * * * *  WELCOME TO THE FLOORING PROGRAM  * * * * * * * * *");
    }

    public int displayMenu() {
        io.print("<<FLOORING PROGRAM>>");
        io.print("1. DISPLAY ORDERS");
        io.print("2. ADD AN ORDER");
        io.print("3. EDIT AN ORDER");
        io.print("4. REMOVE AN ORDER");
        io.print("5. EXPORT ALL DATA");
        io.print("6. QUIT");

        return io.readInt("Please select from the above choices.", 1, 6);

    }
    //public Order(Integer orderNumber, String customerName, String state,
    //                 Product product, BigDecimal area, LocalDate date)

    /**
     * Prompts the user for the customer's name.
     * @return the customer's name
     */
    public String askForCustomerName() {
        return io.readString("What is the customer\'s name?");
        // if null, must not be allowed
    }

    /**
     * Prompts the user for the state associated with the order.
     * @return the state name
     */
    public String askForStateAbbr() {
        String state = io.readString("What is the state associated with this order?");
        // if state in tax file, approve. if not, we cancel the order.
        // create list of acceptable states from tax file?
        return state;
    }

    /**
     * Prompts the user for the date in the format (MM/DD/YYYY).
     * @return the date
     */
    public LocalDate askForDate() {
        while(true) {
            try {
                return io.readLocalDate("Enter the date in the format (MM/DD/YYYY).", DateTimeFormatter.ofPattern("MM/dd/yyyy"));
            } catch (DateTimeParseException e) {
                // keep prompting for a correct format (even if order doesn't exist)
            }
        }

    }
}
