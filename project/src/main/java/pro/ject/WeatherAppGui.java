package pro.ject;

import org.json.simple.JSONObject;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class WeatherAppGui extends JFrame{
    private JTextField searchTextField;
    private JLabel weatherConditionImage;
    private JLabel temperatureText;
    private JLabel weatherConditionDesc;
    private JLabel humidityText;
    private JLabel humidityImage;
    private JLabel windspeedText;
    private JLabel windspeedImage;
    private JButton searchButton;
    private JSONObject weatherData;
    public WeatherAppGui(){
        super("Weather App");

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(450,700);
        setLocationRelativeTo(null);
        setLayout(null);
        setResizable(false);
        setBackground(new Color(173, 196, 206));
        addGuiComponents();

    }

    private void addGuiComponents(){

        searchTextField = new JTextField("Search Location");
        searchTextField.setForeground(new Color(153,153,153));
        searchTextField.setBounds(70,15,300,40);
        searchTextField.setFont(new Font("Dialog",Font.PLAIN,24));
        searchTextField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (searchTextField.getText().equals("Search Location")) {
                    searchTextField.setText("");
                }
                searchTextField.setForeground(Color.BLACK);
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (searchTextField.getText().isEmpty()) {
                    searchTextField.setText("Search Location");
                }
            }
        });

        searchTextField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performSearch();
            }
        });
        add(searchTextField);

        weatherConditionImage = new JLabel(loadImage("src/ImgWeather/clear_1.png"));
        weatherConditionImage.setBounds(0,125,450,217);
        add(weatherConditionImage);

        temperatureText = new JLabel("10 C");
        temperatureText.setBounds(0,360,450,54);
        temperatureText.setFont(new Font("Kanit",Font.BOLD,48));
        temperatureText.setHorizontalAlignment(SwingConstants.CENTER);
        add(temperatureText);

        weatherConditionDesc = new JLabel("Cloudy");
        weatherConditionDesc.setBounds(0,410,450,36);
        weatherConditionDesc.setFont(new Font("Dialog",Font.PLAIN,32));
        weatherConditionDesc.setHorizontalAlignment(SwingConstants.CENTER);
        add(weatherConditionDesc);

        humidityImage = new JLabel(loadImage("src/ImgWeather/humidity (1).png"));
        humidityImage.setBounds(30,530,74,66);
        add(humidityImage);

        humidityText = new JLabel("<html><b>Humidity</b> 100%</html>");
        humidityText.setBounds(110,530,85,55);
        humidityText.setFont(new Font("Dialog",Font.PLAIN,16));
        add(humidityText);

        windspeedImage = new JLabel(loadImage("src/ImgWeather/wind.png"));
        windspeedImage.setBounds(230,530,74,66);
        add(windspeedImage);

        windspeedText = new JLabel("<html><b>Windspeed</b> 15km/h</html>");
        windspeedText.setBounds(320,530,85,55);
        windspeedText.setFont(new Font("Dialog",Font.PLAIN,16));
        add(windspeedText);

        searchButton = new JButton(loadImage("src/ImgUi/search_2.png"));
        searchButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        searchButton.setBounds(375,12,45,45);
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performSearch();

            }
        });
        add(searchButton);