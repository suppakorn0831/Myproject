package pro.ject;

import javafx.scene.control.CheckBox;
import org.json.JSONObject;
import org.json.JSONArray;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class GetAPI {
    private static final String API_key = "7c1388f022de59cbcc9be442d5fcc866";
    private static final HttpClient client = HttpClient.newHttpClient(); // ใช้ HttpClient แบบ singleton

    static int request(String cityName, CheckBox cb_usSys) {

        String url = "https://api.openweathermap.org/data/2.5/weather?q=" + cityName + "&appid=" + API_key;

        try {
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                JSONObject jsonResponse = new JSONObject(response.body());

                JSONObject mainData = jsonResponse.getJSONObject("main");
                double temp = mainData.getDouble("temp");
                int humidity = mainData.getInt("humidity");

                JSONObject windData = jsonResponse.getJSONObject("wind");
                double windSpeed = windData.getDouble("speed");

                JSONArray weatherArray = jsonResponse.getJSONArray("weather");
                String weatherDescription = weatherArray.getJSONObject(0).getString("description");

                // แปลงอุณหภูมิจาก Kelvin -> Celsius
                temp = temp - 273.15;

                boolean usSys = false;
                if (cb_usSys.isSelected()) {
                    usSys = true;
                    windSpeed = windSpeed * 2.237;  // แปลง m/s -> mph
                    temp = (temp * 1.8) + 32;  // แปลง C -> F
                }

                temp = Double.parseDouble(String.format(Locale.ENGLISH, "%.2f", temp));
                windSpeed = Double.parseDouble(String.format(Locale.ENGLISH, "%.2f", windSpeed));

                LocalDateTime currentDateTime = LocalDateTime.now();
                String date = currentDateTime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));

                Weather.getSearchHistory().add(new Weather(cityName, temp,
                        humidity, weatherDescription, windSpeed, usSys, date));

                return 0;
            }

        } catch (IOException ioe) {
            System.out.println("IOException");
        } catch (InterruptedException ie) {
            System.out.println("Interrupted Exception");
        }

        return 1;
    }
}
