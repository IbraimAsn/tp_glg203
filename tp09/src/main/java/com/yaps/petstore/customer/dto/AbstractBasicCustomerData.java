package com.yaps.petstore.customer.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Customer data available in all customer dtos.
 * 
 * Note : we use inheritance here, but it might be a better 
 * option to think in terms of delegation. CustomerCreationDTO,
 * CustomerUpdateDTO and CustomerDTO might simply have a BasicCustomerDTO field (this class),
 * and delegates getters/setters to it.
 * 
 * It would reduce the mapping code a lot.
 * 
 */
public abstract class AbstractBasicCustomerData {
   
    // ======================================
    // =             Attributes             =
    // ======================================


    @NotBlank
    private String firstname = "";
    
    @NotBlank
    private String lastname = "";


    @NotNull
    private String telephone = "";
    
    @NotNull
    private String email = "";
    
    @NotNull
    private final AddressDTO address;

    // ======================================
    // =           Constructors             =
    // ======================================
     
    public AbstractBasicCustomerData() {        
        this("", "", "", "", new AddressDTO());
    }

    public AbstractBasicCustomerData(@NotBlank String firstname, @NotBlank String lastname,
            @NotNull String telephone, @NotNull String email, @NotNull AddressDTO addressDTO) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.telephone = telephone;
        this.email = email;
        this.address = addressDTO;
    }
    
    
  
    // ======================================
    // =         Getters and Setters        =
    // ======================================
    
    


    public String getStreet1() {
        return address.getStreet1();
    }


    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setStreet1(final String street1) {
        address.setStreet1(street1);
    }

    public String getStreet2() {
        return address.getStreet2();
    }

    public void setStreet2(final String street2) {
        address.setStreet2(street2);
    }

    public String getCity() {
        return address.getCity();
    }

    public void setCity(final String city) {
        address.setCity(city);
    }

    public String getState() {
        return address.getState();
    }

    public void setState(final String state) {
        address.setState(state);
    }

    public String getZipcode() {
        return address.getZipcode();
    }

    public void setZipcode(final String zipcode) {
        address.setZipcode(zipcode);
    }

    public String getCountry() {
        return address.getCountry();
    }

    public void setCountry(final String country) {
        address.setCountry(country);
    }


    public String toString() {
        final StringBuilder buf = new StringBuilder();
        buf.append(",firstname=").append(getFirstname());
        buf.append(",lastname=").append(getLastname());
        buf.append(",telephone=").append(getTelephone());
        buf.append(",email=").append(getEmail());
        buf.append(",street1=").append(getStreet1());
        buf.append(",street2=").append(getStreet2());
        buf.append(",city=").append(getCity());
        buf.append(",state=").append(getState());
        buf.append(",zipcode=").append(getZipcode());
        buf.append(",country=").append(getCountry());
        return buf.toString();
    }
}
