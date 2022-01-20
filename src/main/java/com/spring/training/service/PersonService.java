package com.spring.training.service;

import com.spring.training.model.Person;
import org.springframework.stereotype.Service;

@Service
public class PersonService {

    public Person findPerson(String name) {
        Person person = new Person();
        person.setFirstName("Mamadou Lamine");
        person.setLastName("Ba");
        return person;
    }

}
