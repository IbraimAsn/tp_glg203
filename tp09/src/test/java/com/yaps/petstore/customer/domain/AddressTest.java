package com.yaps.petstore.customer.domain;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;


public class AddressTest {

    @Test
    public void testEmptyBuilder() {
        Address a = Address.builder().build();
        assertEquals(Address.EMPTY_ADDRESS, a);
        assertAll(
            () -> assertEquals("", a.getStreet1()),
            () -> assertEquals("", a.getStreet2()),
            () -> assertEquals("", a.getCity()),
            () -> assertEquals("", a.getZipcode()),
            () -> assertEquals("", a.getCountry())
        );        
    }

    @Test
    public void testSimpleBuilder() {
        Address a = Address.builder()
                .setStreet1("292 Rue Saint-Martin")
                .setZipcode("75003")
                .setCity("Paris")
                .setCountry("France")
                .build();
        assertEquals("292 Rue Saint-Martin", a.getStreet1());
        assertEquals("", a.getStreet2());
        assertEquals("Paris", a.getCity());
        assertEquals("France", a.getCountry());
        assertEquals("", a.getState());
        assertEquals("75003", a.getZipcode());
    }

    @Test
    public void testFullBuilder() {
        Address a = Address.builder()
                .setStreet1("292 Rue Saint-Martin")
                .setStreet2("(métro arts et métiers)")
                .setZipcode("75003")
                .setCity("Paris")
                .setCountry("France")
                .setState("Ile de France")
                .build();
        assertEquals("292 Rue Saint-Martin", a.getStreet1());
        assertEquals("(métro arts et métiers)", a.getStreet2());
        assertEquals("Paris", a.getCity());
        assertEquals("France", a.getCountry());
        assertEquals("Ile de France", a.getState());
        assertEquals("75003", a.getZipcode());
    }

    @Test
    public void testCopyBuilder() {
        Address a = Address.builder()
                .setStreet1("292 Rue Saint-Martin")
                .setZipcode("75003")
                .setCity("Paris")
                .setCountry("France")
                .build();
        Address a1 = a.copyBuilder().setStreet1("2, rue Conté").build();

        assertEquals("2, rue Conté", a1.getStreet1());
        assertEquals("", a1.getStreet2());
        assertEquals("Paris", a1.getCity());
        assertEquals("France", a1.getCountry());
        assertEquals("", a1.getState());
        assertEquals("75003", a1.getZipcode());
    }

    @Test
    public void testEquals() {
        Address a = Address.builder()
                .setStreet1("292 Rue Saint-Martin")
                .setZipcode("75003")
                .setCity("Paris")
                .setCountry("France")
                .build();
        Address b = Address.builder()
                .setStreet1("292 Rue Saint-Martin")
                .setZipcode("75003")
                .setCity("Paris")
                .setCountry("France")
                .build();
        assertTrue(a.equals(b), "equals = same values");
        assertTrue(a.hashCode() == b.hashCode(), "same hashcodes");
    }

    @Test
    public void testNotEquals() {
        Address a = Address.builder()
                .setStreet1("2 rue Conté")
                .setZipcode("75003")
                .setCity("Paris")
                .setCountry("France")
                .build();
        Address b = Address.builder()
                .setStreet1("292 Rue Saint-Martin")
                .setZipcode("75003")
                .setCity("Paris")
                .setCountry("France")
                .build();
        assertFalse(a.equals(b), "not the same values, equals should fail");
    }



}
