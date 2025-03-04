package com.sg.flooringmastery.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Order {
    private Integer orderNumber;
    private String customerName;
    private Tax taxObject; // stateName can be derived from Tax obj
    private String state;
    private BigDecimal taxRate;
    private Product product; // productName can be derived from Product obj
    private String productName;
    private BigDecimal area;
    private BigDecimal costPerSquareFoot;
    private BigDecimal laborCostPerSquareFoot;
    private BigDecimal materialCost;
    private BigDecimal laborCost;
    private BigDecimal tax;
    private BigDecimal total;
    private LocalDate date;

    public Order(Integer orderNumber, String customerName, String state,
                 Product product, BigDecimal area, LocalDate date) {
        this.orderNumber = orderNumber;
        this.customerName = customerName;
        this.state = state;
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
     * Sets the order number.
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
     * Returns the Tax object.
     * @return the tax object
     */
    public Tax getTaxObject() {
        return taxObject;
    }

    /**
     * Sets the Tax.
     * @param taxObject the tax object
     */
    public void setTaxObject(Tax taxObject) {
        this.taxObject = taxObject;
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






}
