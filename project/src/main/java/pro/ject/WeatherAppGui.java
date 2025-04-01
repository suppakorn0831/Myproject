package pro.ject;

import javafx.animation.FadeTransition;
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

import java.net.URL;
import java.util.Locale;

public class WeatherAppGui {

    private final Runnable onBack;

    private TextField tf_city;
    private Label lb_temperatureValue = new Label("--");
    private Label lb_windSpeedValue = new Label("--");
    private Label lb_humidityValue = new Label("--");
    private Label lb_weatherStateValue = new Label("--");
    private CheckBox cb_americanSys;
    private ImageView weatherIcon = new ImageView();

    public WeatherAppGui(Stage stage, Runnable onBack) {
        this.onBack = onBack;
        stage.setTitle("Weather App");
        stage.setResizable(false);

        Label lb_title = new Label("Weather Forecast");
        lb_title.setFont(new Font("Arial Rounded MT Bold", 24));
        lb_title.setTextFill(Color.WHITE);

        tf_city = new TextField();
        tf_city.setPromptText("Enter city name");
        tf_city.setPrefWidth(180);

        ImageView searchIcon = loadIcon("/asset/weatherapp_images/search.png", 20, 20);
        Button btn_search = new Button("Search", searchIcon);
        btn_search.setContentDisplay(ContentDisplay.LEFT);
        btn_search.setOnAction(e -> searchBTN());
        btn_search.setStyle("-fx-background-color: #FF9800; -fx-text-fill: white; -fx-font-size: 14px; -fx-background-radius: 15px;");

        HBox searchBox = new HBox(tf_city, btn_search);
        searchBox.setSpacing(10);
        searchBox.setAlignment(Pos.CENTER);

        cb_americanSys = new CheckBox("Fahrenheit");
        cb_americanSys.setTextFill(Color.WHITE);

        VBox mainCard = new VBox(5);
        mainCard.setAlignment(Pos.CENTER);
        mainCard.setPadding(new Insets(20));
        mainCard.setStyle("-fx-background-color: rgba(255,255,255,0.1); -fx-background-radius: 20px;");

        weatherIcon.setFitHeight(80);
        weatherIcon.setFitWidth(80);

        lb_temperatureValue.setFont(new Font(36));
        lb_temperatureValue.setTextFill(Color.WHITE);
        lb_weatherStateValue.setFont(new Font(16));
        lb_weatherStateValue.setTextFill(Color.LIGHTGRAY);

        mainCard.getChildren().addAll(weatherIcon, lb_temperatureValue, lb_weatherStateValue);

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(20);
        grid.setVgap(10);

        grid.add(createInfoBox("Humidity", "humidity.png", lb_humidityValue), 0, 0);
        grid.add(createInfoBox("Wind", "windspeed.png", lb_windSpeedValue), 1, 0);

        Button btn_history = createButton("History", "#4CAF50");
        btn_history.setPrefWidth(120);
        btn_history.setOnAction(e -> historyBTN());

        Button btn_back = createButton("Back", "#F44336");
        btn_back.setOnAction(e -> onBack.run());

        VBox layout = new VBox(15, lb_title, searchBox, cb_americanSys, mainCard, grid, btn_history, btn_back);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(15));
        layout.setStyle("-fx-background-color: linear-gradient(to bottom, #2196F3, #64B5F6);");

        stage.setScene(new Scene(layout, 375, 667));
        stage.show();

        animate(mainCard);
    }

    private VBox createInfoBox(String label, String iconName, Label valueLabel) {
        VBox box = new VBox(5);
        box.setAlignment(Pos.CENTER);

        ImageView icon = loadIcon("/asset/weatherapp_images/" + iconName, 25, 25);

        Label lb = new Label(label);
        lb.setTextFill(Color.WHITE);
        lb.setFont(new Font(12));

        valueLabel.setTextFill(Color.LIGHTGRAY);
        valueLabel.setFont(new Font(14));

        if (icon != null) box.getChildren().add(icon);
        box.getChildren().addAll(valueLabel, lb);
        return box;
    }

    private ImageView loadIcon(String path, int width, int height) {
        URL imageUrl = getClass().getResource(path);
        if (imageUrl != null) {
            ImageView img = new ImageView(new Image(imageUrl.toExternalForm()));
            img.setFitWidth(width);
            img.setFitHeight(height);
            return img;
        } else {
            System.out.println("⚠️ ไม่พบไฟล์: " + path);
            return null;
        }
    }

    private void updateWeatherIcon(String state) {
        String iconFile = "cloudy.png";
        if (state.toLowerCase().contains("clear")) {
            iconFile = "clear.png";
        } else if (state.toLowerCase().contains("rain")) {
            iconFile = "rain.png";
        } else if (state.toLowerCase().contains("snow")) {
            iconFile = "snow.png";
        } else if (state.toLowerCase().contains("cloud")) {
            iconFile = "cloudy.png";
        }
        ImageView icon = loadIcon("/asset/weatherapp_images/" + iconFile, 80, 80);
        if (icon != null) {
            weatherIcon.setImage(icon.getImage());
        }
    }

    private void searchBTN() {
        String city = tf_city.getText();
        if (isAlpha(city)) {
            if (Weatherapi.request(city, cb_americanSys) == 0) {
                String degree = cb_americanSys.isSelected() ? "\u00B0F" : "\u00B0C";
                String speedUnit = cb_americanSys.isSelected() ? "mph" : "m/s";

                Weatherhistory latestWeather = Weatherhistory.getSearchHistory().get(
                        Weatherhistory.getSearchHistory().size() - 1);

                lb_temperatureValue.setText(String.format(Locale.ENGLISH, "%.1f %s", latestWeather.getTemp(), degree));
                lb_humidityValue.setText(latestWeather.getHumidity() + " %");
                lb_weatherStateValue.setText(latestWeather.getState());
                lb_windSpeedValue.setText(String.format(Locale.ENGLISH, "%.1f %s", latestWeather.getWindSpeed(), speedUnit));

                updateWeatherIcon(latestWeather.getState());

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

    private void animate(Region node) {
        FadeTransition fade = new FadeTransition(Duration.millis(500), node);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.play();
    }
}
