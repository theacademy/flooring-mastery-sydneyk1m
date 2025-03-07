package com.sg.flooringmastery.view;

import com.sg.flooringmastery.dao.InvalidOrderException;
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

/**
 * The "View" component of the 3-tiered MVC flooring program architecture.
 */
@Component
public class FlooringView {

    private UserIO io = new UserIOImpl();
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    /**
     * A constructor for FlooringView that takes in an io object.
     * @param io the io
     */
    @Autowired
    public FlooringView(UserIO io) {
        this.io = io;
    }

    /**
     * Displays a welcome banner to be shown every time the menu is shown.
     */
    public void displayWelcomeBanner() {
        io.print("\n* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * ");
        io.print("* * * * * * * * *  WELCOME TO THE FLOORING PROGRAM  * * * * * * * * *");
    }

    /**
     * Displays a banner to be shown every time the user wants to add a new order.
     */
    public void displayAddOrderBanner() {
        io.print("* * * * * * * * * * * * *  ADDING NEW ORDER  * * * * * * * * * * * * * \n");
    }

    /**
     * Displays a banner to be shown every time the user wants to edit an order.
     */
    public void displayEditOrderBanner() {
        io.print("* * * * * * * * * * * * * * EDITING ORDER * * * * * * * * * * * * * *");
        io.print("* * NOTE: TO KEEP EXISTING VALUES, JUST PRESS ENTER WHEN PROMPTED * *\n");
    }

    /**
     * Displays a banner to be shown every time the user wants to remove an order.
     */
    public void displayRemoveOrderBanner() {
        io.print("* * * * * * * * * * * * *  REMOVING ORDER  * * * * * * * * * * * * *");
    }

    /**
     * Displays the start-up menu.
     * @return
     */
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

        io.print("* * * * * * * * * *  ALL ORDERS FOR SELECTED DATE  * * * * * * * * * *");

        if (orders == null || orders.isEmpty()) {
            io.print("No orders were found for the selected date.");
            return;
        }

        try {
            for (Order order : orders) {
                displayOrder(order);
            }
        } catch (Exception e) {
            displayErrorMessage("No orders were found for the selected date.");
            throw new InvalidOrderException("No orders were found for the selected date.");
        }
    }

    /**
     * Displays a note letting the user know their order was placed successfully.
     * @param customerName the customer's name
     */
    public void displaySuccessfulOrder(String customerName) {
        io.print("* * * * * * ORDER SUCCESSFULLY PLACED FOR " +  customerName.toUpperCase() + " * * * * * *\n");
    }

    /**
     * Displays a note letting the user know the order (specified) was placed successfully.
     * @param editedOrderNumber the order number that was edited
     */
    public void displaySuccessfulEdit(Integer editedOrderNumber) {
        io.print("* * * * * * ORDER #" + editedOrderNumber + " WAS SUCCESSFULLY EDITED * * * * * *");
    }

    /**
     * Displays a note letting the user know their order failed.
     */
    public void displayFailedOrder() {
        io.print("* * * * * * * * *  ORDER FAILED. PLEASE TRY AGAIN.  * * * * * * * * *");
    }

    /**
     * Displays a note letting the user know their edit failed.
     * @param failedOrderNum the order num that failed.
     */
    public void displayFailedEdit(Integer failedOrderNum) {
        io.print("* * * * * * ORDER #" + failedOrderNum + " WAS NOT EDITED. TRY AGAIN. * * * * * *");
    }

    /**
     * Displays a note letting the user know that their desired order was successfully removed.
     */
    public void displaySuccessfulRemove() {
        io.print("* * * * * * ORDER WAS SUCCESSFULLY REMOVED * * * * * *\n");
    }

    /**
     * Displays a note letting the user know that their desired order was NOT removed and remains
     * in the system.
     * @param failedOrderNum the order that was not deleted
     */
    public void displayFailedRemove(Integer failedOrderNum) {
        io.print("* * * * * * ORDER #" + failedOrderNum + " WAS NOT REMOVED. TRY AGAIN. * * * * * *\n");
    }

    /**
     * Displays a note letting the user know that the export was a success.
     */
    public void displayExportSuccess() {
        io.print("* * * * * * * EXPORT WAS A SUCCESS * * * * * * *\n");
    }

    /**
     * Displays a note letting the user know that the export failed.
     */
    public void displayExportFailure() {
        io.print("* * * EXPORT FAILED * * *\n");
    }

    /**
     * Displays a note letting the user know they have just entered an unknown command.
     */
    public void displayUnknownCommandBanner() {
        io.print("* * * UNKNOWN COMMAND DETECTED * * *\n");
    }

    /**
     * Displays an exit banner.
     */
    public void displayExitBanner() {
        io.print("* * * EXITING THE PROGRAM. COME BACK SOON! * * *\n");
    }

    /**
     * Prompts the user for the order number.
     * @return the order number
     */
    public Integer askForOrderNumber(Set<Integer> existingOrderNumbers) {

        while (true) {
            try {
                io.print("Here are all the existing order numbers: " + existingOrderNumbers);
                return io.readInt("Which order number would you like to select?");
            } catch (NumberFormatException e) {
                displayErrorMessage("That's not a number! try again.");
            }
        }
    }

    /**
     * Prompts the user for the customer's name.
     * @return the customer's name
     */
    public String askForCustomerName() {
        while (true) {
            try {
                String name = io.readString("Enter the customer name: ");

                // name cannot be NULL
                if (name.isBlank() || name == null || name.equals("\n")) {
                    displayErrorMessage("Customer name cannot be blank.");
                    continue;
                }

                // "[A-z0-9,. ]+"
                if (name.matches("[\\p{Alnum},.'\s]*")) { // Check if valid
                    return name;
                }
                displayErrorMessage("Invalid input. Please try again and enter a valid name.");

            } catch (Exception e) {
                displayErrorMessage("Invalid input. Please try again and enter a valid name.");
            }

        }
    }

    /**
     * Prompts the user for the state associated with the order.
     * @return the state name
     */
    public String askForStateAbbr(Set<String> acceptableStates) {

        while(true) {
            try {
                io.print("Here are the valid states we ship to: " + acceptableStates);
                String stateAbbr = io.readString("What is the state associated with this order?");

                if (stateAbbr.isBlank() || !stateAbbr.contains(stateAbbr)) { // cannot be null
                    displayErrorMessage("Invalid input.");
                    continue;
                }

                if (acceptableStates.contains(stateAbbr)) { // must be contained in tax file
                    return stateAbbr;
                }

                displayErrorMessage("Please enter a state that we ship to.");

            } catch (Exception e) {
                displayErrorMessage("Invalid input.");
            }
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
                displayErrorMessage("That's not the correct format. Try again!");
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
            try {
                productType = io.readString("What product type would you like to select?");
                if (productType.isBlank() || productType == null) {
                    displayErrorMessage("Please enter a valid product type.");
                    continue;
                }

                for (Product product : availableProducts) {
                    // don't ignore case because it messes stuff up
                    if (Objects.equals(product.getProductType(), productType)) { // should this be product and not product.getType?
                        return productType;
                    }
                }

                displayErrorMessage("The selected product type is not available.");
            } catch (Exception e) {
                displayErrorMessage("Invalid input! try again.");
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
            try {
                areaString = io.readString("How many square feet would you like to buy?");

                BigDecimal area = new BigDecimal(areaString);

                // if area reaches minimum, approve
                if (area.compareTo(hundred) >= 0) {
                    return area;
                }

                io.print("You need to order at least 100 square feet. Try again!");
            } catch (NumberFormatException e) {
                displayErrorMessage("Tha'ts not a number! Try again!");
            }
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

    /**
     * Asks for the edited customer name.
     * @param oldName the old name
     * @return the new customer name
     */
    public String askForEditedCustomerName(String oldName) {
        while (true) {
            String newName = io.readString("Enter the customer name (" + oldName + "): ");

            // if they enter null, then we want to keep the old name.
            if (newName.isBlank()) {
                return oldName;
            }

            if (newName.matches("[\\p{Alnum},.'\s]*")) { // Check if valid
                return newName;
            }

            io.print("Invalid input. Please try again and enter a valid name.");
        }
    }

    /**
     * Asks for edited state abbreviation.
     * @param acceptableStates list of acceptable states
     * @param oldOrderStateAbbr the old state abbr
     * @return the new state abbreviation
     */
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

    /**
     * Ask for edited product type.
     * @param availableProducts list of available products
     * @param oldProductType old product
     * @return the new product type
     */
    public String askForEditedProductType(Set<Product> availableProducts, String oldProductType) {
        String newProductType;

        // display all available products and their pricing
        io.print("Here are all of our product options.");
        io.print("Available Products: " + availableProducts.stream().map(prod -> prod.getProductType() +
                ": material cost: $" + prod.getCostPerSquareFoot() + "/square foot & labor cost: $" +
                prod.getLaborCostPerSquareFoot() + "/square foot").collect(Collectors.joining("\n", "\n", "\n")));

        while (true) {
            try {
                newProductType = io.readString("What product type would you like to select (" + oldProductType + ")?: ");
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
                displayErrorMessage("Not valid.");

            } catch (Exception e) {
                displayErrorMessage("Please enter a valid product or just press enter.");
            }
        }
    }

    /**
     * Ask for edited area.
     * @param oldArea the old area
     * @return the new area
     */
    public BigDecimal askForEditedArea(BigDecimal oldArea) {
        String newAreaString;
        BigDecimal hundred = new BigDecimal("100");

        while (true) {
            try {
                newAreaString = io.readString("Enter the desired area to order (" + oldArea.toString() + " sq. ft): ");

                if (newAreaString.isBlank()) { // newline equals give the old area
                    return oldArea;
                }

                BigDecimal newArea = new BigDecimal(newAreaString);

                // if area reaches minimum, approve
                if (newArea.compareTo(hundred) >= 0) {
                    return newArea;
                }

                displayErrorMessage("You need to order at least 100 square feet. Try again!");
            } catch (NumberFormatException e) {
                displayErrorMessage("Please enter a number.");
            }
        }
    }
}
