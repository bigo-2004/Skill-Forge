package SkillForge;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SignupForm extends JFrame{
    private JButton createAccountButton;
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JRadioButton studentRadioButton;
    private JRadioButton instructorRadioButton;
    private JButton returnToLoginPageButton;
    private JPanel mainPanel;
    private ButtonGroup roleGroup = new ButtonGroup();

    public SignupForm() {

        roleGroup.add(studentRadioButton);
        roleGroup.add(instructorRadioButton);

        setTitle("Signup");
        setContentPane(mainPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 400);
        setLocationRelativeTo(null);
        setVisible(true);


        returnToLoginPageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new LoginForm();
            }
        });
    }



}
