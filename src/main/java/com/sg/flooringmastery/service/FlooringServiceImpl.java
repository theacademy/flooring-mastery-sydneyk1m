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

@Component
public class FlooringServiceImpl implements FlooringService{
    private FlooringDao dao;

    /**
     * Constructor for service using FlooringDao input.
     * @param dao the dao
     */
    public FlooringServiceImpl(FlooringDao dao) {
        this.dao = dao;
    }

    @Override
    public Set<Order> getOrdersByDate(LocalDate date) {
        return dao.getOrdersForDate(date);
    }

    @Override
    public void addOrder(Order order) {
        try {
            Order newOrder = new Order(dao.getNextOrderNumber(), order.getCustomerName(),
                    order.getTaxInfo(), order.getProduct(), order.getArea(), order.getDate());
            dao.addOrder(newOrder);
        } catch (FlooringPersistenceException e) {
            throw new FlooringPersistenceException("Unable to add this order.", e);
        }
    }

    @Override
    public Order createNewOrder(String customerName, String stateAbbr, String productType, BigDecimal area, LocalDate date) {
        if (customerName == null || stateAbbr == null || productType == null || area == null) {
            // none of these can be null. must reprompt
            return null;
        }
        // should we set the next order number already here?
        Order order = new Order(dao.getNextOrderNumber(), customerName, dao.getTaxInfoFromAbbr(stateAbbr), dao.getProductFromProductType(productType), area, date);
        return order;
    }

    /**
     * Edits an existing order by aggregating old and new info into a new object.
     * @param orderNumber the order number
     * @param customerName the customer's name
     * @param stateAbbreviation the state abbreviation
     * @param productType the product type
     * @param area the area ordered
     * @return
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

    @Override
    public void replacedOrder(Order order) {
        try {
            dao.addOrder(order);
        } catch (FlooringPersistenceException e) {
            throw new FlooringPersistenceException("The order was unable to be overwritten.", e);
        }
    }


    @Override
    public void removeOrder(Order order) {
        try {
            dao.removeOrder(order.getOrderNumber());
        } catch (FlooringPersistenceException e) {
            throw new FlooringPersistenceException("Unable to remove this order.", e);
        }
    }

    @Override
    public Order getOrder(Integer orderNumber) {
        return dao.getOrder(orderNumber);
    }

    @Override
    public Set<String> getAcceptableStates() {
        return dao.getAcceptableStates();
    }

    @Override
    public Set<Product> getAvailableProducts() {
        return dao.getAvailableProducts();
    }

    @Override
    public Set<Integer> getAllOrderNumbers() {
        return dao.getAllOrderNumbers();
    }

}
