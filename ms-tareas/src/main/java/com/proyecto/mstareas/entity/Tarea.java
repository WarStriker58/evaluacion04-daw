package com.proyecto.mstareas.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tareas")
public class Tarea {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titulo;
    private String descripcion;
    private Long proyectoId;
    private Long usuarioResponsableId; // ID referencial del microservicio ms-usuarios
    private String estado;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public Long getProyectoId() { return proyectoId; }
    public void setProyectoId(Long proyectoId) { this.proyectoId = proyectoId; }
    public Long getUsuarioResponsableId() { return usuarioResponsableId; }
    public void setUsuarioResponsableId(Long usuarioResponsableId) { this.usuarioResponsableId = usuarioResponsableId; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}