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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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

    @Test
    public void testAddAndGetOrder() {

    }

    @Test
    public void testCreateInvalidOrder() throws Exception {
        // 1. create the order
        Integer orderNumber = 4;
        String customerName = ";;;*&&%$^";
        Tax taxInfo = testDao.getTaxInfoFromAbbr("TX");
        Product product = testDao.getProductFromProductType("Carpet");
        BigDecimal area = new BigDecimal("0");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        LocalDate date = LocalDate.parse("10/10/2026", dateFormatter);
        try {
            testService.createNewOrder(customerName, taxInfo.getStateAbbr(), product.getProductType(), area, date);
        } catch (FlooringPersistenceException e) {
            fail("Incorrect exception was thrown");
        } catch (InvalidOrderException e) {

        }
    }

    @Test
    public void testGetAllOrders() throws Exception {

    }



}