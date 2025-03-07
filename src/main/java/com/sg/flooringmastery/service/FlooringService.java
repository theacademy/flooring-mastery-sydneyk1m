package com.sg.flooringmastery.service;

import com.sg.flooringmastery.dto.Order;
import com.sg.flooringmastery.dto.Product;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

public interface FlooringService {

    /**
     * Returns all orders that are associated with the date
     * @param date the specified date
     * @return all orders associated with date
     */
    Set<Order> getOrdersByDate(LocalDate date);

    /**
     * Returns true if an order was successfully added, false if not
     * @param order the order to be added
     * @return true if success, false if failure
     */
    void addOrder(Order order);

    /**
     * Creates a new order object based off of five initial inputs.
     * @param customerName the customer name
     * @param stateAbbr the state abbreviation
     * @param productType the product type
     * @param area the area
     * @param date the date (must be in the future)
     * @return the new order object
     */
    Order createNewOrder(String customerName, String stateAbbr, String productType, BigDecimal area, LocalDate date);

    /**
     * Edits an existing order by subbing in newly defined values
     * @param orderNumber the order number
     * @param customerName the customer's name
     * @param stateAbbreviation the state abbreviation
     * @param productType the product type
     * @param area the area ordered
     * @return the newly updated order
     */
    Order editOrder(Integer orderNumber, String customerName, String stateAbbreviation, String productType, BigDecimal area);

    /**
     * Replaces/updates the existing order.
     * @param order
     */
    void replaceOrder(Order order);

    /**
     * Removes an order.
     * @param order the order to be removed
     * @return true if success, false if failure
     */
    void removeOrder(Order order);

    /**
     * Gets an order based on its order number.
     * @param orderNumber the order number
     * @return the order associated with the order number
     */
    Order getOrder(Integer orderNumber);

    /**
     * Gets a set of all valid states you can sell to.
     * @return set of state abbreviations
     */
    Set<String> getAcceptableStates();

    /**
     * Gets a set of all available products you can buy.
     * @return a set of all available products
     */
    Set<Product> getAvailableProducts();

    /**
     * Gets a set of all existing order numbers.
     * @return a set of all existing order numbers
     */
    Set<Integer> getAllOrderNumbers();

    /**
     * Exports all data to a dataexport.txt file.
     */
    void exportAllData();
}
