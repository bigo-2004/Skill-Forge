package SkillForge;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SignupForm extends JFrame {
    private JButton createAccountButton;
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JRadioButton studentRadioButton;
    private JRadioButton instructorRadioButton;
    private JButton returnToLoginPageButton;
    private JRadioButton adminRadioButton;
    private JPanel mainPanel;
    private JTextField textField4;
    private ButtonGroup roleGroup = new ButtonGroup();

    public SignupForm() {

        roleGroup.add(studentRadioButton);
        roleGroup.add(instructorRadioButton);
        roleGroup.add(adminRadioButton);

        setTitle("Signup");
        setContentPane(mainPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 400);
        setLocationRelativeTo(null);
        setVisible(true);

        UserJsonDatabase db = new UserJsonDatabase("users.json");
        String id;
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = db.readJsonArrayFromFile();
        for (int i = 0; i < jsonArray.length(); i++) {
            jsonObject = jsonArray.getJSONObject(i);
        }
        if(jsonArray.length()>0) {
            String lastId = jsonObject.getString("id");
            int lId = Integer.parseInt(lastId);
            lId++;
            id = String.valueOf(lId);
        }
        else {
            id = String.valueOf(1);
        }
        textField4.setText(id);
        textField4.setEditable(false);


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

                String id = textField4.getText();

                String role = null;
                if (studentRadioButton.isSelected()) {
                    role = "Student";
                } else if (instructorRadioButton.isSelected()) {
                    role = "Instructor";
                }
                else if (adminRadioButton.isSelected()) {
                    role = "Admin";
                }
                if (username.isEmpty() || email.isEmpty() || pd.isEmpty() || (!studentRadioButton.isSelected() && !instructorRadioButton.isSelected() && !adminRadioButton.isSelected())) {
                    JOptionPane.showMessageDialog(null, "Please fill all fields and select a role.");
                    return;
                }

                if (!Validations.isValidName(username) || username.length() < 3) {
                    JOptionPane.showMessageDialog(null, "Invalid username!");
                    return;
                }




                if (!Validations.isValidEmail(email)) {
                    javax.swing.JOptionPane.showMessageDialog(null, "Invalid email!");
                    return;
                }

                if (!Validations.isValidPassword(pd)) {
                    JOptionPane.showMessageDialog(null, "Password must be at least 8 characters and contain at least one capital letter.");
                    return;
                }

                if (SigningOperations.signup(id, username, email, pd, role)) {
                    JOptionPane.showMessageDialog(null, "User added successfully");
                } else {
                    JOptionPane.showMessageDialog(null, "A user with this email already exist");
                    return;
                }
                dispose();
                new LoginForm();
            }
        });
    }


}
