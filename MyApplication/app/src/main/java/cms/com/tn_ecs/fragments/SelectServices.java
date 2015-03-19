package cms.com.tn_ecs.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import cms.com.tn_ecs.R;
import cms.com.tn_ecs.controller.Controller;
import cms.com.tn_ecs.interfaces.FragmentCommunicator;
import cms.com.tn_ecs.utils.SERVICE_TYPE;

/**
 * A simple {@link Fragment} subclass.
 */
public class SelectServices extends android.support.v4.app.Fragment implements View.OnClickListener {

    TextView txtBirthCertificate;
    TextView txtDeathCertificate;
    TextView txtElectricityBill;
    TextView txtPropertyTax;
    TextView txtUserName;
    Controller controller;


    FragmentCommunicator communicator;

    public SelectServices() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_select_services, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        communicator = (FragmentCommunicator) getActivity();
        communicator.showActionBar();
        controller = Controller.getControllerInstance();
        txtBirthCertificate = (TextView) getActivity().findViewById(R.id.txtBirthCertificate);
        txtDeathCertificate = (TextView) getActivity().findViewById(R.id.txtDeathCertificate);
        txtElectricityBill = (TextView) getActivity().findViewById(R.id.txtElectricityBill);
        txtPropertyTax = (TextView) getActivity().findViewById(R.id.txtPropertyTax);
        txtUserName = (TextView) getActivity().findViewById(R.id.txtViewUserName);

        txtBirthCertificate.setOnClickListener(this);
        txtDeathCertificate.setOnClickListener(this);
        txtElectricityBill.setOnClickListener(this);
        txtPropertyTax.setOnClickListener(this);
        communicator.actionBarTitle("E-Sevai Services");
        txtUserName.setText("Welcome : " + controller.getApplicationUserName());
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txtBirthCertificate:
                communicator.launchSelectedService(SERVICE_TYPE.BIRTH_CERTIFICATE);
                break;
            case R.id.txtDeathCertificate:
                communicator.launchSelectedService(SERVICE_TYPE.DEATH_CERTIFICATE);
                break;
            case R.id.txtElectricityBill:
                Toast.makeText(getActivity(), "Work in progress", Toast.LENGTH_LONG).show();
                break;
            case R.id.txtPropertyTax:
                communicator.launchSelectedService(SERVICE_TYPE.PROPERTY_TAX);
                break;
        }
    }
}
