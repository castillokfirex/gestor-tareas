package com.proyecto.gestortareas.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/**
 * Pinta la columna de prioridad con un color distinto segun el valor,
 * para que se identifique de un vistazo sin tener que leer el texto.
 */
public class PrioridadCellRenderer extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                     boolean hasFocus, int row, int column) {
        JLabel label = (JLabel) super.getTableCellRendererComponent(
                table, value, isSelected, hasFocus, row, column);
        label.setHorizontalAlignment(CENTER);
        label.setFont(label.getFont().deriveFont(Font.BOLD));

        String texto = String.valueOf(value);
        Color color = switch (texto) {
            case "ALTA" -> new Color(0xE05252);
            case "MEDIA" -> new Color(0xD98A32);
            case "BAJA" -> new Color(0x3A82C4);
            default -> table.getForeground();
        };
        if (!isSelected) {
            label.setForeground(color);
        }
        return label;
    }
}
