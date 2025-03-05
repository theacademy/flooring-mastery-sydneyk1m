package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.dto.Order;
import com.sg.flooringmastery.dto.Product;
import com.sg.flooringmastery.dto.Tax;

import java.time.LocalDate;
import java.util.Set;

public interface FlooringDao {

    /**
     * Gets an order by its order number.
     * @param orderNumber the order number
     * @return the order
     */
    Order getOrder(Integer orderNumber);

    /**
     * Gets all the orders associated with the given date.
     * @param date the given date
     * @return all the orders associated with the given date
     */
    Set<Order> getOrdersForDate(LocalDate date);

    /**
     * Returns the next eligible order number.
     * @return the next eligible order number
     */
    Integer getNextOrderNumber();

    /**
     * Adds/edits an existing order.
     * @param order the order
     * @return true if success, false if failure
     */
    boolean addOrder(Order order);

    boolean removeOrder(Integer orderNumber);

    /**
     * Gets the Order object based off of an order number.
     * @param orderNumber the order number we'd like to get existing information from
     * @return the Order associated with the orderNumber
     */
    Order getOrderFromOrderNumber(Integer orderNumber);

    /**
     * Identifies the associated tax information from an abbreviation.
     * @param stateAbbr the state abbreviation
     * @return the associated tax information object.
     */
    Tax getTaxInfoFromAbbr(String stateAbbr);

    Set<String> getAcceptableStates();

    Set<Product> getAvailableProducts();

    Set<Integer> getAllOrderNumbers();

//    boolean removeOrder(Integer orderNumber);





}
