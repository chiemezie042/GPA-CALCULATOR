import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class AddCourseScreen {

    public BorderPane getLayout() {
        // === Title ===
        Text title = new Text("📘 Add New Course");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 26));
        title.setFill(Color.CYAN);

        // === Input fields ===
        TextField regField = new TextField();
        regField.setPromptText("Student Registration Number");
        regField.setMaxWidth(300);

        TextField titleField = new TextField();
        titleField.setPromptText("Course Title");
        titleField.setMaxWidth(300);

        TextField codeField = new TextField();
        codeField.setPromptText("Course Code");
        codeField.setMaxWidth(300);

        TextField sessionField = new TextField();
        sessionField.setPromptText("Session (e.g. 2024/2025)");
        sessionField.setMaxWidth(300);

        ComboBox<String> unitBox = new ComboBox<>();
        unitBox.getItems().addAll("1","2","3","4","5","6");
        unitBox.setPromptText("Select Unit");
        unitBox.setMaxWidth(300);

        ComboBox<String> levelBox = new ComboBox<>();
        levelBox.getItems().addAll("100","200","300","400","500");
        levelBox.setPromptText("Select Level");
        levelBox.setMaxWidth(300);

        ComboBox<String> semesterBox = new ComboBox<>();
        semesterBox.getItems().addAll("First","Second");
        semesterBox.setPromptText("Select Semester");
        semesterBox.setMaxWidth(300);

        ComboBox<String> gradeBox = new ComboBox<>();
        gradeBox.getItems().addAll("A","B","C","D","E","F");
        gradeBox.setPromptText("Select Grade");
        gradeBox.setMaxWidth(300);

        // === Buttons ===
        Button saveBtn = new Button("💾 Save Course");
        Button backBtn = new Button("⬅ Back to Home");
        saveBtn.setStyle(buttonStyle());
        backBtn.setStyle(buttonStyle());

        Label statusLabel = new Label();
        statusLabel.setTextFill(Color.LIGHTGREEN);

        // === Form layout in a single centered VBox ===
        VBox formBox = new VBox(12, title, regField, titleField, codeField,
                sessionField, unitBox, levelBox, semesterBox, gradeBox, saveBtn, backBtn, statusLabel);
        formBox.setAlignment(Pos.CENTER);
        formBox.setPadding(new Insets(50));

        // === Main layout ===
        BorderPane mainLayout = new BorderPane();
        mainLayout.setCenter(formBox); // everything centered
        mainLayout.setStyle("-fx-background-color: linear-gradient(to bottom right, #0a0a1a, #1a1a2e);");

        // === Fade animation ===
        FadeTransition fade = new FadeTransition(Duration.seconds(1.2), mainLayout);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.play();

        // === Save button logic ===
        saveBtn.setOnAction(e -> {
            String reg = regField.getText().trim();
            String courseTitle = titleField.getText().trim();
            String courseCode = codeField.getText().trim();
            String session = sessionField.getText().trim();
            String unitStr = unitBox.getValue();
            String levelStr = levelBox.getValue();
            String semester = semesterBox.getValue();
            String grade = gradeBox.getValue();

            if (reg.isEmpty() || courseTitle.isEmpty() || courseCode.isEmpty()
                    || session.isEmpty() || unitStr == null || levelStr == null
                    || semester == null || grade == null) {
                statusLabel.setTextFill(Color.RED);
                statusLabel.setText("⚠️ Please fill in all fields!");
                return;
            }

            try {
                int unit = Integer.parseInt(unitStr);
                int level = Integer.parseInt(levelStr);

                Course c = new Course(courseTitle, unit, semester, session, courseCode, level, grade, reg);
                c.saveToDatabase();

                statusLabel.setTextFill(Color.LIGHTGREEN);
                statusLabel.setText("✅ Course saved successfully!");

                // Clear fields
                regField.clear();
                titleField.clear();
                codeField.clear();
                sessionField.clear();
                unitBox.setValue(null);
                levelBox.setValue(null);
                semesterBox.setValue(null);
                gradeBox.setValue(null);

            } catch (NumberFormatException ex) {
                statusLabel.setTextFill(Color.RED);
                statusLabel.setText("❌ Level and Unit must be numeric!");
            } catch (Exception ex) {
                statusLabel.setTextFill(Color.RED);
                statusLabel.setText("❌ Error: " + ex.getMessage());
            }
        });

        // === Back button navigation ===
        backBtn.setOnAction(e -> {
            Scene homeScene = new Scene(new HomeScreen().getLayout(), 1600, 880);
            Main.switchScene(homeScene, "Home");
        });

        return mainLayout;
    }

    private String buttonStyle() {
        return "-fx-background-color: #12122a; -fx-text-fill: #00bfff; "
             + "-fx-font-weight: bold; -fx-padding: 10 20; "
             + "-fx-border-color: #00bfff; -fx-border-radius: 8; "
             + "-fx-background-radius: 8;";
    }
}
