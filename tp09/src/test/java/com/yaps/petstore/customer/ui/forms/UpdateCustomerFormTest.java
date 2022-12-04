package com.yaps.petstore.customer.ui.forms;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.yaps.petstore.customer.dto.AddressDTO;
import com.yaps.petstore.customer.dto.CustomerDTO;
import com.yaps.petstore.customer.dto.CustomerUpdateDTO;

public class UpdateCustomerFormTest {

    private CustomerDTO customerDTO;
    private AddressDTO adresseDTO;

    @BeforeEach
    public void init() {
        adresseDTO = new AddressDTO("street1_1", "street2_1", 
            "city1", "state1", "0000", "country1");
        customerDTO = new CustomerDTO("id1", "username1",
                "firstname1", "lastname1", "telephone1", "email1@cnam.fr", adresseDTO);
    }

    @Test
    void testConstruct() {
        UpdateCustomerForm form = new UpdateCustomerForm(customerDTO);
        assertAll(
            () -> assertEquals("firstname1", form.getFirstname()),
            () -> assertEquals("lastname1", form.getLastname()),
            () -> assertEquals("telephone1", form.getTelephone()),
            () -> assertEquals("email1@cnam.fr", form.getEmail()),
            () -> assertEquals("street1_1", form.getStreet1()),
            () -> assertEquals("street2_1", form.getStreet2()),
            () -> assertEquals("state1", form.getState()),
            () -> assertEquals("city1", form.getCity()),
            () -> assertEquals("0000", form.getZipcode()),
            () -> assertEquals("country1", form.getCountry())
        );
    }

    @Test
    void testToDTO() {
        UpdateCustomerForm form1 = new UpdateCustomerForm(customerDTO);
        CustomerUpdateDTO updateDTO = form1.toDTO("user1");
        assertAll(
            () -> assertEquals("firstname1", updateDTO.getFirstname()),
            () -> assertEquals("lastname1", updateDTO.getLastname()),
            () -> assertEquals("telephone1", updateDTO.getTelephone()),
            () -> assertEquals("email1@cnam.fr", updateDTO.getEmail()),
            () -> assertEquals("street1_1", updateDTO.getStreet1()),
            () -> assertEquals("street2_1", updateDTO.getStreet2()),
            () -> assertEquals("state1", updateDTO.getState()),
            () -> assertEquals("city1", updateDTO.getCity()),
            () -> assertEquals("0000", updateDTO.getZipcode()),
            () -> assertEquals("country1", updateDTO.getCountry())
        );
    }
}
