package com.sg.flooringmastery.dto;

import java.math.BigDecimal;
import java.util.Objects;

public class Product {

    private String productType;
    private BigDecimal costPerSquareFoot;
    private BigDecimal laborCostPerSquareFoot;

    /**
     * Constructor for a new Product.
     * @param productType the product type
     * @param costPerSquareFoot the cost per square foot
     * @param laborCostPerSquareFoot the labor cost per square foot
     */
    public Product(String productType, BigDecimal costPerSquareFoot, BigDecimal laborCostPerSquareFoot) {
        this.productType = productType;
        this.costPerSquareFoot = costPerSquareFoot;
        this.laborCostPerSquareFoot = laborCostPerSquareFoot;
    }

    /**
     * Returns product type.
     * @return product type
     */
    public String getProductType() {
        return productType;
    }

    /**
     * Sets the product type.
     * @param productType the product type.
     */
    public void setProductType(String productType) {
        this.productType = productType;
    }

    /**
     * Returns the cost per square foot.
     * @return the cost per square foot
     */
    public BigDecimal getCostPerSquareFoot() {
        return costPerSquareFoot;
    }

    /**
     * Sets the cost per square foot.
     * @param costPerSquareFoot cost per square foot
     */
    public void setCostPerSquareFoot(BigDecimal costPerSquareFoot) {
        this.costPerSquareFoot = costPerSquareFoot;
    }

    /**
     * Returns the labor cost per square foot.
     * @return cost per square foot.
     */
    public BigDecimal getLaborCostPerSquareFoot() {
        return laborCostPerSquareFoot;
    }

    /**
     * Sets the labor cost per square foot.
     * @param laborCostPerSquareFoot the labor cost per square foot
     */
    public void setLaborCostPerSquareFoot(BigDecimal laborCostPerSquareFoot) {
        this.laborCostPerSquareFoot = laborCostPerSquareFoot;
    }


    /**
     * Overridden equals.
     * @param o object
     * @return true if equals false if not.
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(productType, product.productType) && Objects.equals(costPerSquareFoot, product.costPerSquareFoot) && Objects.equals(laborCostPerSquareFoot, product.laborCostPerSquareFoot);
    }

    /**
     * Overridden hashcode.
     * @return hashcode
     */
    @Override
    public int hashCode() {
        return Objects.hash(productType, costPerSquareFoot, laborCostPerSquareFoot);
    }

    /**
     * Overridden toString().
     * @return to string
     */
    @Override
    public String toString() {
        return "Product{" +
                "productType='" + productType + '\'' +
                ", costPerSquareFoot=" + costPerSquareFoot +
                ", laborCostPerSquareFoot=" + laborCostPerSquareFoot +
                '}';
    }
}
