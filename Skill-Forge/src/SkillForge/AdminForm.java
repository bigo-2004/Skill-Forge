package SkillForge;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdminForm extends JFrame {
    private JButton viewPendingCoursesButton;
    private JPanel panel1;
    private JButton viewAllCoursesButton;
    private JButton logoutButton;

    private User admin;

    public AdminForm(User admin) {
        this.admin = admin;

        setTitle("Admin Dashboard");
        setContentPane(panel1);
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        viewPendingCoursesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ViewPendingCoursesForm();
            }
        });

        viewAllCoursesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ViewCoursesForm();
            }
        });

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AppUtils.handleLogout();
            }
        });
    }
}
