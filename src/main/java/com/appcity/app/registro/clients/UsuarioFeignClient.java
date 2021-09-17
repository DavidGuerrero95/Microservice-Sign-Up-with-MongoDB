package com.appcity.app.registro.clients;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.appcity.app.registro.models.Usuario;

@FeignClient(name = "app-usuarios")
public interface UsuarioFeignClient {

	@GetMapping("/users/existUsername")
	public Boolean existsByUsername(@RequestParam String username);

	@GetMapping("/users/email")
	public Boolean existsByEmail(@RequestParam String email);

	@GetMapping("/users/phone")
	public Boolean existsByPhone(@RequestParam String phone);

	@PostMapping("/users/crear")
	public String crearUsuario(@RequestBody Usuario usuario);

	@GetMapping("/users/listar")
	public List<Usuario> getUsers();

	@PostMapping("/users/crearUsuariosRegistro")
	public void crearUsuariosRegistro(@RequestBody Usuario usuario);

}
