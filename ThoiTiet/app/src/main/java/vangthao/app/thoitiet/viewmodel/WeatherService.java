package vangthao.app.thoitiet.viewmodel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import vangthao.app.thoitiet.model.WeatherResponse;

public interface WeatherService {
    @GET("data/2.5/weather?")
    Call<List<WeatherResponse>> getCurrentWeatherData(@Query("lat") String lat, @Query("lon") String lon, @Query("APPID") String app_id);
}
