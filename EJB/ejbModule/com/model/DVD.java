package com.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name = "DVDS")
public class DVD {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private String name;
	private double price;
	
	@ManyToOne
	@JoinColumn(name = "UserId", nullable = true)
	private User user;
	

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

	public double getPrice() {
		return price;
	}

	public void setPrice(double weight) {
		this.price = weight;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public int hashCode() {
		return getId();
	}
	
	@Override
	public boolean equals(Object obj) {
		
		if(obj instanceof DVD){
			DVD dvd = (DVD) obj;
			return dvd.getId() == getId();
		}
		
		return false;
	}
}
