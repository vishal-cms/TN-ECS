package cms.com.tn_ecs.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import cms.com.tn_ecs.R;
import cms.com.tn_ecs.controller.Controller;
import cms.com.tn_ecs.interfaces.FragmentCommunicator;
import cms.com.tn_ecs.objectholders.Certificate;
import cms.com.tn_ecs.utils.SERVICE_TYPE;

/**
 * Created by vishal_mokal on 29/1/15.
 */
public class CertificateListAdapter extends BaseAdapter {

    Controller controller;
    ArrayList<Certificate> certificateList;
    Context context;
    Certificate certificate;
    FragmentCommunicator communicator;
    LayoutInflater inflater;

    public CertificateListAdapter(Context context, ArrayList<Certificate> certificateList) {
        this.context = context;
        controller = Controller.getControllerInstance();
        this.certificateList = certificateList;
        checkIfNoRecordAvailable();
        certificate = new Certificate();
        inflater = LayoutInflater.from(context);
        communicator = (FragmentCommunicator) context;
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return certificateList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.adapter_certificate_list, null);
            viewHolder = new ViewHolder();
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        certificate = certificateList.get(position);

        viewHolder.txt_child_name = (TextView) convertView.findViewById(R.id.txt_Child_Name);

        if (controller.getSelectedService() == SERVICE_TYPE.DEATH_CERTIFICATE) {
            viewHolder.txt_child_name.setText("Person Name");
        }

        viewHolder.txt_date = (TextView) convertView.findViewById(R.id.txtDate);
        viewHolder.txt_father_name = (TextView) convertView.findViewById(R.id.txt_Father_Name);
        viewHolder.txt_reg_no = (TextView) convertView.findViewById(R.id.txt_Reg_No);
        viewHolder.fullLayout = (RelativeLayout) convertView.findViewById(R.id.listItem);
        String name = certificate.getName().toString().trim();
        if (name.length() == 0) {
            viewHolder.txt_child_name.setText("<No Name>");
        } else {
            viewHolder.txt_child_name.setText(certificate.getName().toString());
        }
        viewHolder.txt_date.setText(formatDate(certificate.getDate()));

        String fathername = certificate.getFatherName().trim();
        if (fathername.length() == 0 || fathername.equals(".")) {
            viewHolder.txt_father_name.setText("<No Name>");
        } else {
            viewHolder.txt_father_name.setText(certificate.getFatherName());
        }
        viewHolder.txt_reg_no.setText(certificate.getRegNo());
        viewHolder.fullLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Url", certificateList.get(position).getEnglishUrl());
                controller.setSelectedCertificate(certificateList.get(position));
                    communicator.viewCertificate();
            }
        });
        return convertView;
    }


    //checking if record is empty
    private void checkIfNoRecordAvailable() {
        if (certificateList.size() == 1) {
            if (certificateList.get(0).getDate() == null) {
                certificateList.clear();
            }
        }
    }

    //following function will formate date 
    //from 2015-02-05 To 05-feb-2015
    private String formatDate(String date) {
        SimpleDateFormat fromDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat toDateFormat = new SimpleDateFormat("dd-MMM-yyyy");

        String formatedDate = null;
        try {
            fromDateFormat.setLenient(false);
            toDateFormat.setLenient(false);
            Date newdate = fromDateFormat.parse(date);
            formatedDate = toDateFormat.format(date);
            return formatedDate;
        } catch (Exception e) {
            return date;
        }
    }


    class ViewHolder {
        TextView txt_child_name;
        TextView txt_father_name;
        TextView txt_reg_no;
        TextView txt_date;
        RelativeLayout fullLayout;


    }
}
