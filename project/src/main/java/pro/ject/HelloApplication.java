package pro.ject;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.animation.FadeTransition;
import javafx.util.Duration;

public class HelloApplication extends Application {

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("Weather App");
        stage.setResizable(false);
        showMainMenu(stage);
    }

    private void showMainMenu(Stage stage) {
        Label lb_title = new Label("Weather & Air App");
        lb_title.setFont(new Font("Arial Rounded MT Bold", 28));
        lb_title.setTextFill(Color.WHITE);

        ImageView iconView = new ImageView(new Image(getClass().getClassLoader().getResource("asset/weatherapp_images/cloudy.png").toExternalForm()));
        iconView.setFitWidth(50);
        iconView.setFitHeight(50);

        VBox titleBox = new VBox(iconView, lb_title);
        titleBox.setAlignment(Pos.CENTER);
        titleBox.setSpacing(10);

        Button btn_weather = createButton("Weather App", "#FF9800");
        btn_weather.setOnAction(e -> new WeatherAppGui(stage, () -> showMainMenu(stage)));

        Button btn_airQuality = createButton("Air Quality", "#4CAF50");
        btn_airQuality.setOnAction(e -> new AirQualityGui(stage, () -> showMainMenu(stage)));

        VBox layout = new VBox(20, titleBox, btn_weather, btn_airQuality);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(30));
        layout.setStyle("-fx-background-color: linear-gradient(to bottom, #2196F3, #64B5F6);");

        Scene scene = new Scene(layout, 375, 667);
        stage.setScene(scene);
        stage.show();

        applyFadeTransition(layout);
    }

    private Button createButton(String text, String color) {
        Button button = new Button(text);
        button.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-family: 'Arial Rounded MT Bold'; -fx-background-radius: 20px; -fx-padding: 10px 20px;");
        button.setPrefWidth(200);
        return button;
    }

    private void applyFadeTransition(VBox layout) {
        FadeTransition ft = new FadeTransition(Duration.millis(800), layout);
        ft.setFromValue(0);
        ft.setToValue(1);
        ft.play();
    }
}