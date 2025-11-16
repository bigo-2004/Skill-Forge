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
    private JRadioButton studentRadioButton;
    private JRadioButton instructorRadioButton;
    private ButtonGroup roleGroup = new ButtonGroup();

    public LoginForm() {

        roleGroup.add(studentRadioButton);
        roleGroup.add(instructorRadioButton);

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

                if(!studentRadioButton.isSelected() && !instructorRadioButton.isSelected()) {
                    JOptionPane.showMessageDialog(LoginForm.this, "Please select a student or instructor");
                    return;
                }

                if (email.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Enter your email!");
                    return;
                }

                if(!Validations.isValidEmail(email)) {
                    javax.swing.JOptionPane.showMessageDialog(null, "Invalid email!");
                    return;
                }

                if (password.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Enter your password!");
                    return;
                }
                String expectedRole = null;
                if(studentRadioButton.isSelected()) {
                    expectedRole = "Student";
                }
                else if(instructorRadioButton.isSelected()) {
                    expectedRole = "Instructor";
                }
                User u = SigningOperations.login(email, password,expectedRole);

                if(u == null) {
                    JOptionPane.showMessageDialog(null, "Invalid email or password!");
                    return;
                }



                if (u != null && expectedRole.equals("Student") ) {
                    new StudentForm(u);
                    dispose();
                }
                else if (u != null && expectedRole.equals("Instructor") ) {
                    new InstructorForm(u);
                    dispose();
                }

            }

        });
    }
}
