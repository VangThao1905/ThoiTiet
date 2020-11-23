package vangthao.app.thoitiet.views;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.IOException;
import java.util.List;

import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import vangthao.app.thoitiet.R;
import vangthao.app.thoitiet.model.WeatherResponse;
import vangthao.app.thoitiet.viewmodel.APIUtils;
import vangthao.app.thoitiet.viewmodel.WeatherService;

public class HomeFragment extends Fragment {

    private View rootView;
    private TextView txtCountry;
    private String AppID = "ae89bb1e7f56dc6ad44c4731c8154d7b";
    private String BaseUrl = "http://api.openweathermap.org/";
    private String lat = "35";
    private String lon = "139";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        initView();
        LoadDataWeather(lat, lon, AppID);
        return rootView;
    }

    public void LoadDataWeather(String lat, String lon, String appid) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        WeatherService service = retrofit.create(WeatherService.class);
        Call<List<WeatherResponse>> call = service.getCurrentWeatherData(lat, lon, AppID);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                if (response.code() == 200) {
                    List<WeatherResponse> weatherResponse = (List<WeatherResponse>) response.body();
                    txtCountry.setText(weatherResponse.get(0).getSys().getCountry());
                }
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull Throwable t) {
                Log.d("LOAD", t.getMessage());
                txtCountry.setText(t.getMessage());
            }
        });
    }

    private void initView() {
        txtCountry = rootView.findViewById(R.id.txtCountry);
    }
}
