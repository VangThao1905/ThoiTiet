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
import vangthao.app.thoitiet.model.places.CityOnlyTitleAndSolrID;
import vangthao.app.thoitiet.viewmodel.APICityUtils;
import vangthao.app.thoitiet.viewmodel.CityService;
import vangthao.app.thoitiet.viewmodel.CitysAdapter;

public class SeeWeatherByPlace extends AppCompatActivity {

    private RecyclerView rvCitys;
    private ArrayList<CityOnlyTitleAndSolrID> cityNameList;
    private CitysAdapter citysAdapter;
    //List of 64 city of VietNam
    private City city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_weather_by_place);
        setTitle("Xem theo dia diem");

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        cityNameList = new ArrayList<>();
        loadViews();
        initValue();
        loadDataCity();
        //Toast.makeText(this, "Size:"+cityNameList.size(), Toast.LENGTH_SHORT).show();
        //loadEventList();
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
        Call<City> call = cityService.getCurrentCityData();
        call.enqueue(new Callback<City>() {
            @Override
            public void onResponse(Call<City> call, Response<City> response) {
                if (response.code() == 200) {
                    city = response.body();
                    if (city != null) {
                        for (int i = 0; i < city.getLtsItem().size(); i++) {
                            CityOnlyTitleAndSolrID cityItem = new CityOnlyTitleAndSolrID(city.getLtsItem().get(i).getSolrID(), city.getLtsItem().get(i).getTitle());
                            cityNameList.add(cityItem);
                            citysAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<City> call, Throwable t) {
                Log.d("PlacesManagement", t.getMessage());
            }
        });
    }

    private void loadEventList() {
//        lvCity.setOnItemClickListener((AdapterView<?> parent, View view, int position, long id) -> {
//            Intent intent = new Intent(SeeWeatherByPlace.this, HomeActivity.class);
//            String cityName = cityNameList.get(position).getSolrId().replace("-", " ");
//            StringBuilder stringBuilder = new StringBuilder(cityName);
//            stringBuilder.deleteCharAt(0);
//            cityName = stringBuilder.toString();
//            intent.putExtra("cityname", cityName);
//            startActivity(intent);
//        });
    }

    private void initValue() {
        cityNameList = new ArrayList<>();
        //cityNameList.add(new CityOnlyTitleAndSolrID("ABC"));
        citysAdapter = new CitysAdapter(cityNameList);

        rvCitys.setAdapter(citysAdapter);
        rvCitys.setLayoutManager(new LinearLayoutManager(this));
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rvCitys.addItemDecoration(itemDecoration);
        // LoadDataDistrict();
    }

    private void loadViews() {
        rvCitys = findViewById(R.id.rvCitys);
    }
}