
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.animation.FadeTransition;
import javafx.util.Duration;

public class homeScreen {

    private VBox layout;

    public homeScreen(Stage stage) {
        // === Title ===
        Text title = new Text("📚  GPA Calculator");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 32));
        title.setFill(Color.CYAN);

        // === Buttons ===
        Button addStudentBtn = new Button("👤 Add Student");
        Button addCourseBtn = new Button("➕ Add Course");
        Button viewResultsBtn = new Button("📄 View Results");
        Button exitBtn = new Button("❌ Exit App");

        // === Apply glowing style to all buttons ===
        addStudentBtn.setStyle(glowButtonStyle());
        addCourseBtn.setStyle(glowButtonStyle());
        viewResultsBtn.setStyle(glowButtonStyle());
        exitBtn.setStyle(glowButtonStyle());

        // === Button Actions ===
        addStudentBtn.setOnAction(e -> {
            AddStudentScreen studentScreen = new AddStudentScreen(stage);
            Scene studentScene = new Scene(studentScreen.getLayout(), 800, 600);
            stage.setScene(studentScene);
        });

        addCourseBtn.setOnAction(e -> {
            AddCourseScreen addScreen = new AddCourseScreen(stage);
            Scene addScene = new Scene(addScreen.getLayout(), 800, 600);
            stage.setScene(addScene);
        });

        viewResultsBtn.setOnAction(e -> {
            ViewResultScreen viewScreen = new ViewResultScreen(stage);
            Scene viewScene = new Scene(viewScreen.getLayout(), 800, 600);
            stage.setScene(viewScene);
        });

        exitBtn.setOnAction(e -> stage.close());

        // === Layout ===
        layout = new VBox(25, title, addStudentBtn, addCourseBtn, viewResultsBtn, exitBtn);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(40));
        layout.setStyle("-fx-background-color: linear-gradient(to bottom right, #0a0a1a, #1a1a2e);");

        // === Fade Animation ===
        FadeTransition fade = new FadeTransition(Duration.seconds(1.2), layout);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.play();
    }

    // ✅ Returns the layout to set in a Scene
    public VBox getLayout() {
        return layout;
    }

    // === Helper method for glowing button style ===
    private String glowButtonStyle() {
        return "-fx-background-color: #12122a; -fx-text-fill: #00bfff; "
                + "-fx-font-weight: bold; -fx-padding: 12 25 12 25; "
                + "-fx-border-color: #00bfff; -fx-border-radius: 10; "
                + "-fx-background-radius: 10; "
                + "-fx-effect: dropshadow(gaussian, #00bfff, 10, 0.5, 0, 0);";
    }
}
