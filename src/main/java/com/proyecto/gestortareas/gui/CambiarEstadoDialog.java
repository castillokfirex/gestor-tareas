package com.proyecto.gestortareas.gui;

import com.proyecto.gestortareas.dao.StatusTaskDAO;
import com.proyecto.gestortareas.modelo.StatusTask;
import com.proyecto.gestortareas.modelo.Task;
import com.proyecto.gestortareas.servicio.TaskService;

import javax.swing.*;
import java.awt.*;

/** Formulario simple para cambiar el estado de una tarea existente. */
public class CambiarEstadoDialog extends JDialog {

    private boolean actualizada = false;

    public CambiarEstadoDialog(Frame owner, TaskService taskService, Task tarea) {
        super(owner, "Cambiar estado - tarea #" + tarea.getIdTask(), true);
        setLayout(new GridBagLayout());

        JLabel etiquetaTarea = new JLabel("Tarea: " + tarea.getTitle());
        JComboBox<StatusTask> comboEstado = new JComboBox<>();
        try {
            new StatusTaskDAO().listarTodos().forEach(comboEstado::addItem);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "No se pudieron cargar los estados: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 10, 6, 10);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(etiquetaTarea, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        add(new JLabel("Nuevo estado:"), gbc);
        gbc.gridx = 1;
        add(comboEstado, gbc);

        JButton botonGuardar = new JButton("Guardar");
        botonGuardar.setBackground(new Color(0x3A82C4));
        botonGuardar.setForeground(Color.WHITE);
        JButton botonCancelar = new JButton("Cancelar");

        botonGuardar.addActionListener(e -> {
            StatusTask seleccionado = (StatusTask) comboEstado.getSelectedItem();
            if (seleccionado == null) return;
            try {
                taskService.cambiarEstado(tarea.getIdTask(), seleccionado.getIdStatusTask());
                actualizada = true;
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al cambiar el estado: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        botonCancelar.addActionListener(e -> dispose());

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBotones.add(botonCancelar);
        panelBotones.add(botonGuardar);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.EAST;
        add(panelBotones, gbc);

        pack();
        setLocationRelativeTo(owner);
    }

    public boolean fueActualizada() {
        return actualizada;
    }
}
