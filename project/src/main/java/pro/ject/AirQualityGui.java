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

    public AirQualityGui(Stage stage) {
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

        VBox layout = new VBox(15, lb_title, searchBox, resultGrid);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(15));
        layout.setStyle("-fx-background-color: linear-gradient(to bottom, #2196F3, #64B5F6);");

        Scene scene = new Scene(layout, 375, 667);
        stage.setScene(scene);
        stage.show();
    }

    public void fetchAirQuality() {
        String city = locationField.getText();
        String apiKey = "YOUR_AIR_QUALITY_API_KEY"; // 🔹 ใช้ API ที่เกี่ยวข้อง
        String url = "https://api.waqi.info/feed/" + city + "/?token=" + apiKey;

        try {
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                JSONObject jsonResponse = new JSONObject(response.body());
                JSONObject data = jsonResponse.getJSONObject("data");
                int aqi = data.getInt("aqi");

                airQualityIndex.setText(String.valueOf(aqi));
                pollutionText.setText(aqi > 100 ? "Poor" : "Good");
            }

        } catch (Exception e) {
            airQualityIndex.setText("Error");
            pollutionText.setText("N/A");
        }
    }
}
