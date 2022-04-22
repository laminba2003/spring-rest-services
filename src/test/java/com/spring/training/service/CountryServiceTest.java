package com.spring.training.service;

import com.spring.training.BaseTestClass;
import com.spring.training.domain.Country;
import com.spring.training.entity.CountryEntity;
import com.spring.training.exception.EntityNotFoundException;
import com.spring.training.exception.RequestException;
import com.spring.training.mapping.CountryMapper;
import com.spring.training.repository.CountryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CountryServiceTest extends BaseTestClass {

    @Mock
    CountryRepository countryRepository;

    @Spy
    CountryMapper countryMapper = Mappers.getMapper(CountryMapper.class);

    @Spy
    MessageSource messageSource = getMessageSource();

    @InjectMocks
    CountryService countryService;

    @Test
    void testGetCountries() {
        List<Country> countries = Collections.singletonList(getCountry());
        List<CountryEntity> entities = countries.stream().map(countryMapper::fromCountry).collect(Collectors.toList());
        given(countryRepository.findAll()).
                willReturn(entities);
        List<Country> result = countryService.getCountries();
        verify(countryRepository).findAll();
        assertThat(result.size()).isEqualTo(countries.size());
        assertThat(result.get(0)).isEqualTo(countries.get(0));
    }

    @Test
    void testGetCountry() {
        // test country exists
        Country country = getCountry();
        CountryEntity entity = countryMapper.fromCountry(country);
        given(countryRepository.findByNameIgnoreCase(country.getName())).
                willReturn(Optional.of(entity));
        Country result = countryService.getCountry(country.getName());
        verify(countryRepository).findByNameIgnoreCase(country.getName());
        assertThat(country).isEqualTo(result);

        // test country does not exists
        reset(countryRepository);
        given(countryRepository.findByNameIgnoreCase(country.getName()))
                .willReturn(Optional.empty());
        assertThatThrownBy(() -> countryService.getCountry(country.getName()))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining(String.format("country not found with name = %s", country.getName()));

    }


    @Test
    void testCreateCountry() {
        // test country does not exists
        Country country = getCountry();
        CountryEntity entity = countryMapper.fromCountry(country);
        given(countryRepository.save(entity)).
                willReturn(entity);
        Country result = countryService.createCountry(country);
        verify(countryRepository).save(entity);
        assertThat(country).isEqualTo(result);

        // test country exists
        reset(countryRepository);
        given(countryRepository.findByNameIgnoreCase(country.getName()))
                .willReturn(Optional.of(entity));
        assertThatThrownBy(() -> countryService.createCountry(country))
                .isInstanceOf(RequestException.class)
                .hasMessageContaining(String.format("the country with name %s is already created", country.getName()));

    }

    @Test
    void testUpdateCountry() {
        // test country exists
        Country country = getCountry();
        CountryEntity entity = countryMapper.fromCountry(country);
        given(countryRepository.findByNameIgnoreCase(country.getName()))
                .willReturn(Optional.of(entity));
        given(countryRepository.save(entity)).
                willReturn(entity);
        Country result = countryService.updateCountry(country.getName(), country);
        verify(countryRepository).save(entity);
        assertThat(country).isEqualTo(result);

        // test country does not exists
        reset(countryRepository);
        assertThatThrownBy(() -> countryService.updateCountry(country.getName(), country))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining(String.format("country not found with name = %s", country.getName()));

    }

    @Test
    void testDeleteCountry() {
        // test country can be deleted
        String name = getCountry().getName();
        countryService.deleteCountry(name);
        verify(countryRepository).deleteById(name);

        // test country cannot be deleted
        doThrow(RuntimeException.class).when(countryRepository).deleteById(name);
        assertThatThrownBy(() -> countryService.deleteCountry(name))
                .isInstanceOf(RequestException.class)
                .hasMessageContaining(String.format("the country with name %s cannot be deleted", name))
                .hasFieldOrPropertyWithValue("status", HttpStatus.CONFLICT);

    }

}