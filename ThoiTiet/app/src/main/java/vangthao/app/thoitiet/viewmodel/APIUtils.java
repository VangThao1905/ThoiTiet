package vangthao.app.thoitiet.viewmodel;

public class APIUtils {
    public static final String BaseUrl = "http://api.openweathermap.org/";

    public static WeatherService getData(){
        return RetrofitClient.getClient(BaseUrl).create(WeatherService.class);
    }
}
