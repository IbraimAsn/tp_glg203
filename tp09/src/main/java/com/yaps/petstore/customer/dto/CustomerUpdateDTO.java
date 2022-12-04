package com.yaps.petstore.customer.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Data sent to update a given customer.
 * 
 * <p> The id is not included ;
 * <p> the username identifies the user.
 * <p> no password for now.
 */
public class CustomerUpdateDTO extends AbstractCustomerDataWithUsername {

    public CustomerUpdateDTO(@NotBlank String username, @NotBlank String firstname, @NotBlank String lastname,
            @NotNull String telephone, @NotNull String email, @NotNull AddressDTO addressDTO) {
        super(username, firstname, lastname, telephone, email, addressDTO);
    }
    
    
}
