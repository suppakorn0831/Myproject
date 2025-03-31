package pro.ject;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.IOException;
import java.util.Locale;

public class HelloApplication extends Application {

    private TextField tf_city;
    private Label lb_temperatureValue = new Label("-");
    private Label lb_windSpeedValue = new Label("-");
    private Label lb_humidityValue = new Label("-");
    private Label lb_weatherStateValue = new Label("-");
    private CheckBox cb_americanSys;

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        stage.setTitle("Weather App");
        stage.setResizable(false);
        showMainMenu(stage);
    }

    private void showMainMenu(Stage stage) {
        Label lb_title = new Label("Main Menu");
        lb_title.setFont(new Font("Arial Bold", 24));
        lb_title.setTextFill(Color.WHITE);

        Button btn_weather = createButton("Weather App", "#FF9800");
        btn_weather.setOnAction(e -> showWeatherScreen(stage));

        Button btn_airQuality = createButton("Air Quality", "#03A9F4");
        btn_airQuality.setOnAction(e -> new AirQualityGui(stage, () -> showMainMenu(stage)));

        VBox layout = new VBox(15, lb_title, btn_weather, btn_airQuality);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: linear-gradient(to bottom, #2196F3, #64B5F6);");

        stage.setScene(new Scene(layout, 375, 667));
        stage.show();
    }

    private void showWeatherScreen(Stage stage) {
        Label lb_title = new Label("Weather App");
        lb_title.setFont(new Font("Arial Bold", 24));
        lb_title.setTextFill(Color.WHITE);

        Image weatherImage = new Image(getClass().getClassLoader().getResource("asset/weatherapp_images/cloudy.png").toExternalForm());
        ImageView weatherIcon = new ImageView(weatherImage);
        weatherIcon.setFitWidth(40);
        weatherIcon.setFitHeight(40);

        HBox titleBox = new HBox(10, weatherIcon, lb_title);
        titleBox.setAlignment(Pos.CENTER);
        titleBox.setPadding(new Insets(10, 0, 5, 0));

        tf_city = new TextField();
        tf_city.setPromptText("Enter city name");
        tf_city.setStyle("-fx-font-size: 14px; -fx-background-radius: 10px;");
        tf_city.setPrefWidth(220);

        cb_americanSys = new CheckBox("Use Fahrenheit");
        cb_americanSys.setStyle("-fx-text-fill: white; -fx-font-size: 12px;");

        Button btn_search = createButton("Search", "#FF9800");
        btn_search.setPrefWidth(120);
        btn_search.setOnAction(e -> searchBTN());

        VBox searchBox = new VBox(8, tf_city, cb_americanSys, btn_search);
        searchBox.setAlignment(Pos.CENTER);

        GridPane resultGrid = new GridPane();
        resultGrid.setAlignment(Pos.CENTER);
        resultGrid.setHgap(15);
        resultGrid.setVgap(15);
        resultGrid.setStyle("-fx-background-color: rgba(255, 255, 255, 0.2); -fx-background-radius: 15px; -fx-padding: 15px;");

        resultGrid.add(createLabel("\uD83C\uDF21 Temp:"), 0, 0);
        resultGrid.add(lb_temperatureValue, 1, 0);
        resultGrid.add(createLabel("\uD83D\uDCA7 Humidity:"), 0, 1);
        resultGrid.add(lb_humidityValue, 1, 1);
        resultGrid.add(createLabel("\uD83C\uDF2C Wind:"), 0, 2);
        resultGrid.add(lb_windSpeedValue, 1, 2);
        resultGrid.add(createLabel("\uD83C\uDF24 State:"), 0, 3);
        resultGrid.add(lb_weatherStateValue, 1, 3);

        Button btn_history = createButton("History", "#4CAF50");
        btn_history.setPrefWidth(120);
        btn_history.setOnAction(e -> historyBTN());

        Button btn_back = createButton("Back", "#F44336");
        btn_back.setOnAction(e -> showMainMenu(stage));

        VBox historyBox = new VBox(15, btn_history, btn_back);
        historyBox.setAlignment(Pos.CENTER);

        VBox layout = new VBox(15, titleBox, searchBox, resultGrid, historyBox);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(15));
        layout.setStyle("-fx-background-color: linear-gradient(to bottom, #2196F3, #64B5F6);");

        stage.setScene(new Scene(layout, 375, 667));
        stage.show();
    }

    private void searchBTN() {
        String city = tf_city.getText();
        if (isAlpha(city)) {
            if (Weatherapi.request(city, cb_americanSys) == 0) {
                String degree = cb_americanSys.isSelected() ? "°F" : "°C";
                String speedUnit = cb_americanSys.isSelected() ? "mph" : "m/s";

                Weatherhistory latestWeather = Weatherhistory.getSearchHistory().get(Weatherhistory.getSearchHistory().size() - 1);

                lb_temperatureValue.setText(String.format(Locale.ENGLISH, "%.1f %s", latestWeather.getTemp(), degree));
                lb_humidityValue.setText(latestWeather.getHumidity() + " %");
                lb_weatherStateValue.setText(latestWeather.getState());
                lb_windSpeedValue.setText(String.format(Locale.ENGLISH, "%.1f %s", latestWeather.getWindSpeed(), speedUnit));

                animateLabel(lb_temperatureValue);
                animateLabel(lb_humidityValue);
                animateLabel(lb_weatherStateValue);
                animateLabel(lb_windSpeedValue);
            } else {
                showAlert("Error", "City not found", Color.RED);
            }
        } else {
            showAlert("Error", "Invalid City Name", Color.RED);
        }
    }

    private void historyBTN() {
        TableView<Weatherhistory> table = new TableView<>();
        table.setMaxWidth(320);

        table.getColumns().addAll(
                createColumn("Date", "date", 80),
                createColumn("City", "city", 80),
                createColumn("Temp", "temp", 80),
                createColumn("Wind", "windSpeed", 80)
        );

        table.setItems(Weatherhistory.getSearchHistory());

        Scene historyScene = new Scene(table, 375, 500);
        Stage stage = new Stage();
        stage.setTitle("History");
        stage.setScene(historyScene);
        stage.show();
    }

    private <T> TableColumn<Weatherhistory, T> createColumn(String title, String property, int width) {
        TableColumn<Weatherhistory, T> column = new TableColumn<>(title);
        column.setCellValueFactory(new PropertyValueFactory<>(property));
        column.setPrefWidth(width);
        return column;
    }

    private void showAlert(String title, String message, Color color) {
        Label label = new Label(message);
        label.setTextFill(color);
        label.setFont(new Font("Arial", 16));
        label.setAlignment(Pos.CENTER);

        Scene scene = new Scene(new StackPane(label), 300, 150);
        Stage stage = new Stage();
        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
    }

    private boolean isAlpha(String n) {
        return n.matches("[a-zA-Z ]+");
    }

    private Label createLabel(String text) {
        Label label = new Label(text);
        label.setFont(new Font("Arial", 14));
        label.setTextFill(Color.WHITE);
        return label;
    }

    private Button createButton(String text, String color) {
        Button button = new Button(text);
        button.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; -fx-font-size: 14px; -fx-background-radius: 15px;");
        return button;
    }

    private void animateLabel(Label label) {
        FadeTransition fade = new FadeTransition(Duration.millis(500), label);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.play();
    }
}
