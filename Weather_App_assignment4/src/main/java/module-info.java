module com.example.weather_app_assignment4 {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.json;


    opens com.example.weather_app_assignment4 to javafx.fxml;
    exports com.example.weather_app_assignment4;
}