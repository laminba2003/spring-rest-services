package com.spring.training.service;

import com.spring.training.BaseClass;
import com.spring.training.domain.Person;
import com.spring.training.entity.CountryEntity;
import com.spring.training.entity.PersonEntity;
import com.spring.training.exception.EntityNotFoundException;
import com.spring.training.repository.CountryRepository;
import com.spring.training.repository.PersonRepository;
import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PersonServiceTest extends BaseClass {

    @Mock
    private PersonRepository personRepository;

    @Mock
    private CountryRepository countryRepository;

    @Autowired
    @InjectMocks
    private PersonService personService;

    @Test
    void testGetPersons() {
        List<Person> persons = Collections.singletonList(getPerson());
        Pageable pageable = PageRequest.of(1, 5);
        Page<PersonEntity> page = new PageImpl<>(persons.stream().map(PersonEntity::fromPerson).collect(Collectors.toList()), pageable, persons.size());
        given(personRepository.findAll(pageable)).willReturn(page);
        Page<Person> result = personService.getPersons(pageable);
        verify(personRepository).findAll(pageable);
        assertThat(result.toList().size()).isEqualTo(persons.size());
    }

    @Test
    void testGetPerson() {
        // test get existing person
        Person person = getPerson();
        given(personRepository.findById(person.getId())).willReturn(Optional.of(PersonEntity.fromPerson(person)));
        Person result = personService.getPerson(person.getId());
        verify(personRepository).findById(person.getId());
        assertThat(result).isEqualTo(person);

        // test get non existing person
        reset(personRepository);
        Long id = 2L;
        assertThatThrownBy(() -> personService.getPerson(id))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining(String.format("person not found with id = %d", id));
    }

    @Test
    void testCreatePersonCountry() {
        // test create person with existing country
        Person person = getPerson();
        given(countryRepository.findByNameIgnoreCase(anyString()))
                .willReturn(Optional.of(CountryEntity.fromCountry(person.getCountry())));
        given(personRepository.save(any())).willReturn(PersonEntity.fromPerson(person));
        Person result = personService.createPerson(getPerson());
        verify(countryRepository).findByNameIgnoreCase(anyString());
        verify(personRepository).save(any());
        assertThat(person).isEqualTo(result);

        // test create with non existing country
        reset(countryRepository);
        given(countryRepository.findByNameIgnoreCase(anyString()))
                .willReturn(Optional.empty());
        assertThatThrownBy(() -> personService.createPerson(person))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining(String.format("country not found with name = %s", person.getCountry().getName()));
    }


    @Test
    void testUpdatePerson() {
        // test update person with existing country
        Person person = getPerson();
        given(personRepository.findById(person.getId())).willReturn(Optional.of(PersonEntity.fromPerson(person)));
        given(countryRepository.findByNameIgnoreCase(anyString()))
                .willReturn(Optional.of(CountryEntity.fromCountry(person.getCountry())));
        given(personRepository.save(any())).willReturn(PersonEntity.fromPerson(person));
        Person result = personService.updatePerson(person.getId(), getPerson());
        verify(countryRepository).findByNameIgnoreCase(anyString());
        verify(personRepository).save(any());
        assertThat(person).isEqualTo(result);

        // test update person with non existing country
        reset(countryRepository);
        given(countryRepository.findByNameIgnoreCase(anyString()))
                .willReturn(Optional.empty());
        assertThatThrownBy(() -> personService.updatePerson(person.getId(), getPerson()))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining(String.format("country not found with name = %s", person.getCountry().getName()));

        // test update non existing person
        reset(personRepository);
        Long id = 2L;
        assertThatThrownBy(() -> personService.updatePerson(id, getPerson()))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining(String.format("person not found with id = %s", id));

    }

    @Test
    void testDeletePerson() {
        given(personRepository.existsById(any())).willReturn(true);
        personService.deletePerson(1L);
        verify(personRepository).deleteById(any());
    }

}