package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.dto.Order;

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





}
