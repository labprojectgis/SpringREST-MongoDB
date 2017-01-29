package com.jordanec.peopledirectory.model;


import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.jordanec.peopledirectory.util.JsonDateDeserializer;
import com.jordanec.peopledirectory.util.JsonDateSerializer;

public class Person {
	@Id
	private String id;
	private long dni;
	private String firstName;
	private String lastName;
	private String email;
	private String gender;
	private String ipAddress;
	private long mobile;
	private Date dateOfBirth;
	private String color;
	private String frequency;
	private String mac;
	private String company;
	private String language;
	private String shirtSize;
	private String university;
	private String country;
	
	@Transient
	 private SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
	
	public Person() {}
	
	public Person(String id, long dni, String firstName, String lastName, String email, String gender, String ipAddress,
			long mobile, Date dateOfBirth, String color, String frequency, String mac, String company, String language,
			String shirtSize, String university, String country) {
		super();
		this.id = id;
		this.dni = dni;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.gender = gender;
		this.ipAddress = ipAddress;
		this.mobile = mobile;
		this.dateOfBirth = dateOfBirth;
		this.color = color;
		this.frequency = frequency;
		this.mac = mac;
		this.company = company;
		this.language = language;
		this.shirtSize = shirtSize;
		this.university = university;
		this.country = country;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public long getDni() {
		return dni;
	}

	public void setDni(long dni) {
		this.dni = dni;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public long getMobile() {
		return mobile;
	}

	public void setMobile(long mobile) {
		this.mobile = mobile;
	}

	@JsonSerialize(using=JsonDateSerializer.class) 
	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	@JsonDeserialize(using=JsonDateDeserializer.class)
	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getFrequency() {
		return frequency;
	}

	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getShirtSize() {
		return shirtSize;
	}

	public void setShirtSize(String shirtSize) {
		this.shirtSize = shirtSize;
	}

	public String getUniversity() {
		return university;
	}

	public void setUniversity(String university) {
		this.university = university;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	} 	
}
