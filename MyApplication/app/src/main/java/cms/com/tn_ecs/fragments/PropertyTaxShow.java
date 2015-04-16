package cms.com.tn_ecs.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import cms.com.tn_ecs.R;
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
    ArrayList<String> oldReciptNumbers;
    ArrayList<HalfYear> halfyeardata;
    PropertyTaxArrears propertyTaxArrears;
    PropertyTaxPagerAdapter myAdapter;
    FragmentManager manager;
    PagerTabStrip tabStrip;
    ViewPager viewPager;
    TextView txtTotalArrears;

    //listFlag =0 showing arrears //listFlaf =1 old receipt shown; 
    int listFlag;

    public PropertyTaxShow() {
        listFlag = 0;
        controller = Controller.getControllerInstance();
        propertyTaxArrears = controller.getPropertyTaxArrears();

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("FragmentLog" , "onCreateView");
        return inflater.inflate(R.layout.fragment_show_arrears, container, false);
        

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("FragmentLog" , "onCreate");
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.d("FragmentLog" , "FragmentOnAttached");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("FragmentLog" , "FragmentOnStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("FragmentLog" , "FragmentOnResume");
        

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d("FragmentLog" , "FragmentOnActivityCreate");
        FragmentCommunicator communicator = (FragmentCommunicator) getActivity();
        communicator.actionBarTitle("Property Tax Details");
       
        txtName = (TextView) getActivity().findViewById(R.id.txtName);
        txtdoorNo = (TextView) getActivity().findViewById(R.id.txtdoandst);
        txtTotalArrears = (TextView) getActivity().findViewById(R.id.txttotalArears);
        viewPager = (ViewPager)getActivity().findViewById(R.id.pager_propertyTaxDetails);
        tabStrip = (PagerTabStrip)getActivity().findViewById(R.id.tab_title);
        tabStrip.setTabIndicatorColor(getActivity().getResources().getColor(R.color.PrimaryColor));
        tabStrip.setDrawFullUnderline(false);
        
        manager = getChildFragmentManager();
         myAdapter = new PropertyTaxPagerAdapter(manager);
        viewPager.setAdapter(myAdapter);
        txtName.setText(propertyTaxArrears.getName());
        txtdoorNo.setText(propertyTaxArrears.getOd() + " , " + propertyTaxArrears.getSt());


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
       
        }
    }

    private void showLists(int listFlag) {
     
    }


 /*   private double getTotalPropertyTaxArrears() {
        double total = 0.0;
        for (int i = 0; i < halfyeardata.size(); i++) {
            total = total + Double.parseDouble(halfyeardata.get(i).getDemand());
        }
        return total;

    }*/
    
    
    
    
    private class PropertyTaxPagerAdapter extends FragmentPagerAdapter
    {
        private PropertyTaxPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment=null;
            switch ( position)
            {
                case 0:
                    fragment = new PropertyTaxShowArrearsFragment();
                    break;
                case 1:
                    fragment = new PropertyTaxShowOldReceiptNumberFragment();
                    break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            
            switch (position)
            {
                case 0:
                    return "Property Tax Arrears";
                case 1:
                    return "Old Receipts";
                
            }
            
            return null;
        }
    }

}
