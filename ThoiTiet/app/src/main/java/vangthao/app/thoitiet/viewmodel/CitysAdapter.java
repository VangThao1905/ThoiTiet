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
import vangthao.app.thoitiet.model.places.CityOnlyTitleAndSolrID;
import vangthao.app.thoitiet.views.HomeActivity;

public class CitysAdapter extends RecyclerView.Adapter<CitysAdapter.ViewHolder> {
    private ArrayList<CityOnlyTitleAndSolrID> cityList;

    public CitysAdapter(ArrayList<CityOnlyTitleAndSolrID> cityList) {
        this.cityList = cityList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View cityView = inflater.inflate(R.layout.city_row, parent, false);

        ViewHolder viewHolder = new ViewHolder(cityView, context);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CityOnlyTitleAndSolrID citys = cityList.get(position);
        TextView textView = holder.txtCityName;
        textView.setText(citys.getTitle());
    }

    @Override
    public int getItemCount() {
        return cityList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Context context;
        private TextView txtCityName;

        public ViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            this.context = context;
            this.txtCityName = itemView.findViewById(R.id.txtCityNameItem);
            txtCityName.setOnClickListener(this::onClick);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, HomeActivity.class);
            String citySolrId = cityList.get(getAdapterPosition()).getSolrId().replace("-", " ");
            StringBuilder stringBuilder = new StringBuilder(citySolrId);
            stringBuilder.deleteCharAt(0);
            citySolrId = stringBuilder.toString();
            intent.putExtra("citysolrid", citySolrId);
            context.startActivity(intent);
        }
    }
}
