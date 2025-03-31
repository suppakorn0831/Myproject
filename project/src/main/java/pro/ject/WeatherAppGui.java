package pro.ject;

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

public class WeatherAppGui {

    private TextField searchTextField;
    private ImageView weatherImage;
    private Label temperatureText, humidityText, windspeedText;
    private Button searchButton;

    public WeatherAppGui(Stage stage) {
        stage.setTitle("Weather App");
        stage.setResizable(false);

        // 🌤️ ชื่อแอป + ไอคอน
        Label lb_title = new Label("Weather App");
        lb_title.setFont(new Font("Arial Bold", 24));
        lb_title.setTextFill(Color.WHITE);

        Image weatherIconImg = new Image(getClass().getClassLoader().getResource("asset/weatherapp_images/cloudy.png").toExternalForm());
        weatherImage = new ImageView(weatherIconImg);
        weatherImage.setFitWidth(50);
        weatherImage.setFitHeight(50);

        HBox titleBox = new HBox(10, weatherImage, lb_title);
        titleBox.setAlignment(Pos.CENTER);
        titleBox.setPadding(new Insets(10, 0, 5, 0));

        // 🔍 ค้นหาเมือง
        searchTextField = new TextField();
        searchTextField.setPromptText("Enter city name");
        searchTextField.setPrefWidth(220);

        searchButton = new Button("Search");
        searchButton.setOnAction(e -> performSearch());

        VBox searchBox = new VBox(8, searchTextField, searchButton);
        searchBox.setAlignment(Pos.CENTER);

        // 📊 แสดงผลข้อมูล
        temperatureText = new Label("-");
        humidityText = new Label("-");
        windspeedText = new Label("-");

        GridPane resultGrid = new GridPane();
        resultGrid.setAlignment(Pos.CENTER);
        resultGrid.setHgap(15);
        resultGrid.setVgap(15);
        resultGrid.setStyle("-fx-background-color: rgba(255, 255, 255, 0.2); -fx-background-radius: 15px; -fx-padding: 15px;");

        resultGrid.add(new Label("🌡 Temp:"), 0, 0);
        resultGrid.add(temperatureText, 1, 0);
        resultGrid.add(new Label("💧 Humidity:"), 0, 1);
        resultGrid.add(humidityText, 1, 1);
        resultGrid.add(new Label("💨 Wind:"), 0, 2);
        resultGrid.add(windspeedText, 1, 2);

        VBox layout = new VBox(15, titleBox, searchBox, resultGrid);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(15));
        layout.setStyle("-fx-background-color: linear-gradient(to bottom, #2196F3, #64B5F6);");

        Scene scene = new Scene(layout, 375, 667);
        stage.setScene(scene);
        stage.show();
    }

    private void performSearch() {
        String city = searchTextField.getText();
        if (!city.isEmpty()) {
            // ดึงข้อมูลจาก API
            int status = Weatherapi.request(city, new CheckBox());
            if (status == 0) {
                Weatherhistory latestWeather = Weatherhistory.getSearchHistory().get(Weatherhistory.getSearchHistory().size() - 1);
                temperatureText.setText(String.format("%.1f °C", latestWeather.getTemp()));
                humidityText.setText(latestWeather.getHumidity() + " %");
                windspeedText.setText(String.format("%.1f m/s", latestWeather.getWindSpeed()));
            } else {
                showAlert("Error", "City not found", Color.RED);
            }
        }
    }

    private void showAlert(String title, String message, Color color) {
        Label label = new Label(message);
        label.setTextFill(color);
        label.setFont(new Font("Arial", 16));

        Scene scene = new Scene(new StackPane(label), 300, 150);
        Stage stage = new Stage();
        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
    }
}
