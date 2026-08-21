package com.proyecto.gestortareas.gui;

import com.proyecto.gestortareas.dao.PersonDAO;
import com.proyecto.gestortareas.modelo.Person;
import com.proyecto.gestortareas.modelo.Task;
import com.proyecto.gestortareas.servicio.TaskService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

/**
 * Ventana principal del Gestor de Tareas. Minimalista a proposito:
 * un encabezado, una tabla con las tareas, y una barra de acciones.
 */
public class GestorTareasFrame extends JFrame {

    private static final Color COLOR_FONDO = new Color(0xF4F5F7);
    private static final Color COLOR_ENCABEZADO = new Color(0x1F2937);
    private static final Color COLOR_ACENTO = new Color(0x3A82C4);

    private final TaskService taskService = new TaskService();
    private final TareaTableModel modeloTabla = new TareaTableModel(List.of());
    private final JTable tabla = new JTable(modeloTabla);
    private final JLabel etiquetaColaRevision = new JLabel();

    public GestorTareasFrame() {
        super("Gestor de Tareas");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(880, 520);
        setMinimumSize(new Dimension(700, 420));
        getContentPane().setBackground(COLOR_FONDO);
        setLayout(new BorderLayout());

        add(construirEncabezado(), BorderLayout.NORTH);
        add(construirPanelTabla(), BorderLayout.CENTER);
        add(construirBarraAcciones(), BorderLayout.SOUTH);

        cargarTareas();
        setLocationRelativeTo(null);
    }

    // -----------------------------------------------------------------
    // Encabezado
    // -----------------------------------------------------------------

    private JPanel construirEncabezado() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(COLOR_ENCABEZADO);
        panel.setBorder(new EmptyBorder(16, 20, 16, 20));

        JLabel titulo = new JLabel("Gestor de Tareas");
        titulo.setForeground(Color.WHITE);
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 20f));

        JButton botonActualizar = new JButton("Actualizar");
        estilizarBotonSecundario(botonActualizar);
        botonActualizar.addActionListener(e -> cargarTareas());

        panel.add(titulo, BorderLayout.WEST);
        panel.add(botonActualizar, BorderLayout.EAST);
        return panel;
    }

    // -----------------------------------------------------------------
    // Tabla
    // -----------------------------------------------------------------

    private JPanel construirPanelTabla() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(COLOR_FONDO);
        panel.setBorder(new EmptyBorder(16, 20, 8, 20));

        tabla.setRowHeight(28);
        tabla.setFont(tabla.getFont().deriveFont(13f));
        tabla.getTableHeader().setFont(tabla.getFont().deriveFont(Font.BOLD, 13f));
        tabla.setSelectionBackground(new Color(0xDCEBFA));
        tabla.getColumnModel().getColumn(2).setCellRenderer(new PrioridadCellRenderer());
        tabla.setFillsViewportHeight(true);

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(0xD8DCE1)));
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    // -----------------------------------------------------------------
    // Barra de acciones
    // -----------------------------------------------------------------

    private JPanel construirBarraAcciones() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(COLOR_FONDO);
        panel.setBorder(new EmptyBorder(4, 20, 16, 20));

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        panelBotones.setOpaque(false);

        JButton botonNueva = new JButton("+ Nueva tarea");
        estilizarBotonPrincipal(botonNueva);
        botonNueva.addActionListener(e -> abrirNuevaTarea());

        JButton botonAsignar = new JButton("Asignar");
        estilizarBotonSecundario(botonAsignar);
        botonAsignar.addActionListener(e -> abrirAsignar());

        JButton botonEstado = new JButton("Cambiar estado");
        estilizarBotonSecundario(botonEstado);
        botonEstado.addActionListener(e -> abrirCambiarEstado());

        JButton botonCola = new JButton("Revisar cola");
        estilizarBotonSecundario(botonCola);
        botonCola.addActionListener(e -> revisarColaSiguiente());

        panelBotones.add(botonNueva);
        panelBotones.add(botonAsignar);
        panelBotones.add(botonEstado);
        panelBotones.add(botonCola);

        etiquetaColaRevision.setForeground(new Color(0x6B7280));
        actualizarEtiquetaCola();

        panel.add(panelBotones, BorderLayout.WEST);
        panel.add(etiquetaColaRevision, BorderLayout.EAST);
        return panel;
    }

    // -----------------------------------------------------------------
    // Acciones
    // -----------------------------------------------------------------

    private void cargarTareas() {
        try {
            List<Task> tareas = taskService.listarTodas();
            modeloTabla.actualizar(tareas);
        } catch (Exception e) {
            mostrarError("No se pudieron cargar las tareas", e);
        }
    }

    private void abrirNuevaTarea() {
        NuevaTareaDialog dialogo = new NuevaTareaDialog(this, taskService);
        dialogo.setVisible(true);
        if (dialogo.fueCreada()) {
            cargarTareas();
            actualizarEtiquetaCola();
        }
    }

    private void abrirAsignar() {
        Task seleccionada = obtenerTareaSeleccionada();
        if (seleccionada == null) return;
        AsignarPersonaDialog dialogo = new AsignarPersonaDialog(this, taskService, seleccionada);
        dialogo.setVisible(true);
        if (dialogo.fueAsignada()) cargarTareas();
    }

    private void abrirCambiarEstado() {
        Task seleccionada = obtenerTareaSeleccionada();
        if (seleccionada == null) return;
        CambiarEstadoDialog dialogo = new CambiarEstadoDialog(this, taskService, seleccionada);
        dialogo.setVisible(true);
        if (dialogo.fueActualizada()) cargarTareas();
    }

    private void revisarColaSiguiente() {
        Task siguiente = taskService.siguienteEnRevision();
        if (siguiente == null) {
            JOptionPane.showMessageDialog(this, "No hay tareas pendientes de revision.",
                    "Cola de revision", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Revisando: " + siguiente.getTitle(),
                    "Cola de revision", JOptionPane.INFORMATION_MESSAGE);
        }
        actualizarEtiquetaCola();
    }

    private void actualizarEtiquetaCola() {
        etiquetaColaRevision.setText("Tareas en cola de revision: " + taskService.tamanoColaRevision());
    }

    private Task obtenerTareaSeleccionada() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona primero una tarea en la tabla.",
                    "Nada seleccionado", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        return modeloTabla.getTareaEn(fila);
    }

    private void mostrarError(String mensaje, Exception e) {
        JOptionPane.showMessageDialog(this, mensaje + ":\n" + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
    }

    // -----------------------------------------------------------------
    // Estilo de botones (Swing puro, sin librerias externas)
    // -----------------------------------------------------------------

    private void estilizarBotonPrincipal(JButton boton) {
        boton.setBackground(COLOR_ACENTO);
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setBorder(new EmptyBorder(8, 14, 8, 14));
    }

    private void estilizarBotonSecundario(JButton boton) {
        boton.setBackground(Color.WHITE);
        boton.setForeground(new Color(0x1F2937));
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0xD8DCE1)),
                new EmptyBorder(7, 12, 7, 12)));
    }
}
