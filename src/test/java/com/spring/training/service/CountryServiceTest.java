package com.spring.training.service;

import com.spring.training.domain.Country;
import com.spring.training.entity.CountryEntity;
import com.spring.training.exception.EntityNotFoundException;
import com.spring.training.exception.RequestException;
import com.spring.training.repository.CountryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
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
class CountryServiceTest {

    @Mock
    private CountryRepository countryRepository;

    @Autowired
    @InjectMocks
    private CountryService countryService;

    @Test
    void testGetCountries() {
        List<Country> countries = Collections.singletonList(getCountry());
        given(countryRepository.findAll()).
                willReturn(countries.stream().map(CountryEntity::fromCountry).collect(Collectors.toList()));
        List<Country> result = countryService.getCountries();
        verify(countryRepository).findAll();
        assertThat(result.size()).isEqualTo(countries.size());
        assertThat(result.get(0)).isEqualTo(countries.get(0));
    }

    @Test
    void testGetCountry() {
        // test country exists
        Country country = getCountry();
        given(countryRepository.findByNameIgnoreCase(country.getName())).
                willReturn(Optional.of(CountryEntity.fromCountry(country)));
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
        given(countryRepository.save(CountryEntity.fromCountry(country))).
                willReturn(CountryEntity.fromCountry(country));
        Country result = countryService.createCountry(country);
        verify(countryRepository).save(any(CountryEntity.class));
        assertThat(country).isEqualTo(result);

        // test country exists
        reset(countryRepository);
        given(countryRepository.findByNameIgnoreCase(country.getName()))
                .willReturn(Optional.of(CountryEntity.fromCountry(country)));
        assertThatThrownBy(() -> countryService.createCountry(country))
                .isInstanceOf(RequestException.class)
                .hasMessageContaining(String.format("the country with name %s is already created", country.getName()));

    }

    @Test
    void testUpdateCountry() {
        // test country exists
        Country country = getCountry();
        given(countryRepository.findByNameIgnoreCase(country.getName()))
                .willReturn(Optional.of(CountryEntity.fromCountry(country)));
        given(countryRepository.save(CountryEntity.fromCountry(country))).
                willReturn(CountryEntity.fromCountry(country));
        Country result = countryService.updateCountry(country.getName(), country);
        verify(countryRepository).save(any(CountryEntity.class));
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
        given(countryRepository.existsById(name)).
                willReturn(true);
        countryService.deleteCountry(name);
        verify(countryRepository).deleteById(name);

        // test country cannot be deleted
        reset(countryRepository);
        given(countryRepository.existsById(name)).
                willReturn(true);
        doThrow(RuntimeException.class).when(countryRepository).deleteById(name);
        assertThatThrownBy(() -> countryService.deleteCountry(name))
                .isInstanceOf(RequestException.class)
                .hasMessageContaining(String.format("the country with name %s cannot be deleted", name))
                .hasFieldOrPropertyWithValue("status", HttpStatus.CONFLICT);

    }

    private Country getCountry() {
        return new Country("France", "Paris", 1223333677);
    }

}