package com.gittalent.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

/**
 * Created by ldoguin on 25/10/16.
 */
public class CountryList {

    private final List<String> countries;
    private final Random random;
    private final int countryListSize;

    public CountryList() {
        // A collection to store our country object
        this.countries = new ArrayList<String>();

        // Get ISO countries, create Country object and
        // store in the collection.
        String[] isoCountries = Locale.getISOCountries();
        for (String country : isoCountries) {
            Locale locale = new Locale("en", country);
            String name = locale.getDisplayCountry();

            if (!"".equals(name)) {
                this.countries.add(name);
            }
        }
        this.countryListSize = countries.size();
        this.random = new Random();
    }

    public String getRandomCountry(){
        int idx = random.nextInt(countryListSize);
        return countries.get(idx);
    }

}