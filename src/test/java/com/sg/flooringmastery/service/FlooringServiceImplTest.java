package com.sg.flooringmastery.service;

import com.sg.flooringmastery.dao.FlooringDao;
import com.sg.flooringmastery.dao.FlooringDaoStubImpl;
import com.sg.flooringmastery.dao.FlooringPersistenceException;
import com.sg.flooringmastery.dao.InvalidOrderException;
import com.sg.flooringmastery.dto.Order;
import com.sg.flooringmastery.dto.Product;
import com.sg.flooringmastery.dto.Tax;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.cglib.core.Local;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class FlooringServiceImplTest {

    FlooringDao testDao;
    FlooringService testService;

    public FlooringServiceImplTest() {

    }

    @BeforeEach
    public void setUp() throws Exception{
        testDao = new FlooringDaoStubImpl();
        testService = new FlooringServiceStubImpl();
    }

    /**
     * Tests adding and getting order.
     */
    @Test
    public void testAddAndGetOrder() {
        // 1. create a new order
        Integer orderNumber = 4;
        String customerName = "Jolene";
        Tax taxInfo = testDao.getTaxInfoFromAbbr("CA");
        Product product = testDao.getProductFromProductType("Carpet");
        BigDecimal area = new BigDecimal("110");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        LocalDate date = LocalDate.parse("12/12/2029", dateFormatter);

        Order order = testService.createNewOrder(customerName, taxInfo.getStateAbbr(), product.getProductType(), area, date);
        testDao.addOrder(order);

        // 2. we retrieve
        Order retrieved = testDao.getOrder(null);

        assertEquals(order, retrieved);
        assertTrue(order.equals(retrieved));

    }

    /**
     * Tests creating an invalid order.
     * @throws Exception an exception
     */
    @Test
    public void testCreateInvalidOrder() throws Exception {
        // 1. create the order
        Integer orderNumber = 4;
        String customerName = ";;;*&&%$^";
        Tax taxInfo = testDao.getTaxInfoFromAbbr("TX");
        Product product = testDao.getProductFromProductType("Carpet");
        BigDecimal area = new BigDecimal("0");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        LocalDate date = LocalDate.parse("10/08/2026", dateFormatter);

        // 2. try to add faulty data
        try {
            testService.createNewOrder(customerName, taxInfo.getStateAbbr(), product.getProductType(), area, date);
        } catch (FlooringPersistenceException e) {
            fail("Incorrect exception was thrown");
        } catch (InvalidOrderException e) {

        }
    }

    /**
     * Tests getting all orders.
     */
    @Test
    public void testGetAllOrders() {
        // 1. we will be retrieving stub loaded orders on 10/10/2026
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        LocalDate date = LocalDate.parse("10/10/2026", dateFormatter);
        Set<Order> retrieved = testService.getOrdersByDate(date);

        assertEquals(2, retrieved.size());
    }

    /**
     * Tests removing an order.
     */
    @Test
    public void testRemoveOrder() {
        // 1. we will be retrieving stub loaded order #1
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        LocalDate date = LocalDate.parse("10/10/2026", dateFormatter);

        Order retrieved = testService.getOrder(1);
        testService.removeOrder(retrieved);

        Set<Order> orders = testDao.getOrdersForDate(date);

        assertNull(testService.getOrder(1));
        assertFalse(orders.contains(testService.getOrder(1)));
    }

    /**
     * Tests editing an order.
     */
    @Test
    public void testEditOrder() {
        // 1. we will be editing stub loaded order #1
        // Original values we are editing from:
        // (1, "Sydney, Inc.", taxMap.get("TX"), productMap.get("Wood"), new BigDecimal("150"),
        //                LocalDate.parse("10/10/2026", DateTimeFormatter.ofPattern("MM/dd/yyyy")));

        Order updated = testService.editOrder(1, "Edited Editson", "CA", "Carpet",
                new BigDecimal("20"));

        testService.replaceOrder(updated);

        Order standard = new Order(1, "Edited Editson",
                testDao.getTaxInfoFromAbbr("CA"),
                testDao.getProductFromProductType("Carpet"), new BigDecimal("20"),
                LocalDate.parse("10/10/2026", DateTimeFormatter.ofPattern("MM/dd/yyyy")));

        testService.addOrder(standard);

        assertTrue(testService.getOrder(1).equals(standard));
    }

}