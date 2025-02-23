package pro.ject;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.imageio.IIOException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class AirQualityApp {
    //private String token = "6173fbbd843ceb3edf1ae6e7bf87234dd04c4188";

    public static JSONObject getAirQualityData(String locationName){
        locationName = locationName.replaceAll(" ","+");

        String urlString = "https://api.waqi.info/feed/"+ locationName +
                "/?token=6173fbbd843ceb3edf1ae6e7bf87234dd04c4188";

        try{
            HttpURLConnection connection = fetchApiResponse(urlString);
            if (connection.getResponseCode() != 200){
                System.out.println("Error: Could not connect to API");
                return null;
            }else{
                StringBuilder resultJson = new StringBuilder();
                Scanner scanner = new Scanner(connection.getInputStream());

                while(scanner.hasNext()){
                    resultJson.append(scanner.nextLine());
                }
                scanner.close();
                connection.disconnect();

                JSONParser parser = new JSONParser();
                JSONObject resultJsonObj = (JSONObject) parser.parse(resultJson.toString());
                JSONObject dataObj = (JSONObject) resultJsonObj.get("data");

                int aqi = Integer.parseInt(dataObj.get("aqi").toString());
                String aqiCondition = convertAQICode(Long.parseLong(dataObj.get("aqi").toString()));


                JSONObject airQualityData = new JSONObject();
                airQualityData.put("aqi",aqi);
                airQualityData.put("aqi_condition",aqiCondition);
                return airQualityData;
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
    private static HttpURLConnection fetchApiResponse(String urlString){
        try{
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");

            connection.connect();
            return connection;
        }catch(IOException e){
            e.printStackTrace();
        }
        return null;
    }

    private static String convertAQICode(long aqiCode){
        String aqiCondition = "";
        if(aqiCode >= 0 && aqiCode <= 50 ){
            aqiCondition = "GOOD";
        } else if (aqiCode >= 51 && aqiCode <= 100) {
            aqiCondition = "MODERATE";
        } else if (aqiCode >= 101 && aqiCode <= 200) {
            aqiCondition = "UNHEALTHY";
        } else if (aqiCode >= 201) {
            aqiCondition = "HAZARDOUS";
        }

        return aqiCondition;
    }
}