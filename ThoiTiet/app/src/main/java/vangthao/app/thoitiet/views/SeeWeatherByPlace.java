package vangthao.app.thoitiet.views;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vangthao.app.thoitiet.R;
import vangthao.app.thoitiet.model.places.City;
import vangthao.app.thoitiet.model.places.CityOnlyTitleAndSolrID;
import vangthao.app.thoitiet.viewmodel.APICityUtils;
import vangthao.app.thoitiet.viewmodel.CityAdapter;
import vangthao.app.thoitiet.viewmodel.CityService;

public class SeeWeatherByPlace extends AppCompatActivity {

    private ListView lvCity;
    private ArrayList<CityOnlyTitleAndSolrID> cityNameList;
    private CityAdapter adapterCity;
    //List of 64 city of VietNam
    City city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_weather_by_place);
        setTitle("Xem theo dia diem");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        cityNameList = new ArrayList<>();
        initView();
        initValue();
        LoadDataCity();
        adapterCity.notifyDataSetChanged();
        events();
        //Log.d("TEST",districtNameList.size()+"");
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void LoadDataCity() {
        //Toast.makeText(this, "Hello", Toast.LENGTH_SHORT).show();
        CityService districtService = APICityUtils.getDataDistrict();
        Call<City> call = districtService.getCurrentCityData();
        call.enqueue(new Callback<City>() {
            @Override
            public void onResponse(Call<City> call, Response<City> response) {
                //Toast.makeText(PlacesManagement.this, "OK", Toast.LENGTH_SHORT).show();
                if (response.code() == 200) {
                    city = response.body();
                    if (city != null) {
                        for(int i = 0;i < city.getLtsItem().size();i++){
                            cityNameList.add(new CityOnlyTitleAndSolrID(city.getLtsItem().get(i).getSolrID(),city.getLtsItem().get(i).getTitle()));
                            adapterCity.notifyDataSetChanged();
                        }
                        //Toast.makeText(PlacesManagement.this, "Name:"+district.getLtsItem().get(0).getTitle(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<City> call, Throwable t) {
                Log.d("PlacesManagement",t.getMessage());
            }
        });
    }

    private void events() {
        lvCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(PlacesManagement.this, "Hello", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SeeWeatherByPlace.this,HomeActivity.class);
                String cityName = cityNameList.get(position).getSolrID().replace("-"," ");
                StringBuilder stringBuilder = new StringBuilder(cityName);
                stringBuilder.deleteCharAt(0);
                cityName = stringBuilder.toString();
                intent.putExtra("cityname",cityName);
                startActivity(intent);
            }
        });
    }

    private void initValue() {
        adapterCity = new CityAdapter(SeeWeatherByPlace.this, R.layout.city_row, cityNameList);
        lvCity.setAdapter(adapterCity);
       // LoadDataDistrict();
    }

    private void initView() {
        lvCity = findViewById(R.id.lvDistrict);
    }
}