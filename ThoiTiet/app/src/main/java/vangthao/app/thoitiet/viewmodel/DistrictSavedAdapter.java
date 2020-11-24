package vangthao.app.thoitiet.viewmodel;

import android.app.Dialog;
import android.content.Context;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import vangthao.app.thoitiet.R;
import vangthao.app.thoitiet.model.places.DistrictOnlyTitleAndSolrID;
import vangthao.app.thoitiet.model.places.DistrictOnlyTitleAndSolrID_Sys;
import vangthao.app.thoitiet.views.HomeActivity;
import vangthao.app.thoitiet.views.PlacesManagement;

public class DistrictSavedAdapter extends BaseAdapter {

    private PlacesManagement context;
    private int layout;
    private ArrayList<DistrictOnlyTitleAndSolrID_Sys> districtList;

    public DistrictSavedAdapter(PlacesManagement context, int layout, ArrayList<DistrictOnlyTitleAndSolrID_Sys> districtList) {
        this.context = context;
        this.layout = layout;
        this.districtList = districtList;
    }

    @Override
    public int getCount() {
        return districtList.size();
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
        TextView txtDistrictNameItem;
        ImageButton imgBtnDeletePlace;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout, null);
            holder.txtDistrictNameItem = (TextView) convertView.findViewById(R.id.txtDistrictNameItem);
            holder.imgBtnDeletePlace = (ImageButton) convertView.findViewById(R.id.imgBtnDeletePlace);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.txtDistrictNameItem.setText(districtList.get(position).getTitle());

        holder.imgBtnDeletePlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(context);
                dialog.setTitle("Xác nhận");
                dialog.setContentView(R.layout.dialog_delete_place);
                dialog.setCancelable(false);
                Button btnYes = dialog.findViewById(R.id.btnYes_DeletePlace);
                Button btnNo = dialog.findViewById(R.id.btnNo_DeletePlace);
                //String email = HomeActivity.txtEmailHeader.getText().toString();
                DistrictOnlyTitleAndSolrID_Sys districtDelete = new DistrictOnlyTitleAndSolrID_Sys(districtList.get(position).getSolrID(),districtList.get(position).getTitle(),districtList.get(position).getEmail());

                btnYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context, "email:"+districtDelete.getEmail(), Toast.LENGTH_SHORT).show();
                        Query query = PlacesManagement.myDatabase.child("DISTRICT_SAVED").orderByChild("email").equalTo(HomeActivity.txtEmailHeader.getText().toString());
                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (districtDelete.getEmail() != null) {
                                    for (DataSnapshot phongSnapshot : dataSnapshot.getChildren()) {
                                        phongSnapshot.getRef().getRef().removeValue();
                                        Toast.makeText(context, "Xóa dia diem thanh cong!", Toast.LENGTH_SHORT).show();
                                        PlacesManagement.adapterDistrictSaved.notifyDataSetChanged();
                                        dialog.dismiss();
                                    }
                                }else{
                                    //Toast.makeText(context , "Loi Xoa", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        //dialog.dismiss();
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