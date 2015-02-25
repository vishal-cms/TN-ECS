package cms.com.tn_ecs.fragments;


import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;

import cms.com.tn_ecs.R;
import cms.com.tn_ecs.controller.Controller;
import cms.com.tn_ecs.interfaces.FragmentCommunicator;
import cms.com.tn_ecs.network.Connection;
import cms.com.tn_ecs.network.ParseResult;
import cms.com.tn_ecs.utils.SERVICE_TYPE;

/**
 * A simple {@link Fragment} subclass.
 */
public class CertificateSearch extends android.support.v4.app.Fragment implements View.OnClickListener {

    FragmentCommunicator communicator;
    ArrayList<String> places;
    SERVICE_TYPE serviceType;
    String requestUrl;
    Controller controller;
    Connection connection;
    Spinner placeSpinner;
    EditText txtMotherName;
    EditText txtFatherName;
    EditText txtChildName;
    String sex;
    Button btn_search;
    TextView txtSelectDate;
    RadioButton rbMale, rbFemale;

    TextView txt_SearchTitle;
    ProgressDialog progressdialog;


    public CertificateSearch() {
        //Adding birth or death place to places array list 
        places = new ArrayList<String>();
        places.add("Hospital");
        places.add("Home");
        // by default "Male gender is selected" here M stands for Male And F stands for Female 
        sex = "M";
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_certificate_search, container, false);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //getting interface object for fragment communication. 
        communicator = (FragmentCommunicator) getActivity();
        //getting controller instance to store and retrice object from context (singaltone pattern)
        controller = Controller.getControllerInstance();

        //initializing control from fragment 
        txtMotherName = (EditText) getActivity().findViewById(R.id.txtMotherName);
        txtFatherName = (EditText) getActivity().findViewById(R.id.txtFatherName);
        txtChildName = (EditText) getActivity().findViewById(R.id.txtChildName);
        placeSpinner = (Spinner) getActivity().findViewById(R.id.sp_Place);
        txtSelectDate = (TextView) getActivity().findViewById(R.id.btn_selectDate);
        //txt_SearchTitle = (TextView) getActivity().findViewById(R.id.txt_SearchTitle);
        rbMale = (RadioButton) getActivity().findViewById(R.id.rb_male);
        rbFemale = (RadioButton) getActivity().findViewById(R.id.rb_female);
        btn_search = (Button) getActivity().findViewById(R.id.btn_Search);

        //setting default checked radio button
        rbMale.setSelected(true);

        //assignign On click listener to controls
        rbMale.setOnClickListener(this);
        rbFemale.setOnClickListener(this);
        txtSelectDate.setOnClickListener(this);
        btn_search.setOnClickListener(this);

        //Creating ArrayAdapter
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getActivity(), android.R.layout.simple_spinner_dropdown_item, places);

        //setting adapter to place spinner
        placeSpinner.setAdapter(adapter);

        //Changing page title as per selected service.
        setPageTitle();


    }

    @Override
    public void onResume() {
        super.onResume();

    }

    //setting page title based on the service type selected.
    private void setPageTitle() {
        if (controller.getSelectedService() == SERVICE_TYPE.BIRTH_CERTIFICATE) {
            communicator.actionBarTitle(" Search Birth Certificate");
            txtChildName.setHint("Child Name");
            txtSelectDate.setText("Enter Date Of Birth");
        } else if (controller.getSelectedService() == SERVICE_TYPE.DEATH_CERTIFICATE) {
            communicator.actionBarTitle(" Search Deth Certificate");
            txtChildName.setHint("Person Name");
            txtSelectDate.setText("Enter Date Of Death");
        }

    }

    //handeling on click event of control in this fragmetn
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.rb_male:
                sex = "M";

                break;
            case R.id.rb_female:
                sex = "F";
                break;
            case R.id.btn_selectDate:
                communicator.launchDateDialog("dmy");

                break;
            case R.id.btn_Search:
                requestUrl = generateRequestUrl();
                if (!requestUrl.equals("false")) {
                    new GetSearchresult(requestUrl).execute();
                }
                break;

        }
        Toast.makeText(getActivity(), sex, Toast.LENGTH_LONG).show();

    }

    public void showDate(String date) {
        txtSelectDate.setText(date);
    }

    //based on the data provided by user generating requested url.
    public String generateRequestUrl() {
        connection = new Connection(getActivity());
        String place;
        String requestedUrl = null;
        ArrayList<NameValuePair> parameterlsit = new ArrayList<NameValuePair>();

        if (!txtMotherName.getText().toString().trim().equals("")) {
            parameterlsit.add(new BasicNameValuePair("mothername", txtMotherName.getText().toString()));
        }
        if (!txtFatherName.getText().toString().trim().equals("")) {
            parameterlsit.add(new BasicNameValuePair("fathername", txtFatherName.getText().toString()));
        }
        if (!txtChildName.getText().toString().trim().equals("")) {
            parameterlsit.add(new BasicNameValuePair("childname", txtChildName.getText().toString()));
        }
        place = "" + (placeSpinner.getSelectedItemId() + 1);


        parameterlsit.add(new BasicNameValuePair("sex", sex));

        if (controller.getSelectedDate() != null) {
            if (controller.getSelectedService() == SERVICE_TYPE.BIRTH_CERTIFICATE) {
                parameterlsit.add(new BasicNameValuePair("dob", controller.getSelectedDate()));
                parameterlsit.add(new BasicNameValuePair("birthplace", place));
                requestedUrl = connection.getParametriseUrl(parameterlsit);
                Log.d("Url new ", requestedUrl);
                return requestedUrl;
            } else if (controller.getSelectedService() == SERVICE_TYPE.DEATH_CERTIFICATE) {
                parameterlsit.add(new BasicNameValuePair("dod", controller.getSelectedDate()));
                parameterlsit.add(new BasicNameValuePair("deathplace", place));
                requestedUrl = connection.getParametriseUrl(parameterlsit);
                Log.d("Url new ", requestedUrl);
                return requestedUrl;
            }
        } else {
            Toast.makeText(getActivity(), "Please select date", Toast.LENGTH_LONG).show();
            return "false";
        }
        return requestedUrl;
    }


    private class GetSearchresult extends AsyncTask<String, Void, String> {
        String requestUrl;
        String result;

        private GetSearchresult(String requestUrl) {
            this.requestUrl = requestUrl;
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                result = new Connection(getActivity()).getResult(requestUrl);
                if (controller.getSelectedService() == SERVICE_TYPE.BIRTH_CERTIFICATE) {
                    new ParseResult(result).parseBirthCertificate();
                }
                if (controller.getSelectedService() == SERVICE_TYPE.DEATH_CERTIFICATE) {
                    new ParseResult(result).parseDeathCertificate();
                }

            } catch (Exception e) {
                result = e.toString();
            }


            return result;
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
            communicator.launchCertificateList();
        }
    }
}
    
   
    