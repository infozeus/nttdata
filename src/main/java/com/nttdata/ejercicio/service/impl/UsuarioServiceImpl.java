package com.nttdata.ejercicio.service.impl;

import com.nttdata.ejercicio.repository.UsuarioRepository;
import com.nttdata.ejercicio.service.UsuarioService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.nttdata.ejercicio.dao.UsuarioDao;
import com.nttdata.ejercicio.exception.EmailDuplicadoException;
import com.nttdata.ejercicio.model.Telefono;
import com.nttdata.ejercicio.model.Usuario;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${password.regex}")
    public String passwordRegexConfig;
    
    private final Pattern emailRegex = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private Pattern passwordRegex;

    @PostConstruct
    public void init() {
        this.passwordRegex = Pattern.compile(passwordRegexConfig);
    }

    public Usuario crearUsuario(UsuarioDao usuario) {
        if (usuario == null || usuario.getPhones() == null || usuario.getPhones().isEmpty()) {
            throw new RuntimeException("Debe ingresar datos de usuario");
        }

        if (usuario.getName() == null || usuario.getName().trim().isEmpty()) {
            throw new RuntimeException("El nombre no puede estar vacío");
        }

        if (usuarioRepository.findByEmail(usuario.getEmail()) != null) {
            throw new EmailDuplicadoException("El correo ya está registrado");
        }

        if (!emailRegex.matcher(usuario.getEmail()).matches()) {
            throw new RuntimeException("Formato de correo inválido");
        }

        if (!passwordRegex.matcher(usuario.getPassword()).matches()) {
            throw new RuntimeException("Formato de contraseña inválido");
        }
        
        Usuario user = new Usuario();

        user.setName(usuario.getName());
        user.setEmail(usuario.getEmail());
        user.setCreated(LocalDateTime.now());
        user.setModified(LocalDateTime.now());
        user.setLast_login(LocalDateTime.now());
        user.setToken(generarTokenJWT(usuario.getEmail()));
        user.setIsactive(true);
        user.setPassword(new BCryptPasswordEncoder().encode(usuario.getPassword()));
        user.setPhones(usuario.getPhones().stream()
                .map(telefonoDao -> {
                    Telefono telefono = new Telefono();
                    telefono.setNumber(telefonoDao.getNumber());
                    telefono.setCitycode(telefonoDao.getCitycode());
                    telefono.setContrycode(telefonoDao.getContrycode());
                    return telefono;
                })
                .collect(Collectors.toList()));

        return usuarioRepository.save(user);
    }

    private String generarTokenJWT(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 24 horas
                .signWith(Keys.hmacShaKeyFor(jwtSecret.getBytes()), SignatureAlgorithm.HS256)
                .compact();
    }

}
