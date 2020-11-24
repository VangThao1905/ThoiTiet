package vangthao.app.thoitiet.views;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vangthao.app.thoitiet.R;
import vangthao.app.thoitiet.model.places.District;
import vangthao.app.thoitiet.model.places.DistrictOnlyTitleAndSolrID;
import vangthao.app.thoitiet.model.places.DistrictOnlyTitleAndSolrID_Sys;
import vangthao.app.thoitiet.viewmodel.APIDistrictUtils;
import vangthao.app.thoitiet.viewmodel.DistrictAdapter;
import vangthao.app.thoitiet.viewmodel.DistrictSavedAdapter;
import vangthao.app.thoitiet.viewmodel.DistrictService;

public class PlacesManagement extends AppCompatActivity {

    private ListView lvDisttrictSaved;
    public static  ArrayList<DistrictOnlyTitleAndSolrID_Sys> districtNameListSaved;
    public static DistrictSavedAdapter adapterDistrictSaved;
    //List of 64 district of VietNam
    District district;
    public static DatabaseReference myDatabase = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_palces_management);
        setTitle("Quan ly dia diem");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        districtNameListSaved = new ArrayList<>();
        //districtNameListSaved.add(new DistrictOnlyTitleAndSolrID_Sys("a", "b", "c"));
        initView();
        initValue();
        LoadDataDistrictSaved();
        adapterDistrictSaved.notifyDataSetChanged();
        events();
        //Log.d("TEST",districtNameList.size()+"");
        //Toast.makeText(this, "size:" + districtNameListSaved.size(), Toast.LENGTH_SHORT).show();
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

    public void LoadDataDistrictSaved() {

        DatabaseReference myData = myDatabase.child("DISTRICT_SAVED");
        myData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                districtNameListSaved.clear();
                for (DataSnapshot post : snapshot.getChildren()) {
                    DistrictOnlyTitleAndSolrID_Sys district = post.getValue(DistrictOnlyTitleAndSolrID_Sys.class);
                    //Toast.makeText(PlacesManagement.this, ""+district.getTitle(), Toast.LENGTH_SHORT).show();
                    if (district.getEmail().equals(HomeActivity.txtEmailHeader.getText().toString())) {
                        districtNameListSaved.add(new DistrictOnlyTitleAndSolrID_Sys(district.getSolrID(), district.getTitle(), district.getEmail()));
                        //Toast.makeText(PlacesManagement.this, "OK", Toast.LENGTH_SHORT).show();
                    }
                }
                adapterDistrictSaved.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void events() {
        lvDisttrictSaved.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(PlacesManagement.this, "Hello", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(PlacesManagement.this, HomeActivity.class);
                String cityName = districtNameListSaved.get(position).getSolrID().replace("-", " ");
//                StringBuilder stringBuilder = new StringBuilder(cityName);
//                stringBuilder.deleteCharAt(0);
//                cityName = stringBuilder.toString();
                intent.putExtra("cityname", cityName);
                startActivity(intent);
            }
        });
    }

    private void initValue() {
        adapterDistrictSaved = new DistrictSavedAdapter(PlacesManagement.this, R.layout.district_row_management, districtNameListSaved);
        lvDisttrictSaved.setAdapter(adapterDistrictSaved);
        // LoadDataDistrict();
    }

    private void initView() {
        lvDisttrictSaved = findViewById(R.id.lvDistrictSaved);
    }
}