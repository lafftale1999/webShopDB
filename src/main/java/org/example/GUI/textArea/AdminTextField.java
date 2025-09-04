package org.example.GUI.textArea;

import org.example.GUI.guiSettings;

import javax.swing.*;
import java.awt.*;

public class AdminTextField extends JTextField {

    private String prompt;

    public AdminTextField(Color backgroundColor) {
        this.setFont(guiSettings.textFont);
        this.setBackground(backgroundColor);
        this.setCaretColor(Color.BLACK);
        this.prompt = prompt;
        this.setText(prompt);
    }

    public String getTextContent() {
        String text = this.getText();

        this.setText(prompt);

        return text;
    }
}
