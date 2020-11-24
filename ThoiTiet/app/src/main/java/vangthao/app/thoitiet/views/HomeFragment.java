package vangthao.app.thoitiet.views;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vangthao.app.thoitiet.R;
import vangthao.app.thoitiet.model.places.District;
import vangthao.app.thoitiet.model.places.DistrictOnlyTitleAndSolrID;
import vangthao.app.thoitiet.model.places.DistrictOnlyTitleAndSolrID_Sys;
import vangthao.app.thoitiet.model.places.LtsItem;
import vangthao.app.thoitiet.model.weatherdata.WeatherResponse;
import vangthao.app.thoitiet.viewmodel.APIDistrictUtils;
import vangthao.app.thoitiet.viewmodel.APIWeatherUtils;
import vangthao.app.thoitiet.viewmodel.DistrictService;
import vangthao.app.thoitiet.viewmodel.WeatherService;

public class HomeFragment extends Fragment {

    private View rootView;
    private TextView txtCountry;
    private String city = "Ha noi,vn";
    private String units = "metric";
    private String AppID = "ae89bb1e7f56dc6ad44c4731c8154d7b";

    private TextView getTxtCountry, txtTemperature, txtMinTemperature, txtMaxTemperaure, txtDistrictName;
    private ImageView imgIconWeather,imgSavePlace;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        initView();
        Intent intent = getActivity().getIntent();
        city = intent.getStringExtra("cityname");
        if(city == null){
            city = "Ha noi,vn";
        }else{
            city+=",vn";
        }
        LoadDataWeather(city);
        events();
        return rootView;
    }

    private void events() {
        imgSavePlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (HomeActivity.txtUsernameHeader.getText().equals("Guest") && HomeActivity.txtEmailHeader.getText().equals("No Email")) {
                    Toast.makeText(getActivity(), "Vui long dang nhap truoc khi luu!", Toast.LENGTH_SHORT).show();
                } else {
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                    DistrictOnlyTitleAndSolrID_Sys districtSaved = new DistrictOnlyTitleAndSolrID_Sys(city,txtDistrictName.getText().toString(),HomeActivity.txtEmailHeader.getText().toString());
                    databaseReference.child("DISTRICT_SAVED").push().setValue(districtSaved, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                            if(error == null){
                                Toast.makeText(getActivity(), "Luu dia diem thanh cong!", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(getActivity(), "Loi luu dia diem!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

    public void LoadDataWeather(String cityName) {
        WeatherService weatherService = APIWeatherUtils.getDataWeather();
        Call<WeatherResponse> call = weatherService.getCurrentWeatherData(cityName, units, AppID);
        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(@NonNull Call<WeatherResponse> call, @NonNull Response<WeatherResponse> response) {
                if (response.code() == 200) {
                    WeatherResponse weatherResponse = response.body();
                    if (weatherResponse != null) {
                        String countryName = weatherResponse.getSys().getCountry();
                        double temperature = Double.parseDouble(weatherResponse.getMain().getTemp().toString());
                        String minTemperature = weatherResponse.getMain().getTempMin().toString();
                        String maxTemperature = weatherResponse.getMain().getTempMax().toString();
                        String districtName = weatherResponse.getName();

                        int temperatureFinal = (int)temperature;
                        //set background
                        customIconWeather(temperatureFinal);

                        txtCountry.setText("Quoc gia: " + countryName);
                        txtTemperature.setText(temperatureFinal+ "°C");
                        txtMinTemperature.setText("Thap nhat: " + minTemperature + "°C");
                        txtMaxTemperaure.setText("Cao nhat: " + maxTemperature + "°C");
                        txtDistrictName.setText(districtName);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<WeatherResponse> call, @NonNull Throwable t) {
                Log.d("HomeFragment", t.getMessage());
            }
        });
    }

    public void customIconWeather(int temperature){
        if(temperature < 15){
            imgIconWeather.setImageResource(R.drawable.ic_rainy);
        }else if(temperature >= 15 && temperature <= 25){
            imgIconWeather.setImageResource(R.drawable.ic_cloud);
        }else if(temperature > 25){
            imgIconWeather.setImageResource(R.drawable.ic_sunny100);
        }
    }

    private void initView() {
        txtCountry = rootView.findViewById(R.id.txtCountry);
        txtTemperature = rootView.findViewById(R.id.txtTemperature);
        txtMinTemperature = rootView.findViewById(R.id.txtMinTemperature);
        txtMaxTemperaure = rootView.findViewById(R.id.txtMaxTemperature);
        txtDistrictName = rootView.findViewById(R.id.txtDistrictName);
        imgIconWeather = rootView.findViewById(R.id.imgIconWeather);
        imgSavePlace = rootView.findViewById(R.id.imgSavePlace);
    }
}
