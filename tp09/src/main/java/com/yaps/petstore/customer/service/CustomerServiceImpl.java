package com.yaps.petstore.customer.service;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.yaps.petstore.common.service.exception.ObjectNotFoundException;
import com.yaps.petstore.customer.dao.CustomerDAO;
import com.yaps.petstore.customer.domain.Customer;
import com.yaps.petstore.customer.dto.CustomerCreationDTO;
import com.yaps.petstore.customer.dto.CustomerDTO;
import com.yaps.petstore.customer.dto.CustomerUpdateDTO;
import com.yaps.petstore.customer.service.exception.MultipleCustomersForUsername;
import com.yaps.petstore.customer.service.exception.NoCustomerForUsernameException;
import com.yaps.petstore.security.domain.YapsUser;
import com.yaps.petstore.security.domain.YapsUserRole;
import com.yaps.petstore.security.exception.UsernameAlreadyExistsException;
import com.yaps.petstore.security.service.YapsUserService;
import com.yaps.petstore.sequence.service.IdSequenceService;

@Service
@Transactional
@Validated
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    CustomerDAO customerDAO;

    @Autowired
    IdSequenceService idSequenceService;

    @Autowired
    CustomerDTOMapper customerDTOMapper;

    @Autowired
    YapsUserService userService;

    @Override
    public Optional<CustomerDTO> findById(String id) {
        // Remarquer l'usage de map sur un optional :
        // - si celui-ci est vide, renvoie un optional vide
        // - sinon applique la fonction pour créer un optional "plein", ici un
        // Optional<CustomerDTO>
        return customerDAO.findById(id).map(customer -> CustomerDTOMapper.toDTO(customer));
    }

    @Override
    public void remove(String id) throws ObjectNotFoundException {
        if (customerDAO.existsById(id)) {
            customerDAO.deleteById(id);
        } else {
            throw new ObjectNotFoundException();
        }
    }

    @Override
    public List<CustomerDTO> findAll() {
        return customerDAO.findAll().stream().map(CustomerDTOMapper::toDTO).toList();
    }

    @Override
    public String save(@Valid CustomerCreationDTO customerCreationDTO) throws UsernameAlreadyExistsException {
        YapsUser user = userService.createUser(customerCreationDTO.getUsername(), customerCreationDTO.getPassword(),
                YapsUserRole.CUST);

        int newId = idSequenceService.nextCustomerId();
        String id = Integer.toString(newId);
        // Creates the corresponding user.
        customerDAO.save(customerDTOMapper.fromCreationDTO(id, customerCreationDTO, user));
        return Integer.toString(newId);
    }

    @Override
    public void update(@Valid CustomerUpdateDTO updateDTO)  {
        throw new RuntimeException("complétez la méthode update !");
        // Le code est ci-dessous, mais l'idée est de vous le faire lire :-)
        /*
        String username = updateDTO.getUsername();
        Customer customer = getActualCustomerByUsername(username);
        customerDTOMapper.fillFromDTO(customer, updateDTO);
        customerDAO.save(customer);
         */
    }

    @Override
    public CustomerDTO getCustomerByUsername(String username) {
        Customer c = getActualCustomerByUsername(username);
        return CustomerDTOMapper.toDTO(c);
    }

    /**
     * Récupère un "vrai" object customer à partir de son username.
     * Méthode interne, privée.
     * 
     * @param username
     * @return
     */
    private Customer getActualCustomerByUsername(String username) {
        List<Customer> l = customerDAO.findByUsername(username);
        if (l.size() == 0)
            throw new NoCustomerForUsernameException(username);
        else if (l.size() > 1)
            throw new MultipleCustomersForUsername(username);
        else
            return l.get(0);
    }

    @Override
    public boolean isNotEmpty() {
        return customerDAO.count() != 0;
    }

}
