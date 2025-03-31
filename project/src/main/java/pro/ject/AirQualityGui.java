package pro.ject;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.json.JSONObject;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class AirQualityGui {

    private TextField locationField;
    private Label airQualityIndex, pollutionText;
    private Button refreshButton;

    public AirQualityGui(Stage stage, Runnable onBack) {
        stage.setTitle("Air Quality App");
        stage.setResizable(false);

        Label lb_title = new Label("Air Quality");
        lb_title.setFont(new Font("Arial Bold", 24));
        lb_title.setTextFill(Color.WHITE);

        locationField = new TextField();
        locationField.setPromptText("Enter city name");

        refreshButton = new Button("Fetch Data");
        refreshButton.setOnAction(e -> fetchAirQuality());

        airQualityIndex = new Label("-");
        pollutionText = new Label("-");

        Button btnBack = new Button("Back");
        btnBack.setOnAction(e -> onBack.run());

        VBox searchBox = new VBox(8, locationField, refreshButton);
        searchBox.setAlignment(Pos.CENTER);

        GridPane resultGrid = new GridPane();
        resultGrid.setAlignment(Pos.CENTER);
        resultGrid.setHgap(15);
        resultGrid.setVgap(15);
        resultGrid.add(new Label("🌍 AQI:"), 0, 0);
        resultGrid.add(airQualityIndex, 1, 0);
        resultGrid.add(new Label("☁️ Pollution:"), 0, 1);
        resultGrid.add(pollutionText, 1, 1);

        VBox layout = new VBox(15, lb_title, searchBox, resultGrid, btnBack);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(15));
        layout.setStyle("-fx-background-color: linear-gradient(to bottom, #2196F3, #64B5F6);");

        Scene scene = new Scene(layout, 375, 667);
        stage.setScene(scene);
        stage.show();
    }

    public void fetchAirQuality() {
        String city = locationField.getText();
        org.json.simple.JSONObject airData = AirQualityApp.getAirQualityData(city);
    
        if (airData != null) {
            int aqi = Integer.parseInt(airData.get("aqi").toString());
            String condition = airData.get("aqi_condition").toString();
    
            airQualityIndex.setText(String.valueOf(aqi));
            pollutionText.setText(condition); // ✅ ใช้ค่าจาก convertAQICode
        } else {
            airQualityIndex.setText("Error");
            pollutionText.setText("N/A");
        }
    }
    
    
}
