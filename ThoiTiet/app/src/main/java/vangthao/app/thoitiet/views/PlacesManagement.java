package vangthao.app.thoitiet.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

import vangthao.app.thoitiet.R;
import vangthao.app.thoitiet.model.places.CityOnlyTitleAndSolrID_Sysn;
import vangthao.app.thoitiet.viewmodel.CitySavedAdapter;
import vangthao.app.thoitiet.viewmodel.FirebaseDatabaseSingleton;

public class PlacesManagement extends AppCompatActivity {

    private ListView lvCitySaved;
    private ArrayList<CityOnlyTitleAndSolrID_Sysn> cityNameListSaved;
    private CitySavedAdapter adapterCitySaved;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_palces_management);
        setTitle(R.string.places_management);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        cityNameListSaved = new ArrayList<>();
        loadViews();
        initValue();
        loadDataCitySaved();
        adapterCitySaved.notifyDataSetChanged();
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
        Intent intent = getIntent();
        String email = intent.getStringExtra("email");
        DatabaseReference myData = FirebaseDatabaseSingleton.getInstance().child("CITY_SAVED");
        myData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cityNameListSaved.clear();
                for (DataSnapshot post : snapshot.getChildren()) {
                    CityOnlyTitleAndSolrID_Sysn city = post.getValue(CityOnlyTitleAndSolrID_Sysn.class);
                    assert city != null;
                    if (city.getEmail().equals(email)) {
                        cityNameListSaved.add(new CityOnlyTitleAndSolrID_Sysn(city.getID(), city.getSolrId(), city.getTitle(), city.getEmail()));
                    }
                }
                adapterCitySaved.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initValue() {
        adapterCitySaved = new CitySavedAdapter(PlacesManagement.this, R.layout.city_row_management, cityNameListSaved);
        lvCitySaved.setAdapter(adapterCitySaved);
        // LoadDataDistrict();
    }

    private void loadViews() {
        lvCitySaved = findViewById(R.id.lvDistrictSaved);
    }
}