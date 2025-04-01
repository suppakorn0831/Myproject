package pro.ject;

import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.json.simple.JSONObject;

public class AirQualityGui {

    private TextField locationField;
    private Label airQualityIndex, pollutionText;
    private Button refreshButton;
    private ImageView iconView = new ImageView();

    public AirQualityGui(Stage stage, Runnable onBack) {
        stage.setTitle("Air Quality App");
        stage.setResizable(false);

        Label lb_title = new Label("Air Quality");
        lb_title.setFont(new Font("Arial Bold", 24));
        lb_title.setTextFill(Color.WHITE);

        locationField = new TextField();
        locationField.setPromptText("Enter city name");
        locationField.setMaxWidth(200);

        refreshButton = new Button("Fetch Data");
        refreshButton.setStyle("-fx-background-color: #FF9800; -fx-text-fill: white; -fx-background-radius: 15px;");
        refreshButton.setOnAction(e -> fetchAirQuality());

        airQualityIndex = new Label("-");
        airQualityIndex.setFont(new Font(36));
        airQualityIndex.setTextFill(Color.WHITE);

        pollutionText = new Label("-");
        pollutionText.setFont(new Font(16));
        pollutionText.setTextFill(Color.LIGHTGRAY);

        iconView.setFitWidth(80);
        iconView.setFitHeight(80);

        VBox cardBox = new VBox(10, iconView, airQualityIndex, pollutionText);
        cardBox.setAlignment(Pos.CENTER);
        cardBox.setPadding(new Insets(20));
        cardBox.setStyle("-fx-background-color: rgba(255,255,255,0.1); -fx-background-radius: 20px;");

        Button btnBack = new Button("Back");
        btnBack.setStyle("-fx-background-color: #F44336; -fx-text-fill: white; -fx-background-radius: 15px;");
        btnBack.setOnAction(e -> onBack.run());

        VBox searchBox = new VBox(10, locationField, refreshButton);
        searchBox.setAlignment(Pos.CENTER);

        VBox layout = new VBox(20, lb_title, searchBox, cardBox, btnBack);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: linear-gradient(to bottom, #2196F3, #64B5F6);");

        Scene scene = new Scene(layout, 375, 667);
        stage.setScene(scene);
        stage.show();
    }

    public void fetchAirQuality() {
        String city = locationField.getText();
        JSONObject airData = AirQualityApi.getAirQualityData(city);

        if (airData != null) {
            int aqi = Integer.parseInt(airData.get("aqi").toString());
            String condition = airData.get("aqi_condition").toString();

            airQualityIndex.setText(String.valueOf(aqi));
            pollutionText.setText(condition);
            updateAQIIcon(aqi);

            animateLabel(airQualityIndex);
            animateLabel(pollutionText);
        } else {
            airQualityIndex.setText("Error");
            pollutionText.setText("N/A");
        }
    }

    private void updateAQIIcon(int aqi) {
        String iconName = "clear.png";
        if (aqi > 150) {
            iconName = "cloudy.png";
        } else if (aqi > 100) {
            iconName = "humidity.png";
        } else if (aqi > 50) {
            iconName = "windspeed.png";
        }
        try {
            Image image = new Image(getClass().getResource("/asset/weatherapp_images/" + iconName).toExternalForm());
            iconView.setImage(image);
        } catch (Exception e) {
            System.out.println("⚠️ ไม่พบไอคอน AQI: " + iconName);
        }
    }

    private void animateLabel(Label label) {
        FadeTransition fade = new FadeTransition(Duration.millis(500), label);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.play();
    }
}    
