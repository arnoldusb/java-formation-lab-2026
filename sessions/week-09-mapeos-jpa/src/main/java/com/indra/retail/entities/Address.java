package com.indra.retail.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.Objects;

@Embeddable
public class Address {

    @Column(name = "street", length = 160)
    private String street;

    @Column(name = "city", length = 100)
    private String city;

    @Column(name = "postal_code", length = 20)
    private String postalCode;

    @Column(name = "country", length = 100)
    private String country;

    protected Address() {
    }

    public Address(String street, String city, String postalCode, String country) {
        this.street = Objects.requireNonNull(street, "street must not be null");
        this.city = Objects.requireNonNull(city, "city must not be null");
        this.postalCode = Objects.requireNonNull(postalCode, "postalCode must not be null");
        this.country = Objects.requireNonNull(country, "country must not be null");
    }

    public String getStreet() {
        return street;
    }

    public String getCity() {
        return city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getCountry() {
        return country;
    }
}
