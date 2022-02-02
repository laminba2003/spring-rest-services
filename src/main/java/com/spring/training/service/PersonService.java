package com.spring.training.service;

import com.spring.training.model.Person;
import com.spring.training.repository.PersonRepository;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PersonService {

    private final PersonRepository personRepository;

    @Cacheable("persons")
    public List<Person> getPersons() {
        return personRepository.findAll().stream().map(entity -> entity.toPerson()).collect(Collectors.toList());
    }

    @Cacheable("persons")
    public Person getPerson(Long id) {
        return personRepository.findById(id).orElseThrow(() -> new RuntimeException("")).toPerson();
    }

}
