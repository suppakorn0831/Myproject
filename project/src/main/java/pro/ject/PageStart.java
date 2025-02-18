package pro.ject;


import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class PageStart extends JFrame {
    public PageStart(){
        super("Weather ForeCasts");
        setSize(450,700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);
        setLayout(null);
        setLocationRelativeTo(null);
        setBackground(new Color(217,217,217));
        addGuiComponents();
        //Weather App

    }
    private void addGuiComponents(){

        JLabel weatherImg = new JLabel(loadImage("src/ImgStrat/start1.png"));
        weatherImg.setBounds(92,90,256,256);
        add(weatherImg);

        JLabel weatherText = new JLabel("Weather");
        weatherText.setBounds(0,350,450,36);
        weatherText.setFont(new Font("Kanit",Font.BOLD,48));
        weatherText.setHorizontalAlignment(SwingConstants.CENTER);
        //weatherText.setVerticalAlignment(SwingConstants.CENTER);
        add(weatherText);

        JLabel forecastsText = new JLabel("ForeCasts");
        forecastsText.setBounds(0,400,450,36);
        forecastsText.setFont(new Font("Kanit", Font.PLAIN,40));
        forecastsText.setForeground(new Color(0,97,186));
        forecastsText.setHorizontalAlignment(SwingConstants.CENTER);
        add(forecastsText);

        JButton startWeather = new JButton("Weather");
        startWeather.setFont(new Font("Kanit",Font.PLAIN,18));
        startWeather.setBounds(92,527,111,40);
        startWeather.setFocusPainted(false);
        startWeather.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new WeatherAppGui().setVisible(true);
                dispose();
            }
        });
        add(startWeather);

        JButton startAQI = new JButton("AirQuality");
        startAQI.setFont(new Font("Kanit",Font.PLAIN,16));
        startAQI.setBounds(247,527,111,40);
        startAQI.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AirQualityGui().setVisible(true);
                dispose();
            }
        });
        add(startAQI);



    }

    private ImageIcon loadImage(String resourcePath){
        try{
            BufferedImage image = ImageIO.read(new File(resourcePath));
            return new ImageIcon((image));
        }catch(IOException e){
            e.printStackTrace();
        }

        System.out.println("Could not find resource");
        return null;

    }

}
