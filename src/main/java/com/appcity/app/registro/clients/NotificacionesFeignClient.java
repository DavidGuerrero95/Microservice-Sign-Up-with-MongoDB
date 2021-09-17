package com.appcity.app.registro.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "app-notificaciones")
public interface NotificacionesFeignClient {

	@PostMapping("/notificaciones/registro")
	public void enviarMensajeSuscripciones(@RequestParam String correo, @RequestParam Integer codigo);

}
