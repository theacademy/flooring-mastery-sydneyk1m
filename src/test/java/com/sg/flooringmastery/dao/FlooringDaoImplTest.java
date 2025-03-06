package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.dto.Order;
import com.sg.flooringmastery.dto.Product;
import com.sg.flooringmastery.dto.Tax;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class FlooringDaoImplTest {

    FlooringDao testDao;

    public FlooringDaoImplTest() {

    }

    @BeforeEach
    public void setUp() throws Exception{
        testDao = new FlooringDaoStubImpl();
    }

    /**
     * Tests getOrder() from FlooringDao.
     */
    @Test
    public void testAddAndGetOrder() {
        // 1. create the order
        Integer orderNumber = 4;
        String customerName = "Mthree, Inc.";
        Tax taxInfo = testDao.getTaxInfoFromAbbr("TX");
        Product product = testDao.getProductFromProductType("Carpet");
        BigDecimal area = new BigDecimal("100");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        LocalDate date = LocalDate.parse("10/10/2026", dateFormatter);

        Order order = new Order(orderNumber, customerName, taxInfo, product, area, date);

        // 2. add order
        testDao.addOrder(order);

        // 3. retrieve that very same order
        Order retrieved = testDao.getOrder(orderNumber);

        // 4. assert that they are the same object
        assertTrue(order.equals(retrieved));
        assertEquals(order, retrieved);
    }

    @Test
    public void testGetOrdersForDate() {
        // 1. create the orders
        Integer orderNumber = 4;
        String customerName = "Joe Schmoe";
        Tax taxInfo = testDao.getTaxInfoFromAbbr("CA");
        Product product = testDao.getProductFromProductType("Wood");
        BigDecimal area = new BigDecimal("100");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        LocalDate date = LocalDate.parse("11/11/2026", dateFormatter);

        Integer orderNumber2 = 5;
        String customerName2 = "Jane Schmae";
        Tax taxInfo2 = testDao.getTaxInfoFromAbbr("CA");
        Product product2 = testDao.getProductFromProductType("Wood");
        BigDecimal area2 = new BigDecimal("100");
        LocalDate date2 = LocalDate.parse("11/11/2026", dateFormatter);


        Order order1 = new Order(orderNumber, customerName, taxInfo, product, area, date);
        Order order2 = new Order(orderNumber2, customerName2, taxInfo2, product2, area2, date2);


        // 2. add orders
        testDao.addOrder(order1);
        testDao.addOrder(order2);

        // 3. retrieve the orders for the date

        Set<Order> retrieved = testDao.getOrdersForDate(date);

        Set<Order> expected = new HashSet<>();
        expected.add(order1);
        expected.add(order2);

        assertEquals(expected, retrieved);
        assertTrue(expected.equals(retrieved));


    }

    @Test
    public void testRemoveOrder() {
        // 1. create the order
        Integer orderNumber = 4;
        String customerName = "Joe Schmoe";
        Tax taxInfo = testDao.getTaxInfoFromAbbr("CA");
        Product product = testDao.getProductFromProductType("Wood");
        BigDecimal area = new BigDecimal("100");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        LocalDate date = LocalDate.parse("11/11/2026", dateFormatter);

        // 2. add the order
        Order order = new Order(orderNumber, customerName, taxInfo, product, area, date);
        testDao.addOrder(order);

        // 3. verify it exists
        Order retrieved = testDao.getOrder(orderNumber);
        assertEquals(order, retrieved);

        // 4. remove the order
        testDao.removeOrder(orderNumber);

        // 5. verify it does not exist
        assertNull(testDao.getOrder(orderNumber));
    }

}