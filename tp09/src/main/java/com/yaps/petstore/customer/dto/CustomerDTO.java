package com.yaps.petstore.customer.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * A client as exported from the service layer.
 * 
 * <p> has an id
 * <p> doesn't have a password.
 */
public class CustomerDTO extends AbstractCustomerDataWithUsername {

    private String id;
    

    public CustomerDTO() {
        this("", "", "", "", "", "", new AddressDTO());
    }
    
    public CustomerDTO(@NotBlank String id, @NotBlank String username, @NotBlank String firstname, @NotBlank String lastname,
            @NotNull String telephone, @NotNull String email, @NotNull AddressDTO addressDTO) {
        super(username, firstname, lastname, telephone, email, addressDTO);
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
