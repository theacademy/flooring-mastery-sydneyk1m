package com.sg.flooringmastery.controller;

import com.sg.flooringmastery.dao.FlooringPersistenceException;
import com.sg.flooringmastery.dao.InvalidOrderException;
import com.sg.flooringmastery.dto.Order;
import com.sg.flooringmastery.service.FlooringService;
import com.sg.flooringmastery.view.FlooringView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Set;

/**
 * The "Controller" component of the 3-tiered MVC flooring program architecture.
 * This layer coordinates all other layers to ensure that the program is run smoothly and correctly.
 */
@Component
public class FlooringController {

    private FlooringView view;
    private FlooringService service;

    /**
     * Constructor for a FlooringController.
     * @param view the view
     * @param service the service layer
     */
    @Autowired
    public FlooringController(FlooringView view, FlooringService service) {
        this.view = view;
        this.service = service;
    }

    /**
     * Executes the program.
     */
    public void run() {
        boolean keepGoing = true;
        int menuSelection = 0;

        try {
            while (keepGoing) {
                menuSelection = getMenuSelection();

                switch (menuSelection) {
                    case 1:
                        displayOrdersForDate();
                        break;
                    case 2:
                        addOrder();
                        break;
                    case 3:
                        editOrder();
                        break;
                    case 4:
                        removeOrder();
                        break;
                    case 5:
                        exportAllData();
                        break;
                    case 6:
                        keepGoing = false;
                        break;
                    default:
                        unknownCommand();
                        break;
                }
            }
        } catch (FlooringPersistenceException e) {
            view.displayErrorMessage(e.getMessage());
        }
        exitMessage();
    }

    /**
     * Displays the menu options and returns an int representing
     * the selected action.
     * @return the selected action
     */
    private int getMenuSelection() {
        view.displayWelcomeBanner();
        return view.displayMenu();
    }

    /**
     * Calls the view to prompt the user for a date, then returns
     * all orders from that date if there exists an Orders_____.txt
     * file for that date. Otherwise, it displays an error message.
     */
    private void displayOrdersForDate() {
        LocalDate date = view.askForDate();
        // get orders from service
        Set<Order> ordersToDisplay = service.getOrdersByDate(date);
        view.displayOrders(ordersToDisplay);
    }

    /**
     * Adds a new order.
     */
    private void addOrder() throws FlooringPersistenceException{
        view.displayAddOrderBanner();
        try {

            Order newOrder = service.createNewOrder(
                    view.askForCustomerName(),
                    view.askForStateAbbr(service.getAcceptableStates()),
                    view.askForProductType(service.getAvailableProducts()),
                    view.askForArea(),
                    view.askForFutureDate()
            );

            // If you would still like to place this order
            if (view.placeOrderConfirmation(newOrder)) {
                try {
                    service.addOrder(newOrder);
                    // if it was successfully added
                    view.displaySuccessfulOrder(newOrder.getCustomerName());

                } catch (InvalidOrderException e) {
                    view.displayFailedOrder();
                }
            }
        } catch (InvalidOrderException e) {
            view.displayErrorMessage(e.getMessage());
        }
    }

    /**
     * Edits an existing order.
     */
    private void editOrder() {
        while (true) {

            view.displayEditOrderBanner();
            Integer orderNum = view.askForOrderNumber(service.getAllOrderNumbers());
            Order oldOrder = service.getOrder(orderNum);

            try {
                Order newOrder = service.editOrder(
                        orderNum,
                        view.askForEditedCustomerName(oldOrder.getCustomerName()),
                        view.askForEditedStateAbbr(service.getAcceptableStates(), oldOrder.getTaxInfo().getStateAbbr()),
                        view.askForEditedProductType(service.getAvailableProducts(), oldOrder.getProduct().getProductType()),
                        view.askForEditedArea(oldOrder.getArea())
                );

                // Ask if they'd like to confirm editing the order
                if (view.editOrderConfirmation(newOrder)) {
                    service.replaceOrder(newOrder);
                    view.displaySuccessfulEdit(orderNum);
                } else {
                    view.displayFailedEdit(orderNum);
                }
                break;
            } catch (NullPointerException e) {
                view.displayErrorMessage("That's not a valid order number.");
            }
        }
    }

    /**
     * Removes an order.
     */
    private void removeOrder() {
        while (true) {

            view.displayRemoveOrderBanner();

            try {
                Order removeMe = service.getOrder(view.askForOrderNumber(service.getAllOrderNumbers()));
                Integer removedOrderNum = removeMe.getOrderNumber();

                if (view.removeOrderConfirmation(removeMe)) {
                    try {
                        service.removeOrder(removeMe);
                        view.displaySuccessfulRemove();
                    } catch (InvalidOrderException e) {
                        view.displayFailedRemove(removedOrderNum);
                    }
                }
                view.displayFailedRemove(removedOrderNum);
                break;
            } catch (NullPointerException e) {
                view.displayErrorMessage("That's not a valid order number.");
            }
        }
    }

    /**
     * Exports all data.
     */
    private void exportAllData() {
        try {
            service.exportAllData();
            view.displayExportSuccess();
        } catch (Exception e) {
            view.displayExportFailure();
        }

    }

    /**
     * Displays an unknown command acknowledgement.
     */
    private void unknownCommand() {
        view.displayUnknownCommandBanner();
    }

    /**
     * Displays an exit message upon exiting the program.
     */
    private void exitMessage() {
        view.displayExitBanner();
    }
}
