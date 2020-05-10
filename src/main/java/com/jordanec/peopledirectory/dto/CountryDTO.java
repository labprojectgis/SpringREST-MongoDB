package com.jordanec.peopledirectory.dto;

import lombok.Data;

@Data
public class CountryDTO
{
    private String id;
    private String name;
    private String code;
    private String capital;
    private String region;
    private CurrencyDTO currency;
    private LanguageDTO language;
    private String flag;
    private Long population;
}
