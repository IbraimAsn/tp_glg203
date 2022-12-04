package com.yaps.petstore.customer.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Extension of Customer data with an username.
 */
public abstract class AbstractCustomerDataWithUsername extends AbstractBasicCustomerData {

    @NotBlank
    private String username = "";

    public AbstractCustomerDataWithUsername(
            @NotBlank String username,
            @NotBlank String firstname, @NotBlank String lastname,
            @NotNull String telephone, @NotNull String email, @NotNull AddressDTO addressDTO) {
        super(firstname, lastname, telephone, email, addressDTO);
        this.username = username;
    }


    public AbstractCustomerDataWithUsername() {
        this("", "", "", "", "", new AddressDTO());
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
