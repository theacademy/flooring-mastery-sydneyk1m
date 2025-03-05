package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.dto.Order;
import com.sg.flooringmastery.dto.Product;
import com.sg.flooringmastery.dto.Tax;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class FlooringDaoImpl implements FlooringDao{

    // <OrderNumber, Order>
    private Map<Integer, Order> orderMap;

    // <product type (name), Product>
    private Map<Integer, Product> productMap;

//    private Map<Integer, Tax> taxMap;
    // <State abbreviation, Tax information>

    private Map<String, Tax> taxMap;
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
    public Set<Order> getOrdersForDate(LocalDate date) throws FlooringPersistenceException {
        try {
            return orderMap.values().stream().filter(order -> order.getDate().equals(date)).collect(Collectors.toSet());
        } catch (NullPointerException e) {
            return null;
            // TODO: MUST DEAL WITH PROBLEM WHERE IF ORDERS IS NULL THERE IS AN UGLY EXCEPTION
        }
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
            // does this add 1 to the order number regardless of success?
            orderMap.put(getNextOrderNumber(), order);
            // write data
        } catch (InvalidOrderException e) {
            // failed
            return false;
        }
        return true;
    }

    @Override
    public boolean removeOrder(Integer orderNumber) {
        return false;
    }

    @Override
    public Tax getTaxInfoFromAbbr(String stateAbbr) {
        return taxMap.get(stateAbbr);
    }

    @Override
    public Product getProductFromProductType(String productType) {
        return productMap.get(productType);
    }

    public Order getOrderFromOrderNumber(Integer orderNumber) {
        return orderMap.get(orderNumber);
    }

    @Override
    public Set<String> getAcceptableStates() {
        return taxMap.values().stream().map(Tax::getStateAbbr).collect(Collectors.toSet());
    }

    @Override
    public Set<Product> getAvailableProducts() {
        Set<Product> productSet = new HashSet<>(productMap.values());
        return productSet;
    }

    @Override
    public Set<Integer> getAllOrderNumbers() {
        Set<Integer> orderNumberSet = new HashSet<>(orderMap.keySet());
        return orderNumberSet;
    }


}
