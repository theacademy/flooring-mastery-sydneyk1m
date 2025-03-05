package com.sg.flooringmastery.controller;

import com.sg.flooringmastery.dao.FlooringPersistenceException;
import com.sg.flooringmastery.dto.Order;
import com.sg.flooringmastery.service.FlooringService;
import com.sg.flooringmastery.view.FlooringView;
import java.time.LocalDate;
import java.util.Set;

public class FlooringController {

    private FlooringView view;
    private FlooringService service;

    public FlooringController(FlooringView view, FlooringService service) {
        this.view = view;
        this.service = service;
    }

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
//                        removeOrder();
                        break;
                    case 5:
//                        exportAllData();
                        break;
                    case 6:
                        keepGoing = false;
                        break;
                    default:
//                        unknownCommand();
                        break;
                }
            }
        } catch (FlooringPersistenceException e) {
            view.displayErrorMessage(e.getMessage());
        }
        System.out.println("exiting");
//        view.displayExitBanner();
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
        try {

            Order newOrder = service.createNewOrder(
                    view.askForCustomerName(),
                    view.askForStateAbbr(service.getAcceptableStates()),
                    view.askForProductType(service.getAvailableProducts()),
                    view.askForArea(),
                    view.askForDate()
            );

            // If you would still like to place this order
            if (view.placeOrderConfirmation(newOrder)) {
                // if it was successfully added
                if (service.addOrder(newOrder)) {
                    view.displaySuccessfulOrder(newOrder.getCustomerName());
                } else {
                    view.displayFailedOrder();
                }
            }
        } catch (FlooringPersistenceException e) {
            view.displayErrorMessage(e.getMessage());
        }
    }

    private void editOrder() {
        view.displayEditOrderBanner();
        Integer orderNum = view.askForOrderNumber(service.getAllOrderNumbers());
        Order oldOrder = service.getOrder(orderNum);
        Order newOrder = service.editOrder(
                view.askForOrderNumber(service.getAllOrderNumbers()),
                view.askForEditedCustomerName(oldOrder.getCustomerName()),
                view.askForEditedStateAbbr(),
                view.askForEditedProductType(),
                view.askForEditedArea()
        );



        )
    }
}
