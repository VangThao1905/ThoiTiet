package vangthao.app.thoitiet.viewmodel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import vangthao.app.thoitiet.R;
import vangthao.app.thoitiet.model.places.City;

public class CitysSavedAdapter extends RecyclerView.Adapter<CitysSavedAdapter.ViewHolder> {
    private ArrayList<City> cityListSaved;

    public CitysSavedAdapter(ArrayList<City> cityListSaved) {
        this.cityListSaved = cityListSaved;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View cityView = inflater.inflate(R.layout.city_row_management, parent, false);

        ViewHolder viewHolder = new ViewHolder(cityView, context);
        viewHolder.imgBtnDeletePlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = context.getIntent();
//                String email = intent.getStringExtra("email");
//                Query query = FirebaseDatabaseSingleton.getInstance().child("CITY_SAVED").orderByChild("email").equalTo(email);
//                query.addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        for (DataSnapshot phongSnapshot : dataSnapshot.getChildren()) {
//                            CityOnlyTitleAndSolrID_Sysn city = phongSnapshot.getValue(CityOnlyTitleAndSolrID_Sysn.class);
//                            assert city != null;
//                            if (city.getID() == cityDelete.getID()) {
//                                phongSnapshot.getRef().removeValue();
//                                Toast.makeText(context, "Xoa dia diem thanh cong!", Toast.LENGTH_SHORT).show();
//                                dialog.dismiss();
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//                });
            }
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

    public class ViewHolder extends RecyclerView.ViewHolder{
        private Context context;
        private TextView txtCityNameSaved;
        private ImageButton imgBtnDeletePlace;

        public ViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            this.context = context;
            this.txtCityNameSaved = itemView.findViewById(R.id.txtCityNameItemSaved);
            this.imgBtnDeletePlace = itemView.findViewById(R.id.imgBtnDeletePlace);
            //txtCityNameSaved.setOnClickListener(this::onClick);

        }

//        @Override
//        public void onClick(View v) {
//            Intent intent = new Intent(context, HomeActivity.class);
//            String citySolrId = cityListSaved.get(getAdapterPosition()).getSolrId();
//            intent.putExtra("citysolrid", citySolrId);
//            context.startActivity(intent);
//        }
    }
}
