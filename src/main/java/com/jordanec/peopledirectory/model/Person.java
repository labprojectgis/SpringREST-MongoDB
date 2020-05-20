package com.jordanec.peopledirectory.model;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.jordanec.peopledirectory.dto.CountryDTO;
import com.jordanec.peopledirectory.dto.HobbyDTO;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "persons")
@RequiredArgsConstructor
@Data
public class Person
{
	@Transient
	private static final long serialVersionUID = 6513485617455789714L;
	@Id
	private String id;
	@Indexed(unique=true)
	private long dni;
	private String firstName;
	private String lastName;
	private String email;
	private String gender;
	private String ipAddress;
	private long mobile;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	@JsonDeserialize(using = LocalDateDeserializer.class)
	@JsonSerialize(using = LocalDateSerializer.class)
	private LocalDate dateOfBirth;
	private Integer age;
	private String color;
	private String frequency;
	private String mac;
	private String company;
	private String language;
	private String shirtSize;
	private String university;
	private CountryDTO country;
	private List<HobbyDTO> hobbies;
	private Long total;
}
