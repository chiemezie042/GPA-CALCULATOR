import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.animation.FadeTransition;
import javafx.stage.Stage;
import javafx.util.Duration;

public class AddCourseScreen {

     Stage stage;

    public AddCourseScreen(Stage stage) {
        this.stage = stage;
    }

    public BorderPane getLayout() {
        // ✅ Title
        Text title = new Text("Add New Course");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 26));
        title.setFill(Color.CYAN);

        // ✅ Input fields
        TextField regField = new TextField();
        regField.setPromptText("Enter Student Reg Number");

        TextField titleField = new TextField();
        titleField.setPromptText("Course Title");

        TextField codeField = new TextField();
        codeField.setPromptText("Course Code");

        TextField unitField = new TextField();
        unitField.setPromptText("Course Unit");

        TextField sessionField = new TextField();
        sessionField.setPromptText("Session (e.g. 2024/2025)");

        ComboBox<String> levelBox = new ComboBox<>();
        levelBox.getItems().addAll("100", "200", "300", "400", "500");
        levelBox.setPromptText("Select Level");

        ComboBox<String> semesterBox = new ComboBox<>();
        semesterBox.getItems().addAll("First", "Second");
        semesterBox.setPromptText("Select Semester");

        ComboBox<String> gradeBox = new ComboBox<>();
        gradeBox.getItems().addAll("A", "B", "C", "D", "E", "F");
        gradeBox.setPromptText("Select Grade");

        Button saveBtn = new Button("💾 Save Course");
        Button backBtn = new Button("⬅ Back to Home");
        saveBtn.setStyle(buttonStyle());
        backBtn.setStyle(buttonStyle());

        Label statusLabel = new Label();
        statusLabel.setTextFill(Color.LIGHTGREEN);

        // ✅ Left side form (VBox)
        VBox formBox = new VBox(12, title, regField, titleField, codeField, unitField,
                sessionField, levelBox, semesterBox, gradeBox, saveBtn, backBtn, statusLabel);
        formBox.setPadding(new Insets(40, 50, 40, 50));
        formBox.setAlignment(Pos.CENTER_LEFT);

        // ✅ Right side (Info or Logo)
        VBox infoBox = new VBox(15);
        infoBox.setAlignment(Pos.CENTER);
        infoBox.setPadding(new Insets(20));

        // Example: Add an image or text (your logo/title area)
        Text sideText = new Text(" GPA Calculator");
        sideText.setFont(Font.font("Segoe UI", FontWeight.BOLD, 60));
        sideText.setFill(Color.DEEPSKYBLUE);

        Text slogan = new Text("Track. Analyze. Improve.");
        slogan.setFont(Font.font("Segoe UI", FontPosture.ITALIC, 28));
        slogan.setFill(Color.LIGHTGRAY);

        infoBox.getChildren().addAll(sideText, slogan);

        // ✅ Main layout using BorderPane
        BorderPane mainLayout = new BorderPane();
        mainLayout.setLeft(formBox);
        mainLayout.setRight(infoBox);
        mainLayout.setStyle("-fx-background-color: linear-gradient(to bottom right, #0a0a1a, #1a1a2e);");

        // ✅ Fade animation
        FadeTransition fade = new FadeTransition(Duration.seconds(1.2), mainLayout);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.play();

        // ✅ Save button logic (same as before)
        saveBtn.setOnAction(e -> {
            try {
                String reg = regField.getText().trim();
                String courseTitle = titleField.getText().trim();
                String courseCode = codeField.getText().trim();
                int unit = Integer.parseInt(unitField.getText().trim());
                String session = sessionField.getText().trim();
                int level = Integer.parseInt(levelBox.getValue().trim());
                String semester = semesterBox.getValue();
                String grade = gradeBox.getValue();

                if (reg.isEmpty() || courseTitle.isEmpty() || courseCode.isEmpty()
                        || session.isEmpty() || semester == null || grade == null) {
                    statusLabel.setTextFill(Color.RED);
                    statusLabel.setText("⚠️ Please fill all fields!");
                    return;
                }

                Course c = new Course(courseTitle, unit, semester, session, courseCode, level, grade, reg);
                c.saveToDatabase();
                //c.display();

                statusLabel.setTextFill(Color.LIGHTGREEN);
                statusLabel.setText("✅ Course saved successfully!");
                regField.clear();
                titleField.clear();
                codeField.clear();
                unitField.clear();
                sessionField.clear();
                levelBox.setValue(null);
                semesterBox.setValue(null);
                gradeBox.setValue(null);

            } catch (NumberFormatException ex) {
                statusLabel.setTextFill(Color.RED);
                statusLabel.setText("❌ Level and Unit must be numbers!");
            } catch (Exception ex) {
                Logger.getLogger(AddCourseScreen.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        // ✅ Back button
        backBtn.setOnAction(e -> {
            homeScreen home = new homeScreen(stage);
            Scene homeScene = new Scene(home.getLayout(), 800, 600);
            stage.setScene(homeScene);
        });

        return mainLayout;
    }

    private String buttonStyle() {
        return "-fx-background-color: #12122a; -fx-text-fill: #00bfff; "
                + "-fx-font-weight: bold; -fx-padding: 10 20 10 20; "
                + "-fx-border-color: #00bfff; -fx-border-radius: 8; "
                + "-fx-background-radius: 8;";
    }
}
