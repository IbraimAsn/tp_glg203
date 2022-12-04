package com.yaps.petstore.customer.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.yaps.petstore.security.domain.YapsUser;


/**
 * This class represents a customer for the YAPS company.
 * 
 * 
 * A customer is linked to a YapsUser, who is supposed to have the "Customer" ROLE.
 */
@Entity
@Table(name = "customer")
public class Customer {

	@Id
	@NotBlank
	private String id;

	// ======================================
	// = Attributes =
	// ======================================
	
	@NotNull
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn( name="username" )
	private YapsUser user;

	@Column(name = "firstname", length = 50)
	@NotBlank
	private  String firstname = "";

	@NotBlank
	private  String lastname = "";

	@NotNull
	private  String telephone = "";
	
	@NotNull
	@Email
	private  String email = ""; // nouveau champ !
	
	@Embedded
	@NotNull
	private  Address address = Address.EMPTY_ADDRESS;


	// ======================================
	// = Constructors =
	// ======================================
	
	// For JPA
	Customer() {
	}

	public Customer(final String id, YapsUser user) {
		this(id, "", "", user);
	}

	/**
	 * initialization of a customer objet
	 * All unset properties will be the empty string.
	 * 
	 * @param id        can be null
	 * @param login  can't be null or empty
	 * @param firstname can't be null or empty
	 * @param lastname  can't be null or empty
	 */
	public Customer(final String id, final String firstname, final String lastname, YapsUser user) {
		this(id, firstname, lastname, "", "", Address.EMPTY_ADDRESS, user);		
	}

	/**
	 * Full initialization of a customer objet.
	 * <p>
	 * Only id can be null (it might be given by the DAO).
	 * </p>
	 * 
	 * @param id can't be empty.
	 * @param login can't be empty.
	 * @param firstname the first name, mandatory (can't be empty)
	 * @param lastname  the last name, mandatory (can't be empty)
	 * @param telephone can be the empty string.
	 * @param address   the address (can't be null).
	 */
	public Customer(final String id, final String firstname, final String lastname,
			String telephone, String email, Address address, YapsUser user) {
		if (	id == null
				|| firstname == null
				|| lastname == null
				|| telephone == null
				|| email == null
				|| address == null
				|| user == null
				)
			throw new NullPointerException();
		this.id = id;
		this.firstname = firstname;
		this.lastname = lastname;
		this.telephone = telephone;
		this.email = email;
		this.address = address;
		this.user = user;
	}

	/**
	 * Full initialization of a customer objet.
	 * <p>
	 * Only id can be null (it might be given by the DAO).
	 * </p>
	 * 
	 * @param id
	 * @param firstname the first name, mandatory (can't be empty)
	 * @param lastname  the last name, mandatory (can't be empty)
	 * @param telephone can be the empty string.
	 * @param street1   can be the empty string.
	 * @param street2   can be the empty string.
	 * @param city      can be the empty string.
	 * @param state     can be the empty string.
	 * @param zipcode   can be the empty string.
	 * @param country   can be the empty string.
	 */

	public Customer(String id,  String firstname, String lastname, String telephone, String email, String street1, String street2,
			String city, String state, String zipcode, String country, YapsUser user) {
		this(id,  firstname, lastname, telephone, email,
				new Address(street1, street2, city, state, zipcode, country), user);
	}



	// ======================================
	// = Getters and Setters =
	// ======================================

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFirstname() {
		return this.firstname;
	}

	public void setFirstname(final String firstname) {
		if (firstname == null)
			throw new NullPointerException();
		this.firstname = firstname;
	}

	public String getLastname() {
		return this.lastname;
	}

	public void setLastname(final String lastname) {
		if (lastname == null)
			throw new NullPointerException();
		this.lastname = lastname;
	}

	public String getTelephone() {
		return this.telephone;
	}

	public void setTelephone(final String telephone) {
		if (telephone == null)
			throw new NullPointerException();
		this.telephone = telephone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		if (email == null)
			throw new NullPointerException();
		this.email = email;
	}

	public String getStreet1() {
		return this.address.getStreet1();
	}

	public String getStreet2() {
		return this.address.getStreet2();
	}

	public String getCity() {
		return this.address.getCity();
	}

	public String getState() {
		return this.address.getState();
	}

	public String getZipcode() {
		return this.address.getZipcode();
	}

	public String getCountry() {
		return this.address.getCountry();
	}

	/**
	 * @param address the address to set
	 */
	public void setAddress(Address address) {
		if (address == null)
			throw new NullPointerException();
		this.address = address;
	}


	public void setStreet1(String s) {
		address = address.copyBuilder().setStreet1(s).build();
	}

	public void setStreet2(String s) {
		address = address.copyBuilder().setStreet2(s).build();
	}

	public void setCity(String s) {
		address = address.copyBuilder().setCity(s).build();
	}

	public void setState(String s) {
		address = address.copyBuilder().setState(s).build();

	}

	public void setZipcode(String s) {
		address = address.copyBuilder().setZipcode(s).build();
	}

	public void setCountry(String s) {
		address = address.copyBuilder().setCountry(s).build();
	}
	

	public Address getAddress() {
		return address;
	}

	public void setUser(YapsUser user) {
		this.user = user;
	}

	public YapsUser getUser() {
		return user;
	}
	
	public String toString() {
		final StringBuilder buf = new StringBuilder();
		buf.append("\n\tCustomer {");
		buf.append("\n\t\tId=").append(this.id);
		buf.append("\n\t\tFirst Name=").append(this.firstname);
		buf.append("\n\t\tLast Name=").append(this.lastname);
		buf.append("\n\t\tTelephone=").append(this.telephone);
		buf.append("\n\t\tStreet 1=").append(this.getStreet1());
		buf.append("\n\t\tStreet 2=").append(this.getStreet2());
		buf.append("\n\t\tCity=").append(this.getCity());
		buf.append("\n\t\tState=").append(this.getState());
		buf.append("\n\t\tZipcode=").append(this.getZipcode());
		buf.append("\n\t\tCountry=").append(this.getCountry());
		buf.append("\n\t}");
		return buf.toString();
	}

	public String shortDisplay() {
		return id + "\t" + firstname + " " + lastname;
	}
}
