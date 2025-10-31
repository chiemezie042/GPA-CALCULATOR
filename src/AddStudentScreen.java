
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.animation.FadeTransition;
import javafx.stage.Stage;
import javafx.util.Duration;

public class AddStudentScreen {

    private final Stage stage;

    // ✅ Constructor that accepts Stage
    public AddStudentScreen(Stage stage) {
        this.stage = stage;
    }

    public VBox getLayout() {
        Text title = new Text("Add New Student");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 26));
        title.setFill(javafx.scene.paint.Color.CYAN);

        TextField regField = new TextField();
        regField.setPromptText("Registration Number (e.g. 2024/123456)");

        TextField nameField = new TextField();
        nameField.setPromptText("Student Name");

        TextField levelField = new TextField();
        levelField.setPromptText("Level (100, 200, 300...)");

        TextField sessionField = new TextField();
        sessionField.setPromptText("Session (e.g. 2024/2025)");

        ComboBox<String> semesterBox = new ComboBox<>();
        semesterBox.getItems().addAll("First", "Second");
        semesterBox.setPromptText("Select Semester");

        Button saveBtn = new Button("💾 Save Student");
        Button backBtn = new Button("⬅ Back to Home");
        saveBtn.setStyle(buttonStyle());
        backBtn.setStyle(buttonStyle());

        Label message = new Label();
        message.setTextFill(javafx.scene.paint.Color.LIGHTGREEN);

        VBox layout = new VBox(12, title, regField, nameField, levelField,
                sessionField, semesterBox, saveBtn, backBtn, message);
        layout.setPadding(new Insets(30));
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: linear-gradient(to bottom right, #0a0a1a, #1a1a2e);");

        FadeTransition fade = new FadeTransition(Duration.seconds(1.2), layout);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.play();

        // ✅ Save Student Logic
        saveBtn.setOnAction(e -> {
            try {
                Student s = new Student(
                        regField.getText(),
                        nameField.getText(),
                        Integer.parseInt(levelField.getText()),
                        sessionField.getText(),
                        semesterBox.getValue()
                );
                s.saveToDatabase();
                message.setText("✅ Student Saved Successfully!");
            } catch (Exception ex) {
                message.setText("❌ Error: " + ex.getMessage());
            }
        });

        // ✅ Back to Home
        backBtn.setOnAction(e -> {
            homeScreen home = new homeScreen(stage);
            Scene homeScene = new Scene(home.getLayout(), 800, 600);
            stage.setScene(homeScene);
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
