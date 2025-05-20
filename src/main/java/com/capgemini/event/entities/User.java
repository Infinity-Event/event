package com.capgemini.event.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "users")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private Long userId;

	@NotBlank(message = "Name is required")
	@Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
	@Column(name = "name")
	private String name;

	@NotBlank(message = "Email is required")
	@Email(message = "Email should be valid")
	@Column(name = "email", unique = true)
	private String email;

	@NotBlank(message = "Password is required")
	@Size(min = 6, message = "Password must be at least 6 characters")
	@Column(name = "password")
	private String password;

	@NotBlank(message = "Phone number is required")
	@Pattern(regexp = "^\\d{10}$", message = "Phone number must be 10 digits")
	@Column(name = "phone")
	private String phone;

	@Enumerated(EnumType.STRING)
	@NotNull(message = "User type is required")
	@Column(name = "user_type")
	private UserType userType;

	public User() {
	}

	public User(Long userId, String name, String email, String password, String phone, UserType userType) {
		this.userId = userId;
		this.name = name;
		this.email = email;
		this.password = password;
		this.phone = phone;
		this.userType = userType;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public UserType getType() {
		return userType;
	}

	public void setType(UserType userType) {
		this.userType = userType;
	}

	@Override
	public String toString() {
		return "User [userId=" + userId + ", name=" + name + ", email=" + email + ", password=" + password + ", phone="
				+ phone + ", userType=" + userType + "]";
	}

	public User orElseThrow(Object object) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'orElseThrow'");
	}
}