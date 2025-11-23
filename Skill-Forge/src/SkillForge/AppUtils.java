package SkillForge;

import javax.swing.JFrame;
import java.awt.Window;

public class AppUtils {

    public static void handleLogout() {
        // Close all existing frames/windows
        Window[] windows = Window.getWindows();
        for (Window window : windows) {
            if (window instanceof JFrame) {
                window.dispose();
            }
        }
        new LoginForm();
    }
}