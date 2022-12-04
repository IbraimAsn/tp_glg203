package com.yaps.petstore.customer.ui.forms;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.yaps.petstore.customer.dto.AddressDTO;
import com.yaps.petstore.customer.dto.CustomerDTO;
import com.yaps.petstore.customer.dto.CustomerUpdateDTO;

public class UpdateCustomerForm {
    
    @NotBlank(message = "champ obligatoire")
    @Size(max=50)
    private String firstname;
    
    
    @NotBlank(message = "champ obligatoire")
    @Size(max=50)
    private String lastname;


    @NotNull
    @Size(max=255)
    @Email
    private String email;


    @NotNull
    @Size(max=10)
    private String telephone;

    @NotNull
    @Size(max=50)
    private String street1;


    @NotNull
    @Size(max=50)
    private String street2;
    
    @NotNull
    @Size(max=25)
    private String city;

    @NotNull
    @Size(max=25)
    private String state;

    @NotNull
    @Size(max=25)
    private String country;

    @NotNull
    @Size( max=10)
    private String zipcode;

    /**
     * Initialise a customer form from a customer dto.
     * @param customer
     */
    public UpdateCustomerForm(CustomerDTO customer) {
        this(customer.getFirstname(), customer.getLastname(), 
            customer.getEmail(), customer.getTelephone(), 
            customer.getStreet1(), customer.getStreet2(), 
            customer.getCity(), customer.getState(), customer.getCountry(), customer.getZipcode());
    }

    

    public UpdateCustomerForm(@NotBlank(message = "champ obligatoire") @Size(max = 50) String firstname,
            @NotBlank(message = "champ obligatoire") @Size(max = 50) String lastname,
            @NotNull @Size(max = 255) @Email String email, @NotNull @Size(max = 10) String telephone,
            @NotNull @Size(max = 50) String street1, @NotNull @Size(max = 50) String street2,
            @NotNull @Size(max = 25) String city, @NotNull @Size(max = 25) String state,
            @NotNull @Size(max = 25) String country, @NotNull @Size(max = 10) String zipcode) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.telephone = telephone;
        this.street1 = street1;
        this.street2 = street2;
        this.city = city;
        this.state = state;
        this.country = country;
        this.zipcode = zipcode;
    }



    /**
     * Default constructor (needed for Spring Injection)
     */
    public UpdateCustomerForm() { 
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

    public void setLastname(String lastName) {
        this.lastname = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }


    public CustomerUpdateDTO toDTO(String username) {
        CustomerUpdateDTO dto = new CustomerUpdateDTO(
            username, firstname, lastname, 
            telephone, email, 
            new AddressDTO(street1, street2, city, state, zipcode, country));
        return dto;
    }

}
