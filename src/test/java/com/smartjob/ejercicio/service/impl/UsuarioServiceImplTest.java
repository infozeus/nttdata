
package com.smartjob.ejercicio.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import java.util.Collections;

import com.smartjob.ejercicio.dao.TelefonoDao;
import com.smartjob.ejercicio.dao.UsuarioDao;
import com.smartjob.ejercicio.model.Usuario;
import com.smartjob.ejercicio.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


public class UsuarioServiceImplTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioServiceImpl usuarioService;

   @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        usuarioService.passwordRegexConfig = "^(?=.*[A-Z])(?=.*\\\\d)[A-Za-z\\\\d]{8,}$";
        usuarioService.init();
    }

    @Test
    public void testCrearUsuario_NullUsuario() {
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            usuarioService.crearUsuario(null);
        });
        assertEquals("Debe ingresar datos de usuario", exception.getMessage());
    }

    @Test
    public void testCrearUsuario_EmptyPhones() {
        UsuarioDao usuarioDao = new UsuarioDao();
        usuarioDao.setPhones(Collections.emptyList());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            usuarioService.crearUsuario(usuarioDao);
        });
        assertEquals("Debe ingresar datos de usuario", exception.getMessage());
    }

    @Test
    public void testCrearUsuario_EmailAlreadyRegistered() {
        UsuarioDao usuarioDao = new UsuarioDao();
        usuarioDao.setEmail("test@example.com");
        TelefonoDao telefonoDao = new TelefonoDao();
        usuarioDao.setPhones(Collections.singletonList(telefonoDao));

        when(usuarioRepository.findByEmail("test@example.com")).thenReturn(new Usuario());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            usuarioService.crearUsuario(usuarioDao);
        });
        assertEquals("El correo ya está registrado", exception.getMessage());
    }

    @Test
    public void testCrearUsuario_InvalidEmailFormat() {
        UsuarioDao usuarioDao = new UsuarioDao();
        usuarioDao.setEmail("invalid-email");
        usuarioDao.setPhones(Collections.singletonList(new TelefonoDao()));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            usuarioService.crearUsuario(usuarioDao);
        });
        assertEquals("Formato de correo inválido", exception.getMessage());
    }

    @Test
    public void testCrearUsuario_InvalidPasswordFormat() {
        UsuarioDao usuarioDao = new UsuarioDao();
        usuarioDao.setEmail("test@example.com");
        usuarioDao.setPassword("123");
        usuarioDao.setPhones(Collections.singletonList(new TelefonoDao()));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            usuarioService.crearUsuario(usuarioDao);
        });
        assertEquals("Formato de contraseña inválido", exception.getMessage());
    }

    @Test
    public void testCrearUsuario_Success() {
        UsuarioDao usuarioDao = new UsuarioDao();
        usuarioDao.setName("Test User");
        usuarioDao.setEmail("test@example.com");
        usuarioDao.setPassword("Secure99");
        usuarioDao.setPhones(Collections.singletonList(new TelefonoDao()));

        when(usuarioRepository.findByEmail("test@example.com")).thenReturn(null);
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Usuario result = usuarioService.crearUsuario(usuarioDao);

        assertNotNull(result);
        assertEquals("Test User", result.getName());
        assertEquals("test@example.com", result.getEmail());
        assertNotNull(result.getCreated());
        assertNotNull(result.getModified());
        assertNotNull(result.getLast_login());
        assertNotNull(result.getToken());
        assertTrue(result.isIsactive());
        assertTrue(new BCryptPasswordEncoder().matches("Secure99", result.getPassword()));
        assertEquals(1, result.getPhones().size());
    }
}