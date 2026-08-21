package com.proyecto.gestortareas.gui;

import com.proyecto.gestortareas.dao.AssessmentTaskDAO;
import com.proyecto.gestortareas.dao.TeamDAO;
import com.proyecto.gestortareas.modelo.AssessmentTask;
import com.proyecto.gestortareas.modelo.Team;
import com.proyecto.gestortareas.servicio.TaskService;

import javax.swing.*;
import java.awt.*;

/**
 * Formulario simple para crear una tarea nueva: titulo, descripcion,
 * equipo y prioridad.
 */
public class NuevaTareaDialog extends JDialog {

    private boolean creada = false;

    public NuevaTareaDialog(Frame owner, TaskService taskService) {
        super(owner, "Nueva tarea", true);
        setLayout(new GridBagLayout());
        setResizable(false);

        JTextField campoTitulo = new JTextField(22);
        JTextArea campoDescripcion = new JTextArea(3, 22);
        campoDescripcion.setLineWrap(true);
        JComboBox<Team> comboEquipo = new JComboBox<>();
        JComboBox<AssessmentTask> comboPrioridad = new JComboBox<>();

        try {
            new TeamDAO().listarTodos().forEach(comboEquipo::addItem);
            new AssessmentTaskDAO().listarTodos().forEach(comboPrioridad::addItem);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "No se pudo cargar equipos/prioridades: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 10, 6, 10);
        gbc.anchor = GridBagConstraints.WEST;

        agregarFila(gbc, 0, "Titulo:", campoTitulo);
        agregarFila(gbc, 1, "Descripcion:", new JScrollPane(campoDescripcion));
        agregarFila(gbc, 2, "Equipo:", comboEquipo);
        agregarFila(gbc, 3, "Prioridad:", comboPrioridad);

        JButton botonCrear = new JButton("Crear tarea");
        JButton botonCancelar = new JButton("Cancelar");
        botonCrear.setBackground(new Color(0x3A82C4));
        botonCrear.setForeground(Color.WHITE);

        botonCrear.addActionListener(e -> {
            String titulo = campoTitulo.getText().trim();
            if (titulo.isEmpty() || comboEquipo.getSelectedItem() == null || comboPrioridad.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(this, "Completa el titulo, el equipo y la prioridad.",
                        "Datos incompletos", JOptionPane.WARNING_MESSAGE);
                return;
            }
            try {
                Team equipo = (Team) comboEquipo.getSelectedItem();
                AssessmentTask prioridad = (AssessmentTask) comboPrioridad.getSelectedItem();
                taskService.crearTarea(titulo, campoDescripcion.getText().trim(),
                        equipo.getIdTeam(), 1, prioridad.getIdAssessmentTask());
                creada = true;
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al crear la tarea: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        botonCancelar.addActionListener(e -> dispose());

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBotones.add(botonCancelar);
        panelBotones.add(botonCrear);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.EAST;
        add(panelBotones, gbc);

        pack();
        setLocationRelativeTo(owner);
    }

    private void agregarFila(GridBagConstraints gbc, int fila, String etiqueta, Component campo) {
        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.gridwidth = 1;
        add(new JLabel(etiqueta), gbc);
        gbc.gridx = 1;
        add(campo, gbc);
    }

    public boolean fueCreada() {
        return creada;
    }
}
