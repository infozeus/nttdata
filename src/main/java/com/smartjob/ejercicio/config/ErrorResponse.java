package com.smartjob.ejercicio.config;

import io.swagger.v3.oas.annotations.media.Schema;


@Schema(description = "Estructura de los mensajes de error")
public class ErrorResponse {

    @Schema(description = "Mensaje de error", example = "{\"mensaje\": \"El correo ya registrado\"}")
    private String mensaje;

    public ErrorResponse(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

}
