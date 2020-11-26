package vangthao.app.thoitiet.viewmodel;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import vangthao.app.thoitiet.R;
import vangthao.app.thoitiet.model.places.CityOnlyTitleAndSolrID_Sysn;
import vangthao.app.thoitiet.views.HomeActivity;
import vangthao.app.thoitiet.views.PlacesManagement;

public class CitySavedAdapter extends BaseAdapter {

    private PlacesManagement context;
    private int layout;
    private ArrayList<CityOnlyTitleAndSolrID_Sysn> cityList;

    public CitySavedAdapter(PlacesManagement context, int layout, ArrayList<CityOnlyTitleAndSolrID_Sysn> cityList) {
        this.context = context;
        this.layout = layout;
        this.cityList = cityList;
    }

    @Override
    public int getCount() {
        return cityList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private class ViewHolder {
        TextView txtCityNameItemSaved;
        ImageButton imgBtnDeletePlace;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout, null);
            holder.txtCityNameItemSaved = (TextView) convertView.findViewById(R.id.txtCityNameItemSaved);
            holder.imgBtnDeletePlace = (ImageButton) convertView.findViewById(R.id.imgBtnDeletePlace);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.txtCityNameItemSaved.setText(cityList.get(position).getTitle());

        holder.txtCityNameItemSaved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, HomeActivity.class);
                String cityName = cityList.get(position).getSolrID();
                intent.putExtra("cityname", cityName);
                context.startActivity(intent);
            }
        });

        holder.imgBtnDeletePlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(context);
                dialog.setTitle("Xác nhận");
                dialog.setContentView(R.layout.dialog_delete_place);
                dialog.setCancelable(false);
                Button btnYes = dialog.findViewById(R.id.btnYes_DeletePlace);
                Button btnNo = dialog.findViewById(R.id.btnNo_DeletePlace);
                CityOnlyTitleAndSolrID_Sysn cityDelete = new CityOnlyTitleAndSolrID_Sysn(cityList.get(position).getID(), cityList.get(position).getSolrID(), cityList.get(position).getTitle(), cityList.get(position).getEmail());

                btnYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Toast.makeText(context, "email:"+districtDelete.getEmail(), Toast.LENGTH_SHORT).show();
                        Query query = PlacesManagement.myDatabase.child("CITY_SAVED").orderByChild("email").equalTo(HomeActivity.txtEmailHeader.getText().toString());
                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for (DataSnapshot phongSnapshot : dataSnapshot.getChildren()) {
                                        CityOnlyTitleAndSolrID_Sysn city = phongSnapshot.getValue(CityOnlyTitleAndSolrID_Sysn.class);
                                        //Toast.makeText(context, cityDelete.getID() + "," + city.getID(), Toast.LENGTH_SHORT).show();
                                        if (city.getID() == cityDelete.getID()) {
                                            phongSnapshot.getRef().removeValue();
                                            Toast.makeText(context, "Xoa dia diem thanh cong!", Toast.LENGTH_SHORT).show();
                                            PlacesManagement.adapterCitySaved.notifyDataSetChanged();
                                            dialog.dismiss();
                                        }
                                    }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                });

                btnNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
        return convertView;
    }
}
