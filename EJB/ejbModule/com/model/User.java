package com.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "USERS")
@NamedQuery(name="User.findUserByEmail", query="select u from User u where u.email = :email")
public class User {
	
	public static final String FIND_BY_EMAIL = "User.findUserByEmail";
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	@Column(unique = true)
	private String email;
	private String password;
	private String name;
	private String role;
	
	@Column(name="dvduser")
	@OneToMany(mappedBy ="user")
	private Set<DVD> dvd = new HashSet<DVD>(0);

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	
	public Set<DVD> getDVD() {
		return dvd;
	}

	public void setDvdUser(Set<DVD> dvdUser) {
		this.dvd = dvdUser;
	}

	@Override
	public int hashCode() {
		return getId();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof User){
			User user = (User) obj;
			return user.getEmail().equals(getEmail());
		}
		
		return false;
	}
}
