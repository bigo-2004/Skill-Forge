package SkillForge;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.FileWriter;
import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONObject;

public class CertificatesPanel extends JFrame {

    private Student student;
    private CertificateManager certificateManager;

    // المكونات من الـ .form
    private JPanel mainPanel;
    private JLabel CertificateName;
    private JLabel StudentName;
    private JLabel CourseName;
    private JButton DownlaodButton;
    private JLabel Label1;
    private JLabel Label2;
    private JLabel Label3;
    private JLabel Label4;
    private JLabel Label5;

    public CertificatesPanel(Student student, CertificateManager certificateManager) {
        this.student = student;
        this.certificateManager = certificateManager;

        // إنشاء الـ JPanel الرئيسي
        mainPanel = new JPanel();
        mainPanel.setLayout(null); // هنعمل manual positioning زي التصميم الأصلي

        // إضافة المكونات
        CertificateName = new JLabel("C E R T I F I C A T E");
        CertificateName.setFont(new java.awt.Font("Monotype Corsiva", 1, 28));
        CertificateName.setBounds(50, 20, 700, 40);
        CertificateName.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(CertificateName);

        Label1 = new JLabel("------------- OF APPRECIATION --------------");
        Label1.setFont(new java.awt.Font("Dialog", 0, 15));
        Label1.setBounds(50, 70, 700, 20);
        Label1.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(Label1);

        Label2 = new JLabel("THIS CERTIFICATE IS PROUDLY IS PRESENTED TO");
        Label2.setFont(new java.awt.Font("Dialog", 0, 15));
        Label2.setBounds(50, 100, 700, 20);
        Label2.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(Label2);

        StudentName = new JLabel("STUDENT NAME");
        StudentName.setFont(new java.awt.Font("Lucida Calligraphy", 1, 22));
        StudentName.setBounds(50, 130, 700, 30);
        StudentName.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(StudentName);

        Label3 = new JLabel("Congratulations for successfully completing");
        Label3.setFont(new java.awt.Font("Dialog", 2, 14));
        Label3.setBounds(50, 170, 700, 20);
        Label3.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(Label3);

        Label4 = new JLabel("all quizzes and met the graduation requirements for");
        Label4.setFont(new java.awt.Font("Dialog", 2, 14));
        Label4.setBounds(50, 190, 700, 20);
        Label4.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(Label4);

        CourseName = new JLabel("Course Name");
        CourseName.setFont(new java.awt.Font("Lucida Calligraphy", 1, 22));
        CourseName.setBounds(50, 220, 700, 30);
        CourseName.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(CourseName);

        Label5 = new JLabel("SkillForge");
        Label5.setFont(new java.awt.Font("Freestyle Script", 0, 22));
        Label5.setBounds(600, 400, 150, 30);
        mainPanel.add(Label5);

        DownlaodButton = new JButton("Download");
        DownlaodButton.setBounds(350, 400, 120, 30);
        mainPanel.add(DownlaodButton);

        setContentPane(mainPanel);

        loadCertificateInfo();
        setupDownloadButton();

        setTitle("Certificates");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void loadCertificateInfo() {
        JSONArray certs = certificateManager.getCertificates(student);
        if (certs.length() == 0) {
            CertificateName.setText("No certificates found!");
            CourseName.setText("-");
            StudentName.setText(student.getUserName());
            return;
        }

        JSONObject cert = certs.getJSONObject(0);
        CertificateName.setText("C E R T I F I C A T E");
        StudentName.setText(student.getUserName());

        CourseJsonDatabase courseDatabase = new CourseJsonDatabase("courses.json");
        Course course = (Course) courseDatabase.getObjectById(cert.getString("courseId"));
        CourseName.setText(course.getCourseTitle());
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
