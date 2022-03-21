package com.spring.training.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Country {
    @NotNull
    private String name;
    @NotNull
    private String capital;
    private int population;
}
