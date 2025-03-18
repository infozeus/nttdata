
package com.smartjob.ejercicio.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
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
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        setPrivateField(usuarioService, "jwtSecret", "mysecrettokenmysecrettokenmysecrettoken"); // Debe ser >= 32 bytes 
        usuarioService.passwordRegexConfig = "^(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d]{8,}$";
        usuarioService.init();
    }

    private void setPrivateField(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
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
        usuarioDao.setName("Test User");
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
        usuarioDao.setName("Test User");
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
        usuarioDao.setName("Test User");
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
        usuarioDao.setPassword("Hunter2024");
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

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        assertTrue(passwordEncoder.matches("Hunter2024", result.getPassword()));

        assertEquals(1, result.getPhones().size());
    }

    @Test
    public void testCrearUsuario_Failure_NombreVacio() {
        UsuarioDao usuarioDao = new UsuarioDao();
        usuarioDao.setName(""); // Nombre vacío
        usuarioDao.setEmail("test@example.com");
        usuarioDao.setPassword("Hunter2024");
        usuarioDao.setPhones(Collections.singletonList(new TelefonoDao()));

        when(usuarioRepository.findByEmail("test@example.com")).thenReturn(null);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            usuarioService.crearUsuario(usuarioDao);
        });

        assertEquals("El nombre no puede estar vacío", exception.getMessage());
    }
}