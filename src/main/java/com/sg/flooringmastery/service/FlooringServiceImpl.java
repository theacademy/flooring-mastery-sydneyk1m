package com.sg.flooringmastery.service;

import com.sg.flooringmastery.dao.FlooringDao;
import com.sg.flooringmastery.dto.Order;
import com.sg.flooringmastery.dto.Product;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

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
    public boolean addOrder(Order order) {
        return false;
    }

    @Override
    public Order createNewOrder(String customerName, String stateAbbr, String productType, BigDecimal area, LocalDate date) {
        if (customerName == null || stateAbbr == null || productType == null || area == null) {
            // none of these can be null. must reprompt
            return null;
        }
        Order order = new Order(null, customerName, dao.get )
    }

    @Override
    public boolean editOrder(Order order) {
        return false;
    }

    @Override
    public boolean removeOrder(Order order) {
        if (order == null) {
            return false;
        }

        return dao.removeOrder(order.getOrderNumber());
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
