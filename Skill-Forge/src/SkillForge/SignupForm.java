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
    private JTextField textField4;
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
        createAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = textField1.getText();
                String email = textField2.getText();
                String pd = textField3.getText();
                String id =textField4.getText();
                String role=null;
                if(studentRadioButton.isSelected()){
                    role="Student";
                }
                else if(instructorRadioButton.isSelected()){
                    role="Instructor";
                }

                if (username.isEmpty() || email.isEmpty() || pd.isEmpty()  || (!studentRadioButton.isSelected() && !instructorRadioButton.isSelected()))
                {
                    JOptionPane.showMessageDialog(null, "Please fill all fields and select a role.");
                    return;
                }

                if(SigningOperations.signup(id ,username,email,pd,role)){
                    JOptionPane.showMessageDialog(null, "User added successfully");
                }
                else{
                    JOptionPane.showMessageDialog(null, "User already exist");
                }
                dispose();
                new LoginForm();
            }
        });
    }



}
