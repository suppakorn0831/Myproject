package pro.ject;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

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
        Label lb_title = new Label("Main Menu");
        lb_title.setFont(new Font("Arial Bold", 24));
        lb_title.setTextFill(Color.WHITE);

        Button btn_weather = createButton("Weather App", "#FF9800");
        btn_weather.setOnAction(e -> new WeatherAppGui(stage, () -> showMainMenu(stage)));

        Button btn_airQuality = createButton("Air Quality", "#03A9F4");
        btn_airQuality.setOnAction(e -> new AirQualityGui(stage, () -> showMainMenu(stage)));

        VBox layout = new VBox(15, lb_title, btn_weather, btn_airQuality);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: linear-gradient(to bottom, #2196F3, #64B5F6);");

        stage.setScene(new Scene(layout, 375, 667));
        stage.show();
    }

    private Button createButton(String text, String color) {
        Button button = new Button(text);
        button.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; -fx-font-size: 14px; -fx-background-radius: 15px;");
        return button;
    }
}
