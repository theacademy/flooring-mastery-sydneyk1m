package com.sg.flooringmastery.service;

import com.sg.flooringmastery.dao.FlooringDao;
import com.sg.flooringmastery.dao.FlooringDaoStubImpl;
import com.sg.flooringmastery.dto.Order;
import com.sg.flooringmastery.dto.Product;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

/**
 * Stub implementation of the FlooringService service layer. We only need to change the dependency to the Stub.
 */
public class FlooringServiceStubImpl extends FlooringServiceImpl {

    public FlooringServiceStubImpl() {
        super(new FlooringDaoStubImpl());
    }
}
