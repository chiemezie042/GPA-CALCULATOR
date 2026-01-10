import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;

/**
 * Home screen for School Result Management System
 */
public class HomeScreen {

    private final VBox layout;

    public HomeScreen() {

        // === Title ===
        Text title = new Text("📚 School Result Management System");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 32));
        title.setFill(Color.CYAN);

        // === Buttons ===
        Button addStudentBtn = new Button("👤 Register Student");
        Button addCourseBtn = new Button("➕ Manage Courses");
        Button viewResultsBtn = new Button("📄 View Results");
        Button exitBtn = new Button("❌ Exit Application");

        // === Apply style ===
        addStudentBtn.setStyle(glowButtonStyle());
        addCourseBtn.setStyle(glowButtonStyle());
        viewResultsBtn.setStyle(glowButtonStyle());
        exitBtn.setStyle(glowButtonStyle());

        // === Button Actions (centralized navigation) ===
        addStudentBtn.setOnAction(e -> {
            AddStudentScreen screen = new AddStudentScreen();
            Scene scene = new Scene(screen.getLayout(), 1600, 880);
            Main.switchScene(scene, "Register Student");
        });

        addCourseBtn.setOnAction(e -> {
            AddCourseScreen screen = new AddCourseScreen();
            Scene scene = new Scene(screen.getLayout(), 1600, 880);
            Main.switchScene(scene, "Manage Courses");
        });

        viewResultsBtn.setOnAction(e -> {
            ViewResultScreen screen = new ViewResultScreen();
            Scene scene = new Scene(screen.getLayout(), 1600, 880);
            Main.switchScene(scene, "View Results");
        });

        exitBtn.setOnAction(e -> Main.getMainStage().close());

        // === Layout ===
        layout = new VBox(25, title, addStudentBtn, addCourseBtn, viewResultsBtn, exitBtn);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(40));
        layout.setStyle(
                "-fx-background-color: linear-gradient(to bottom right, #0a0a1a, #1a1a2e);"
        );

        // === Fade Animation ===
        FadeTransition fade = new FadeTransition(Duration.seconds(1.2), layout);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.play();
    }

    public VBox getLayout() {
        return layout;
    }

    // === Button style helper ===
    private String glowButtonStyle() {
        return "-fx-background-color: #12122a; "
             + "-fx-text-fill: #00bfff; "
             + "-fx-font-weight: bold; "
             + "-fx-padding: 12 25; "
             + "-fx-border-color: #00bfff; "
             + "-fx-border-radius: 10; "
             + "-fx-background-radius: 10; "
             + "-fx-effect: dropshadow(gaussian, #00bfff, 10, 0.5, 0, 0);";
    }
}
