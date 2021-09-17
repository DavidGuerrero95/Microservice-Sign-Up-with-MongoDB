package com.appcity.app.registro.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

import com.appcity.app.registro.models.UsuarioRegistro;


public interface UsuarioRegistroRepository extends MongoRepository<UsuarioRegistro, String> {

	@RestResource(path = "findUser")
	public UsuarioRegistro findByUsername(@Param("username") String username);

}