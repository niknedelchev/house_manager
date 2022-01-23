package com.housemanager.nn.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Digits;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "fees")
public class Fee {

	@Id
	@Column(name = "fee_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Digits(integer=5, fraction=2)
	private BigDecimal amountDue;
	
	@Digits(integer=5, fraction=2)
	private BigDecimal amountReceived;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate issueDate;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate paymentDate;

	@ManyToOne
	@JoinColumn(name = "appartment_id", nullable = true)
	private Apartment apartment;

	@ManyToOne
	@JoinColumn(name = "employee_id", nullable = false)
	private Employee employee;

	
	public Fee() {
	}

	public Fee(BigDecimal amountDue, BigDecimal amountReceived, LocalDate issueDate, LocalDate paymentDate, Apartment apartment, Employee employee) {
		this.amountDue = amountDue.setScale(2, RoundingMode.HALF_UP);
		this.amountReceived = amountReceived.setScale(2, RoundingMode.HALF_UP);
		this.issueDate = issueDate;
		this.paymentDate = paymentDate;
		this.apartment = apartment;
		this.employee= employee;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public BigDecimal getAmountDue() {
		return amountDue;
	}

	public void setAmountDue(BigDecimal amountDue) {
		this.amountDue = amountDue;
	}
	
	public BigDecimal getAmountReceived() {
		return amountReceived;
	}

	public void setAmountReceived(BigDecimal amountReceived) {
		this.amountReceived = amountReceived.setScale(2, RoundingMode.HALF_UP);
	}

	public LocalDate getIssueDate() {
		return issueDate;
	}

	public void setPaymentDate(LocalDate paymentDate) {
		this.paymentDate = paymentDate;
	}
	
	public void setIssueDate(LocalDate issueDate) {
		this.issueDate = issueDate;
	}

	public LocalDate getPaymentDate() {
		return paymentDate;
	}

	public Apartment getApartment() {
		return apartment;
	}

	public void setAppartment(Apartment apartment) {
		this.apartment = apartment;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}
	
	
}
