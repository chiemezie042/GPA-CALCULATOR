
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.sql.*;

public class ViewResultScreen {

    Stage stage;

    // ✅ Constructor with Stage
    public ViewResultScreen(Stage stage) {
        this.stage = stage;
    }

    public VBox getLayout() {
        Text title = new Text("📊 View Student Results");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 26));
        title.setFill(javafx.scene.paint.Color.CYAN);

        TextField regField = new TextField();
        regField.setPromptText("Enter Registration Number");

        Button searchBtn = new Button("🔍 Search Results");
        Button backBtn = new Button("⬅ Back to Home");
        searchBtn.setStyle(buttonStyle());
        backBtn.setStyle(buttonStyle());

        TextArea resultArea = new TextArea();
        resultArea.setEditable(false);
        resultArea.setPrefHeight(400);
        resultArea.setStyle("-fx-control-inner-background: #0e0e20; -fx-text-fill: white;");

        VBox layout = new VBox(15, title, regField, searchBtn, resultArea, backBtn);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(30));
        layout.setStyle("-fx-background-color: linear-gradient(to bottom right, #0a0a1a, #1a1a2e);");

        searchBtn.setOnAction(e -> {
            String reg = regField.getText().trim();
            if (reg.isEmpty()) {
                resultArea.setText("⚠ Please enter a registration number.");
                return;
            }
            fetchResults(reg, resultArea);
        });

        // ✅ Back to Home
        backBtn.setOnAction(e -> {
            homeScreen home = new homeScreen(stage);
            Scene homeScene = new Scene(home.getLayout(), 800, 600);
            stage.setScene(homeScene);
        });

        return layout;
    }

    private void fetchResults(String regNo, TextArea output) {
        String sql = "SELECT course_code, course_title, credit, grade, gpa, level, semester "
                + "FROM result WHERE reg_number = ? ORDER BY level, semester";

        try //(Connection conn = sqlconnector.connect(); PreparedStatement stmt = conn.prepareStatement(sql)) 
        {

            Connection conn = sqlconnector.connect();
            if (conn != null) {
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, regNo);

                ResultSet rs = stmt.executeQuery();

                StringBuilder sb = new StringBuilder();
                sb.append("╔══════════════════════════════════════════════════════════════════════╗\n");
                sb.append(String.format("║   🎓  GPA RESULTS for: %-45s ║\n", regNo));
                sb.append("╠══════════════════════════════════════════════════════════════════════╣\n");
                sb.append(String.format("║ %-8s │ %-25s │ %-6s │ %-6s │ %-4s │ %-6s │ %-8s ║%n",
                        "Code", "Title", "Credit", "Grade", "GP", "Level", "Semester"));
                sb.append("╠══════════════════════════════════════════════════════════════════════╣\n");

                double totalPoints = 0;
                int totalCredits = 0;
                boolean hasData = false;

                while (rs.next()) {
                    hasData = true;
                    String code = rs.getString("course_code");
                    String title = rs.getString("course_title");
                    int credit = rs.getInt("credit");
                    String grade = rs.getString("grade");
                    double gpa = rs.getDouble("gpa");
                    int level = rs.getInt("level");
                    String semester = rs.getString("semester");

                    sb.append(String.format("║ %-8s │ %-25s │ %-6d │ %-6s │ %-4.2f │ %-6d │ %-8s ║%n",
                            code, title, credit, grade, gpa, level, semester));

                    totalCredits += credit;
                    totalPoints += gpa * credit;
                }

                sb.append("╠══════════════════════════════════════════════════════════════════════╣\n");

                if (hasData) {
                    double cgpa = totalPoints / totalCredits;
                    sb.append(String.format("║ 🎯  CGPA: %-10.2f                                      %-18s ║%n", cgpa, ""));
                } else {
                    sb.append("║ ⚠ No results found for this registration number.                  ║\n");
                }

                sb.append("╚══════════════════════════════════════════════════════════════════════╝\n");
                output.setText(sb.toString());
            }

        } catch (SQLException e) {
            output.setText("❌ Error fetching results: " + e.getMessage());
        }
    }

    private String buttonStyle() {
        return "-fx-background-color: #12122a; -fx-text-fill: #00bfff; "
                + "-fx-font-weight: bold; -fx-padding: 10 20 10 20; "
                + "-fx-border-color: #00bfff; -fx-border-radius: 8; "
                + "-fx-background-radius: 20;";
    }
}
