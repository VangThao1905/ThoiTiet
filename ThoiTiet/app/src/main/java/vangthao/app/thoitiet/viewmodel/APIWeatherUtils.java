package vangthao.app.thoitiet.viewmodel;

public class APIWeatherUtils {
    public static final String BaseUrlWeather = "http://api.openweathermap.org/";

    public static WeatherService getDataWeather() {
        return RetrofitClient.getClient(BaseUrlWeather).create(WeatherService.class);
    }
}
