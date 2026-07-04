package com.proyecto.mstareas.controller;

import com.proyecto.mstareas.entity.Tarea;
import com.proyecto.mstareas.repository.TareaRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@RestController
@RequestMapping("/api/tareas")
public class TareaController {

    private static final Logger log = LoggerFactory.getLogger(TareaController.class);
    private final TareaRepository repository;
    private final WebClient.Builder webClientBuilder;

    @Value("${custom.message:Mensaje por defecto}")
    private String customMessage;

    public TareaController(TareaRepository repository, WebClient.Builder webClientBuilder) {
        this.repository = repository;
        this.webClientBuilder = webClientBuilder;
    }

    @GetMapping("/mensaje")
    public ResponseEntity<String> obtenerMensaje() {
        log.info("Obteniendo mensaje de Tareas desde Config Server");
        return ResponseEntity.ok(customMessage);
    }

    @GetMapping
    public List<Tarea> listarTodas() {
        log.info("Operación principal: Listando todas las tareas");
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tarea> buscarPorId(@PathVariable Long id) {
        log.info("Operación principal: Buscando tarea con ID {}", id);
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Tarea crear(@RequestBody Tarea tarea) {
        log.info("Operación principal: Creando nueva tarea");
        return repository.save(tarea);
    }

    // Endpoint de integración que llama a ms-usuarios usando Circuit Breaker y Retry
    @GetMapping("/{id}/validar-responsable")
    @CircuitBreaker(name = "usuariosCB", fallbackMethod = "fallbackValidarResponsable")
    @Retry(name = "usuariosRetry")
    public ResponseEntity<String> validarResponsableDeTarea(@PathVariable Long id) {
        log.info("Intentando consultar ms-usuarios vía WebClient balanceado...");

        Tarea tarea = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarea no encontrada"));

        // Consumimos ms-usuarios usando el nombre registrado en Eureka
        String usuarioJson = webClientBuilder.build()
                .get()
                .uri("http://ms-usuarios/api/usuarios/" + tarea.getUsuarioResponsableId())
                .retrieve()
                .bodyToMono(String.class)
                .block();

        return ResponseEntity.ok("Responsable verificado correctamente. Datos: " + usuarioJson);
    }

    // Método Fallback con el texto exacto exigido en la rúbrica del caso
    public ResponseEntity<String> fallbackValidarResponsable(Long id, Throwable t) {
        log.error("Circuit Breaker activado debido a fallo en ms-usuarios. Razón: {}", t.getMessage());
        return ResponseEntity.status(503).body("No fue posible consultar el servicio. Intente nuevamente.");
    }

    @PutMapping("/{id}")
    public ResponseEntity<Tarea> actualizar(@PathVariable Long id, @RequestBody Tarea tareaDetalles) {
        log.info("Operación principal: Actualizando tarea con ID {}", id);
        return repository.findById(id)
                .map(tarea -> {
                    tarea.setTitulo(tareaDetalles.getTitulo());
                    tarea.setDescripcion(tareaDetalles.getDescripcion());
                    tarea.setProyectoId(tareaDetalles.getProyectoId());
                    tarea.setUsuarioResponsableId(tareaDetalles.getUsuarioResponsableId());
                    tarea.setEstado(tareaDetalles.getEstado());
                    return ResponseEntity.ok(repository.save(tarea));
                }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.info("Operación principal: Eliminando tarea con ID {}", id);
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}