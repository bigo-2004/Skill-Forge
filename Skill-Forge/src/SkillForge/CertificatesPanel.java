package SkillForge;

import javax.swing.*;
import java.awt.*;
import java.io.FileWriter;
import org.json.JSONArray;
import org.json.JSONObject;

public class CertificatesPanel extends JFrame {

    private Student student;
    private CertificateManager certificateManager;
    private JLabel CertificateName;
    private JLabel StudentLabel;
    private JLabel CourseLabel;
    private JLabel StudentName;
    private JLabel Signature;
    private JLabel Date;
    private JButton downlaodButton;
    private JLabel CourseID;
    private JLabel Label1;
    private JLabel Label2;
    private JLabel Label3;
    private JLabel Label4;
    private JLabel Label5;
    private JLabel Label6;


    public CertificatesPanel(Student student, CertificateManager certificateManager) {
        this.student = student;
        this.certificateManager = certificateManager;

        initComponents();
        loadCertificate();

        downlaodButton.addActionListener(e -> downloadCertificate());
    }

    private void initComponents() {
        setTitle("Certificate");
        setSize(500, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(null);
        getContentPane().setBackground(new Color(250, 250, 210));

        StudentName = new JLabel();
        StudentName.setFont(new Font("Arial", Font.BOLD, 20));
        StudentName.setBounds(160, 100, 300, 25);
        StudentName.setForeground(new Color(0, 102, 204));
        add(StudentName);

        Date = new JLabel();
        Date.setFont(new Font("Arial", Font.BOLD, 17));
        Date.setBounds(200, 235, 300, 20);
        Date.setForeground(new Color(0, 153, 0));
        add(Date);

        Signature = new JLabel("Instructor Signature: __________________");
        Signature.setFont(new Font("Arial", Font.ITALIC, 14));
        Signature.setBounds(100, 280, 350, 20);
        add(Signature);

        downlaodButton = new JButton("Download JSON ⬇");
        downlaodButton.setBounds(180, 310, 160, 25);
        add(downlaodButton);

        if(certificateManager.getCertificates(student).length() > 0){
            JSONObject cert = certificateManager.getCertificates(student).getJSONObject(0);
            CourseID.setText("Course ID: " + cert.getString("courseId"));
        }
    }

    private void loadCertificate() {
        JSONArray certs = certificateManager.getCertificates(student);
        if(certs.length() == 0){
            JOptionPane.showMessageDialog(this, "No certificates yet");
            return;
        }

        JSONObject cert = certs.getJSONObject(0);
        StudentName.setText(student.getUserName());
        Date.setText(cert.getString("issueDate"));
        Signature.setText("Instructor Signature: __________________");
    }

    private void downloadCertificate() {
        JSONArray certs = certificateManager.getCertificates(student);
        if(certs.length() == 0) return;

        JSONObject cert = certs.getJSONObject(0);

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setSelectedFile(new java.io.File("Certificate_" + cert.getString("certificateId") + ".json"));
        if(fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION){
            try(FileWriter fw = new FileWriter(fileChooser.getSelectedFile())){
                fw.write(cert.toString(4));
                JOptionPane.showMessageDialog(this, "Certificate saved as JSON!");
            } catch(Exception ex){
                JOptionPane.showMessageDialog(this, "Error saving JSON: " + ex.getMessage());
            }
        }
    }
}
