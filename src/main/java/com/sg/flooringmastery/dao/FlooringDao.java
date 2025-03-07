package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.dto.Order;
import com.sg.flooringmastery.dto.Product;
import com.sg.flooringmastery.dto.Tax;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Set;

/**
 * An interface for the flooring DAO.
 */
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
    void addOrder(Order order);

    void removeOrder(Integer orderNumber);

    Product getProductFromProductType(String productType);

    /**
     * Identifies the associated tax information from an abbreviation.
     * @param stateAbbr the state abbreviation
     * @return the associated tax information object.
     */
    Tax getTaxInfoFromAbbr(String stateAbbr);

    /**
     * Gets all acceptable state abbreviations.
     * @return a set of all acceptable state abbrs
     */
    Set<String> getAcceptableStates();

    /**
     * Gets all available products.
     * @return a set of all available products
     */
    Set<Product> getAvailableProducts();

    /**
     * Gets all existing order numbers.
     * @return a set of all existing order numbers
     */
    Set<Integer> getAllOrderNumbers();

    /**
     * Reads and loads data from existing file
     */
    void loadData();

    /**
     * Writes data to file
     */
    void writeData();

    /**
     * Exports all data.
     */
    void exportData();
}
