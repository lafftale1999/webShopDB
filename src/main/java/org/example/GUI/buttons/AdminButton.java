package org.example.GUI.buttons;

import org.example.GUI.guiSettings;

import javax.swing.*;
import java.awt.*;

public class AdminButton extends JButton {

    public AdminButton(String text, Color color, boolean border) {
        this.setText(text);
        this.setFocusable(false);
        this.setFont(guiSettings.buttonFont);
        this.setBackground(color);

        if(border) {
            this.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2, true));
        }
    }
}
