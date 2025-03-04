package com.sg.flooringmastery.view;

import com.sg.flooringmastery.dto.Order;
import com.sg.flooringmastery.dto.Product;

import java.math.BigDecimal;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.Objects;
import java.util.Set;

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

    public void displayOrder(Order displayMe) {
        String orderNumber = "";
        if (displayMe.getOrderNumber() != null) {
            orderNumber = displayMe.getOrderNumber().toString();
        }

        // in order:
        // Order ID %s
        // Customer name %s
        // state name %s
        // product type %s
        // area %.0f
        // material cost $%.2f
        // labor cost $%.2f
        // tax $%.2f
        // total cost $%.2f

        io.print("%s - %s %s : %s - %.0f sq ft\n $%.2f + $%.2f (+ $%.2f) = $%.2f\n",
                orderNumber, displayMe.getCustomerName(), displayMe.getTaxObject().getStateName(),
                displayMe.getProduct().getProductType(), displayMe.getArea(), displayMe.getMaterialCost(),
                displayMe.getLaborCost(), displayMe.getTax(), displayMe.getTotalCost());

    }

    /**
     * Prompts the user for the customer's name.
     * @return the customer's name
     */
    public String askForCustomerName() {
        while (true) {
            String name = io.readString("Enter the customer name: ");

            // name cannot be NULL
            if (name.isBlank()) {
                return null;
            }

            if (name.matches("[A-z0-9,. ]+")) { // Check if valid
                return name;
            }

            io.print("Invalid input. Please try again and enter a valid name.");
        }
    }

    /**
     * Prompts the user for the state associated with the order.
     * @return the state name
     */
    public String askForStateAbbr(Set<String> acceptableStates) {
        io.print("Here are the valid states we ship to: " + acceptableStates);

        while(true) {
            String stateAbbr = io.readString("What is the state associated with this order?");

            if (stateAbbr.isBlank()) { // cannot be null
                return null;
            }

            if (acceptableStates.contains(stateAbbr)) { // must be contained in tax file
                return stateAbbr;
            }

            io.print("The entered state is invalid. Please enter one within the valid list provided.");
        }
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

    /**
     * Prompts the user for the product type.
     * @return the product type (Can access from Product)
     */
    public String askForProductType(Set<Product> productSet) {
        String productType;

        // display all available products and their pricing
        io.print("Here are all of our product options.");
        // lambda.........

        while (true) {
            productType = io.readString("What product type would you like to select?");
            if (productType.isBlank()) {
                return null;
            }

            for (Product product : productSet) {
                // how to ignore case Lol
                if (Objects.equals(product.getProductType(), productType)) { // should this be product and not product.getType?
                    return productType;
                }
            }
        }
    }

    /**
     * Prompts the user for the area they'd like to order, with a minimum of 100 square feet.
     * @return the area they'd like to order
     */
    public BigDecimal askForArea() {
        String areaString;
        BigDecimal hundred = new BigDecimal("100");

        while (true) {
            areaString = io.readString("How many square feet would you like to buy?");

            BigDecimal area = new BigDecimal(areaString);

            // if area reaches minimum, approve
            if (area.compareTo(hundred) >= 0) {
                return area;
            }
            io.print("You need to order at least 100 square feet. Try again!");
        }
    }

    /**
     * Display all inputted parameters, then ask if the user would still like to
     * go ahead with the order.
     * @param newOrder the order being checked
     * @return true if we would like to proceed to order, false if we don't proceed
     */
    public boolean placeOrderConfirmation(Order newOrder) {
        if (newOrder == null) {
            return true;
        }

        io.print("* * * ORDER IN PROGRESS * * *");
        displayOrder(newOrder);
    }
}
