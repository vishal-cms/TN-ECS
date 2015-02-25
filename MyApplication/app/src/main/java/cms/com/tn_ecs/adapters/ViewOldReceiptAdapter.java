package cms.com.tn_ecs.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import cms.com.tn_ecs.R;

/**
 * Created by vishal_mokal on 19/2/15.
 */
public class ViewOldReceiptAdapter extends BaseAdapter {

    ArrayList<String> oldreceipts;
    LayoutInflater inflater;
    Context context;

    public ViewOldReceiptAdapter(Context context, ArrayList<String> oldreceipts) {

        this.oldreceipts = oldreceipts;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return oldreceipts.size();
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
        viewHolder viewholder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.adapter_old_receipt_view, null);
            viewholder = new viewHolder();
            convertView.setTag(viewholder);
        } else {
            viewholder = (ViewOldReceiptAdapter.viewHolder) convertView.getTag();
        }

        viewholder.txtoldreceiptview = (TextView) convertView.findViewById(R.id.txt_Receipt_Number);
        viewholder.txtoldreceiptview.setText(oldreceipts.get(position));
        viewholder.txtoldreceiptview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Work in progress", Toast.LENGTH_LONG).show();
            }
        });

        return convertView;
    }


    class viewHolder {
        TextView txtoldreceiptview;
    }

}
