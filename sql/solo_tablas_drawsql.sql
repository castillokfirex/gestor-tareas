CREATE TABLE type_person (
    id_type_person  INT AUTO_INCREMENT PRIMARY KEY,
    name            VARCHAR(50) NOT NULL UNIQUE
);
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
CREATE TABLE team (
    id_team      INT AUTO_INCREMENT PRIMARY KEY,
    name         VARCHAR(100) NOT NULL,
    description  VARCHAR(255)
);
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
CREATE TABLE status_task (
    id_status_task  INT AUTO_INCREMENT PRIMARY KEY,
    name            VARCHAR(50) NOT NULL UNIQUE
);
CREATE TABLE assessment_task (
    id_assessment_task  INT AUTO_INCREMENT PRIMARY KEY,
    name                VARCHAR(50) NOT NULL UNIQUE,
    weight              INT NOT NULL
);
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
