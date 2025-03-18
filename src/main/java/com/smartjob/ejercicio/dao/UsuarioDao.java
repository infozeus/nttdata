package com.smartjob.ejercicio.dao;

import java.util.List;

import jakarta.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDao {
    @NotBlank(message = "El nombre no puede estar vacío")
    private String name;
   
    @NotBlank(message = "El correo no puede estar vacío")
    private String email;
   
    @NotBlank(message = "La contraseña no puede estar vacía")
    private String password;

    private List<TelefonoDao> phones;

}
