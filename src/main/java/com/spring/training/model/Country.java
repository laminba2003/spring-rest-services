package com.spring.training.model;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
public class Country {
    private Long id;
    private String name;
    private String capital;
    private int population;
}
