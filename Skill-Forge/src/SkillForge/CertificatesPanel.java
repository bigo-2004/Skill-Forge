package SkillForge;

import javax.swing.*;
import java.awt.*;
import org.json.JSONArray;
import org.json.JSONObject;

public class CertificatesPanel extends JPanel {

    private Student student;
    private CertificateManager certificateManager;

    public CertificatesPanel(Student student, CertificateManager certificateManager) {
        this.student = student;
        this.certificateManager = certificateManager;

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JLabel title = new JLabel("📜 Certificates Earned");
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(title, BorderLayout.NORTH);

        // Load certificates
        JSONArray certificates = certificateManager.getCertificates(student);

        if (certificates.length() == 0) {
            add(new JLabel("No certificates yet"), BorderLayout.CENTER);
            return;
        }

        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(Color.WHITE);

        for (int i = 0; i < certificates.length(); i++) {
            JSONObject cert = certificates.getJSONObject(i);
            listPanel.add(createCertificateCard(cert));
        }

        JScrollPane scrollPane = new JScrollPane(listPanel);
        scrollPane.setBorder(null);

        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel createCertificateCard(JSONObject cert) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10),
                BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1)
        ));
        card.setBackground(new Color(245, 245, 245));

        String certId = cert.getString("certificateId");
        String courseId = cert.getString("courseId");
        String date = cert.getString("issueDate");

        JLabel info = new JLabel(
                "<html>"
                        + "<b>Certificate ID:</b> " + certId + "<br>"
                        + "<b>Course:</b> " + courseId + "<br>"
                        + "<b>Issued:</b> " + date
                        + "</html>"
        );
        info.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JButton viewBtn = new JButton("View");
        viewBtn.addActionListener(e -> showCertificatePopup(cert));

        card.add(info, BorderLayout.CENTER);
        card.add(viewBtn, BorderLayout.EAST);

        return card;
    }

    private void showCertificatePopup(JSONObject cert) {
        JOptionPane.showMessageDialog(
                this,
                "Certificate ID: " + cert.getString("certificateId")
                        + "\nCourse: " + cert.getString("courseId")
                        + "\nIssued: " + cert.getString("issueDate"),
                "Certificate Details",
                JOptionPane.INFORMATION_MESSAGE
        );
    }
}
