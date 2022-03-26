package com.spring.training;

import com.spring.training.domain.Country;
import com.spring.training.domain.Person;

public abstract class BaseClass {

    protected Person getPerson() {
        return new Person(1L, "Mamadou Lamine", "Ba", getCountry());
    }

    protected Country getCountry() {
        return new Country("France", "Paris", 1223333677);
    }
}
