package com.sg.flooringmastery.view;

import com.sg.flooringmastery.dao.FlooringPersistenceException;
import com.sg.flooringmastery.dto.Order;
import com.sg.flooringmastery.dto.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class FlooringView {

    private UserIO io = new UserIOImpl();
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    @Autowired
    public FlooringView(UserIO io) {
        this.io = io;
    }

    /**
     * Displays a welcome banner to be shown every time the menu
     * is shown.
     */
    public void displayWelcomeBanner() {
        io.print("\n* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * ");
        io.print("* * * * * * * * *  WELCOME TO THE FLOORING PROGRAM  * * * * * * * * *");
    }

    public void displayAddOrderBanner() {
        io.print("* * * ADDING NEW ORDER * * *\n");
    }

    public void displayEditOrderBanner() {
        io.print("* * * EDITING ORDER * * *");
        io.print("* NOTE: IF YOU'D LIKE TO KEEP EXISTING VALUES, SIMPLY PRESS ENTER WHEN PROMPTED FOR A VALUE * \n");
    }

    public void displayRemoveOrderBanner() {
        io.print("* * * REMOVING ORDER * * *\n");
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

    /**
     * Displays a single order
     * @param displayMe the order to be displayed.
     */
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
        try {

            io.printF("#%s - %s || %s || %s - %.0f sq ft\n    $%.2f material + $%.2f labor (+ $%.2f tax) = $%.2f\n",
                    orderNumber, displayMe.getCustomerName(), displayMe.getTaxInfo().getStateName(),
                    displayMe.getProduct().getProductType(), displayMe.getArea(), displayMe.getMaterialCost(),
                    displayMe.getLaborCost(), displayMe.getTax(), displayMe.getTotalCost());
        } catch (NullPointerException e) {
            throw new NullPointerException("ERROR: One or more values in your data is null.");
        }


    }

    /**
     * Displays multiple orders given a set.
     * @param orders the set of orders.
     */
    public void displayOrders(Set<Order> orders) {

        if (orders == null || orders.isEmpty()) {
            io.print("No orders were found for the selected date.\n");
            return;
        }

        for (Order order : orders) {
            displayOrder(order);
        }
    }

    /**
     * Displays a note letting the user know their order was placed successfully.
     * @param customerName the customer's name
     */
    public void displaySuccessfulOrder(String customerName) {
        io.print("* * * ORDER SUCCESSFULLY PLACED FOR " +  customerName.toUpperCase() + " * * *\n");
    }

    public void displaySuccessfulEdit(Integer editedOrderNumber) {
        io.print("* * * ORDER #" + editedOrderNumber + " WAS SUCCESSFULLY EDITED * * *\n");
    }

    /**
     * Displays a note letting the user know their order failed.
     */
    public void displayFailedOrder() {
        io.print("* * * ORDER FAILED. PLEASE TRY AGAIN. * * *\n");
    }

    public void displayFailedEdit(Integer failedOrderNum) {
        io.print("* * * ORDER #" + failedOrderNum + " WAS NOT EDITED. TRY AGAIN. * * *\n");
    }

    public void displaySuccessfulRemove() {
        io.print("* * * ORDER WAS SUCCESSFULLY REMOVED * * *\n");
    }

    public void displayFailedRemove(Integer failedOrderNum) {
        io.print("* * * ORDER #" + failedOrderNum + " WAS NOT REMOVED. TRY AGAIN. * * *\n");
    }

    public void displayUnknownCommandBanner() {
        io.print("* * * UNKNOWN COMMAND DETECTED * * *\n");
    }

    public void displayExitBanner() {
        io.print("* * * EXITING THE PROGRAM. COME BACK SOON! * * *\n");
    }

    /**
     * Prompts the user for the order number.
     * @return the order number
     */
    public Integer askForOrderNumber(Set<Integer> existingOrderNumbers) {
        io.print("Here are all the existing order numbers: " + existingOrderNumbers);
        return io.readInt("\nWhich order number would you like to select?");
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
                io.print("That's not the correct format. Try again!\n");
                // keep prompting for a correct format (even if order doesn't exist)
            }
        }

    }

    /**
     * Prompts the user for a FUTURE date in the format (MM/DD/YYYY).
     * @return the date, which must be in the future.
     */
    public LocalDate askForFutureDate() {
        LocalDate rightNow = LocalDate.now();
        while(true) {
            try {
                LocalDate futureDateCandidate = io.readLocalDate("Enter a future date in the format (MM/DD/YYYY).", DateTimeFormatter.ofPattern("MM/dd/yyyy"));
                if (futureDateCandidate.isAfter(rightNow)) {
                    return futureDateCandidate;
                } else {
                    // just keep going
                    io.print("The date must be in the future. Try again!");
                }
            } catch (DateTimeParseException e) {
                io.print("That's not the correct format. Try again!\n");
                // keep prompting for a correct format (even if order doesn't exist)
            }
        }

    }

    /**
     * Prompts the user for the product type.
     * @return the product type (Can access from Product)
     */
    public String askForProductType(Set<Product> availableProducts) {
        String productType;

        // display all available products and their pricing
        io.print("Here are all of our product options.");
        io.print("Available Products: " + availableProducts.stream().map(prod -> prod.getProductType() +
                ": material cost: $" + prod.getCostPerSquareFoot() + "/square foot & labor cost: $" +
                prod.getLaborCostPerSquareFoot() + "/square foot").collect(Collectors.joining("\n", "\n", "\n")));

        while (true) {
            productType = io.readString("\nWhat product type would you like to select?");
            if (productType.isBlank()) {
                return null;
            }

            for (Product product : availableProducts) {
                // how to ignore case Lol
                // Objects.equals(product.getProductType(), productType
                if (product.getProductType().equalsIgnoreCase(productType)) { // should this be product and not product.getType?
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
        String confirmation = io.readString("Still want to place this order? (y/n)");
        return confirmation.equalsIgnoreCase("y");
    }

    /**
     * Display all inputted parameters, then ask if the user would still like to
     * go ahead with the order.
     * @param editOrder the order being checked
     * @return true if we would like to proceed to order, false if we don't proceed
     */
    public boolean editOrderConfirmation(Order editOrder) {
        if (editOrder == null) {
            return true;
        }

        io.print("* * * EDITING ORDER IN PROGRESS * * *");
        displayOrder(editOrder);
        String confirmation = io.readString("Would you like to confirm these edits? (y/n)");
        return confirmation.equalsIgnoreCase("y");
    }

    /**
     * Display the order to be removed, then ask if the user would still like to go ahead and
     * remove it.
     * @param removeOrder order to be removed
     * @return true if we want to remove, false if we don't want to
     */
    public boolean removeOrderConfirmation(Order removeOrder) {
        if (removeOrder == null) {
            return true;
        }

        io.print("* * * ORDER DETAILS * * *");
        displayOrder(removeOrder);
        String confirmation = io.readString("Still want to remove this order? This cannot be reversed. (y/n)");
        return confirmation.equalsIgnoreCase("y");
    }

    /**
     * Displays an error message
     * @param message the error message
     */
    public void displayErrorMessage(String message) {
        io.print("* * * AN ERROR HAS OCCURRED * * *");
        io.print(message);
        io.readString("Please hit enter to continue.");
    }

    /********* EDITING METHODS PAST THIS POINT *********/
    public String askForEditedCustomerName(String oldName) {
        while (true) {
            String newName = io.readString("Enter the customer name (" + oldName + "): ");

            // if they enter null, then we want to keep the old name.
            if (newName.isBlank()) {
                return oldName;
            }

            if (newName.matches("[A-z0-9,. ]+")) { // Check if valid
                return newName;
            }

            io.print("Invalid input. Please try again and enter a valid name.");
        }
    }


    public String askForEditedStateAbbr(Set<String> acceptableStates, String oldOrderStateAbbr) {
        io.print("Here are the valid states we ship to: " + acceptableStates);

        while(true) {
            String stateAbbr = io.readString("Enter the state (" + oldOrderStateAbbr + "): ");

            if (stateAbbr.isBlank()) {
                // if they press enter, just set it to the old one
                return oldOrderStateAbbr;
            }

            if (acceptableStates.contains(stateAbbr)) { // must be contained in tax file
                return stateAbbr;
            }

            io.print("The entered state is invalid. Please enter one within the valid list provided.");
        }
    }

    public String askForEditedProductType(Set<Product> availableProducts, String oldProductType) {
        String newProductType;

        // display all available products and their pricing
        io.print("Here are all of our product options.");
        io.print("Available Products: " + availableProducts.stream().map(prod -> prod.getProductType() +
                ": material cost: $" + prod.getCostPerSquareFoot() + "/square foot & labor cost: $" +
                prod.getLaborCostPerSquareFoot() + "/square foot\n").collect(Collectors.joining("\n", "\n", "\n")));

        while (true) {
            newProductType = io.readString("\nWhat product type would you like to select (" + oldProductType + ")?: ");
            if (newProductType.isBlank()) { // newline, we'd like to keep the old
                return oldProductType;
            }

            for (Product product : availableProducts) {
                // how to ignore case Lol
                //Objects.equals(product.getProductType(), newProductType
                if (product.getProductType().equalsIgnoreCase(newProductType)) { // should this be product and not product.getType?
                    return newProductType;
                }
            }
        }
    }

    public BigDecimal askForEditedArea(BigDecimal oldArea) {
        String newAreaString;
        BigDecimal hundred = new BigDecimal("100");

        while (true) {
            newAreaString = io.readString("Enter the desired area to order (" + oldArea.toString() + "): ");

            if (newAreaString.isBlank()) { // newline equals give the old area
                return oldArea;
            }

            BigDecimal area = new BigDecimal(newAreaString);

            // if area reaches minimum, approve
            if (area.compareTo(hundred) >= 0) {
                return area;
            }
            io.print("You need to order at least 100 square feet. Try again!");
        }
    }
}
