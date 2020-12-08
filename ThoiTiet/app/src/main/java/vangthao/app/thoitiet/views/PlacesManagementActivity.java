package vangthao.app.thoitiet.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

import vangthao.app.thoitiet.R;
import vangthao.app.thoitiet.model.places.City;
import vangthao.app.thoitiet.viewmodel.CitysSavedAdapter;
import vangthao.app.thoitiet.viewmodel.FirebaseDatabaseSingleton;

public class PlacesManagementActivity extends AppCompatActivity {

    private RecyclerView rvCitysSaved;
    private ArrayList<City> cityNameListSaved;
    private CitysSavedAdapter citysAdapterSaved;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places_management);
        setTitle("Quản lý địa điểm");

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        cityNameListSaved = new ArrayList<>();
        Intent intent = getIntent();
        email = intent.getStringExtra("email");
        loadViews();
        initValue();
        loadDataCitySaved();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void loadDataCitySaved() {
        DatabaseReference myData = FirebaseDatabaseSingleton.getInstance().child("CITY_SAVED");
        myData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cityNameListSaved.clear();
                for (DataSnapshot post: snapshot.getChildren()){
                    City city = post.getValue(City.class);
                    assert city != null;
                    if(city.getEmail().equals(email)){
                        cityNameListSaved.add(city);
                        citysAdapterSaved.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initValue() {
        cityNameListSaved = new ArrayList<>();
        citysAdapterSaved = new CitysSavedAdapter(cityNameListSaved);
        rvCitysSaved.setAdapter(citysAdapterSaved);
        rvCitysSaved.setLayoutManager(new LinearLayoutManager(this));
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rvCitysSaved.addItemDecoration(itemDecoration);
    }

    private void loadViews() {
        rvCitysSaved = findViewById(R.id.rvCitysSaved);
    }
}