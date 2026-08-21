package com.proyecto.gestortareas;

import com.proyecto.gestortareas.gui.GestorTareasFrame;

import javax.swing.*;

/**
 * Punto de entrada de la version grafica (Swing) del Gestor de Tareas.
 * La version de consola sigue disponible en Main.java, por si se necesita.
 */
public class AppSwing {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
            // Si el look and feel del sistema no esta disponible, se usa el por defecto de Swing.
        }

        SwingUtilities.invokeLater(() -> new GestorTareasFrame().setVisible(true));
    }
}
