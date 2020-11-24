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
import vangthao.app.thoitiet.model.places.District;
import vangthao.app.thoitiet.model.places.DistrictOnlyTitleAndSolrID;
import vangthao.app.thoitiet.viewmodel.APIDistrictUtils;
import vangthao.app.thoitiet.viewmodel.DistrictAdapter;
import vangthao.app.thoitiet.viewmodel.DistrictService;

public class SeeWeatherByPlace extends AppCompatActivity {

    private ListView lvDisttrict;
    private ArrayList<DistrictOnlyTitleAndSolrID> districtNameList;
    private DistrictAdapter adapterDistrict;
    //List of 64 district of VietNam
    District district;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_weather_by_place);
        setTitle("Xem theo dia diem");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        districtNameList = new ArrayList<>();
        initView();
        initValue();
        LoadDataDistrict();
        adapterDistrict.notifyDataSetChanged();
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

    public void LoadDataDistrict() {
        //Toast.makeText(this, "Hello", Toast.LENGTH_SHORT).show();
        DistrictService districtService = APIDistrictUtils.getDataDistrict();
        Call<District> call = districtService.getCurrentDistrictData();
        call.enqueue(new Callback<District>() {
            @Override
            public void onResponse(Call<District> call, Response<District> response) {
                //Toast.makeText(PlacesManagement.this, "OK", Toast.LENGTH_SHORT).show();
                if (response.code() == 200) {
                    district = response.body();
                    if (district != null) {
                        for(int i = 0;i < district.getLtsItem().size();i++){
                            districtNameList.add(new DistrictOnlyTitleAndSolrID(district.getLtsItem().get(i).getSolrID(),district.getLtsItem().get(i).getTitle()));
                            adapterDistrict.notifyDataSetChanged();
                        }
                        //Toast.makeText(PlacesManagement.this, "Name:"+district.getLtsItem().get(0).getTitle(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<District> call, Throwable t) {
                Log.d("PlacesManagement",t.getMessage());
            }
        });
    }

    private void events() {
        lvDisttrict.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(PlacesManagement.this, "Hello", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SeeWeatherByPlace.this,HomeActivity.class);
                String cityName = districtNameList.get(position).getSolrID().replace("-"," ");
                StringBuilder stringBuilder = new StringBuilder(cityName);
                stringBuilder.deleteCharAt(0);
                cityName = stringBuilder.toString();
                intent.putExtra("cityname",cityName);
                startActivity(intent);
            }
        });
    }

    private void initValue() {
        adapterDistrict = new DistrictAdapter(SeeWeatherByPlace.this, R.layout.district_row, districtNameList);
        lvDisttrict.setAdapter(adapterDistrict);
       // LoadDataDistrict();
    }

    private void initView() {
        lvDisttrict = findViewById(R.id.lvDistrict);
    }
}