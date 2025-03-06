package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.dto.Order;
import com.sg.flooringmastery.dto.Product;
import com.sg.flooringmastery.dto.Tax;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * A stub implementation of FlooringDao that hard codes all relevant values.
 */
public class FlooringDaoStubImpl implements FlooringDao {

    private static final String ORDER_HEADER =
            "OrderNumber;CustomerName;State;TaxRate;ProductType;Area;CostPerSquareFoot;LaborCostPerSquareFoot;MaterialCost;LaborCost;Tax;Total";
    private Map<Integer, Order> orderMap = new HashMap<>();
    private Map<String, Product> productMap = new HashMap<>();
    private Map<String, Tax> taxMap = new HashMap<>();
    private final String TEST_FOLDER = "data-t";

    private Integer orderNumberTracker = 0;

    public Order order1;
    public Order order2;
    public Order order3;

    /**
     * Constructor that loads the default values in.
     */
    public FlooringDaoStubImpl() {
        loadData();
    }

    /**
     * Constructor that takes in an order.
     *
     * @param testOrder
     */
    public FlooringDaoStubImpl(Order testOrder) {
        this.order1 = testOrder;
    }

    /**
     * Manually load in the data.
     */
    @Override
    public void loadData() {
        // hard loading tax map
        taxMap.put("TX", new Tax("TX", "Texas", new BigDecimal("4.45")));
        taxMap.put("CA", new Tax("CA", "California", new BigDecimal("25.00")));

        // hard loading product map
        productMap.put("Wood", new Product("Wood", new BigDecimal("5.15"), new BigDecimal("4.75")));
        productMap.put("Carpet", new Product("Carpet", new BigDecimal("2.25"), new BigDecimal("2.10")));

        // hard loading orders
        Order order1 = new Order(1,
                "Sydney, Inc.",
                taxMap.get("TX"),
                productMap.get("Wood"),
                new BigDecimal("150"),
                LocalDate.parse("10/10/2026", DateTimeFormatter.ofPattern("MM/dd/yyyy")));
        Order order2 = new Order(2,
                "George Washington",
                taxMap.get("CA"),
                productMap.get("Carpet"),
                new BigDecimal("200"),
                LocalDate.parse("10/10/2026", DateTimeFormatter.ofPattern("MM/dd/yyyy")));
        Order order3 = new Order(3,
                "Jennie An",
                taxMap.get("CA"),
                productMap.get("Wood"),
                new BigDecimal("101"),
                LocalDate.parse("10/11/2026", DateTimeFormatter.ofPattern("MM/dd/yyyy")));

        orderMap.put(order1.getOrderNumber(), order1);
        orderMap.put(order2.getOrderNumber(), order2);
        orderMap.put(order3.getOrderNumber(), order3);

    }

    public Order getOrder(Integer orderNumber) {
        return orderMap.get(orderNumber);
    }

    public Set<Order> getOrdersForDate(LocalDate date) {
        return orderMap.values().stream().filter(order -> order.getDate().equals(date)).collect(Collectors.toSet());
    }

    public Integer getNextOrderNumber() {
        return ++orderNumberTracker;
    }

    public void addOrder(Order order) {
        orderMap.put(order.getOrderNumber(), order);
    }

    public void removeOrder(Integer orderNumber) {
        orderMap.remove(orderNumber);
    }

    public Tax getTaxInfoFromAbbr(String stateAbbr) {
        return taxMap.get(stateAbbr);
    }

    public Product getProductFromProductType(String productType) {
        return productMap.get(productType);
    }

    public Set<String> getAcceptableStates() {
        return new HashSet<>(taxMap.keySet());
    }

    public Set<Product> getAvailableProducts() {
        return new HashSet<>(productMap.values());
    }

    public Set<Integer> getAllOrderNumbers() {
        return new HashSet<>(orderMap.keySet());
    }

    @Override
    public void writeData() {
        try {
            File dir = new File(TEST_FOLDER + "/orders");

            // because we're retaining similar files through iterations of the program
            // it might be good practice to wipe directory and re-upload files
            // each time so we don't have duplicate problems
            // potential downside: data may be lost if there is a failure in between
            // deleting and rewriting
            for (File file : Objects.requireNonNull(dir.listFiles())) {
                if (!file.delete()) {
                    throw new FlooringPersistenceException("Could not delete file: " + file.getName());
                }
            }

            // https://stackoverflow.com/questions/5242433/create-file-name-using-date-and-time
            Map<LocalDate, PrintWriter> writers = new HashMap<>();

            // go through all orders
            for (Order order : orderMap.values()) {
                LocalDate date = order.getDate();
                // if file DNE
                if (!writers.containsKey(date)) {
                    File newFile = new File(
                            TEST_FOLDER + "/orders/Orders_" + date.format(DateTimeFormatter.ofPattern("MM/dd/yyyy")) + ".txt");
                    writers.put(date, new PrintWriter(new FileWriter(newFile, true)));
                    // add at top of file
                    writers.get(date).println(ORDER_HEADER);
                }
                // write rest of data
                writers.get(date).println(order);

            }

            // clean up all filewriters
            for (PrintWriter printWriter : writers.values()) {
                printWriter.flush();
                printWriter.close();
            }

        } catch (IOException e) {
            throw new FlooringPersistenceException("Error was encountered while writing order data to a file.", e);
        }
    }

}



