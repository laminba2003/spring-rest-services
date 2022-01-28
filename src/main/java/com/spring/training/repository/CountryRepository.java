package com.spring.training.repository;

import com.spring.training.entity.CountryEntity;
import org.springframework.data.repository.CrudRepository;
import java.util.List;
import java.util.Optional;

public interface CountryRepository extends CrudRepository<CountryEntity, Long> {

    public Optional<CountryEntity> findByNameIgnoreCase(String name);

    @Override
    public List<CountryEntity> findAll();

}
