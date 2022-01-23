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
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "buildings")
public class Building {
	
	@Id
	@Column(name = "building_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@NotBlank
	private String address;

	@Min(value=1, message = "Floors must be above 0.")
	private int totalFloors;
	
	@DecimalMin(value = "0.00", message = "Area size is above zero.")
	private double commonAreaSize;
	
	@DecimalMin(value = "0.00", message = "Area size is above zero.")
	private double apartmentsAreaSize;
	
	@DecimalMin(value = "0.00", message = "Area size is above zero.")
	private double totalAreaSize;

	@OneToMany(mappedBy =  "building")
	private List<Apartment> apartments;
	
	@ManyToOne
	@JoinColumn(name = "employee_id", nullable = true)
	private Employee serviceEmployee;

	public Building() {
	}

	public Building(String address, int totalFloors, double commonAreaSize, List<Apartment> apartments,
			Employee serviceEmployee, double totalAreaSize, double apartmentsAreaSize) {
		this.address = address;
		this.totalFloors = totalFloors;
		this.commonAreaSize = commonAreaSize;
		this.apartments = apartments;
		this.serviceEmployee = serviceEmployee;
		this.apartmentsAreaSize = apartmentsAreaSize;
		this.totalAreaSize = totalAreaSize;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getTotalFloors() {
		return totalFloors;
	}

	public void setTotalFloors(int totalFloors) {
		this.totalFloors = totalFloors;
	}

	public double getCommonAreaSize() {
		return commonAreaSize;
	}

	public void setCommonAreaSize(double commonAreaSize) {
		this.commonAreaSize = commonAreaSize;
	}

	public List<Apartment> getApartments() {
		return apartments;
	}

	public void setApartments(List<Apartment> apartments) {
		this.apartments = apartments;
	}

	public Employee getServiceEmployee() {
		return serviceEmployee;
	}

	public void setServiceEmployee(Employee serviceEmployee) {
		this.serviceEmployee = serviceEmployee;
	}

	public double getTotalAreaSize() {
		return totalAreaSize;
	}

	public void setTotalAreaSize(double totalAreaSize) {
		this.totalAreaSize = totalAreaSize;
	}

	public double getApartmentsAreaSize() {
		return apartmentsAreaSize;
	}

	public void setApartmentsAreaSize(double apartmentsAreaSize) {
		this.apartmentsAreaSize = apartmentsAreaSize;
	}
}
