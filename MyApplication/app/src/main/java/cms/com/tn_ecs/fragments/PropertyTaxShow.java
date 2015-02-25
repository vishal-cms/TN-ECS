package cms.com.tn_ecs.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import cms.com.tn_ecs.R;
import cms.com.tn_ecs.adapters.ShowArrearsAdapter;
import cms.com.tn_ecs.adapters.ViewOldReceiptAdapter;
import cms.com.tn_ecs.controller.Controller;
import cms.com.tn_ecs.interfaces.FragmentCommunicator;
import cms.com.tn_ecs.objectholders.HalfYear;
import cms.com.tn_ecs.objectholders.PropertyTaxArrears;


public class PropertyTaxShow extends android.support.v4.app.Fragment implements View.OnClickListener {

    Controller controller;
    TextView txtName;
    TextView txtstrret;
    TextView txtdoorNo;
    ListView lstShowArrears;
    ListView lstOld_Receipts;
    TextView listLabel;
    RelativeLayout emptyView;

    //listFlag =0 showing arrears //listFlaf =1 old receipt shown; 
    int listFlag;

    public PropertyTaxShow() {
        listFlag = 0;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_show_arrears, container, false);


    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        FragmentCommunicator communicator = (FragmentCommunicator) getActivity();
        communicator.actionBarTitle("Property Tax Details");
        controller = Controller.getControllerInstance();
        txtName = (TextView) getActivity().findViewById(R.id.txtName);
        txtdoorNo = (TextView) getActivity().findViewById(R.id.txtdoandst);
        listLabel = (TextView) getActivity().findViewById(R.id.ListLabel);
        lstOld_Receipts = (ListView) getActivity().findViewById(R.id.lst_oldReceipt);
        emptyView = (RelativeLayout) getActivity().findViewById(R.id.panel_emptyView);
        lstOld_Receipts.setEmptyView(emptyView);

        if (listFlag == 0) {
            listLabel.setText("Old Receipts");
            lstOld_Receipts.setVisibility(View.INVISIBLE);
        }

        listLabel.setOnClickListener(this);
        lstShowArrears = (ListView) getActivity().findViewById(R.id.lst_assrers);
        PropertyTaxArrears propertyTaxArrears = controller.getPropertyTaxArrears();

        ArrayList<HalfYear> halfyeardata = propertyTaxArrears.getHalfYear();

        ShowArrearsAdapter showArrearsAdapter = new ShowArrearsAdapter(getActivity(), halfyeardata);

        lstShowArrears.setAdapter(showArrearsAdapter);
        
        
        
        ArrayList<String> oldReciptNumbers = controller.getOldReceiptNumber();

        ViewOldReceiptAdapter oldreceiptsAdapter = new ViewOldReceiptAdapter(getActivity(), oldReciptNumbers);
        lstOld_Receipts.setAdapter(oldreceiptsAdapter);

        txtName.setText(propertyTaxArrears.getName());
        txtdoorNo.setText(propertyTaxArrears.getOd() + " , " + propertyTaxArrears.getSt());
       

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ListLabel:
                if (listFlag == 0) {
                    listFlag = 1;
                    listLabel.setText("View Arrears");
                    lstOld_Receipts.setVisibility(View.VISIBLE);
                    lstShowArrears.setVisibility(View.INVISIBLE);
                } else if (listFlag == 1) {
                    listFlag = 0;
                    listLabel.setText("View Old Receipts");
                    lstOld_Receipts.setVisibility(View.INVISIBLE);
                    lstShowArrears.setVisibility(View.VISIBLE);
                }
                break;
        }
    }
}
