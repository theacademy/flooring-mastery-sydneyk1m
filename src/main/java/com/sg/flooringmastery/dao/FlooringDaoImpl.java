package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.controller.FlooringController;
import com.sg.flooringmastery.dto.Order;
import com.sg.flooringmastery.dto.Product;
import com.sg.flooringmastery.dto.Tax;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Component;

import java.io.*;
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
    private Map<String, Product> productMap;

    // <State abbreviation, Tax information>
    private Map<String, Tax> taxMap;
    private Integer orderNumberTracker = 0;

    private final String DATA_FOLDER;
    private final static String ORDER_HEADER =
            "OrderNumber;CustomerName;State;TaxRate;ProductType;Area;CostPerSquareFoot;LaborCostPerSquareFoot;MaterialCost;LaborCost;Tax;Total";
    private final static String PRODUCT_HEADER = "ProductType;CostPerSquareFoot;LaborCostPerSquareFoot";
    private final static String TAX_HEADER = "State;StateName;TaxRate";
    private final static String DELIMITER = ";";
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMddyyyy");
//    private DateTimeFormatter exportDateFormatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");

    /**
     * Default constructor.
     */
    public FlooringDaoImpl() {
        DATA_FOLDER = "data"; // because we want to access many different files inside this folder
        loadData();
    }

    /**
     * Constructor that takes in the data source folder.
     * @param dataFolder the source folder
     */
    public FlooringDaoImpl(String dataFolder) {
        DATA_FOLDER = dataFolder;
        loadData();
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
            throw new FlooringPersistenceException("Unable to get orders for this date.", e);
        }
    }

    /**
     * Adds one to the previous order number to ensure no duplicates.
     * @return the new order number
     */
    @Override
    public Integer getNextOrderNumber() {
        // pre increment?
        return ++orderNumberTracker;
    }

    /**
     * Adds the order to the .
     * @param order the order
     */
    @Override
    public void addOrder(Order order) {
        try {
            // we already set it back in the service
            Integer orderNum = order.getOrderNumber();
            orderMap.put(orderNum, order);
            writeData();
        } catch (FlooringPersistenceException e) {
            throw new FlooringPersistenceException("The order was unable to be added.", e);
        }
    }

    /**
     * Removes the order.
     * @param orderNumber the order number
     */
    @Override
    public void removeOrder(Integer orderNumber) {
        try {
            orderMap.remove(orderNumber);
            writeData();
        } catch (FlooringPersistenceException e) {
            throw new FlooringPersistenceException("The order was unable to be deleted.", e);
        }
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
     */
    private void readOrderData() {
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
                    String regex = ".*(\\d{8})";
                    Pattern pattern = Pattern.compile(regex);
                    Matcher matcher = pattern.matcher(file.getName());

                    String dateExtractedString = "";
                    if (matcher.find()) {
                        dateExtractedString = matcher.group(1);
                    } else {
                        throw new FlooringPersistenceException("Date was not found in the filename: " + file.getName());
                    }
                    LocalDate dateExtracted = LocalDate.parse(dateExtractedString, dateFormatter);

                    // skip header
                    sc.nextLine();

                    // UNMARSHALLING HERE

                    // While we have more lines in the file
                    while (sc.hasNextLine()) {
                        String[] tokens = sc.nextLine().split(DELIMITER); // split on SEMICOLONS
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
    }

    /**
     * Reads tax data from Taxes.txt.
     */
    private void readTaxData() {
        try {
            Scanner sc;
            sc = new Scanner(new BufferedReader(new FileReader(DATA_FOLDER + "/Taxes.txt")));

            taxMap = new HashMap<>();

            // skip header
            sc.nextLine();

            while (sc.hasNextLine()) {
                String[] tokens = sc.nextLine().split(DELIMITER);
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
    }

    /**
     * Reads product data from Products.txt
     */
    private void readProductData() {
        try {
            Scanner sc;
            sc = new Scanner(new BufferedReader(new FileReader(DATA_FOLDER + "/Products.txt")));

            productMap = new HashMap<>();

            // skip header
            sc.nextLine();

            while (sc.hasNextLine()) {
                String[] tokens = sc.nextLine().split(DELIMITER);

                // ProductType,CostPerSquareFoot,LaborCostPerSquareFoot
                Product extractedProduct = new Product(
                        tokens[0], // productType
                        new BigDecimal(tokens[1]), // cost sq ft
                        new BigDecimal(tokens[2]) // labor cost sq ft
                );

                // insert each into productMap
                productMap.put(tokens[0], extractedProduct);
            }
            sc.close();

        } catch (FileNotFoundException e) {
            throw new FlooringPersistenceException("-_- Could not load product data into memory.", e);
        }
    }

    @Override
    public void loadData() {
        try {
            readProductData();
            readTaxData();
            readOrderData();
        } catch (FlooringPersistenceException e) {
            throw new FlooringPersistenceException("Could not load data from files.", e);
        }

    }

    /**
     * Writes current data to respective files.
     */
    @Override
    public void writeData() throws FlooringPersistenceException {
        try {
            File dir = new File(DATA_FOLDER + "/orders");

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
                            DATA_FOLDER + "/orders/Orders_" + date.format(dateFormatter) + ".txt");
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

    /**
     * Exports all data when prompted.
     */
    @Override
    public void exportData() {
        try {
            PrintWriter writer = new PrintWriter(new FileWriter(DATA_FOLDER + "/backup/dataexport.txt"));
            writer.println(ORDER_HEADER);
            for (Order order : orderMap.values()) {
                writer.println(order + DELIMITER + order.getDate().format(dateFormatter));
                writer.flush();
            }
            writer.close();
        } catch (IOException e) {
            throw new FlooringPersistenceException("Unable to export data.", e);
        }
    }

}
