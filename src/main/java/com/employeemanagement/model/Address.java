package com.employeemanagement.model;

import java.util.Objects;

public class Address {
    private AddressTypes addressType;
    private String streetName;
    private String state;

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Address address = (Address) o;
        return Objects.equals(streetName, address.streetName) && Objects.equals(state, address.state);
    }

    public AddressTypes getAddressType() {
        return addressType;
    }

    public void setAddressType(AddressTypes addressType) {
        this.addressType = addressType;
    }

    @Override
    public String toString() {
        if (addressType == null) {
            return "";
        }
        return "{"
                + "\"address type\":\"" + addressType + "\","
                + "\"street name\":\"" + streetName + "\","
                + "\"state\":\"" + state + "\""
                + "}";
    }
}