/*
 * Copyright (c) 2016 Couchbase, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gittalent.model;

/**
 * Created by ldoguin on 05/08/16.
 */
public class Address {

    private String street;

    private String city;

    private String country;

    private Coord coords;

    public Address() {
    }

    public Address(String street, String city, String country, Coord coords) {
        this.street = street;
        this.city = city;
        this.country = country;
        this.coords = coords;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street.trim();
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city.trim();
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country.trim();
    }

    public Coord getCoords() {
        return coords;
    }

    public void setCoords(Coord coords) {
        this.coords = coords;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Address address = (Address) o;

        if (street != null ? !street.equals(address.street) : address.street != null) return false;
        if (city != null ? !city.equals(address.city) : address.city != null) return false;
        if (country != null ? !country.equals(address.country) : address.country != null) return false;
        return coords != null ? coords.equals(address.coords) : address.coords == null;

    }

    @Override
    public int hashCode() {
        int result = street != null ? street.hashCode() : 0;
        result = 31 * result + (city != null ? city.hashCode() : 0);
        result = 31 * result + (country != null ? country.hashCode() : 0);
        result = 31 * result + (coords != null ? coords.hashCode() : 0);
        return result;
    }
}