package com.spring.training.controller;

import com.spring.training.model.Person;
import com.spring.training.service.PersonService;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("persons")
@AllArgsConstructor
public class PersonController {

    private final PersonService personService;

    @GetMapping
    @Cacheable("persons")
    public List<Person> getPersons() {
        return personService.getPersons();
    }

}