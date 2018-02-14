package iperette.domain;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

@Entity
public class Account {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(nullable = false)
	private String firstname;

	@Column(nullable = false)
	private String lastname;

	@Column(unique = true, nullable = false)
	private String email;

	@Column(nullable = false)
	@JsonProperty(access = Access.WRITE_ONLY)
	private String password;

	@Column(nullable = false)
	private String color;
	
	@Enumerated(EnumType.STRING)
	private Role role;

	// administrative stuff

	@Column(name = "created_at")
	public LocalDateTime createdAt;

	@Column(name = "updated_at")
	public LocalDateTime updatedAt;

	@PrePersist
	void createdAt() {
		this.createdAt = this.updatedAt = LocalDateTime.now();
	}

	@PreUpdate
	void updatedAt() {
		this.updatedAt = LocalDateTime.now();
	}

	public Account() {
		this.role = Role.USER;
		  String letters = "0123456789ABCDEF";
		  String color = "#";
		  for (int i = 0; i < 6; i++) {
		    color += letters.split("")[(int) Math.floor(Math.random() * 16)];
		  }
		  this.color = color;
	}

	public Account(String email, String password) {
		this();
		this.email = email;
		this.password = password;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	/*
	 * public List<Booking> getBookings() { return bookings; }
	 * 
	 * public void setBookings(List<Booking> bookings) { this.bookings = bookings; }
	 */

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}
}