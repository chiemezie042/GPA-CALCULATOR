
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    private static Stage mainStage;

    @Override
    public void start(Stage stage) {
        mainStage = stage;
        homeScreen home = new homeScreen(stage);
        Scene homeScene = new Scene(home.getLayout(), 500, 600);

        stage.setScene(homeScene);
        stage.setTitle(" GPA Calculator");
        stage.show();
    }

    // ✅ Static method to switch between screens
    public static void switchScene(Scene newScene, String title) {
        mainStage.setScene(newScene);
        mainStage.setTitle(title);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
