package com.smartjob.ejercicio.controller;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartjob.ejercicio.dao.TelefonoDao;
import com.smartjob.ejercicio.dao.UsuarioDao;
import com.smartjob.ejercicio.model.Usuario;
import com.smartjob.ejercicio.security.SecurityTestConfig;
import com.smartjob.ejercicio.service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;


@WebMvcTest(UsuarioController.class)
@Import(SecurityTestConfig.class)
public class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService usuarioService;

    @InjectMocks
    private UsuarioController usuarioController;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @WithMockUser
    public void testCrearUsuarioSuccess() throws Exception {
        UsuarioDao usuarioDao = new UsuarioDao();
        TelefonoDao telefonoDao = new TelefonoDao();
        telefonoDao.setCitycode("1");
        telefonoDao.setContrycode("57");
        telefonoDao.setNumber("1234567");
        usuarioDao.setPhones(List.of(telefonoDao));

        usuarioDao.setEmail("juan.rodriguez@correo.com");
        usuarioDao.setName("Juan Rodriguez");
        usuarioDao.setPassword("Hunter2025");
        
        Usuario usuario = new Usuario();
        usuario.setId(UUID.randomUUID());
        usuario.setName("Juan Rodriguez");
        usuario.setEmail("juan.rodriguez@correo.com");
        usuario.setIsactive(true);

        when(usuarioService.crearUsuario(usuarioDao)).thenReturn(usuario);

        mockMvc.perform(post("/api/usuarios/crear")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuarioDao)))
                .andExpect(status().isCreated());
               
    }

    @Test
    @WithMockUser
    public void testCrearUsuarioFailure() throws Exception {
        UsuarioDao usuarioDao = null;
        

        when(usuarioService.crearUsuario(usuarioDao)).thenThrow(new RuntimeException("Debe ingresar datos de usuario"));

        mockMvc.perform(post("/api/usuarios/crear")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuarioDao)))
                .andExpect(status().isBadRequest());
              
    }
}