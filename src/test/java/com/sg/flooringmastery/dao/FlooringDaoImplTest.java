package com.sg.flooringmastery.dao;

import org.junit.jupiter.api.BeforeEach;

import java.io.FileWriter;

import static org.junit.jupiter.api.Assertions.*;

public class FlooringDaoImplTest {

    FlooringDao testDao;

    public FlooringDaoImplTest() {

    }

    @BeforeEach
    public void setUp() throws Exception{
        String testFile = "testOrder.txt";
        // blank the file
        new FileWriter(testFile);
        testDao = new FlooringDaoImpl(testFile);
    }

}