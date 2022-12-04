package com.yaps.petstore.customer.domain;

import java.util.Objects;

import javax.persistence.Embeddable;

import org.springframework.data.annotation.Immutable;

/**
 * An address, seen as a value.
 * 
 */
@Embeddable
@Immutable // Tells hibernate the object is immutable. needed here.
public class Address {

    /*
     * We used to have a constant here, but this is dangerous. This object is supposed to be immutable,
     * but is not really so, as it is used by JPA, which can bypass our methods and directly modify the fields.
     * 
     * For a mutable object : either create a public default constructor or a static factory method.
     */

    public static final Address EMPTY_ADDRESS = new Address("", "", "", "", "", "");

    private  final String street1;
    private  final String street2;
    private  final String city;
    private  final String state;
    private  final String zipcode;
    private  final String country;

    /**
     * Default constructor.
     * 
     * The JPA specification requires that all persistent classes have a no-arg constructor
     * 
     * <p> Not in line with the "immutable" approach of our class, 
     * but needed for JPA.
     * 
     * Note : it caused a weird bug as the fields were not final.
     */
    Address() {
        this("", "", "", "", "", "");
    }

    /**
     * Creates a new address.
     * No element of the address can be nul. They can be empty strings, though.
     * 
     * @param street1
     * @param street2
     * @param city
     * @param state
     * @param zipcode
     * @param country
     * @throws NullPointerException if any argument is null.
     */
    public Address(String street1, String street2, String city, String state, String zipcode, String country) {
        if (street1 == null || street2 == null || city == null || state == null || zipcode == null || country == null)
            throw new NullPointerException("No argument of address may be null");
        this.street1 = street1;
        this.street2 = street2;
        this.city = city;
        this.state = state;
        this.zipcode = zipcode;
        this.country = country;
    }

    /*
     * Factory method to create an empty address.
     * @return
     
    public static Address createEmptyAddress() {
        return builder().build();
    }
    */

    /**
     * @return the street1
     */
    public String getStreet1() {
        return street1;
    }

    /**
     * @return the street2
     */
    public String getStreet2() {
        return street2;
    }

    /**
     * @return the city
     */
    public String getCity() {
        return city;
    }

    /**
     * @return the state
     */
    public String getState() {
        return state;
    }

    /**
     * @return the zipcode
     */
    public String getZipcode() {
        return zipcode;
    }

    /**
     * @return the country
     */
    public String getCountry() {
        return country;
    }

    @Override
    public String toString() {
        return "Address [street1=" + street1 + ", street2=" + street2 +
                ", city=" + city + ", state=" + state
                + ", zipcode=" + zipcode + ", country=" + country + "]";
    }

    @Override
    public int hashCode() {
        return Objects.hash(street1, street2, city, state, zipcode, country);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Address other = (Address) obj;
        return Objects.equals(street1, other.street1) && Objects.equals(street2, other.street2)
                && Objects.equals(city, other.city) && Objects.equals(state, other.state)
                && Objects.equals(zipcode, other.zipcode) && Objects.equals(country, other.country);
    }

    /**
     * A builder for addresses.
     * 
     * Helps to create an address gradually.
     * 
     * Uses the <span style="font-variant: small-caps">Construction Builder</span> pattern
     * 
     * Use is typically :
     * <pre>
     * Address.Builder builder = Address.builder();
     * builder.setStreet1("....")
     *        .setStreet2("....");
     * Address a = builder.build();
     * </pre>
     * or alternatively :
     * <pre>
     * Address a = Address.builder()
     *               .setStreet1("....")
     *               .setStreet2("....")
     *               .build();
     * </pre>
     * 
     * To modify an existing address, {@link }
     * It seems that we might need to modify addresses...
     * Hence, our nice system of immutable addresses is problematic.
     * 
     * When there is a problem, there is a pattern.
     * 
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Creates a builder to generated a modified version of the current address. 
     * 
     * Typical use :
     * <pre>
     * Address a = ....;
     * ...
     * Address newAddress = a.copyBuilder()
     *                       .setCity("Paris")
     *                       .build();
     * </pre>
     * @return
     */
    public  Builder copyBuilder() {
        return new Builder(this);
    }

    public static class Builder {
        private String street1 = "";
        private String street2 = "";
        private String city= "";
        private String state= "";
        private String zipcode= "";
        private String country= "";

        private Builder() {
        }

        private Builder(Address toCopy) {
            this.street1 = toCopy.getStreet1();
            this.street2 = toCopy.getStreet2();
            this.city = toCopy.getCity();
            this.state = toCopy.getState();
            this.zipcode = toCopy.getZipcode();
            this.country = toCopy.getCountry();
        }



        /**
         * @param street1 the street1 to set
         */
        public Builder setStreet1(String street1) {
            this.street1 = street1;
            return this;
        }

        /**
         * @param street2 the street2 to set
         */
        public Builder setStreet2(String street2) {
            this.street2 = street2;
            return this;
        }

        /**
         * @param city the city to set
         */
        public Builder setCity(String city) {
            this.city = city;
            return this;
        }

        /**
         * @param state the state to set
         */
        public Builder setState(String state) {
            this.state = state;
            return this;
        }

        /**
         * @param zipcode the zipcode to set
         */
        public Builder setZipcode(String zipcode) {
            this.zipcode = zipcode;
            return this;
        }

        /**
         * @param country the country to set
         */
        public Builder setCountry(String country) {
            this.country = country;
            return this;
        }

        public Address build() {
            return new Address(street1, street2, city, state, zipcode, country);
        }
        
    }
}
