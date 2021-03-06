package com.spring.training.service;

import com.spring.training.BaseTestClass;
import com.spring.training.domain.Person;
import com.spring.training.entity.CountryEntity;
import com.spring.training.entity.PersonEntity;
import com.spring.training.exception.EntityNotFoundException;
import com.spring.training.exception.RequestException;
import com.spring.training.mapping.CountryMapper;
import com.spring.training.mapping.PersonMapper;
import com.spring.training.repository.CountryRepository;
import com.spring.training.repository.PersonRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PersonServiceTest extends BaseTestClass {

    @Mock
    PersonRepository personRepository;

    @Mock
    CountryRepository countryRepository;

    @Spy
    PersonMapper personMapper = Mappers.getMapper(PersonMapper.class);

    @Spy
    CountryMapper countryMapper = Mappers.getMapper(CountryMapper.class);

    @Spy
    MessageSource messageSource = getMessageSource();

    @InjectMocks
    PersonService personService;

    @Test
    void testGetPersons() {
        List<Person> persons = Collections.singletonList(getPerson());
        Pageable pageable = PageRequest.of(1, 5);
        Page<PersonEntity> page = new PageImpl<>(persons.stream().map(personMapper::fromPerson).collect(Collectors.toList()), pageable, persons.size());
        given(personRepository.findAll(pageable)).willReturn(page);
        Page<Person> result = personService.getPersons(pageable);
        verify(personRepository).findAll(pageable);
        assertThat(result.getContent().size()).isEqualTo(persons.size());
    }

    @Test
    void testGetPerson() {
        // test get existing person
        Person person = getPerson();
        PersonEntity personEntity = personMapper.fromPerson(person);
        given(personRepository.findById(person.getId())).willReturn(Optional.of(personEntity));
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
        CountryEntity countryEntity = countryMapper.fromCountry(person.getCountry());
        given(countryRepository.findByNameIgnoreCase(person.getCountry().getName()))
                .willReturn(Optional.of(countryEntity));
        PersonEntity personEntity = personMapper.fromPerson(person);
        given(personRepository.save(any())).willReturn(personEntity);
        Person result = personService.createPerson(getPerson());
        verify(countryRepository).findByNameIgnoreCase(person.getCountry().getName());
        verify(personRepository).save(any());
        assertThat(person).isEqualTo(result);

        // test create with non existing country
        reset(countryRepository);
        given(countryRepository.findByNameIgnoreCase(person.getCountry().getName()))
                .willReturn(Optional.empty());
        assertThatThrownBy(() -> personService.createPerson(person))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining(String.format("country not found with name = %s", person.getCountry().getName()));
    }


    @Test
    void testUpdatePerson() {
        // test update person with existing country
        Person person = getPerson();
        PersonEntity personEntity = personMapper.fromPerson(person);
        given(personRepository.findById(person.getId())).willReturn(Optional.of(personEntity));
        CountryEntity countryEntity = countryMapper.fromCountry(person.getCountry());
        given(countryRepository.findByNameIgnoreCase(person.getCountry().getName()))
                .willReturn(Optional.of(countryEntity));
        given(personRepository.save(personEntity)).willReturn(personEntity);
        Person result = personService.updatePerson(person.getId(), getPerson());
        verify(countryRepository).findByNameIgnoreCase(person.getCountry().getName());
        verify(personRepository).save(any());
        assertThat(person).isEqualTo(result);

        // test update person with non existing country
        reset(countryRepository);
        given(countryRepository.findByNameIgnoreCase(person.getCountry().getName()))
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
        Long id = 1L;
        personService.deletePerson(id);
        verify(personRepository).deleteById(id);

        // test person cannot be deleted
        doThrow(RuntimeException.class).when(personRepository).deleteById(id);
        assertThatThrownBy(() -> personService.deletePerson(id))
                .isInstanceOf(RequestException.class)
                .hasMessageContaining(String.format("the person with id %s cannot be deleted", id))
                .hasFieldOrPropertyWithValue("status", HttpStatus.CONFLICT);

    }

}