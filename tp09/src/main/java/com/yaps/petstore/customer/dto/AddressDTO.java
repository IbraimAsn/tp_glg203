package com.yaps.petstore.customer.dto;

/**
 * This class encapsulates all the data for an address.
 *
 * @see CustomerDTO
 * @see OrderDTO
 */
public class AddressDTO {

    // ======================================
    // = Attributes =
    // ======================================
    private String street1 = "";
    private String street2 = "";
    private String city = "";
    private String state = "";
    private String zipcode = "";
    private String country = "";

    // ======================================
    // = Constructors =
    // ======================================

    public AddressDTO() {
    }

    public AddressDTO(String street1, String street2, String city, String state, String zipcode, String country) {
        this.street1 = street1;
        this.street2 = street2;
        this.city = city;
        this.state = state;
        this.zipcode = zipcode;
        this.country = country;
    }

    // ======================================
    // = Getters and Setters =
    // ======================================
    public String getStreet1() {
        return street1;
    }

    public void setStreet1(String street1) {
        this.street1 = street1;
    }

    public String getStreet2() {
        return street2;
    }

    public void setStreet2(String street2) {
        this.street2 = street2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
