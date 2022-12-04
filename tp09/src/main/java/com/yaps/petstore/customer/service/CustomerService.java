package com.yaps.petstore.customer.service;


import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.yaps.petstore.common.service.exception.ObjectNotFoundException;
import com.yaps.petstore.customer.dto.CustomerCreationDTO;
import com.yaps.petstore.customer.dto.CustomerDTO;
import com.yaps.petstore.customer.dto.CustomerUpdateDTO;
import com.yaps.petstore.customer.service.exception.MultipleCustomersForUsername;
import com.yaps.petstore.customer.service.exception.NoCustomerForUsernameException;
import com.yaps.petstore.security.exception.UsernameAlreadyExistsException;

@Service
@Validated
public interface CustomerService {

    /**
     * Cherche un customer, connaissant son id.
     */
	Optional<CustomerDTO> findById(String id);

    /**
     * Supprime un customer, connaissant sont id.
     */
	void remove(String id) throws ObjectNotFoundException;

    /**
     * Renvoie tous les customers.
     */
	List<CustomerDTO> findAll();

	/**
	 * Sauvegarde un customer et crée l'utilisateur correspondant.
     * La méthode lui attribue un nouvel id.
     * Les ids attribués doivent être strictement croissants dans la base.
     * 
     * post : un customer et l'utilisateur correspondant sont créés.
     * le customer doit avoir 
	 * @param customer
	 * @return	 
	 * @throws UsernameAlreadyExistsException
	 * @throws CheckException si les données du customer sont incorrectes.
	 */
	String save(@Valid CustomerCreationDTO customer) throws UsernameAlreadyExistsException ;


    /**
     * Retourne un customer dont on connaît le login.
     * @param username
     * @return un DTO représentant ce customer.
     * @throws MultipleCustomersForUsername
     * @throw NoCustomerForUsernameException 
     */
    CustomerDTO getCustomerByUsername(String username);

    /**
     * Met à jour un customer.
     * <p> Ne doit changer ni le login, ni l'id du customer.
     * @throws MultipleCustomersForUsername
     * @throw NoCustomerForUsernameException
     */
	void update(@Valid CustomerUpdateDTO updateDTO) throws MultipleCustomersForUsername, NoCustomerForUsernameException;

    /**
     * Vérifie qu'il y a au moins un customer.
     */
	boolean isNotEmpty();





}
