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
        return dao.getOrdersByDate();
    }

    @Override
    public boolean addOrder(Order order) {
        return false;
    }

    @Override
    public Order createNewOrder(String customerName, String stateAbbr, String productType, BigDecimal area, LocalDate date) {
        return null;
    }

    @Override
    public boolean editOrder(Order order) {
        return false;
    }

    @Override
    public boolean removeOrder(Order order) {
        return false;
    }

    @Override
    public Order getOrder(Integer orderNumber) {
        return null;
    }

    @Override
    public Set<String> getAcceptableStates() {
        // TODO: FINISH THIS
        return Set.of();
    }

    @Override
    public Set<Product> getAvailableProducts() {
        // TODO: FINISH THIS
        return Set.of();
    }

}
