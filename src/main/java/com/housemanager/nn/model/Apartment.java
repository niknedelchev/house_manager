package com.housemanager.nn.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "apartments")
public class Apartment {
	@Id
	@Column(name = "apartment_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private int floor;
	private double size;
	
	@ManyToOne
	@JoinColumn(name = "owner_id", nullable = false)
	private Owner owner;
	
	@OneToMany(mappedBy =  "apartment")
	private List<Occupant> occupants;
	
	@NotNull
	@Column(name = "has_pet")
	private boolean hasPet;
	
	@OneToMany(mappedBy =  "apartment")
	private List<Fee> fees;
	
	@ManyToOne
	@JoinColumn(name = "building_id", nullable = false)
	private Building building;
	
	public Apartment() {
	}
	
	
	public Apartment(int floor, double size, Owner owner, List<Occupant> occupants, boolean hasPet, Building building) {
		this.floor = floor;
		this.size = size;
		this.owner = owner;
		this.occupants = occupants;
		this.hasPet = hasPet;
		this.building=building;
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public int getFloor() {
		return floor;
	}


	public void setFloor(int floor) {
		this.floor = floor;
	}


	public double getSize() {
		return size;
	}


	public void setSize(double size) {
		this.size = size;
	}


	public Owner getOwner() {
		return owner;
	}


	public void setOwner(Owner owner) {
		this.owner = owner;
	}


	public List<Occupant> getOccupants() {
		return occupants;
	}


	public void setOccupants(List<Occupant> occupants) {
		this.occupants = occupants;
	}


	public boolean isHasPet() {
		return hasPet;
	}


	public void setHasPet(boolean hasPet) {
		this.hasPet = hasPet;
	}


	public List<Fee> getFees() {
		return fees;
	}


	public void setFees(List<Fee> fees) {
		this.fees = fees;
	}


	public Building getBuilding() {
		return building;
	}


	public void setBuilding(Building building) {
		this.building = building;
	}
	
}
