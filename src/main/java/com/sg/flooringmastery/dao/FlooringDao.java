package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.dto.Order;

import java.util.Set;

public interface FlooringDao {
    public Set<Order> getOrdersByDate();


}
