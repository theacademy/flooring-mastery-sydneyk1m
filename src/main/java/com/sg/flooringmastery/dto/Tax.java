package com.sg.flooringmastery.dto;

import java.math.BigDecimal;

public class Tax {

    private String stateAbbr;
    private String stateName;
    private BigDecimal taxRate;

    public Tax(String stateAbbr, String stateName, BigDecimal taxRate) {
        this.stateAbbr = stateAbbr;
        this.stateName = stateName;
        this.taxRate = taxRate;
    }

    /**
     * Returns the state abbreviation.
     * @return the state abbreviation
     */
    public String getStateAbbr() {
        return stateAbbr;
    }

    /**
     * Sets the state abbreviation.
     * @param stateAbbr the state abbreviation
     */
    public void setStateAbbr(String stateAbbr) {
        this.stateAbbr = stateAbbr;
    }

    /**
     * Returns the state name.
     * @return the state name
     */
    public String getStateName() {
        return stateName;
    }

    /**
     * Sets the state name.
     * @param stateName the state name
     */
    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    /**
     * Returns the tax rate.
     * @return the tax rate
     */
    public BigDecimal getTaxRate() {
        return taxRate;
    }

    /**
     * Sets the tax rate.
     * @param taxRate the tax rate
     */
    public void setTaxRate(BigDecimal taxRate) {
        this.taxRate = taxRate;
    }
}
