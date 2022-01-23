package com.housemanager.nn.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "owners")
public class Owner {

	@Id
	@Column(name = "owner_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int id;
	
	@NotBlank
	String name;

	@Min(value=1, message = "Age must be above 0.")
	int age;

	@OneToMany(mappedBy =  "owner")
	List<Apartment> apartments;

	public Owner() {
	}

	public Owner(String name, int age, List<Apartment> apartments) {
		this.name = name;
		this.age = age;
		this.apartments = apartments;
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

	public List<Apartment> getApartments() {
		return apartments;
	}

	public void setApartment(List<Apartment> apartments) {
		this.apartments = apartments;
	}
	
	
}
