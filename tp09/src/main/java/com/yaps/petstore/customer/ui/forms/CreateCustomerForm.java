package com.yaps.petstore.customer.ui.forms;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.yaps.petstore.customer.dto.CustomerCreationDTO;

/**
 * Modèle du formulaire pour CreateCustomer.
 * 
 * Note : on peut envisager d'utiliser directement le DTO pour ça.
 * On a toujours ce fichu dilemne :
 * 
 * <ul>
 *  <li> respecter scrupuleusement les couches et dupliquer éventuellement des données ;
 *  <li> réutiliser du code mais être trop laxiste quant à la séparation des couches.
 * </ul>
 */
public class CreateCustomerForm {

    @NotBlank(message = "champ obligatoire")
    @Size(max=20)
    private String username;
    
    @NotBlank(message = "champ obligatoire")
    @Size(max=50)
    private String firstname;
    
    
    @NotBlank(message = "champ obligatoire")
    @Size(max=50)
    private String lastname;

    @NotBlank(message = "champ obligatoire")
    @Size(min=3, max = 32)
    private String password1;

    @NotBlank(message = "champ obligatoire")
    @Size(min=3, max = 32)
    private String password2;


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

    public void setPassword1(String password1) {
        this.password1 = password1;
    }

    public void setPassword2(String password2) {
        this.password2 = password2;
    }

    public String getPassword1() {
        return password1;
    }

    public String getPassword2() {
        return password2;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    
    public CustomerCreationDTO toDTO() {
        CustomerCreationDTO dto = new CustomerCreationDTO();
        dto.setUsername(username);
        dto.setPassword(password1);
        dto.setFirstname(firstname);
        dto.setLastname(lastname);
        dto.setEmail(email);
        dto.setTelephone(telephone);
        dto.setStreet1(street1);
        dto.setStreet2(street2);
        dto.setCity(city);
        dto.setZipcode(zipcode);
        dto.setState(state);
        dto.setCountry(country);
        return dto;
    }

}
