package vangthao.app.thoitiet.viewmodel;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import vangthao.app.thoitiet.R;
import vangthao.app.thoitiet.model.places.City;
import vangthao.app.thoitiet.views.HomeActivity;

public class CitysSavedAdapter extends RecyclerView.Adapter<CitysSavedAdapter.ViewHolder> {
    private final ArrayList<City> cityListSaved;

    public CitysSavedAdapter(ArrayList<City> cityListSaved) {
        this.cityListSaved = cityListSaved;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View cityView = inflater.inflate(R.layout.city_row_management, parent, false);

        ViewHolder viewHolder = new ViewHolder(cityView);
        viewHolder.txtCityNameSaved.setOnClickListener(v -> {
            Intent intent = new Intent(context, HomeActivity.class);
            String citySolrId = cityListSaved.get(viewHolder.getAdapterPosition()).getSolrId();
            intent.putExtra("citysolrid", citySolrId);
            context.startActivity(intent);
        });
        viewHolder.imgBtnDeletePlace.setOnClickListener(v -> {
            Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.dialog_delete_place);
            dialog.setTitle("Xác nhận xóa");

            Button btnYes_DeletePlace = dialog.findViewById(R.id.btnYes_DeletePlace);
            Button btnNo_DeletePlace = dialog.findViewById(R.id.btnNo_DeletePlace);
            btnYes_DeletePlace.setOnClickListener(v12 -> {
                String email = cityListSaved.get(viewHolder.getAdapterPosition()).getEmail();
                Query query = FirebaseDatabaseSingleton.getInstance().child("CITY_SAVED").orderByChild("email").equalTo(email);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot phongSnapshot : dataSnapshot.getChildren()) {
                            City city = phongSnapshot.getValue(City.class);
                            assert city != null;
                            if (city.getID() == cityListSaved.get(viewHolder.getAdapterPosition()).getID()) {
                                phongSnapshot.getRef().removeValue();
                                Toast.makeText(context, "Xóa địa điểm thành công!", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(context, "Xoa loi", Toast.LENGTH_SHORT).show();
                    }
                });
            });

            btnNo_DeletePlace.setOnClickListener(v1 -> dialog.dismiss());
            dialog.show();
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        City citys = cityListSaved.get(position);
        TextView textView = holder.txtCityNameSaved;
        textView.setText(citys.getTitle());
    }

    @Override
    public int getItemCount() {
        return cityListSaved.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView txtCityNameSaved;
        private final ImageButton imgBtnDeletePlace;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.txtCityNameSaved = itemView.findViewById(R.id.txtCityNameItemSaved);
            this.imgBtnDeletePlace = itemView.findViewById(R.id.imgBtnDeletePlace);
        }
    }
}
