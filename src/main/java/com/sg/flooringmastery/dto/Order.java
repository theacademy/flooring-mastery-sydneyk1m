package com.sg.flooringmastery.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

import static java.math.RoundingMode.UP;

/**
 * A class representing an Order.
 */
public class Order {
    private Integer orderNumber;
    private String customerName;
    private Tax taxInfo; // stateName can be derived from Tax obj
    private String state;
    private Product product; // productName can be derived from Product obj
    private String productName;
    private BigDecimal area;
    private BigDecimal costPerSquareFoot;

    private BigDecimal laborCostPerSquareFoot;

    private BigDecimal materialCost;

    private BigDecimal laborCost;
    private BigDecimal tax;
    private BigDecimal totalCost;
    private LocalDate date;
    /**
     * Constructs a new Order with assigned parameters.
     * @param orderNumber the order number
     * @param customerName the customer's name
     * @param taxInfo the tax associated information
     * @param product the product
     * @param area the area
     * @param date the date
     */
    public Order(Integer orderNumber, String customerName, Tax taxInfo,
                 Product product, BigDecimal area, LocalDate date) {
        this.orderNumber = orderNumber;
        this.customerName = customerName;
        this.taxInfo = taxInfo;
        this.product = product;
        this.area = area;
        this.date = date;
    }
    /**
     * Returns the order number.
     * @return the order number
     */
    public Integer getOrderNumber() {
        return orderNumber;
    }

    /**
     * Sets the order number. NOT to be used by the client.
     * @param orderNumber the order number
     */
    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
    }

    /**
     * Returns the customer name.
     * @return the customer name
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     * Sets the customer name.
     * @param customerName the customer name
     */
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    /**
     * Returns the Tax information object.
     * @return the tax information object
     */
    public Tax getTaxInfo() {
        return taxInfo;
    }

    /**
     * Sets the Tax.
     * @param taxInfo the tax information object
     */
    public void setTaxInfo(Tax taxInfo) {
        this.taxInfo = taxInfo;
    }

    /**
     * Returns the area.
     * @return the area
     */
    public BigDecimal getArea() {
        return area;
    }

    /**
     * Sets the area.
     * @param area the area
     */
    public void setArea(BigDecimal area) {
        this.area = area;
    }

    /**
     * Returns the product.
     * @return the product
     */
    public Product getProduct() {
        return product;
    }

    /**
     * Sets the product.
     * @param product the product
     */
    public void setProduct(Product product) {
        this.product = product;
    }

    /**
     * Returns the date of the Order object.
     * @return the date
     */
    public LocalDate getDate() {
        return this.date;
    }

    /**
     * Sets the date of the Order object.
     * @param date the date
     */
    public void setDate(LocalDate date) {
        this.date = date;
    }

    /**
     * Returns material cost per square foot
     * @return mateiral cost per square foot
     */
    public BigDecimal getCostPerSquareFoot() {
        return costPerSquareFoot;
    }

    /**
     * Sets material cost per square foot
     * @param costPerSquareFoot material cost per square foot
     */
    public void setCostPerSquareFoot(BigDecimal costPerSquareFoot) {
        this.costPerSquareFoot = costPerSquareFoot;
    }

    /**
     * Gets labor cost per square foot
     * @return labor cost per square foot
     */
    public BigDecimal getLaborCostPerSquareFoot() {
        return laborCostPerSquareFoot;
    }

    /**
     * Sets labor cost per square foot
     * @param laborCostPerSquareFoot labor cost per sq foot
     */
    public void setLaborCostPerSquareFoot(BigDecimal laborCostPerSquareFoot) {
        this.laborCostPerSquareFoot = laborCostPerSquareFoot;
    }

    /**
     * Sets material cost
     * @param materialCost material cost
     */
    public void setMaterialCost(BigDecimal materialCost) {
        this.materialCost = materialCost;
    }

    /**
     * Calculates and returns the cost of materials for the order.
     * @return the cost of materials for the order.
     */
    public BigDecimal getMaterialCost() {
        return materialCost;
    }

    /**
     * Calculates and returns the cost of labor for the order.
     * @return the cost of labor for the order.
     */
    public BigDecimal getLaborCost() {
        return laborCost;
    }

    /**
     * Sets labor cost
     * @param laborCost labor cost
     */
    public void setLaborCost(BigDecimal laborCost) {
        this.laborCost = laborCost;
    }

    /**
     * Sets tax
     * @param tax tax
     */
    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }

    /**
     * Not to be confused with getTaxObject().
     * Returns the taxes incurred based on the state and the expenses.
     * @return taxes incurred
     */
    public BigDecimal getTax() {
        return tax;
    }

    /**
     * Calculates the order's total cost.
     * @return the total cost
     */
    public BigDecimal getTotalCost() {
        return (getMaterialCost().add(getLaborCost())).add(getTax());
    }

    /**
     * Sets total cost
     * @param totalCost total cost
     */
    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }

    /**
     * New toString() method because of order.toString() implicitly called in
     * writeData() of flooringdaoimpl.
     * @return a correctly formatted string for the Orders_MMddyyyy.txt files.
     */
    public String toString() {
        return orderNumber + ";" +
                customerName + ";" +
                taxInfo.getStateAbbr() + ";" + taxInfo.getTaxRate() + ";" +
                product.getProductType() + ";" +
                area.setScale(2, UP) + ";" +
                product.getCostPerSquareFoot().setScale(2, UP) + ";" +
                product.getLaborCostPerSquareFoot().setScale(2, UP) + ";" +
                getMaterialCost().setScale(2, UP) + ";" +
                getLaborCost().setScale(2, UP) + ";" +
                getTax().setScale(2, UP) + ";" +
                getTotalCost().setScale(2, UP);
    }

    /**
     * Overridden equals operator.
     * @param o object
     * @return true if equal false if not
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(orderNumber, order.orderNumber) && Objects.equals(customerName, order.customerName) && Objects.equals(taxInfo, order.taxInfo) && Objects.equals(state, order.state) && Objects.equals(product, order.product) && Objects.equals(productName, order.productName) && Objects.equals(area, order.area) && Objects.equals(costPerSquareFoot, order.costPerSquareFoot) && Objects.equals(laborCostPerSquareFoot, order.laborCostPerSquareFoot) && Objects.equals(materialCost, order.materialCost) && Objects.equals(laborCost, order.laborCost) && Objects.equals(tax, order.tax) && Objects.equals(totalCost, order.totalCost) && Objects.equals(date, order.date);
    }

    /**
     * Overridden hashCode.
     * @return hashcode.
     */
    @Override
    public int hashCode() {
        return Objects.hash(orderNumber, customerName, taxInfo, state, product, productName, area, costPerSquareFoot, laborCostPerSquareFoot, materialCost, laborCost, tax, totalCost, date);
    }
}
