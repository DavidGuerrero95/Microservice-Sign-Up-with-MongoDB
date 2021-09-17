package com.appcity.app.registro.models;

import java.util.List;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "registro")
public class UsuarioRegistro {

	@Id
	private String id;

	@NotBlank(message = "Username cannot be null")
	@Size(max = 20)
	@Indexed(unique = true)
	private String username;

	@NotBlank(message = "Password cannot be null")
	@Size(min = 6, max = 20, message = "About Me must be between 6 and 20 characters")
	private String password;

	@NotBlank(message = "Phone cannot be null")
	@Size(max = 50)
	@Indexed(unique = true)
	private String phone;

	@NotBlank(message = "Email cannot be null")
	@Size(max = 50)
	@Email(message = "Email should be valid")
	@Indexed(unique = true)
	private String email;

	private String codigo;

	private List<String> roles;

	public UsuarioRegistro() {
	}

	public UsuarioRegistro(String username, String password, String phone, String email, String codigo,
			List<String> roles) {
		super();
		this.username = username;
		this.password = password;
		this.phone = phone;
		this.email = email;
		this.codigo = codigo;
		this.roles = roles;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
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

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

}
