package vangthao.app.thoitiet.views;

import android.annotation.SuppressLint;
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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vangthao.app.thoitiet.R;
import vangthao.app.thoitiet.model.places.CityOnlyTitleAndSolrID_Sysn;
import vangthao.app.thoitiet.model.weatherdata.WeatherResponse;
import vangthao.app.thoitiet.viewmodel.APIWeatherUtils;
import vangthao.app.thoitiet.viewmodel.WeatherService;

public class HomeFragment extends Fragment {

    private View rootView;
    private int idCity;
    private String citySolrId;
    private TextView txtCountry, txtTemperature, txtMinTemperature, txtMaxTemperaure, txtCityName;
    private ImageView imgIconWeather, imgSavePlace;

    public HomeFragment() {
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        initView();
        Intent intent = Objects.requireNonNull(getActivity()).getIntent();
        citySolrId = intent.getStringExtra("citysolrid");
        String countryCode = ",vn";
        if (citySolrId == null) {
            citySolrId = "Ha noi" + countryCode;
        } else {
            if (!haveCommasInCityName(citySolrId)) {
                citySolrId += countryCode;
            }
        }
        loadDataWeather(citySolrId);
        loadEventList();
        return rootView;
    }

    public boolean haveCommasInCityName(String cityName) {
        for (int i = 0; i < cityName.length(); i++) {
            if (cityName.charAt(i) == ',') {
                return true;
            }
        }
        return false;
    }

    private void loadEventList() {
        imgSavePlace.setOnClickListener(v -> {
            HomeActivity homeActivity = (HomeActivity) getActivity();
            assert homeActivity != null;
            String username = homeActivity.getUsernameLogin();
            String email = homeActivity.getEmailLogin();
            if (username.equals("Guest") && email.equals("No Email")) {
                Toast.makeText(getActivity(), "Vui long dang nhap truoc khi luu!", Toast.LENGTH_SHORT).show();
            } else {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                CityOnlyTitleAndSolrID_Sysn citySaved = new CityOnlyTitleAndSolrID_Sysn(idCity, citySolrId, txtCityName.getText().toString(), email);
                databaseReference.child("CITY_SAVED").push().setValue(citySaved, (error, ref) -> {
                    if (error == null) {
                        Toast.makeText(getActivity(), "Luu dia diem thanh cong!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "Loi luu dia diem!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    public void loadDataWeather(String cityName) {
        WeatherService weatherService = APIWeatherUtils.getDataWeather();
        String units = "metric";
        String appId = "ae89bb1e7f56dc6ad44c4731c8154d7b";
        Call<WeatherResponse> call = weatherService.getCurrentWeatherData(cityName, units, appId);
        call.enqueue(new Callback<WeatherResponse>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<WeatherResponse> call, @NonNull Response<WeatherResponse> response) {
                if (response.code() == 200) {
                    WeatherResponse weatherResponse = response.body();
                    if (weatherResponse != null) {
                        idCity = weatherResponse.getId();
                        String countryName = weatherResponse.getSys().getCountry();
                        double temperature = Double.parseDouble(weatherResponse.getMain().getTemp().toString());
                        String minTemperature = weatherResponse.getMain().getTempMin().toString();
                        String maxTemperature = weatherResponse.getMain().getTempMax().toString();
                        String cityNameResponse = weatherResponse.getName();

                        int temperatureFinal = (int) temperature;
                        customIconWeather(temperatureFinal);

                        txtCountry.setText(R.string.contry + ": " + countryName);
                        txtTemperature.setText(temperatureFinal + "°C");
                        txtMinTemperature.setText("Thấp nhất: " + minTemperature + "°C");
                        txtMaxTemperaure.setText("Cao nhất: " + maxTemperature + "°C");
                        txtCityName.setText(cityNameResponse);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<WeatherResponse> call, @NonNull Throwable t) {
                Log.d("HomeFragment", t.getMessage());
            }
        });
    }

    public void customIconWeather(int temperature) {
        if (temperature < 15) {
            imgIconWeather.setImageResource(R.mipmap.ic_rainy);
        } else if (temperature <= 25) {
            imgIconWeather.setImageResource(R.mipmap.ic_cloud);
        } else {
            imgIconWeather.setImageResource(R.mipmap.ic_sunny100);
        }
    }

    private void initView() {
        txtCountry = rootView.findViewById(R.id.txtCountry);
        txtTemperature = rootView.findViewById(R.id.txtTemperature);
        txtMinTemperature = rootView.findViewById(R.id.txtMinTemperature);
        txtMaxTemperaure = rootView.findViewById(R.id.txtMaxTemperature);
        txtCityName = rootView.findViewById(R.id.txtCityName);
        imgIconWeather = rootView.findViewById(R.id.imgIconWeather);
        imgSavePlace = rootView.findViewById(R.id.imgSavePlace);
    }
}
