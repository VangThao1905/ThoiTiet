package vangthao.app.thoitiet.viewmodel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import vangthao.app.thoitiet.R;
import vangthao.app.thoitiet.model.places.DistrictOnlyTitleAndSolrID;
import vangthao.app.thoitiet.views.SeeWeatherByPlace;

public class DistrictAdapter extends BaseAdapter {

    private SeeWeatherByPlace context;
    private int layout;
    private ArrayList<DistrictOnlyTitleAndSolrID> districtList;

    public DistrictAdapter(SeeWeatherByPlace context, int layout, ArrayList<DistrictOnlyTitleAndSolrID> districtList) {
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

    private class ViewHolder{
        TextView txtDistrictNameItem;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if(convertView == null){
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout,null);
            holder.txtDistrictNameItem    = (TextView) convertView.findViewById(R.id.txtDistrictNameItem);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.txtDistrictNameItem.setText(districtList.get(position).getTitle());
        return convertView;
    }
}
