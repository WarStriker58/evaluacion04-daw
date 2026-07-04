-- Estructura para el Microservicio de Usuarios (Puerto Físico del Contenedor: 3307)
CREATE DATABASE IF NOT EXISTS db_usuarios;
USE db_usuarios;
CREATE TABLE IF NOT EXISTS usuarios (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    rol VARCHAR(50) NOT NULL
);
INSERT INTO usuarios (nombre, email, rol) VALUES ('Carlos Mendoza', 'carlos@empresa.com', 'Lider Tecnico');
INSERT INTO usuarios (nombre, email, rol) VALUES ('Ana Gomez', 'ana@empresa.com', 'Desarrollador Backend');

-- Estructura para el Microservicio de Proyectos (Puerto Físico del Contenedor: 3308)
CREATE DATABASE IF NOT EXISTS db_proyectos;
USE db_proyectos;
CREATE TABLE IF NOT EXISTS proyectos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion VARCHAR(255),
    estado VARCHAR(50) NOT NULL
);
INSERT INTO proyectos (nombre, descripcion, estado) VALUES ('Migracion Cloud', 'Modernizar core bancario', 'EN_PROCESO');

-- Estructura para el Microservicio de Tareas (Puerto Físico del Contenedor: 3309)
CREATE DATABASE IF NOT EXISTS db_tareas;
USE db_tareas;
CREATE TABLE IF NOT EXISTS tareas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(100) NOT NULL,
    descripcion VARCHAR(255),
    proyecto_id BIGINT,
    usuario_responsable_id BIGINT,
    estado VARCHAR(50) NOT NULL
);
INSERT INTO tareas (titulo, descripcion, proyecto_id, usuario_responsable_id, estado) VALUES ('Diseño de BD', 'Crear scripts de tablas', 1, 1, 'PENDIENTE');
