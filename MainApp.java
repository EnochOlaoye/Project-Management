import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainApp extends Application {
    
    private Scene scene;
    private boolean isDarkMode = false;
    
    @Override
    public void start(Stage stage) {
        // Create layout
        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setPrefSize(400, 300);
        
        // Title
        Label title = new Label("Solutions 4 U");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        
        // Sample text
        Label sampleText = new Label("Cost analysis and saving");
        
        // Theme toggle button
        Button themeButton = new Button("🌙 Switch to Dark Mode");
        themeButton.setOnAction(e -> {
            if (isDarkMode) {
                // Switch to light mode
                scene.getStylesheets().clear();
                scene.getStylesheets().add(getClass().getResource("light-theme.css").toExternalForm());
                themeButton.setText("🌙 Switch to Dark Mode");
                isDarkMode = false;
            } else {
                // Switch to dark mode
                scene.getStylesheets().clear();
                scene.getStylesheets().add(getClass().getResource("dark-theme.css").toExternalForm());
                themeButton.setText("☀️ Switch to Light Mode");
                isDarkMode = true;
            }
        });
        
        // Add everything to layout
        root.getChildren().addAll(title, sampleText, themeButton);
        
        // Create scene
        scene = new Scene(root);
        
        // Apply light theme by default
        scene.getStylesheets().add(getClass().getResource("light-theme.css").toExternalForm());
        
        // Setup stage
        stage.setTitle("Solutions 4 U");
        stage.setScene(scene);
        stage.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}