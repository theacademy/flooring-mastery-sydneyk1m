package com.sg.flooringmastery.service;

import com.sg.flooringmastery.dao.FlooringDao;
import com.sg.flooringmastery.dao.FlooringPersistenceException;
import com.sg.flooringmastery.dto.Order;
import com.sg.flooringmastery.dto.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

/**
 * The Service layer, which handles all business logic.
 */
@Component
public class FlooringServiceImpl implements FlooringService{
    private FlooringDao dao;

    /**
     * Constructor for service using FlooringDao input.
     * @param dao the dao
     */
    @Autowired
    public FlooringServiceImpl(FlooringDao dao) {
        this.dao = dao;
    }

    /**
     * Returns a set of all orders from that date.
     * @param date the specified date
     * @return a set of all orders from that date.
     */
    @Override
    public Set<Order> getOrdersByDate(LocalDate date) {
        return dao.getOrdersForDate(date);
    }

    /**
     * Adds a new order to the map.
     * @param order the order to be added
     */
    @Override
    public void addOrder(Order order) {
        try {
            // set order number right before adding to the orderMap
            order.setOrderNumber(dao.getNextOrderNumber());
            dao.addOrder(order);
        } catch (FlooringPersistenceException e) {
            throw new FlooringPersistenceException("Unable to add this order.", e);
        }
    }

    /**
     * Creates a new order.
     * @param customerName the customer name
     * @param stateAbbr the state abbreviation
     * @param productType the product type
     * @param area the area
     * @param date the date (must be in the future)
     * @return a new order
     */
    @Override
    public Order createNewOrder(String customerName, String stateAbbr, String productType, BigDecimal area, LocalDate date) {
        if (customerName == null || stateAbbr == null || productType == null || area == null) {
            // none of these can be null. must reprompt
            return null;
        }
        // do NOT set the order number here just yet.
        Order order = new Order(null, customerName, dao.getTaxInfoFromAbbr(stateAbbr), dao.getProductFromProductType(productType), area, date);
        return order;
    }

    /**
     * Edits an existing order by aggregating old and new info into a new object.
     * @param orderNumber the order number
     * @param customerName the customer's name
     * @param stateAbbreviation the state abbreviation
     * @param productType the product type
     * @param area the area ordered
     * @return the newly edited order
     */
    @Override
    public Order editOrder(Integer orderNumber, String customerName, String stateAbbreviation, String productType, BigDecimal area) {
        Order oldOrder = getOrder(orderNumber);
        if (oldOrder == null) {
            return null;
        }

        // variable = Expression1 ? Expression2: Expression3
        // if expr1 true, then var =2. otherwise, var = 3
        Order newOrder = new Order(
                orderNumber,
                customerName == null ? oldOrder.getCustomerName() : customerName,
                stateAbbreviation == null ? oldOrder.getTaxInfo() : dao.getTaxInfoFromAbbr(stateAbbreviation),
                productType == null ? oldOrder.getProduct() : dao.getProductFromProductType(productType),
                area == null ? oldOrder.getArea() : area,
                oldOrder.getDate()
        );
        return newOrder;
    }

    /**
     * Replaces an order in the map (step 2).
     * @param order the order to replace the old order with.
     */
    @Override
    public void replaceOrder(Order order) {
        try {
            dao.addOrder(order);
        } catch (FlooringPersistenceException e) {
            throw new FlooringPersistenceException("The order was unable to be overwritten.", e);
        }
    }


    /**
     * Removes an order.
     * @param order the order to be removed
     */
    @Override
    public void removeOrder(Order order) {
        try {
            dao.removeOrder(order.getOrderNumber());
        } catch (FlooringPersistenceException e) {
            throw new FlooringPersistenceException("Unable to remove this order.", e);
        }
    }

    /**
     * Returns an order associated with the order number
     * @param orderNumber the order number
     * @return the associated order
     */
    @Override
    public Order getOrder(Integer orderNumber) {
        return dao.getOrder(orderNumber);
    }

    /**
     * Returns a set of all existing acceptable states.
     * @return a set of all existing acceptable states
     */
    @Override
    public Set<String> getAcceptableStates() {
        return dao.getAcceptableStates();
    }

    /**
     * Returns a set of all existing available products.
     * @return a set of all existing available products
     */
    @Override
    public Set<Product> getAvailableProducts() {
        return dao.getAvailableProducts();
    }

    /**
     * Returns a set of all existing order numbers.
     * @return a set of all order numbers.
     */
    @Override
    public Set<Integer> getAllOrderNumbers() {
        return dao.getAllOrderNumbers();
    }

    /**
     * Exports all data to a dataexport.txt file.
     */
    @Override
    public void exportAllData() {
        dao.exportData();
    }

}
