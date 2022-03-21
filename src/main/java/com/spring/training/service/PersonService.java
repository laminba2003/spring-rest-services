package com.spring.training.service;

import com.spring.training.entity.PersonEntity;
import com.spring.training.exception.EntityNotFoundException;
import com.spring.training.domain.Person;
import com.spring.training.repository.CountryRepository;
import com.spring.training.repository.PersonRepository;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@CacheConfig(cacheNames = "persons")
@AllArgsConstructor
public class PersonService {

    private final PersonRepository personRepository;
    private final CountryRepository countryRepository;

    public Page<Person> getPersons(Pageable pageable) {
        return personRepository.findAll(pageable).map(PersonEntity::toPerson);
    }

    @Cacheable(key = "#id")
    public Person getPerson(Long id) {
        return personRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(String.format("person not found with id = %s", id)))
                .toPerson();
    }

    public Person createPerson(Person person) {
        person.setCountry(countryRepository.findByNameIgnoreCase(person.getCountry().getName()).orElseThrow(() ->
                new EntityNotFoundException(String.format("country not found with name = %s", person.getCountry().getName()))).toCountry());
        return personRepository.save(PersonEntity.fromPerson(person)).toPerson();
    }

    @CachePut(key = "#id")
    public Person updatePerson(Long id, Person person) {
        person.setCountry(countryRepository.findByNameIgnoreCase(person.getCountry().getName()).orElseThrow(() ->
                new EntityNotFoundException(String.format("country not found with name = %s", person.getCountry().getName()))).toCountry());
        return personRepository.findById(id)
                .map(entity -> {
                    PersonEntity modified = PersonEntity.fromPerson(person);
                    modified.setId(id);
                    return personRepository.save(modified).toPerson();
                }).orElseThrow(() -> new EntityNotFoundException(String.format("person not found with id = %d", id)));
    }

    @CacheEvict(key = "#id")
    public void deletePerson(Long id) {
        if(personRepository.existsById(id)) {
            personRepository.deleteById(id);
        }
    }

}