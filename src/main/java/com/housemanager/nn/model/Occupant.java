package com.housemanager.nn.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "occupants")
public class Occupant {

	@Id
	@Column(name = "occupant_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@NotBlank
	private String name;

	@Min(value=0, message = "Age must be above 0.")
	private int age;
	
	@ManyToOne
	@JoinColumn(name = "apartment_id", nullable = false)
	private Apartment apartment;

	public Occupant() {
	}

	public Occupant(String name, int age, Apartment apartment) {
		this.name = name;
		this.age = age;
		this.apartment = apartment;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public Apartment getApartment() {
		return apartment;
	}

	public void setApartment(Apartment apartment) {
		this.apartment = apartment;
	}
	
	
}
