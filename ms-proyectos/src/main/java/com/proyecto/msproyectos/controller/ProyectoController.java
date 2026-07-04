package com.proyecto.msproyectos.controller;

import com.proyecto.msproyectos.entity.Proyecto;
import com.proyecto.msproyectos.repository.ProyectoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/proyectos")
public class ProyectoController {

    private static final Logger log = LoggerFactory.getLogger(ProyectoController.class);
    private final ProyectoRepository repository;

    @Value("${custom.message:Mensaje por defecto}")
    private String customMessage;

    public ProyectoController(ProyectoRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/mensaje")
    public ResponseEntity<String> obtenerMensaje() {
        log.info("Obteniendo mensaje personalizado de Proyectos desde Config Server");
        return ResponseEntity.ok(customMessage);
    }

    @GetMapping
    public List<Proyecto> listarTodos() {
        log.info("Operación principal: Listando todos los proyectos");
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Proyecto> buscarPorId(@PathVariable Long id) {
        log.info("Operación principal: Buscando proyecto con ID {}", id);
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Proyecto crear(@RequestBody Proyecto proyecto) {
        log.info("Operación principal: Creando nuevo proyecto {}", proyecto.getNombre());
        return repository.save(proyecto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Proyecto> actualizar(@PathVariable Long id, @RequestBody Proyecto proyectoDetalles) {
        log.info("Operación principal: Actualizando proyecto con ID {}", id);
        return repository.findById(id)
                .map(proyecto -> {
                    proyecto.setNombre(proyectoDetalles.getNombre());
                    proyecto.setDescripcion(proyectoDetalles.getDescripcion());
                    proyecto.setEstado(proyectoDetalles.getEstado());
                    return ResponseEntity.ok(repository.save(proyecto));
                }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.info("Operación principal: Eliminando proyecto con ID {}", id);
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}