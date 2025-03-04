package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.dto.Order;
import com.sg.flooringmastery.dto.Product;
import com.sg.flooringmastery.dto.Tax;

import java.time.LocalDate;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class FlooringDaoImpl implements FlooringDao{

    private Map<Integer, Order> orderMap;
    private Map<Integer, Product> productMap;
    private Map<Integer, Tax> taxMap;
    private Integer orderNumberTracker = 0;

    public FlooringDaoImpl() {
        // read data stuff here
    }

    /**
     * Returns a set containing all orders from a specified date.
     * @param date the specified date
     * @return the set containing all orders from that date
     */
    public Set<Order> getOrdersForDate(LocalDate date) {
        return orderMap.values().stream().filter(order -> order.getDate().equals(date)).collect(Collectors.toSet());
    }
}
