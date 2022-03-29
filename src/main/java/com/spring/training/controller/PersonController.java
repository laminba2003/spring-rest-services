package com.spring.training.controller;

import com.spring.training.annotation.IsAdmin;
import com.spring.training.domain.Person;
import com.spring.training.domain.User;
import com.spring.training.service.PersonService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@RestController
@RequestMapping("persons")
@AllArgsConstructor
@Slf4j
public class PersonController {

    private final PersonService personService;

    @GetMapping
    public Page<Person> getPersons(Pageable pageable) {
        log.debug("returning the list of persons with {}", pageable);
        return personService.getPersons(pageable);
    }

    @GetMapping("{id}")
    public Person getPerson(@PathVariable("id") Long id) {
        log.debug("returning the person with id = {}", id);
        return personService.getPerson(id);
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    @IsAdmin
    public Person createPerson(Authentication authentication, @Valid @RequestBody Person person) {
        User user = (User) authentication.getPrincipal();
        log.debug("creating person with values = {} and user : {}", person, user.getEmail());
        return personService.createPerson(person);
    }

    @PutMapping("{id}")
    @IsAdmin
    public Person updatePerson(Authentication authentication, @PathVariable("id") Long id, @Valid @RequestBody Person person) {
        User user = (User) authentication.getPrincipal();
        log.debug("updating person with id = {}, values = {} and user : {}", id, person, user.getEmail());
        return personService.updatePerson(id, person);
    }

    @DeleteMapping("{id}")
    public void deletePerson(Authentication authentication, @PathVariable("id") Long id) {
        User user = (User) authentication.getPrincipal();
        log.debug("deleting person with id = {} and user : {}", id, user.getEmail());
        personService.deletePerson(id);
    }

}