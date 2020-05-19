package com.jordanec.peopledirectory.model;

import com.jordanec.peopledirectory.dto.CurrencyDTO;
import com.jordanec.peopledirectory.dto.LanguageDTO;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "countries")
@RequiredArgsConstructor
@Data
public class Country
{
    @Transient
    private static final long serialVersionUID = 1L;
    @Id
    private String id;
    @Indexed(unique=true)
    private String name;
    private String code;
    private String capital;
    private String region;
    private String demonym;
    private CurrencyDTO currency;
    private LanguageDTO language;
    private String flag;
    private Long population;
}
