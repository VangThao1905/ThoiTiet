package vangthao.app.thoitiet.viewmodel;

public class APIWeatherUtils {
    public static final String BASE_URL_WEATHER = "http://api.openweathermap.org/";

    public static WeatherService getDataWeather() {
        return RetrofitClient.getClient(BASE_URL_WEATHER).create(WeatherService.class);
    }
}
