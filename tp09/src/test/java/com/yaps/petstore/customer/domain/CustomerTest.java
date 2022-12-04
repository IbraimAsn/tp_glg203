package com.yaps.petstore.customer.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.yaps.petstore.security.domain.YapsUser;
import com.yaps.petstore.security.domain.YapsUserRole;

public class CustomerTest {
	// ----------------------------------------------
	// For testing validation annotations
	// -----------------------------------------------
	private static ValidatorFactory validatorFactory;
	private static Validator validator;

	@BeforeAll
	public static void createValidator() {
		validatorFactory = Validation.buildDefaultValidatorFactory();
		validator = validatorFactory.getValidator();
	}

	@AfterAll
	public static void close() {
		validatorFactory.close();
	}

	private YapsUser billUser = new YapsUser("bill", YapsUserRole.CUST);

	// ==================================
	// = Test cases =
	// ==================================

	/**
	 * This test tries to create an object with valid values.
	 */
	@Test
	public void testCreateValidCustomer() {

		// Creates a valid customer
		// try {		
		final Customer customer = new Customer("bill000", "Bill", "Gates", billUser);
		assertEquals("Bill", customer.getFirstname());
		assertEquals("Gates", customer.getLastname());
		Set<ConstraintViolation<Customer>> res = validator.validate(customer);
		assertTrue(res.isEmpty(), "Customer should be valid");
	}

	@Test
	public void testCreateCustomerEmptyFirstName() {
		Customer customer = new Customer("1234", "", "Gates", billUser);
		Set<ConstraintViolation<Customer>> res = validator.validate(customer);
		assertFalse(res.isEmpty(), "Customer with empty values are not valid");
	}


	@Test
	public void testCreateCustomerEmptyLastname() {
		Customer customer = new Customer("1234", "dsfds", "", billUser);
		Set<ConstraintViolation<Customer>> res = validator.validate(customer);
		assertFalse(res.isEmpty(), "Customer with empty values are not valid");
	}

	/**
	 * This test tries to create an object with invalid values.
	 */
	@Test
	public void testCreateCustomerWithNullLastName() throws Exception {
		// Creates objects with null values
		assertThrows(NullPointerException.class, () -> {
			new Customer("1234", "Bill", null, billUser);
		});
	}

	/**
	 * This test tries to create an object with invalid values.
	 */
	@Test
	public void testCreateCustomerWithNoUser() throws Exception {
		// Creates objects with null values
		assertThrows(NullPointerException.class, () -> {
			new Customer("1234", "Bill", "Gates", null);
		});
	}

	@Test
	public void testCreateCustomerWithUserNotCustomer() throws Exception {
		// Creates objects with null values
		YapsUser badUser = new YapsUser("u", YapsUserRole.EMP); // not customer
		Customer customer = new Customer("1234", "Bill", "Gates", badUser);
		
		Set<ConstraintViolation<Customer>> res = validator.validate(customer);
		assertTrue(res.isEmpty(), "Customer should be valid");
	}

}
