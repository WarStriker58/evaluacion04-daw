package com.proyecto.msusuarios.controller;

import com.proyecto.msusuarios.entity.Usuario;
import com.proyecto.msusuarios.repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private static final Logger log = LoggerFactory.getLogger(UsuarioController.class);
    private final UsuarioRepository repository;

    @Value("${custom.message:Mensaje por defecto}")
    private String customMessage;

    public UsuarioController(UsuarioRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/mensaje")
    public ResponseEntity<String> obtenerMensaje() {
        log.info("Obteniendo mensaje personalizado desde Config Server");
        return ResponseEntity.ok(customMessage);
    }

    @GetMapping
    public List<Usuario> listarTodos() {
        log.info("Operación principal: Listando todos los usuarios");
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscarPorId(@PathVariable Long id) {
        log.info("Operación principal: Buscando usuario con ID {}", id);
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Usuario crear(@RequestBody Usuario usuario) {
        log.info("Operación principal: Creando nuevo usuario con nombre {}", usuario.getNombre());
        return repository.save(usuario);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> actualizar(@PathVariable Long id, @RequestBody Usuario usuarioDetalles) {
        log.info("Operación principal: Actualizando usuario con ID {}", id);
        return repository.findById(id)
                .map(usuario -> {
                    usuario.setNombre(usuarioDetalles.getNombre());
                    usuario.setEmail(usuarioDetalles.getEmail());
                    usuario.setRol(usuarioDetalles.getRol());
                    return ResponseEntity.ok(repository.save(usuario));
                }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.info("Operación principal: Eliminando usuario con ID {}", id);
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}