package cms.com.tn_ecs.fragments;


import android.app.Fragment;
import android.app.ProgressDialog;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import cms.com.tn_ecs.R;
import cms.com.tn_ecs.controller.Controller;
import cms.com.tn_ecs.interfaces.FragmentCommunicator;
import cms.com.tn_ecs.network.Connection;
import cms.com.tn_ecs.network.ParseResult;
import cms.com.tn_ecs.objectholders.PropertyTaxSearchDetails;
import cms.com.tn_ecs.objectholders.ZoneInfo;
import cms.com.tn_ecs.utils.GeneralUtilities;
import cms.com.tn_ecs.utils.URLConstants;

/**
 * A simple {@link Fragment} subclass.
 */
public class PropertyTaxGet extends android.support.v4.app.Fragment implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    EditText txtBillNo;
    EditText txtSubNo;
   
    Drawable drawable;
    RadioButton rb_NewProperty , rb_OldProperty;
    Spinner sp_zone;
    Spinner sp_subDivision;
    Spinner sp_localBody;
    Button btn_getArries;

    String requestUrlForGetArrears;
    String requestUrlForGetOldReceipts;

    ArrayList<NameValuePair> parameterlsit;

    Controller controller;
    FragmentCommunicator communicator;

    String channelID;
    String ZONE;
    String DIV_CD;
    String propertyType;
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
    
    private ArrayList<String> localBodyDisplayList;
    private ArrayList<String> localBodyValueList;

    public PropertyTaxGet() {

        controller = Controller.getControllerInstance();
        zoneDetailList = controller.getZoneInfo();
        if (zoneDetailList == null) {
            new ParseResult().ParseZonalDetails();
            zoneDetailList = controller.getZoneInfo();
            
        }
        localBodyDisplayList = new ArrayList<String>();
        localBodyValueList = new ArrayList<String>();
       if(propertyType == null)
       {
           propertyType = "new";
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
        Collections.addAll(localBodyDisplayList , getActivity().getResources().getStringArray(R.array.local_body_displya_name));       
        Collections.addAll(localBodyValueList , getActivity().getResources().getStringArray(R.array.local_body_displya_value));
        sp_zone = (Spinner) getActivity().findViewById(R.id.sp_zone);
        sp_subDivision = (Spinner) getActivity().findViewById(R.id.sp_subDiv);
        sp_localBody = (Spinner) getActivity().findViewById(R.id.sp_localBody);
        
        rb_NewProperty = (RadioButton)getActivity().findViewById(R.id.rb_newProperty);
        rb_OldProperty = (RadioButton)getActivity().findViewById(R.id.rb_oldProperty);
        
        rb_NewProperty.setOnClickListener(this);
        rb_OldProperty.setOnClickListener(this);
        
        sp_zone.setOnItemSelectedListener(this);
        btn_getArries = (Button) getActivity().findViewById(R.id.btn_getAries);
        btn_getArries.setOnClickListener(this);

        txtBillNo = (EditText) getActivity().findViewById(R.id.txtBillNo);
        txtSubNo = (EditText) getActivity().findViewById(R.id.txtSubNo);
        if (zoneDetailList.size() > 0) {
            zoneList = new ArrayList<String>();
            zoneList.add("Please Select Zone *");
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

            ArrayAdapter<String> localBodyAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item , localBodyDisplayList);
            sp_localBody.setAdapter(localBodyAdapter);

        }
        if(propertyType.equalsIgnoreCase("new"))
        {
            sp_zone.setVisibility(View.VISIBLE);
            sp_localBody.setVisibility(View.GONE);
        }
        else if(propertyType.equalsIgnoreCase("old"))
        {
            sp_zone.setVisibility(View.GONE);
            sp_subDivision.setVisibility(View.GONE);
            sp_localBody.setVisibility(View.VISIBLE);
        }
            
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position != 0) {
            String[] subdivlist = subDivisionStringList.get(sp_zone.getSelectedItemPosition() - 1).split(",");
            communicator = (FragmentCommunicator) getActivity();
            zoneId = "" + zoneID.get(sp_zone.getSelectedItemPosition()-1);
         
            selectedzonesubdivisionlist = new ArrayList<String>(Arrays.asList(subdivlist));
            selectedzonesubdivisionlist.add(0, "Select Sub Division *");
            ArrayAdapter<String> subDivListView = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, selectedzonesubdivisionlist);
            sp_subDivision.setVisibility(View.VISIBLE);
            sp_subDivision.setAdapter(subDivListView);
        } else {
            Toast.makeText(getActivity(), "Please Select Zone And Sub Division", Toast.LENGTH_LONG).show();
            sp_subDivision.setVisibility(View.GONE);
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
                OLD_BILL = txtBillNo.getText().toString().trim();
                ZONE = zoneId;
                if (rb_NewProperty.isChecked())
                {
                   
                    if(!ZONE.equalsIgnoreCase("-1"))
                    {
                        if (sp_subDivision.getSelectedItemPosition() == 0) {
                            GeneralUtilities.showToastMessage(getActivity(), "please Select Sub Division");
                            break;
                        } else {
                            DIV_CD = "" + selectedzonesubdivisionlist.get(sp_subDivision.getSelectedItemPosition());
                            OLD_SUB = "000000";
                            if (!OLD_BILL.equalsIgnoreCase("")) {
                                getArrearsService = getRequeastUrls("GetArrears");

                                if (!getArrearsService.equalsIgnoreCase("Some parameter are missing.")) {

                                    getRcptsService = getRequeastUrls("getRcpts");

                                    new getPropertyTaxData().execute();

                                } else {
                                    Toast.makeText(getActivity(), "Please enter correct data. ", Toast.LENGTH_LONG).show();
                                }
                            }
                            else
                            {
                                   GeneralUtilities.showToastMessage(getActivity() , "Please Enter Bill Number"); 
                            }
                        }
                        
                    } else {
                        GeneralUtilities.showToastMessage(getActivity(), "Please select Zone And Sub Division");
                        break;
                    
                    }
                   
                }
                
                
                ///////////Logic for Fetching Old Property Tax receipts and arrears//////////////
                
                else if(rb_OldProperty.isChecked())
                {
                if(sp_localBody.getSelectedItemPosition() != 0)
                {
                    if(!OLD_BILL.equalsIgnoreCase(""))
                    {
                        OLD_SUB = "000000";
                        
                        if(!localBodyValueList.get(sp_localBody.getSelectedItemPosition()).equalsIgnoreCase("coc"))
                        {
                            OLD_BILL = localBodyValueList.get(sp_localBody.getSelectedItemPosition()).toUpperCase()+ "-"+OLD_BILL;
                            OLD_BILL = OLD_BILL.toUpperCase();
                        }
                        
                        getArrearsService = getRequestUrlForOldPropertyTax("GetArrears");
                        
                        Log.d("Old_propoerty" , getArrearsService);

                        if (!getArrearsService.equalsIgnoreCase("Some parameter are missing.")) {

                            getRcptsService = getRequestUrlForOldPropertyTax("getRcpts");
                            
                            Log.d("Old_propoerty" , getRcptsService);
                            new getPropertyTaxData().execute();
                        } else {
                            Toast.makeText(getActivity(), "Please enter correct data. ", Toast.LENGTH_LONG).show();
                        }
                    }
                    else
                    {
                        GeneralUtilities.showToastMessage(getActivity() , "Please Enter Old Bill Number");
                    }
                }
                    else
                {
                    GeneralUtilities.showToastMessage(getActivity() , "Please Select local Body.");
                }
                }
                break;
            case R.id.rb_newProperty:
                if (sp_localBody.getVisibility() == View.VISIBLE)
                {
                    sp_localBody.setVisibility(View.GONE);
                    sp_subDivision.setVisibility(View.VISIBLE);
                    sp_zone.setVisibility(View.VISIBLE);
                    if(controller.getProprtyPropertyTaxSearchDetails() != null) {
                        controller.getProprtyPropertyTaxSearchDetails().setLocalBody("no");
                    }
                      
                    propertyType = "new";
                    txtBillNo.setHint("New Property Tax Bill No");
                    controller.setRequestedUrl(URLConstants.PROPERTY_TAX_MASTER_URL);
                }
                break;
            case  R.id.rb_oldProperty:
            {
                if (sp_localBody.getVisibility() != View.VISIBLE)
                {
                    sp_localBody.setVisibility(View.VISIBLE);
                    sp_subDivision.setVisibility(View.GONE);
                    sp_zone.setVisibility(View.GONE);
                    txtBillNo.setHint("Old Property Tax Bill No");
                    
                }
                propertyType = "old";
                controller.setRequestedUrl(URLConstants.OLD_PROPERTY_TAX_MASTER_URL);
            }
                break;
            
        }


    }


    private String getRequeastUrls(String ServiceId) {

        PropertyTaxSearchDetails propertyTaxSearchDetails = new PropertyTaxSearchDetails();

        parameterlsit = new ArrayList<NameValuePair>();
        parameterlsit.add(new BasicNameValuePair("channelID", channelID.trim()));
        propertyTaxSearchDetails.setChannelID(channelID.trim());
        parameterlsit.add(new BasicNameValuePair("ZONE", ZONE.trim()));
        propertyTaxSearchDetails.setZONE(ZONE.trim());
        parameterlsit.add(new BasicNameValuePair("DIV_CD", DIV_CD.trim()));
        propertyTaxSearchDetails.setDIV_CD(DIV_CD.trim());
        parameterlsit.add(new BasicNameValuePair("OLD_BILL", OLD_BILL.trim()));
        propertyTaxSearchDetails.setOLD_BILL(OLD_BILL.trim());
        parameterlsit.add(new BasicNameValuePair("OLD_SUB", OLD_SUB.trim()));
        propertyTaxSearchDetails.setOLD_SUB(OLD_SUB.trim());
        parameterlsit.add(new BasicNameValuePair("serviceId", ServiceId));
        
        propertyTaxSearchDetails.setPropertyType("new");
        String result = new Connection(getActivity()).getParametriseUrl(parameterlsit);
        controller.setProprtyPropertyTaxSearchDetails(propertyTaxSearchDetails);

        return result;

    }
    
    private String getRequestUrlForOldPropertyTax(String ServiceId)
    {
        PropertyTaxSearchDetails propertyTaxSearchDetails = new PropertyTaxSearchDetails();

        parameterlsit = new ArrayList<NameValuePair>();
        parameterlsit.add(new BasicNameValuePair("channelID", channelID.trim()));
    
        parameterlsit.add(new BasicNameValuePair("OLD_PROP_ID", OLD_BILL.trim()));
        propertyTaxSearchDetails.setOLD_BILL(OLD_BILL.trim());

       
        parameterlsit.add(new BasicNameValuePair("serviceId", ServiceId));

        propertyTaxSearchDetails.setLocalBody(localBodyValueList.get(sp_localBody.getSelectedItemPosition()));

        propertyTaxSearchDetails.setPropertyType("old");
        String result = new Connection(getActivity()).getParametriseUrl(parameterlsit);
        controller.setProprtyPropertyTaxSearchDetails(propertyTaxSearchDetails);

        return result;
    }


    private class getPropertyTaxData extends AsyncTask<String, Void, String> {

        boolean result = false;

        @Override
        protected String doInBackground(String... params) {


            String Result = new Connection(getActivity()).getResult(getArrearsService);
            if (!Result.contains("Error")) {
                if (Result.contains("RESP") && new ParseResult(Result).parseGerArriesResult()) {

                    String oldreceiptresult = new Connection(getActivity()).getResult(getRcptsService);
                    new ParseResult(oldreceiptresult).parseOldReceiptResult();
                    result = true;
                } else {
                    result = false;
                }
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
                communicator.launchMessageDialog("Sorry! No Record Found Or There Is No Connectivity To server Please try After Some Time.", "Error");
            }

        }
    }
    
    
    
  

}