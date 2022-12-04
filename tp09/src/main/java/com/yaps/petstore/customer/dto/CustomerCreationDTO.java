package com.yaps.petstore.customer.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Data needed for customer creation.
 * 
 * <p> Doesn't have id (it will be computed);
 * <p> has a password field.
 */
public class CustomerCreationDTO extends AbstractCustomerDataWithUsername {

    @NotBlank
    @Size(min = 3, max = 32)
    private String password = "";

    public CustomerCreationDTO() {
        this("", "", "", "", "", "", new AddressDTO());
    }

    public CustomerCreationDTO(@NotBlank String username, @NotBlank @Size(min = 3, max = 32) String password, @NotBlank String firstname, @NotBlank String lastname,
            @NotNull String telephone, @NotNull String email, @NotNull AddressDTO addressDTO
            ) {
        super(username, firstname, lastname, telephone, email, addressDTO);
        this.password = password;
    }



    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }
}
