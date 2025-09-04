package org.example.GUI.panels;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class AdminPanel extends JPanel {

    public AdminPanel(int x, int y, int width, int height, Color backgroundColor, int columns, int rows, int hgap, int vgap) {
        this.setBounds(x, y, width, height);
        this.setBackground(backgroundColor);
        this.setLayout(new GridLayout(rows, columns, hgap, vgap));
    }

    public void addComponents(ArrayList<JComponent> components) {
        for(JComponent component : components) {
            this.add(component);
        }
    }
}
