package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.controller.FlooringController;
import com.sg.flooringmastery.dto.Order;
import com.sg.flooringmastery.dto.Product;
import com.sg.flooringmastery.dto.Tax;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
public class FlooringDaoImpl implements FlooringDao{

    // <OrderNumber, Order>
    private Map<Integer, Order> orderMap;

    // <product type (name), Product>
    private Map<Integer, Product> productMap;

    // <State abbreviation, Tax information>
    private Map<String, Tax> taxMap;
    private Integer orderNumberTracker = 0;

    private final String DATA_FOLDER;
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMddyyyy");

    /**
     * Default constructor.
     */
    public FlooringDaoImpl() {
        DATA_FOLDER = "/data"; // because we want to access many different files inside this folder
    }

    /**
     * Constructor that takes in the data source folder.
     * @param dataFolder the source folder
     */
    public FlooringDaoImpl(String dataFolder) {
        DATA_FOLDER = dataFolder;
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

    /****************** FILE I/O METHODS BELOW ******************/

    /**
     * Reads the order data from the order_MMDDYYYY.txt files in order to
     * populate the OrderMap.
     * @return true if success, false if failure
     */
    private boolean readOrderData() {
        try {
            File dir = new File(DATA_FOLDER + "/orders");

            Scanner sc;
            orderMap = new HashMap<>();

            // iterate through each file in /orders
            for (File file : Objects.requireNonNull(dir.listFiles())) {
                if (file.getName().toLowerCase().endsWith(".txt") && file.isFile()) {
                    // initialize scanner

                    sc = new Scanner(new BufferedReader(new FileReader(file)));

                    // from stackoverflow please dont ask me about it. extracts date
                    // https://stackoverflow.com/questions/40886116/how-to-extract-date-from-the-given-filename-in-java
                    String regex = ".*(\\\\d{8})";
                    Pattern pattern = Pattern.compile(regex);
                    Matcher matcher = pattern.matcher(file.getName());

                    String dateExtractedString = "";
                    if (matcher.find()) {
                        dateExtractedString = matcher.group(1);
                    }
                    LocalDate dateExtracted = LocalDate.parse(dateExtractedString, dateFormatter);

                    // skip header
                    sc.nextLine();

                    // UNMARSHALLING HERE

                    // While we have more lines in the file
                    while (sc.hasNextLine()) {
                        String[] tokens = sc.nextLine().split(","); // split on commas
                        // 0OrderNumber,1CustomerName,2State,3TaxRate,4ProductType,5Area,6CostPerSquareFoot,7LaborCostPerSquareFoot,8MaterialCost,9LaborCost,10Tax,11Total
                        Order extractedOrder = new Order(
                                Integer.parseInt(tokens[0]), // order number
                                tokens[1], // customer name
                                taxMap.get(tokens[2]), // state abbr -> tax object
                                productMap.get(tokens[4]), // product type -> product object
                                new BigDecimal(tokens[5]), // area
                                dateExtracted
                        );

                        // keep track of largest order number
                        if (extractedOrder.getOrderNumber() > orderNumberTracker) {
                            orderNumberTracker = extractedOrder.getOrderNumber();
                        }

                        // finally put order in orderMap
                        orderMap.put(extractedOrder.getOrderNumber(), extractedOrder);

                    }
                    sc.close();
                }
            }

        } catch (NullPointerException | FileNotFoundException e) {
            throw new FlooringPersistenceException("-_- Could not load order data into memory.", e);
        }
        return true;
    }

    /**
     * Reads tax data from Taxes.txt.
     * @return true if success false if failure
     */
    private boolean readTaxData() {
        try {
            Scanner sc;
            sc = new Scanner(new BufferedReader(new FileReader(DATA_FOLDER + "/Taxes.txt")));

            taxMap = new HashMap<>();

            // skip header
            sc.nextLine();

            while (sc.hasNextLine()) {
                String[] tokens = sc.nextLine().split(",");
                // StateAbbr,StateName,TaxRate
                Tax extractedTax = new Tax(tokens[0], // state abbr
                        tokens[1], // state name
                        new BigDecimal(tokens[2])); // tax rate

                taxMap.put(tokens[0], extractedTax);
            }
            sc.close();

        } catch (FileNotFoundException e) {
            throw new FlooringPersistenceException("-_- Could not load tax data into memory.", e);
        }
        return true;
    }

    @Override
    public boolean loadData() {
        return false;
    }

    @Override
    public boolean writeData() {
        return false;
    }

}
