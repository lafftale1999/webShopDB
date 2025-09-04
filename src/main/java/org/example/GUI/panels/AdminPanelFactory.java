package org.example.GUI.panels;

import javax.swing.*;
import java.awt.*;

public class AdminPanelFactory {

    public enum PanelType {
        TAB_PANEL {
            public String toString() {
                return "Tab Panel";
            }
        },
        OPTIONS_PANEL {
            public String toString() {
                return "Options Panel";
            }
        },
        INFORMATION_PANEL{
            public String toString() {
                return "Information Panel";
            }
        }
    }

    public static AdminPanel createPanel(PanelType type, JFrame parent, int rows, int columns) {

        AdminPanel panel = null;
        boolean createdPanel = false;

        if (type == PanelType.TAB_PANEL) {
            panel = createTabPanel(parent, rows, columns);
            createdPanel = true;
        }

        else if (type == PanelType.OPTIONS_PANEL) {
            panel = createOptionsPanel(parent, rows, columns);
            createdPanel = true;
        }

        else if (type == PanelType.INFORMATION_PANEL) {
            panel = createInformationPanel(parent, rows, columns);
            createdPanel = true;
        }

        if(!createdPanel) {
            throw new IllegalStateException("Unable to create " + type.toString());
        }

        return panel;
    }

    private static AdminPanel createTabPanel(JFrame parent, int rows, int columns) {
        return new AdminPanel(
                0,0,
                parent.getWidth(),
                (int)(parent.getHeight() * 0.05),
                Color.GRAY,
                columns, rows,
                0,0);
    }

    private static AdminPanel createOptionsPanel(JFrame parent, int rows, int columns) {
        return new AdminPanel(
                0,(int)(parent.getHeight() * 0.05),
                parent.getWidth(),
                (int)(parent.getHeight() * 0.10),
                Color.WHITE,
                columns, rows,
                10, 20);
    }

    private static AdminPanel createInformationPanel(JFrame parent, int rows, int columns) {
        return new AdminPanel(
                0,(int)((parent.getHeight() * 0.05) + (parent.getHeight() * 0.10)),
                parent.getWidth(),
                parent.getHeight() - (int)((parent.getHeight() * 0.05) + (parent.getHeight() * 0.10)),
                Color.GRAY,
                columns, rows,
                20,20);
    }
}
