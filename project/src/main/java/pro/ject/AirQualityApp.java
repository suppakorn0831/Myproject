package pro.ject;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class AirQualityApp {

    public static JSONObject getAirQualityData(String locationName) {
        locationName = locationName.replaceAll(" ", "+");

        String urlString = "https://api.waqi.info/feed/" + locationName +
                "/?token=6173fbbd843ceb3edf1ae6e7bf87234dd04c4188";

        try {
            HttpURLConnection connection = fetchApiResponse(urlString);
            if (connection.getResponseCode() != 200) {
                System.out.println("Error: Could not connect to API");
                return null;
            } else {
                StringBuilder resultJson = new StringBuilder();
                Scanner scanner = new Scanner(connection.getInputStream());

                while (scanner.hasNext()) {
                    resultJson.append(scanner.nextLine());
                }
                scanner.close();
                connection.disconnect();

                JSONParser parser = new JSONParser();
                JSONObject resultJsonObj = (JSONObject) parser.parse(resultJson.toString());

                // Check for status before accessing data
                String status = (String) resultJsonObj.get("status");
                if (!"ok".equalsIgnoreCase(status)) {
                    System.out.println("API returned status: " + status);
                    return null;
                }

                JSONObject dataObj = (JSONObject) resultJsonObj.get("data");
                Object aqiObj = dataObj.get("aqi");

                if (aqiObj == null || aqiObj.toString().equals("-")) {
                    System.out.println("No AQI data available.");
                    return null;
                }

                int aqi = Integer.parseInt(aqiObj.toString());
                String aqiCondition = convertAQICode(aqi);

                JSONObject airQualityData = new JSONObject();
                airQualityData.put("aqi", aqi);
                airQualityData.put("aqi_condition", aqiCondition);
                return airQualityData;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static HttpURLConnection fetchApiResponse(String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            return connection;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String convertAQICode(long aqiCode) {
        if (aqiCode >= 0 && aqiCode <= 50) {
            return "GOOD";
        } else if (aqiCode >= 51 && aqiCode <= 100) {
            return "MODERATE";
        } else if (aqiCode >= 101 && aqiCode <= 200) {
            return "UNHEALTHY";
        } else if (aqiCode >= 201) {
            return "HAZARDOUS";
        }
        return "UNKNOWN";
    }
}
