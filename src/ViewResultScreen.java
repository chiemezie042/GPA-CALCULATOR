import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * ViewResultScreen with PDF download and email sending functionality
 */
public class ViewResultScreen {

    private List<ResultItem> lastResults = new ArrayList<>();
    private Student lastStudent;
    private final String schoolName = "UNIVERSITY OF NIGERIA NSUKKA";

    public VBox getLayout() {

        // ===== Title =====
        Text title = new Text("📊 View Student Results");
        title.setFont(javafx.scene.text.Font.font("Segoe UI", FontWeight.BOLD, 26));
        title.setFill(javafx.scene.paint.Color.CYAN);

        // ===== Registration Input =====
        TextField regField = new TextField();
        regField.setPromptText("Enter Registration Number");
        regField.setMaxWidth(300);

        // ===== Email Input =====
        TextField emailField = new TextField();
        emailField.setPromptText("Enter email to send PDF");
        emailField.setMaxWidth(300);

        // ===== Buttons =====
        Button searchBtn = new Button("🔍 Search Results");
        Button downloadBtn = new Button("⬇ Download PDF");
        Button sendEmailBtn = new Button("📧 Send PDF via Email");
        Button backBtn = new Button("⬅ Back to Home");
        searchBtn.setStyle(buttonStyle());
        downloadBtn.setStyle(buttonStyle());
        sendEmailBtn.setStyle(buttonStyle());
        backBtn.setStyle(buttonStyle());

        // ===== Result display =====
        TextArea resultArea = new TextArea();
        resultArea.setEditable(false);
        resultArea.setPrefHeight(400);
        resultArea.setStyle("-fx-control-inner-background: #0e0e20; -fx-text-fill: white;");

        // ===== Layout =====
        VBox layout = new VBox(15, title, regField, searchBtn, downloadBtn, emailField, sendEmailBtn, resultArea, backBtn);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(30));
        layout.setStyle("-fx-background-color: linear-gradient(to bottom right, #0a0a1a, #1a1a2e);");

        // ===== SEARCH ACTION =====
        searchBtn.setOnAction(e -> {
            String reg = regField.getText().trim();
            if (reg.isEmpty()) {
                resultArea.setText("⚠ Please enter a registration number.");
                return;
            }
            fetchResults(reg, resultArea);
        });

        // ===== DOWNLOAD PDF =====
        downloadBtn.setOnAction(e -> {
            if (lastResults.isEmpty() || lastStudent == null) {
                resultArea.setText("⚠ No result to export. Search first.");
                return;
            }
            savePdfWithChooser(lastStudent, lastResults);
        });

        // ===== SEND EMAIL =====
        sendEmailBtn.setOnAction(e -> {
            String email = emailField.getText().trim();
            if (email.isEmpty()) {
                resultArea.setText("⚠ Please enter an email address.");
                return;
            }
            if (lastResults.isEmpty() || lastStudent == null) {
                resultArea.setText("⚠ No result to send. Search first.");
                return;
            }

            try {
                // Temporary PDF file
                File tempPdf = File.createTempFile("result_", ".pdf");
                tempPdf.deleteOnExit(); // auto-delete on JVM exit

                savePdfToFile(lastStudent, lastResults, tempPdf);

                // Send email using EmailSender
                EmailSender sender = new EmailSender("schoolemailsender321@gmail.com", "doik mrzk flxy dkwt");
                sender.sendEmailWithAttachment(
                        email,
                        "Your Result PDF",
                        "Attached is your result PDF from " + schoolName,
                        tempPdf
                );

                resultArea.setText("✅ PDF sent successfully to " + email);

            } catch (Exception ex) {
                resultArea.setText("❌ Error sending email: " + ex.getMessage());
            }
        });

        // ===== BACK BUTTON =====
        backBtn.setOnAction(e -> {
            Scene homeScene = new Scene(new HomeScreen().getLayout(), 1600, 880);
            Main.switchScene(homeScene, "Home");
        });

        return layout;
    }

    // ===== FETCH RESULTS =====
    private void fetchResults(String regNo, TextArea output) {
        String studentSQL = "SELECT reg_number, name, level, session, semester FROM students WHERE reg_number = ?";
        String resultSQL = "SELECT course_code, course_title, credit, grade, level, semester FROM result WHERE reg_number = ? ORDER BY level, semester";

        List<ResultItem> results = new ArrayList<>();

        try (Connection conn = sqlconnector.getConnection()) {
            // Fetch student info
            try (PreparedStatement stmt = conn.prepareStatement(studentSQL)) {
                stmt.setString(1, regNo);
                ResultSet rs = stmt.executeQuery();
                if (!rs.next()) {
                    output.setText("⚠ No student found with registration number: " + regNo);
                    return;
                }
                lastStudent = new Student(
                        rs.getString("reg_number"),
                        rs.getString("name"),
                        rs.getInt("level"),
                        rs.getString("session"),
                        rs.getString("semester")
                );
            }

            // Fetch results
            try (PreparedStatement stmt = conn.prepareStatement(resultSQL)) {
                stmt.setString(1, regNo);
                ResultSet rs = stmt.executeQuery();

                StringBuilder sb = new StringBuilder();
                sb.append("RESULTS FOR: ").append(lastStudent.getName())
                  .append(" (").append(lastStudent.getRegNumber()).append(")\n")
                  .append("Level: ").append(lastStudent.getLevel())
                  .append(" | Semester: ").append(lastStudent.getSemester())
                  .append(" | Session: ").append(lastStudent.getSession()).append("\n\n")
                  .append("Course     Title                  Unit Grade GPA\n")
                  .append("----------------------------------------------------\n");

                double totalPoints = 0;
                int totalUnits = 0;

                while (rs.next()) {
                    String code = rs.getString("course_code");
                    String title = rs.getString("course_title");
                    int unit = rs.getInt("credit");
                    String grade = rs.getString("grade");
                    double gpa = getGradePoint(grade);

                    results.add(new ResultItem(code, unit, grade, gpa));
                    sb.append(String.format("%-10s %-20s %-4d %-6s %.2f\n", code, title, unit, grade, gpa));

                    totalUnits += unit;
                    totalPoints += gpa * unit;
                }

                if (results.isEmpty()) {
                    output.setText("⚠ No results found for this student.");
                    return;
                }

                double cgpa = totalUnits == 0 ? 0 : totalPoints / totalUnits;
                sb.append("\nTotal Units: ").append(totalUnits).append(" | CGPA: ").append(String.format("%.2f", cgpa));

                lastResults = results;
                output.setText(sb.toString());
            }

        } catch (SQLException e) {
            output.setText("❌ Error fetching results: " + e.getMessage());
        }
    }

    // ===== SAVE PDF (FileChooser) =====
    private void savePdfWithChooser(Student student, List<ResultItem> results) {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Result as PDF");
            fileChooser.setInitialFileName(student.getName() + "_Result.pdf");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF File", "*.pdf"));

            File file = fileChooser.showSaveDialog(Main.getMainStage());
            if (file != null) {
                savePdfToFile(student, results, file);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("PDF Created Successfully");
                alert.setContentText("Saved to: " + file.getAbsolutePath());
                alert.show();
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error Creating PDF");
            alert.setContentText(e.getMessage());
            alert.show();
        }
    }

    // ===== SAVE PDF TO SPECIFIC FILE (for email) =====
    private void savePdfToFile(Student student, List<ResultItem> results, File file) throws Exception {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(file));
        document.open();

        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20);
        Paragraph schoolTitle = new Paragraph(schoolName.toUpperCase(), titleFont);
        schoolTitle.setAlignment(Element.ALIGN_CENTER);
        document.add(schoolTitle);
        document.add(new Paragraph("\n"));

        Font infoFont = FontFactory.getFont(FontFactory.HELVETICA, 12);
        document.add(new Paragraph("Student Name: " + student.getName(), infoFont));
        document.add(new Paragraph("Student REGNO: " + student.getRegNumber(), infoFont));
        document.add(new Paragraph("Level: " + student.getLevel(), infoFont));
        document.add(new Paragraph("Semester: " + student.getSemester(), infoFont));
        document.add(new Paragraph("Session: " + student.getSession(), infoFont));
        document.add(new Paragraph("\n"));

        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10);
        table.addCell("Course");
        table.addCell("Unit");
        table.addCell("Grade");
        table.addCell("GPA");

        int totalUnits = 0;
        double totalPoints = 0;

        for (ResultItem item : results) {
            table.addCell(item.getCourse());
            table.addCell(String.valueOf(item.getUnit()));
            table.addCell(item.getGrade());
            table.addCell(String.format("%.2f", item.getGpa()));

            totalUnits += item.getUnit();
            totalPoints += item.getGpa() * item.getUnit();
        }

        document.add(table);

        double cgpa = totalUnits == 0 ? 0 : totalPoints / totalUnits;
        Paragraph cgpaPara = new Paragraph("\nCGPA: " + String.format("%.2f", cgpa),
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14));
        document.add(cgpaPara);

        document.close();
    }

    // ===== GRADE POINT CALCULATION =====
    private double getGradePoint(String grade) {
        return switch (grade.toUpperCase()) {
            case "A" -> 5.0;
            case "B" -> 4.0;
            case "C" -> 3.0;
            case "D" -> 2.0;
            case "E" -> 1.0;
            default -> 0.0;
        };
    }

    // ===== BUTTON STYLE =====
    private String buttonStyle() {
        return "-fx-background-color: #12122a; -fx-text-fill: #00bfff;"
                + "-fx-font-weight: bold; -fx-padding: 10 20;"
                + "-fx-border-color: #00bfff; -fx-border-radius: 8;"
                + "-fx-background-radius: 20;";
    }
}
