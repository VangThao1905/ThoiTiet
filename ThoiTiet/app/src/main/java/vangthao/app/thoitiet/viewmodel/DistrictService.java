package vangthao.app.thoitiet.viewmodel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import vangthao.app.thoitiet.model.places.District;
import vangthao.app.thoitiet.model.places.LtsItem;

public interface DistrictService {
    @GET("/api/city?")
    Call<District> getCurrentDistrictData();
}
