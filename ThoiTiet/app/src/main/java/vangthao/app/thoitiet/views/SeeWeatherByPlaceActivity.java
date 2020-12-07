package vangthao.app.thoitiet.views;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vangthao.app.thoitiet.R;
import vangthao.app.thoitiet.model.places.City;
import vangthao.app.thoitiet.model.places.CityResponse;
import vangthao.app.thoitiet.viewmodel.APICityUtils;
import vangthao.app.thoitiet.viewmodel.CityService;
import vangthao.app.thoitiet.viewmodel.CitysAdapter;

public class SeeWeatherByPlaceActivity extends AppCompatActivity {

    private RecyclerView rvCitys;
    private ArrayList<City> cityNameList;
    private CitysAdapter citysAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_weather_by_place);
        setTitle("Xem theo địa điểm");

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        cityNameList = new ArrayList<>();
        loadViews();
        initValue();
        loadDataCity();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void loadDataCity() {
        CityService cityService = APICityUtils.getDataCity();
        Call<CityResponse> call = cityService.getCurrentCityData();
        call.enqueue(new Callback<CityResponse>() {
            @Override
            public void onResponse(Call<CityResponse> call, Response<CityResponse> response) {
                if (response.code() == 200) {
                    CityResponse cityResponse = response.body();
                    if (cityResponse != null) {
                        for (int i = 0; i < cityResponse.getLtsItem().size(); i++) {
                            City cityItem = new City(cityResponse.getLtsItem().get(i).getSolrID(), cityResponse.getLtsItem().get(i).getTitle());
                            cityNameList.add(cityItem);
                            citysAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<CityResponse> call, Throwable t) {
                Log.d("PlacesManagement", t.getMessage());
            }
        });
    }

    private void initValue() {
        cityNameList = new ArrayList<>();
        citysAdapter = new CitysAdapter(cityNameList);
        rvCitys.setAdapter(citysAdapter);
        rvCitys.setLayoutManager(new LinearLayoutManager(this));
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rvCitys.addItemDecoration(itemDecoration);
    }

    private void loadViews() {
        rvCitys = findViewById(R.id.rvCitys);
    }
}