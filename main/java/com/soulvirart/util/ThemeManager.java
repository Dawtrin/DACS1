package com.soulvirart.util;

import javax.swing.*;
import java.awt.*;

public class ThemeManager {
    private static boolean isDarkTheme = false;
    private static final Color LIGHT_BACKGROUND = Color.WHITE;
    private static final Color LIGHT_FOREGROUND = Color.BLACK;
    private static final Color DARK_BACKGROUND = new Color(51, 51, 51);
    private static final Color DARK_FOREGROUND = Color.WHITE;

    public static void applyTheme(Component component) {
        if (component instanceof JComponent jComponent) {
            jComponent.setBackground(isDarkTheme ? DARK_BACKGROUND : LIGHT_BACKGROUND);
            jComponent.setForeground(isDarkTheme ? DARK_FOREGROUND : LIGHT_FOREGROUND);
            for (Component child : jComponent.getComponents()) {
                applyTheme(child);
            }
        }
    }

    public static void toggleTheme(Component component) {
        isDarkTheme = !isDarkTheme;
        applyTheme(component);
        component.repaint();
    }

    public static boolean isDarkTheme() {
        return isDarkTheme;
    }
}