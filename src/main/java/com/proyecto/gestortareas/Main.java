package com.proyecto.gestortareas;

import com.proyecto.gestortareas.dao.*;
import com.proyecto.gestortareas.modelo.*;
import com.proyecto.gestortareas.servicio.TaskService;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;

/**
 * Menu de consola conectado a MySQL (version de transicion, mientras se
 * construye la interfaz grafica). Usa el mismo TaskService que ya probamos
 * con la base de datos real.
 */
public class Main {

    private static final Scanner sc = new Scanner(System.in, StandardCharsets.UTF_8);
    private static final TaskService taskService = new TaskService();
    private static final TeamDAO teamDAO = new TeamDAO();
    private static final PersonDAO personDAO = new PersonDAO();
    private static final TypePersonDAO typePersonDAO = new TypePersonDAO();
    private static final StatusTaskDAO statusTaskDAO = new StatusTaskDAO();
    private static final AssessmentTaskDAO assessmentTaskDAO = new AssessmentTaskDAO();

    public static void main(String[] args) throws Exception {
        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));
        System.out.println("=======================================");
        System.out.println("  GESTOR DE TAREAS - CONECTADO A MYSQL");
        System.out.println("=======================================");

        boolean salir = false;
        while (!salir) {
            System.out.println();
            System.out.println("1. Ver todas las tareas");
            System.out.println("2. Crear tarea");
            System.out.println("3. Asignar tarea a una persona");
            System.out.println("4. Cambiar estado de una tarea");
            System.out.println("5. Ver tareas de una persona (por prioridad)");
            System.out.println("6. Ver cola de revision (siguiente tarea nueva)");
            System.out.println("7. Salir");
            int opcion = leerEntero("Elige una opcion: ");
            switch (opcion) {
                case 1 -> taskService.listarTodas().forEach(System.out::println);
                case 2 -> crearTarea();
                case 3 -> asignarTarea();
                case 4 -> cambiarEstado();
                case 5 -> verPorPersona();
                case 6 -> verColaRevision();
                case 7 -> salir = true;
                default -> System.out.println(">> Opcion invalida.");
            }
        }
        System.out.println("Hasta luego!");
    }

    private static void crearTarea() throws Exception {
        String titulo = leerTexto("Titulo: ");
        String descripcion = leerTexto("Descripcion: ");

        System.out.println("Equipos disponibles:");
        List<Team> equipos = teamDAO.listarTodos();
        equipos.forEach(e -> System.out.println("  " + e.getIdTeam() + " - " + e.getName()));
        int idTeam = leerEntero("ID del equipo: ");

        System.out.println("Prioridades disponibles:");
        List<AssessmentTask> prioridades = assessmentTaskDAO.listarTodos();
        prioridades.forEach(p -> System.out.println("  " + p.getIdAssessmentTask() + " - " + p.getName()));
        int idPrioridad = leerEntero("ID de la prioridad: ");

        // Toda tarea nueva empieza en "Por hacer" (normalmente id 1, segun el script)
        Task creada = taskService.crearTarea(titulo, descripcion, idTeam, 1, idPrioridad);
        System.out.println(">> Tarea creada: " + creada);
    }

    private static void asignarTarea() throws Exception {
        int idTarea = leerEntero("ID de la tarea: ");
        System.out.println("Personas disponibles:");
        personDAO.listarTodos().forEach(p -> System.out.println("  " + p.getIdPerson() + " - " + p.getName()));
        int idPersona = leerEntero("ID de la persona: ");
        taskService.asignarPersona(idTarea, idPersona);
        System.out.println(">> Tarea asignada.");
    }

    private static void cambiarEstado() throws Exception {
        int idTarea = leerEntero("ID de la tarea: ");
        System.out.println("Estados disponibles:");
        statusTaskDAO.listarTodos().forEach(s -> System.out.println("  " + s.getIdStatusTask() + " - " + s.getName()));
        int idEstado = leerEntero("ID del nuevo estado: ");
        taskService.cambiarEstado(idTarea, idEstado);
        System.out.println(">> Estado actualizado.");
    }

    private static void verPorPersona() throws Exception {
        System.out.println("Personas disponibles:");
        personDAO.listarTodos().forEach(p -> System.out.println("  " + p.getIdPerson() + " - " + p.getName()));
        int idPersona = leerEntero("ID de la persona: ");
        taskService.listarPorPersona(idPersona).forEach(System.out::println);
    }

    private static void verColaRevision() {
        Task siguiente = taskService.siguienteEnRevision();
        if (siguiente == null) {
            System.out.println(">> La cola de revision esta vacia.");
        } else {
            System.out.println(">> Revisando: " + siguiente);
        }
        System.out.println(">> Tareas restantes en la cola: " + taskService.tamanoColaRevision());
    }

    private static String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return sc.nextLine().trim();
    }

    private static int leerEntero(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println(">> Escribe un numero valido.");
            }
        }
    }
}
