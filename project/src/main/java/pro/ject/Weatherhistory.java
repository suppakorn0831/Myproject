package pro.ject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class Weatherhistory {
    private static ObservableList<Weatherhistory> searchHistory = FXCollections.observableArrayList();
    private String date;
    private String city;
    private double temp;
    private String state;
    private double windSpeed;
    private double humidity;
    private boolean usSys;

    public Weatherhistory(String city, double temp, double humidity, String state, double windSpeed,
                   boolean usSys, String date){
        this.city = city;
        this.humidity = humidity;
        this.temp = temp;
        this.windSpeed = windSpeed;
        this.state = state;
        this.usSys = usSys;
        this.date = date;
    }

    public String getCity(){
        return city;
    }

    public static ObservableList<Weatherhistory> getSearchHistory() {
        return searchHistory;
    }

    public double getTemp() {
        return temp;
    }

    public String getState() {
        return state;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public double getHumidity() {
        return humidity;
    }

    public boolean getUsSys(){
        return usSys;
    }

    public String getDate(){
        return date;
    }
}