package com.jordanec.peopledirectory.dto;

import lombok.Data;

@Data
public class LanguageDTO
{
    private String code;
    private String name;
    private String iso639_2;
    private String nativeName;
}
