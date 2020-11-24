package vangthao.app.thoitiet.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import vangthao.app.thoitiet.R;
import vangthao.app.thoitiet.model.places.City;
import vangthao.app.thoitiet.model.places.CityOnlyTitleAndSolrID_Sysn;
import vangthao.app.thoitiet.viewmodel.CitySavedAdapter;

public class PlacesManagement extends AppCompatActivity {

    private ListView lvCitySaved;
    public static  ArrayList<CityOnlyTitleAndSolrID_Sysn> cityNameListSaved;
    public static CitySavedAdapter adapterCitySaved;
    //List of 64 city of VietNam
    private City city;
    public static DatabaseReference myDatabase = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_palces_management);
        setTitle("Quan ly dia diem");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        cityNameListSaved = new ArrayList<>();
        initView();
        initValue();
        LoadDataCitySaved();
        adapterCitySaved.notifyDataSetChanged();
        events();
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

    public void LoadDataCitySaved() {

        DatabaseReference myData = myDatabase.child("CITY_SAVED");
        myData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cityNameListSaved.clear();
                for (DataSnapshot post : snapshot.getChildren()) {
                    CityOnlyTitleAndSolrID_Sysn city = post.getValue(CityOnlyTitleAndSolrID_Sysn.class);
                    if (city.getEmail().equals(HomeActivity.txtEmailHeader.getText().toString())) {
                        cityNameListSaved.add(new CityOnlyTitleAndSolrID_Sysn(city.getID(),city.getSolrID(), city.getTitle(), city.getEmail()));
                    }
                }
                adapterCitySaved.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void events() {
//        lvCitySaved.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(PlacesManagement.this, "Ahiihi", Toast.LENGTH_SHORT).show();
//            }
//        });
//        lvCitySaved.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(PlacesManagement.this, "Hello", Toast.LENGTH_SHORT).show();
////                Intent intent = new Intent(PlacesManagement.this, HomeActivity.class);
////                String cityName = cityNameListSaved.get(position).getSolrID().replace("-", " ");
//////                StringBuilder stringBuilder = new StringBuilder(cityName);
//////                stringBuilder.deleteCharAt(0);
//////                cityName = stringBuilder.toString();
////                intent.putExtra("cityname", cityName);
////                startActivity(intent);
//            }
//        });
    }

    private void initValue() {
        adapterCitySaved = new CitySavedAdapter(PlacesManagement.this, R.layout.city_row_management, cityNameListSaved);
        lvCitySaved.setAdapter(adapterCitySaved);
        // LoadDataDistrict();
    }

    private void initView() {
        lvCitySaved = findViewById(R.id.lvDistrictSaved);
    }
}