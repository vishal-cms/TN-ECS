package cms.com.tn_ecs.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import cms.com.tn_ecs.R;
import cms.com.tn_ecs.controller.Controller;
import cms.com.tn_ecs.objectholders.HalfYear;

/**
 * Created by vishal_mokal on 30/1/15.
 */
public class ShowArrearsAdapter extends BaseAdapter {

    Controller controller;
    ArrayList<HalfYear> halfyear;
    LayoutInflater inflater;
    Context context;

    public ShowArrearsAdapter(Context context, ArrayList<HalfYear> halfyear) {
        controller = Controller.getControllerInstance();
        this.halfyear = halfyear;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return halfyear.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder _viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.adapter_arrears_view, null);
            _viewHolder = new ViewHolder();
            convertView.setTag(_viewHolder);
        } else {
            _viewHolder = (ViewHolder) convertView.getTag();
        }
        HalfYear _haHalfYear = halfyear.get(position);
        _viewHolder.txtArrears = (TextView) convertView.findViewById(R.id.txt_Arrears);
        _viewHolder.txtColl = (TextView) convertView.findViewById(R.id.txt_coll);
        _viewHolder.txtDemand = (TextView) convertView.findViewById(R.id.txt_demand);
        _viewHolder.txtHy = (TextView) convertView.findViewById(R.id.txt_hy);

        _viewHolder.txtArrears.setText(_haHalfYear.getArrears().toString());
        _viewHolder.txtColl.setText(_haHalfYear.getColl());
        _viewHolder.txtDemand.setText(_haHalfYear.getDemand());
        _viewHolder.txtHy.setText(_haHalfYear.getHy());
        return convertView;
    }


    class ViewHolder {
        TextView txtHy;
        TextView txtDemand;
        TextView txtColl;
        TextView txtArrears;

    }
}
