package SkillForge;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginForm extends JFrame {
    private JTextField textField1;
    private JPasswordField passwordField1;
    private JButton loginButton;
    private JButton createNewAccountButton;
    private JPanel mainPanel;

    public LoginForm() {

        setTitle("Login");
        setContentPane(mainPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 400);
        setLocationRelativeTo(null);
        setVisible(true);


        createNewAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new SignupForm();
            }
        });


    }
}
