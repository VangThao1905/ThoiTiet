package vangthao.app.thoitiet.viewmodel;

import retrofit2.Call;
import retrofit2.http.GET;
import vangthao.app.thoitiet.model.places.City;

public interface CityService {
    @GET("/api/city?")
    Call<City> getCurrentCityData();
}
