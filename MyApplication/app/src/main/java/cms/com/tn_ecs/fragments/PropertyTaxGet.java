package cms.com.tn_ecs.fragments;


import android.app.Fragment;
import android.app.ProgressDialog;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.Arrays;

import cms.com.tn_ecs.R;
import cms.com.tn_ecs.controller.Controller;
import cms.com.tn_ecs.interfaces.FragmentCommunicator;
import cms.com.tn_ecs.network.Connection;
import cms.com.tn_ecs.network.ParseResult;
import cms.com.tn_ecs.objectholders.ZoneInfo;
import cms.com.tn_ecs.utils.Messages;

/**
 * A simple {@link Fragment} subclass.
 */
public class PropertyTaxGet extends android.support.v4.app.Fragment implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    EditText txtBillNo;
    EditText txtSubNo;
    EditText txtOldPropTaxNo;
    Drawable drawable;
    Spinner sp_zone;
    Spinner sp_subDivision;
    Button btn_getArries;

    String requestUrlForGetArrears;
    String requestUrlForGetOldReceipts;

    ArrayList<NameValuePair> parameterlsit;

    Controller controller;
    FragmentCommunicator communicator;

    String channelID;
    String ZONE;
    String DIV_CD;

    String OLD_BILL;
    String OLD_SUB;
    String getArrearsService;
    String getRcptsService;
    ProgressDialog progressdialog;
    private ArrayList<ZoneInfo> zoneDetailList;
    private ArrayList<String> zoneList;
    private ArrayList<String> zoneID;
    private ArrayList<String> subDivisionStringList;
    private ArrayList<String> selectedzonesubdivisionlist;
    private String zoneName;
    private String zoneId = "-1";
    private String subDivision;

    public PropertyTaxGet() {

        controller = Controller.getControllerInstance();
        zoneDetailList = controller.getZoneInfo();
        if (zoneDetailList == null) {
            new ParseResult().ParseZonalDetails();
            zoneDetailList = controller.getZoneInfo();

        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_property_tax_arrears, container, false);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        communicator = (FragmentCommunicator) getActivity();
        communicator.actionBarTitle("Search Property Tax Details.");
        sp_zone = (Spinner) getActivity().findViewById(R.id.sp_zone);
        sp_subDivision = (Spinner) getActivity().findViewById(R.id.sp_subDiv);
        sp_zone.setOnItemSelectedListener(this);
        btn_getArries = (Button) getActivity().findViewById(R.id.btn_getAries);
        btn_getArries.setOnClickListener(this);
        //txtOldPropTaxNo = (EditText) getActivity().findViewById(R.id.txtOldPropTaxNo);
        txtBillNo = (EditText) getActivity().findViewById(R.id.txtBillNo);
        txtSubNo = (EditText) getActivity().findViewById(R.id.txtSubNo);
        if (zoneDetailList.size() > 0) {
            zoneList = new ArrayList<String>();
            zoneList.add("Please Select Zone");
            subDivisionStringList = new ArrayList<String>();
            zoneID = new ArrayList<String>();

            for (int i = 0; i < zoneDetailList.size(); i++) {
                zoneList.add(zoneDetailList.get(i).getZoneName());
                subDivisionStringList.add(zoneDetailList.get(i).getSunDivisions());
                zoneID.add(zoneDetailList.get(i).getZoneID());
            }

            ArrayAdapter<String> zoneAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, zoneList);

            sp_zone.setAdapter(zoneAdapter);
            drawable = getActivity().getResources().getDrawable(R.drawable.text_error_border);


        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position != 0) {
            String[] subdivlist = subDivisionStringList.get(sp_zone.getSelectedItemPosition() - 1).split(",");
            communicator = (FragmentCommunicator) getActivity();
            zoneId = "" + zoneID.get(sp_zone.getSelectedItemPosition() - 1);
            Toast.makeText(getActivity(), zoneName, Toast.LENGTH_SHORT).show();
            selectedzonesubdivisionlist = new ArrayList<String>(Arrays.asList(subdivlist));
            selectedzonesubdivisionlist.add(0, "Select Sub Division");
            ArrayAdapter<String> subDivListView = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, selectedzonesubdivisionlist);

            sp_subDivision.setAdapter(subDivListView);
        } else {
            Toast.makeText(getActivity(), "Please Select Zone And Sub Division", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_getAries:


                channelID = "3";
                ZONE = zoneId;
                if (!ZONE.equals("-1")) {
                    DIV_CD = "" + selectedzonesubdivisionlist.get(sp_subDivision.getSelectedItemPosition());
                } else {
                    Toast.makeText(getActivity(), "Please select Zone And Sub Division", Toast.LENGTH_LONG).show();
                    break;
                }
                if (!txtBillNo.getText().toString().trim().equals("")) {
                    OLD_BILL = txtBillNo.getText().toString().trim();
                    OLD_SUB = "000000";
                    getArrearsService = getRequeastUrls("GetArrears");

                    if (!getArrearsService.equalsIgnoreCase("Some parameter are missing.")) {

                        getRcptsService = getRequeastUrls("getRcpts");
                    } else {
                        Toast.makeText(getActivity(), "Please enter correct data. ", Toast.LENGTH_LONG).show();
                    }


                    new getPropertyTaxData().execute();
                } else {
                    txtBillNo.setBackground(drawable);
                    Toast.makeText(getActivity(), "Please enter correct bill number ", Toast.LENGTH_LONG).show();
                }
                break;
        }


    }


    private String getRequeastUrls(String ServiceId) {
        parameterlsit = new ArrayList<NameValuePair>();
        parameterlsit.add(new BasicNameValuePair("channelID", channelID.trim()));
        parameterlsit.add(new BasicNameValuePair("ZONE", ZONE.trim()));
        parameterlsit.add(new BasicNameValuePair("DIV_CD", DIV_CD.trim()));
        parameterlsit.add(new BasicNameValuePair("OLD_BILL", OLD_BILL.trim()));
        parameterlsit.add(new BasicNameValuePair("OLD_SUB", OLD_SUB.trim()));
        parameterlsit.add(new BasicNameValuePair("serviceId", ServiceId));

        String result = new Connection(getActivity()).getParametriseUrl(parameterlsit);

        return result;

    }


    private class getPropertyTaxData extends AsyncTask<String, Void, String> {

        boolean result = false;

        @Override
        protected String doInBackground(String... params) {


            String Result = new Connection(getActivity()).getResult(getArrearsService);
            if (Result.contains("RESP") && new ParseResult(Result).parseGerArriesResult()) {

                String oldreceiptresult = new Connection(getActivity()).getResult(getRcptsService);
                new ParseResult(oldreceiptresult).parseOldReceiptResult();
                result = true;
            } else {
                result = false;
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressdialog = new ProgressDialog(getActivity());
            progressdialog.setMessage("Please wait.");
            progressdialog.setCanceledOnTouchOutside(false);
            progressdialog.show();

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (progressdialog != null) {
                progressdialog.hide();
            }
            if (result) {
                communicator.launchPropertyTaxShowScreen();
            } else {
                communicator.launchErrorDialog(Messages.SERVER_CONNECTIVITY_ERROR_MESSAGE);
            }

        }
    }

}