package com.yaps.petstore.customer.ui;

import java.util.HashMap;

import com.yaps.petstore.customer.domain.Address;
import com.yaps.petstore.customer.domain.Customer;
import com.yaps.petstore.customer.dto.CustomerDTO;
import com.yaps.petstore.customer.service.CustomerDTOMapper;
import com.yaps.petstore.security.domain.YapsUser;
import com.yaps.petstore.security.domain.YapsUserRole;

/**
 * Abstract base for tests.
 * Note that the fact that annotations are not inherited
 * precludes their use here.
 * 
 * Predefines a map with customers in it.
 * <p>
 * The map is protected, and can freely be accessed by tests.
 */
public abstract class AbstractCustomerServletTest {

    /**
     * A map from customers ids to customers.
     */
    protected HashMap<String, CustomerDTO> map = new HashMap<>();

    /**
     * An address to use.
     */
    protected Address cnamAddress = Address.builder()
            .setStreet1("292 Rue Saint-Martin")
            .setZipcode("75003")
            .setCity("Paris")
            .setCountry("France").build();

    /**
     * An address with no empty field
     */
    protected Address fullAddress = Address.builder()
            .setStreet1("street1xa")
            .setStreet2("street2xa")
            .setCity("cityxa")
            .setState("statexa")
            .setZipcode("zipcodexa")
            .setCountry("countryxa").build();

    /**
     * A method to be called before each test.
     * 
     * Please call it from the actual test. See {@link BackOfficeCustomerControllerTest#beforeEach()}.
     */
    public void beforeEach() {
        YapsUser u1 = new YapsUser("u1", YapsUserRole.CUST);
        YapsUser u2 = new YapsUser("u2", YapsUserRole.CUST);
        Customer customer1 = new Customer("1", "firstnamexa", "lastnamexa", "101010", "c1@cnam.fr", fullAddress, u1);
        Customer customer2 = new Customer("2", "Pascal", "Graffion", "06020305", "c2@cnam.fr", cnamAddress, u2);
        // CustomerDTOMapper should probably not be used here. But, well, it just works.

        map.put(customer1.getId(), CustomerDTOMapper.toDTO(customer1));
        map.put(customer2.getId(), CustomerDTOMapper.toDTO(customer2));
    }

    /**
     * Returns customer "1" or "2".
     * @param id
     * @return
     */
    public CustomerDTO getCustomer(String id) {
        return map.get(id);
    }


}
