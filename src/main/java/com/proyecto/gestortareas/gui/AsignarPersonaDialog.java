package com.proyecto.gestortareas.gui;

import com.proyecto.gestortareas.dao.PersonDAO;
import com.proyecto.gestortareas.modelo.Person;
import com.proyecto.gestortareas.modelo.Task;
import com.proyecto.gestortareas.servicio.TaskService;

import javax.swing.*;
import java.awt.*;

/** Formulario simple para asignar una tarea existente a una persona. */
public class AsignarPersonaDialog extends JDialog {

    private boolean asignada = false;

    public AsignarPersonaDialog(Frame owner, TaskService taskService, Task tarea) {
        super(owner, "Asignar tarea #" + tarea.getIdTask(), true);
        setLayout(new GridBagLayout());

        JLabel etiquetaTarea = new JLabel("Tarea: " + tarea.getTitle());
        JComboBox<Person> comboPersona = new JComboBox<>();
        try {
            new PersonDAO().listarTodos().forEach(comboPersona::addItem);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "No se pudieron cargar las personas: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 10, 6, 10);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(etiquetaTarea, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        add(new JLabel("Asignar a:"), gbc);
        gbc.gridx = 1;
        add(comboPersona, gbc);

        JButton botonAsignar = new JButton("Asignar");
        botonAsignar.setBackground(new Color(0x3A82C4));
        botonAsignar.setForeground(Color.WHITE);
        JButton botonCancelar = new JButton("Cancelar");

        botonAsignar.addActionListener(e -> {
            Person seleccionada = (Person) comboPersona.getSelectedItem();
            if (seleccionada == null) return;
            try {
                taskService.asignarPersona(tarea.getIdTask(), seleccionada.getIdPerson());
                asignada = true;
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al asignar: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        botonCancelar.addActionListener(e -> dispose());

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBotones.add(botonCancelar);
        panelBotones.add(botonAsignar);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.EAST;
        add(panelBotones, gbc);

        pack();
        setLocationRelativeTo(owner);
    }

    public boolean fueAsignada() {
        return asignada;
    }
}
