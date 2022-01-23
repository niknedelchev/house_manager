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

@Entity
@Table(name = "employees")
public class Employee {

	@Id
	@Column(name = "employee_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@NotBlank
	private String name;
	
	@ManyToOne
	@JoinColumn(name = "service_company_id", nullable = false)
	private ServiceCompany serviceCompany;
	
	@OneToMany(mappedBy =  "serviceEmployee")
	private List<Building> buildings;
	
	@OneToMany(mappedBy =  "employee")
	private List<Fee> fees;


	public Employee() {
	}

	public Employee(String name, ServiceCompany serviceCompany, List<Building> buildings, List<Fee> fees) {
		this.name = name;
		this.serviceCompany = serviceCompany;
		this.buildings = buildings;
		this.fees = fees;
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

	public ServiceCompany getServiceCompany() {
		return serviceCompany;
	}

	public void setServiceCompany(ServiceCompany serviceCompany) {
		this.serviceCompany = serviceCompany;
	}

	public List<Building> getBuildings() {
		return buildings;
	}

	public void setBuildings(List<Building> buildings) {
		this.buildings = buildings;
	}
	
	public List<Fee> getFees() {
		return fees;
	}

	public void setFees(List<Fee> fees) {
		this.fees = fees;
	}
}
