-- =====================================================================
-- Gestor de Tareas — Modelo Físico (MySQL 8.0)
-- Basado en el modelo E-R: person, type_person, team, team_person,
-- task, status_task, assessment_task
-- =====================================================================

DROP DATABASE IF EXISTS gestor_tareas;
CREATE DATABASE gestor_tareas
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE gestor_tareas;

-- ---------------------------------------------------------------------
-- Catálogo: tipo de persona (rol dentro del sistema)
-- ---------------------------------------------------------------------
CREATE TABLE type_person (
    id_type_person  INT AUTO_INCREMENT PRIMARY KEY,
    name            VARCHAR(50) NOT NULL UNIQUE
);

-- ---------------------------------------------------------------------
-- Persona (integrante del sistema)
-- ---------------------------------------------------------------------
CREATE TABLE person (
    id_person       INT AUTO_INCREMENT PRIMARY KEY,
    name            VARCHAR(100) NOT NULL,
    email           VARCHAR(120) NOT NULL UNIQUE,
    id_type_person  INT NOT NULL,
    CONSTRAINT fk_person_type
        FOREIGN KEY (id_type_person) REFERENCES type_person(id_type_person)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
);

-- ---------------------------------------------------------------------
-- Equipo de trabajo
-- ---------------------------------------------------------------------
CREATE TABLE team (
    id_team      INT AUTO_INCREMENT PRIMARY KEY,
    name         VARCHAR(100) NOT NULL,
    description  VARCHAR(255)
);

-- ---------------------------------------------------------------------
-- Relación muchos a muchos: personas dentro de equipos
-- ---------------------------------------------------------------------
CREATE TABLE team_person (
    id_team_person  INT AUTO_INCREMENT PRIMARY KEY,
    id_team         INT NOT NULL,
    id_person       INT NOT NULL,
    joined_at       DATE NOT NULL DEFAULT (CURRENT_DATE),
    CONSTRAINT fk_teamperson_team
        FOREIGN KEY (id_team) REFERENCES team(id_team)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT fk_teamperson_person
        FOREIGN KEY (id_person) REFERENCES person(id_person)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT uq_team_person UNIQUE (id_team, id_person)
);

-- ---------------------------------------------------------------------
-- Catálogo: estado de una tarea (Por hacer / En proceso / Finalizado)
-- ---------------------------------------------------------------------
CREATE TABLE status_task (
    id_status_task  INT AUTO_INCREMENT PRIMARY KEY,
    name            VARCHAR(50) NOT NULL UNIQUE
);

-- ---------------------------------------------------------------------
-- Catálogo: nivel de prioridad de una tarea (ALTA / MEDIA / BAJA)
-- ---------------------------------------------------------------------
CREATE TABLE assessment_task (
    id_assessment_task  INT AUTO_INCREMENT PRIMARY KEY,
    name                VARCHAR(50) NOT NULL UNIQUE,
    weight              INT NOT NULL
);

-- ---------------------------------------------------------------------
-- Tarea
-- ---------------------------------------------------------------------
CREATE TABLE task (
    id_task             INT AUTO_INCREMENT PRIMARY KEY,
    title                VARCHAR(150) NOT NULL,
    description          VARCHAR(500),
    created_at           DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    due_date             DATE,
    id_team              INT NOT NULL,
    id_person            INT,
    id_status_task       INT NOT NULL,
    id_assessment_task   INT NOT NULL,
    CONSTRAINT fk_task_team
        FOREIGN KEY (id_team) REFERENCES team(id_team)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,
    CONSTRAINT fk_task_person
        FOREIGN KEY (id_person) REFERENCES person(id_person)
        ON UPDATE CASCADE
        ON DELETE SET NULL,
    CONSTRAINT fk_task_status
        FOREIGN KEY (id_status_task) REFERENCES status_task(id_status_task)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,
    CONSTRAINT fk_task_assessment
        FOREIGN KEY (id_assessment_task) REFERENCES assessment_task(id_assessment_task)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
);

-- =====================================================================
-- Datos base (catálogos) — necesarios para que el sistema funcione
-- =====================================================================

INSERT INTO type_person (name) VALUES
    ('Administrador'),
    ('Colaborador');

INSERT INTO status_task (name) VALUES
    ('Por hacer'),
    ('En proceso'),
    ('Finalizado');

INSERT INTO assessment_task (name, weight) VALUES
    ('ALTA', 3),
    ('MEDIA', 2),
    ('BAJA', 1);

-- =====================================================================
-- Datos de ejemplo (para probar el modelo)
-- =====================================================================

INSERT INTO team (name, description) VALUES
    ('Equipo Backend', 'Encargado de la logica del servidor');

INSERT INTO person (name, email, id_type_person) VALUES
    ('Ana Torres', 'ana.torres@correo.com', 2),
    ('Carlos Ruiz', 'carlos.ruiz@correo.com', 1);

INSERT INTO team_person (id_team, id_person) VALUES
    (1, 1),
    (1, 2);

INSERT INTO task (title, description, id_team, id_person, id_status_task, id_assessment_task) VALUES
    ('Configurar base de datos', 'Preparar el esquema inicial', 1, 2, 1, 1),
    ('Escribir documentacion README', 'Instrucciones de instalacion y uso', 1, 1, 1, 2);
