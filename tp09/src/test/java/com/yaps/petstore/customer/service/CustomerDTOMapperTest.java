package com.yaps.petstore.customer.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.yaps.petstore.customer.domain.Address;
import com.yaps.petstore.customer.domain.Customer;
import com.yaps.petstore.customer.dto.CustomerDTO;
import com.yaps.petstore.security.domain.YapsUser;
import com.yaps.petstore.security.domain.YapsUserRole;

public class CustomerDTOMapperTest {

    @Test
    void testToDTO() {
        // @formatter:off
        Address address = Address.builder()
            .setStreet1("street1").setStreet2("street2")
            .setCity("city1").setState("state1")
            .setZipcode("zipcode1").setCountry("country1").build();
        // @formatter:on

        YapsUser u = new YapsUser("u1", YapsUserRole.CUST);

        Customer customer = new Customer("id1", "firstname1", "lastname1", "telephone1",
                "email1", address, u);

        CustomerDTO customerDTO = CustomerDTOMapper.toDTO(customer);
        assertEquals("id1", customerDTO.getId());
        assertEquals("firstname1", customer.getFirstname());
        assertEquals("lastname1", customer.getLastname());
        assertEquals("telephone1", customer.getTelephone());
        assertEquals("email1", customer.getEmail());
        assertEquals("street1", customer.getStreet1());
        assertEquals("street2", customer.getStreet2());
        assertEquals("city1", customer.getCity());
        assertEquals("zipcode1", customer.getZipcode());
        assertEquals("state1", customer.getState());
        assertEquals("country1", customer.getCountry());
        assertEquals("u1", customerDTO.getUsername());
    }
}
