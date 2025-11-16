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


        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = textField1.getText().trim();
                String password = new String(passwordField1.getPassword());

                if (email.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Enter your email!");
                    return;
                }

                if (password.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Enter your password!");
                    return;
                }

                User x = SigningOperations.login(email, password);

                if (x == null) {
                    JOptionPane.showMessageDialog(null, "Invalid email or password!");
                    return;
                }

                if (x.getRole().equals("Student")) {
                    new StudentForm();
                    dispose();
                }
                else if (x.getRole().equals("Instructor")) {
                    //new InstructorForm();
                    dispose();
                }
            }

        });
    }
}
