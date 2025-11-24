package SkillForge;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.FileWriter;
import java.io.IOException;
import org.json.JSONArray;
import org.json.JSONObject;

public class CertificatesPanel extends JFrame {

    private Student student;
    private CertificateManager certificateManager;

    private JLabel CertificateName;
    private JLabel StudentLabel;
    private JLabel CourseLabel;
    private JLabel StudentName;
    private JButton DownlaodButton;
    private JLabel CourseName;
    private JLabel Label1;
    private JLabel Label2;
    private JLabel Label3;
    private JLabel Label4;
    private JLabel Label5;


    public CertificatesPanel(Student student, CertificateManager certificateManager) {
        this.student = student;
        this.certificateManager = certificateManager;

        setTitle("Certificate Viewer");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(6, 1));

        StudentLabel = new JLabel("Student:");
        StudentLabel.setFont(new Font("Arial", Font.BOLD, 18));

        StudentName = new JLabel(student.getUserName());
        StudentName.setFont(new Font("Arial", Font.PLAIN, 18));

        CourseLabel = new JLabel("Course:");
        CourseLabel.setFont(new Font("Arial", Font.BOLD, 18));

        CourseName = new JLabel("—");
        CourseName.setFont(new Font("Arial", Font.PLAIN, 18));

        CertificateName = new JLabel("Certificate:");
        CertificateName.setFont(new Font("Arial", Font.BOLD, 18));

        DownlaodButton = new JButton("Download Certificate");
        DownlaodButton.setFont(new Font("Arial", Font.BOLD, 15));

        add(StudentLabel);
        add(StudentName);
        add(CourseLabel);
        add(CourseName);
        add(CertificateName);
        add(DownlaodButton);

        loadCertificateInfo();
        setupDownloadButton();

        setVisible(true);
    }

    private void loadCertificateInfo() {
        JSONArray certs = certificateManager.getCertificates(student);
        if (certs.length() == 0) {
            CertificateName.setText("No certificates found!");
            CourseName.setText("-");
            return;
        }

        JSONObject cert = certs.getJSONObject(0);

        CertificateName.setText("Certificate: " + cert.getString("certificateId"));
        CourseName.setText(cert.getString("courseName"));
    }

    private void setupDownloadButton() {
        DownlaodButton.addActionListener((ActionEvent e) -> {
            JSONArray certs = certificateManager.getCertificates(student);
            if (certs.length() == 0) return;

            JSONObject cert = certs.getJSONObject(0);

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setSelectedFile(new java.io.File(
                    "Certificate_" + cert.getString("certificateId") + ".json"
            ));

            if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                try (FileWriter writer = new FileWriter(fileChooser.getSelectedFile())) {
                    writer.write(cert.toString(4));
                    JOptionPane.showMessageDialog(this, "Certificate downloaded successfully!");
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "Error saving file!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}
