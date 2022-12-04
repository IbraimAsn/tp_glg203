package com.yaps.petstore.customer.service;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yaps.petstore.customer.dao.CustomerDAO;
import com.yaps.petstore.customer.domain.Address;
import com.yaps.petstore.customer.domain.Customer;
import com.yaps.petstore.customer.dto.AbstractBasicCustomerData;
import com.yaps.petstore.customer.dto.AddressDTO;
import com.yaps.petstore.customer.dto.CustomerCreationDTO;
import com.yaps.petstore.customer.dto.CustomerDTO;
import com.yaps.petstore.security.domain.YapsUser;

@Service
public class CustomerDTOMapper {

    @Autowired
    CustomerDAO customerDAO;

  
    CustomerDTOMapper() {}

    /**
     * Returns a dto with usual user visualisation data.
     * @param customer
     * @return
     */
    public static CustomerDTO toDTO(Customer customer) {
        String username = customer.getUser().getUsername();
        AddressDTO addressDTO = new AddressDTO(
            customer.getStreet1(), customer.getStreet2(), customer.getCity(), customer.getState(), customer.getZipcode(), customer.getCountry());
        CustomerDTO dto = new CustomerDTO(
            customer.getId(), username, 
            customer.getFirstname(), customer.getLastname(), 
            customer.getTelephone(), customer.getEmail(), 
            addressDTO);
        return dto;            
    }

    /**
     * Extract customer information from a creation dto parameter.
     * <p> Prerequesite : the corresponding user exists, and a unique id has been chosen.
     * @param id
     * @param dto
     * @return
     */
    public Customer fromCreationDTO(String id, CustomerCreationDTO dto, YapsUser user) {
        Customer c = new Customer(id, user);
        fillFromDTO(c, dto);
        return c;
    }

    /**
     * Fills a customer object using the information included in a AbstractBasicCustomerData.
     * @param customer
     * @param customerDTO
     */
    public void fillFromDTO(Customer customer, @Valid AbstractBasicCustomerData customerDTO) {
        customer.setAddress(addressFromCustomerData(customerDTO));
        customer.setFirstname(customerDTO.getFirstname());
        customer.setLastname(customerDTO.getLastname());
        customer.setTelephone(customerDTO.getTelephone());
        customer.setEmail(customerDTO.getEmail());
    }


    private Address addressFromCustomerData(AbstractBasicCustomerData customerDATA) {
        // @formatter:off
        Address address = Address.builder()
            .setStreet1(customerDATA.getStreet1())
            .setStreet2(customerDATA.getStreet2())
            .setCity(customerDATA.getCity())
            .setZipcode(customerDATA.getZipcode())
            .setState(customerDATA.getState())
            .setCountry(customerDATA.getCountry())
            .build();
        // @formatter:on
        return address;
    }

    
}