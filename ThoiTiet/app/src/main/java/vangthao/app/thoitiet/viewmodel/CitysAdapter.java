package vangthao.app.thoitiet.viewmodel;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import vangthao.app.thoitiet.R;
import vangthao.app.thoitiet.model.places.City;
import vangthao.app.thoitiet.views.HomeActivity;

public class CitysAdapter extends RecyclerView.Adapter<CitysAdapter.ViewHolder> {
    private final ArrayList<City> cityList;

    public CitysAdapter(ArrayList<City> cityList) {
        this.cityList = cityList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View cityView = inflater.inflate(R.layout.city_row, parent, false);
        return new ViewHolder(cityView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        City citys = cityList.get(position);
        TextView textView = holder.txtCityName;
        textView.setText(citys.getTitle());
    }

    @Override
    public int getItemCount() {
        return cityList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView txtCityName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.txtCityName = itemView.findViewById(R.id.txtCityNameItem);
            this.txtCityName.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(), HomeActivity.class);
            String citySolrId = cityList.get(getAdapterPosition()).getSolrId().replace("-", " ");
            StringBuilder stringBuilder = new StringBuilder(citySolrId);
            stringBuilder.deleteCharAt(0);
            citySolrId = stringBuilder.toString();
            intent.putExtra("citysolrid", citySolrId);
            v.getContext().startActivity(intent);
        }
    }
}
