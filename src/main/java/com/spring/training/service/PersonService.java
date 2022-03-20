package com.spring.training.service;

import com.spring.training.exception.EntityNotFoundException;
import com.spring.training.model.Person;
import com.spring.training.repository.PersonRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PersonService {

    private final PersonRepository personRepository;

    public List<Person> getPersons() {
        return personRepository.findAll().stream().map(entity -> entity.toPerson()).collect(Collectors.toList());
    }

    public Person getPerson(Long id) {
        return personRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(String.format("person not found with id = %s", id)))
                .toPerson();
    }

    public Person createPerson(Person person) {
        return person;
    }

    public Person updatePerson(Person person) {
        return person;
    }

}