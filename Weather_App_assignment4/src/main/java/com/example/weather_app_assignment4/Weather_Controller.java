package com.example.weather_app_assignment4;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ResourceBundle;
import java.util.Scanner;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import org.json.JSONObject;
public class Weather_Controller {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button button;

    @FXML
    private TextField feild_for_country;

    @FXML
    private Text humidity;

    @FXML
    private Text temp;

    @FXML
    private Text wind;

    @FXML
    void initialize() {
        button.setOnAction(actionEvent -> {
            String city = feild_for_country.getText(); // Get the city from the text field


            String url = new ApiAdapter().adapt("http://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=");

            try {
                String adaptedJson = new JsonAdapter().adapt(url);
                Scanner scanner = new Scanner(adaptedJson);

                String temperature = scanner.next();
                String humidityText = scanner.next();
                String windSpeed = scanner.next();
                // Update the UI components
                temp.setText("Temperature: " + temperature + "°C");
                humidity.setText("Humidity: " + humidityText + "%");
                wind.setText("Wind Speed: " + windSpeed + " m/s");

            } catch (Exception e) {
                System.out.println("Error fetching or parsing weather data: " + e.getMessage());
            }
        });
    }






}

abstract class Adapter{
    abstract public String adapt(String needAdapt);
}

class ApiAdapter extends Adapter{
    private static String apiKey = "a572ff64ef842c6f5f11415c68cf55c6";
    @Override
    public String adapt(String needAdapt){
        StringBuffer content = new StringBuffer();
        try{   URL url = new URL(needAdapt + apiKey);
            URLConnection urlConnection = url.openConnection();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line;
            while ((line = bufferedReader.readLine()) != null){
                content.append(line+"\n");
            }
            bufferedReader.close();
        }catch (Exception e){
            System.out.println("Такой город не найден");
        }
        return content.toString();
    }

}


class JsonAdapter extends Adapter{

    @Override
    public String adapt(String needAdapt) {
        try {
            JSONObject json = new JSONObject(needAdapt);
            JSONObject main = json.getJSONObject("main");
            JSONObject windtemp = json.getJSONObject("wind");

            double temperatureInKelvin = main.getDouble("temp") - 273.15;
            String temperature = String.format("%.2f", temperatureInKelvin);
            String humidityText = String.valueOf(main.getDouble("humidity"));
            String windSpeed = String.valueOf(windtemp.getDouble("speed"));
            return temperature + " " + humidityText + " " + windSpeed;
        } catch (Exception e) {
            return "Error fetching or parsing weather data: " + e.getMessage();
        }
    }
}