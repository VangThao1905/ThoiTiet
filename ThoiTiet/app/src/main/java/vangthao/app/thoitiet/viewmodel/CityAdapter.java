package vangthao.app.thoitiet.viewmodel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import vangthao.app.thoitiet.R;
import vangthao.app.thoitiet.model.places.CityOnlyTitleAndSolrID;
import vangthao.app.thoitiet.views.SeeWeatherByPlace;

public class CityAdapter extends BaseAdapter {

    private SeeWeatherByPlace context;
    private int layout;
    private ArrayList<CityOnlyTitleAndSolrID> cityList;

    public CityAdapter(SeeWeatherByPlace context, int layout, ArrayList<CityOnlyTitleAndSolrID> cityOnlyTitleAndSolrIDS) {
        this.context = context;
        this.layout = layout;
        this.cityList = cityOnlyTitleAndSolrIDS;
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

    private class ViewHolder{
        TextView txtCityNameItem;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if(convertView == null){
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout,null);
            holder.txtCityNameItem    = (TextView) convertView.findViewById(R.id.txtDistrictNameItem);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.txtCityNameItem.setText(cityList.get(position).getTitle());
        return convertView;
    }
}
