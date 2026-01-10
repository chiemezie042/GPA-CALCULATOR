import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;

/**
 * Screen to register students in the School Result Management System
 */
public class AddStudentScreen {

    public VBox getLayout() {
        // === Title ===
        Text title = new Text("📋 Register New Student");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 26));
        title.setFill(javafx.scene.paint.Color.CYAN);

        // === Input Fields ===
        TextField regField = new TextField();
        regField.setPromptText("Registration Number (e.g. 2024/123456)");
        regField.setMaxWidth(280);

        TextField nameField = new TextField();
        nameField.setPromptText("Student Name");
        nameField.setMaxWidth(280);

        TextField levelField = new TextField();
        levelField.setPromptText("Level (100, 200, 300...)");
        levelField.setMaxWidth(280);

        TextField sessionField = new TextField();
        sessionField.setPromptText("Session (e.g. 2024/2025)");
        sessionField.setMaxWidth(280);

        ComboBox<String> semesterBox = new ComboBox<>();
        semesterBox.getItems().addAll("First", "Second");
        semesterBox.setPromptText("Select Semester");

        // === Buttons ===
        Button saveBtn = new Button("💾 Save Student");
        Button backBtn = new Button("⬅ Back to Home");
        saveBtn.setStyle(buttonStyle());
        backBtn.setStyle(buttonStyle());

        Label message = new Label();
        message.setTextFill(javafx.scene.paint.Color.GREEN);

        VBox layout = new VBox(12, title, regField, nameField, levelField,
                sessionField, semesterBox, saveBtn, backBtn, message);
        layout.setPadding(new Insets(30));
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: linear-gradient(to bottom right, #0a0a1a, #1a1a2e);");

        // === Fade animation ===
        FadeTransition fade = new FadeTransition(Duration.seconds(1.2), layout);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.play();

        // === Save Student Logic with basic validation ===
        saveBtn.setOnAction(e -> {
            String reg = regField.getText().trim();
            String name = nameField.getText().trim();
            String levelText = levelField.getText().trim();
            String session = sessionField.getText().trim();
            String semester = semesterBox.getValue();

            if (reg.isEmpty() || name.isEmpty() || levelText.isEmpty()
                    || session.isEmpty() || semester == null) {
                message.setText("❌ Please fill in all fields.");
                message.setTextFill(javafx.scene.paint.Color.RED);
                return;
            }

            int level;
            try {
                level = Integer.parseInt(levelText);
            } catch (NumberFormatException ex) {
                message.setText("❌ Level must be a number (100, 200, ...).");
                message.setTextFill(javafx.scene.paint.Color.RED);
                return;
            }

            try {
                Student s = new Student(reg, name, level, session, semester);
                s.saveToDatabase();
                message.setText("✅ Student Saved Successfully!");
                message.setTextFill(javafx.scene.paint.Color.GREEN);

                // Optional: clear fields after saving
                regField.clear();
                nameField.clear();
                levelField.clear();
                sessionField.clear();
                semesterBox.getSelectionModel().clearSelection();

            } catch (Exception ex) {
                message.setText("❌ Error: " + ex.getMessage());
                message.setTextFill(javafx.scene.paint.Color.RED);
            }
        });

        // === Back to Home using centralized navigation ===
        backBtn.setOnAction(e -> {
            Scene homeScene = new Scene(new HomeScreen().getLayout(), 1600, 880);
            Main.switchScene(homeScene, "Home");
        });

        return layout;
    }

    private String buttonStyle() {
        return "-fx-background-color: #12122a; -fx-text-fill: #00bfff; "
             + "-fx-font-weight: bold; -fx-padding: 10 20 10 20; "
             + "-fx-border-color: #00bfff; -fx-border-radius: 8; "
             + "-fx-background-radius: 8;";
    }
}
