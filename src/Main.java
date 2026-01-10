import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Application entry point for
 * School Result Management System
 */
public class Main extends Application {

    private static Stage mainStage;

    @Override
    public void start(Stage stage) {
        mainStage = stage;

        HomeScreen home = new HomeScreen();
        Scene homeScene = new Scene(home.getLayout(), 1600, 880);

        stage.setScene(homeScene);
        stage.setTitle("School Result Management System");
        stage.show();
    }

    /**
     * Centralized scene switcher
     */
    public static void switchScene(Scene newScene, String title) {
        mainStage.setScene(newScene);
        mainStage.setTitle(title);
    }

    public static Stage getMainStage() {
        return mainStage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}

