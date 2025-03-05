package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.dto.Order;
import com.sg.flooringmastery.dto.Product;
import com.sg.flooringmastery.dto.Tax;

import java.time.LocalDate;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class FlooringDaoImpl implements FlooringDao{

    // all maps are <OrderNumber, Object>
    private Map<Integer, Order> orderMap;
    private Map<Integer, Product> productMap;
    private Map<Integer, Tax> taxMap;
    private Integer orderNumberTracker = 0;

    public FlooringDaoImpl() {
        // read data stuff here
    }

    /**
     * Returns a single order corresponding to the order number
     * @param orderNumber the order number
     * @return the order corresponding to the order number
     */
    @Override
    public Order getOrder(Integer orderNumber) {
        return orderMap.get(orderNumber);
    }

    /**
     * Returns a set containing all orders from a specified date.
     * @param date the specified date
     * @return the set containing all orders from that date
     */
    public Set<Order> getOrdersForDate(LocalDate date) {
        return orderMap.values().stream().filter(order -> order.getDate().equals(date)).collect(Collectors.toSet());
    }

    /**
     * Adds one to the previous order number to ensure no duplicates.
     * @return the new order number
     */
    @Override
    public Integer getNextOrderNumber() {
        return orderNumberTracker++;
    }

    /**
     * Adds the order.
     * @param order the order
     * @return
     */
    @Override
    public boolean addOrder(Order order) {
        try {
            orderMap.put(order.getOrderNumber(), order);
            // write data
        } catch (InvalidOrderException e) {

        }
    }
}
