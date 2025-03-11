package com.smartjob.ejercicio.dao;

import java.util.List;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDao {
    @NotBlank
    private String name;
   
    private String email;
   
    private String password;

    private List<TelefonoDao> phones;

}
