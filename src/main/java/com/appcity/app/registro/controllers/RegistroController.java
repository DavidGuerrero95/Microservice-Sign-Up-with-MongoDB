package com.appcity.app.registro.controllers;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.appcity.app.registro.clients.NotificacionesFeignClient;
import com.appcity.app.registro.clients.UsuarioFeignClient;
import com.appcity.app.registro.models.Usuario;
import com.appcity.app.registro.models.UsuarioRegistro;
import com.appcity.app.registro.repository.UsuarioRegistroRepository;
import com.appcity.app.registro.request.MessageResponse;

@CrossOrigin
@RestController
public class RegistroController {

	@Autowired
	private UsuarioFeignClient client;

	@Autowired
	private UsuarioRegistroRepository registroRepository;

	@Autowired
	private NotificacionesFeignClient notificaciones;

	@Autowired
	PasswordEncoder encoder;

	@GetMapping("/registro/ver/{username}")
	public Boolean verUsuarios(@PathVariable String username) {
		return client.existsByEmail(username);
	}

	@PostMapping("/registro/crearNuevo")
	public ResponseEntity<?> crearNuevo(@RequestBody UsuarioRegistro usuarioRegistro) {
		System.out.println("Prueba: .--->" + usuarioRegistro.getEmail() + "   " + usuarioRegistro.getPhone() + "    "
				+ usuarioRegistro.getUsername());
		if (usuarioRegistro.getUsername() == null || usuarioRegistro.getEmail() == null
				|| usuarioRegistro.getPhone() == null || usuarioRegistro.getPassword() == null) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Llene todos los datos"));
		} else {
			if (usuarioRegistro.getUsername() != "" && usuarioRegistro.getEmail() != ""
					&& usuarioRegistro.getPhone() != "" && usuarioRegistro.getPassword() != "") {
				if (usuarioRegistro.getUsername().contains(" ") || usuarioRegistro.getEmail().contains(" ")) {
					return ResponseEntity.ok(new MessageResponse("Carácter invalido: espacio en blanco"));
				} else {
					if (usuarioRegistro.getPhone().contains(" ")) {
						String phoneRegistro = usuarioRegistro.getPhone();
						phoneRegistro = phoneRegistro.replace(" ", "");
						usuarioRegistro.setPhone(phoneRegistro);
					}
					if (client.existsByUsername(usuarioRegistro.getUsername())) {
						return ResponseEntity.badRequest()
								.body(new MessageResponse("Error: Username is already taken!"));
					} else if (client.existsByPhone(usuarioRegistro.getPhone())) {
						return ResponseEntity.badRequest().body(new MessageResponse("Error: Phone is already taken!"));
					} else if (client.existsByEmail(usuarioRegistro.getEmail())) {
						return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already taken!"));
					} else {
						Integer codigo = (int) (100000 * Math.random() + 99999);
						UsuarioRegistro usuario = new UsuarioRegistro();
						usuario.setUsername(usuarioRegistro.getUsername());
						usuario.setPhone(usuarioRegistro.getPhone());
						usuario.setEmail(usuarioRegistro.getEmail());
						usuario.setPassword(encoder.encode(usuarioRegistro.getPassword()));
						usuario.setCodigo(codigo.toString());
						System.out.println("HERE ----> " + usuarioRegistro.getRoles());
						if (usuarioRegistro.getRoles() == null) {

							usuario.setRoles(new ArrayList<>());
						} else {
							usuario.setRoles(usuarioRegistro.getRoles());
						}

						registroRepository.save(usuario);
						notificaciones.enviarMensajeSuscripciones(usuarioRegistro.getEmail(), codigo);
						return ResponseEntity.ok(new MessageResponse("Usuario creado exitosamente"));
					}
				}

			} else {
				return ResponseEntity.ok(new MessageResponse("Ingrese todos los datos!"));
			}
		}

	}

	@PostMapping("/registro/confirmarSuscripcion/{username}")
	@ResponseStatus(code = HttpStatus.OK)
	public Boolean confirmarSuscripcion(@PathVariable String username, @RequestBody UsuarioRegistro usuario) {
		UsuarioRegistro usuarioRegistro = registroRepository.findByUsername(username);
		Integer codigo1 = Integer.parseInt(usuarioRegistro.getCodigo());
		Integer codigo2 = Integer.parseInt(usuario.getCodigo());
		Integer bandera = codigo1 - codigo2;
		if (bandera == 0) {
			Usuario usuarioRegistrar = new Usuario(usuarioRegistro.getUsername(), usuarioRegistro.getPhone(),
					usuarioRegistro.getEmail(), usuarioRegistro.getPassword(), null, null, null, null, null,
					usuarioRegistro.getRoles(), null, null, null, null, null, null, null, null, null);

			client.crearUsuariosRegistro(usuarioRegistrar);
			System.out.println("......PASO.....");
			String id = usuarioRegistro.getId();
			registroRepository.deleteById(id);
			return true;
		} else {
			return false;
		}
	}
	
	@GetMapping("/registro/contraseña")
	public String contraseña(@RequestParam String contraseña) {
		return encoder.encode(contraseña);
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}