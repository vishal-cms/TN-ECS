package cms.com.tn_ecs.fragments;


import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import cms.com.tn_ecs.R;
import cms.com.tn_ecs.controller.Controller;
import cms.com.tn_ecs.interfaces.FragmentCommunicator;
import cms.com.tn_ecs.network.Connection;
import cms.com.tn_ecs.objectholders.ApplicationUser;
import cms.com.tn_ecs.utils.GeneralUtilities;
import cms.com.tn_ecs.utils.SERVICE_TYPE;

/**
 * A simple {@link Fragment} subclass.
 */
public class SplashScreen extends android.support.v4.app.Fragment {


    TextView txtErrorMessage;
    EditText txtUserName;
    EditText txtPassword;
    TextView txtnewuser;
    Button btnLogin;
    ProgressDialog progressdialog;
    ScrollView loginPanel;
    boolean isNetWorkAvailable;
    boolean isHostAvailable = true;
    boolean isValidUserName;
    FragmentCommunicator communicator;
    boolean isNetworkStateChecked;
    RelativeLayout progressbarLayout;

    public SplashScreen() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_splash_screen1, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        communicator = (FragmentCommunicator) getActivity();
        communicator.actionBarTitle("Tamilnadu E-Sevai");
        txtErrorMessage = (TextView) getActivity().findViewById(R.id.txt_errorMessage);
        txtErrorMessage.setVisibility(View.GONE);
        progressbarLayout = (RelativeLayout) getActivity().findViewById(R.id.relativelayout_progress);
        progressbarLayout.setVisibility(View.GONE);
       
        txtUserName = (EditText) getActivity().findViewById(R.id.txtUserName);
        txtPassword = (EditText) getActivity().findViewById(R.id.txtPassword);
        btnLogin = (Button) getActivity().findViewById(R.id.btn_login);
        loginPanel = (ScrollView) getActivity().findViewById(R.id.loginPanel);
        txtnewuser = (TextView) getActivity().findViewById(R.id.btn_newUser);
        txtnewuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                communicator.launchSelectedService(SERVICE_TYPE.REGISTER_USER);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isValidUserName = GeneralUtilities.validateUserName(txtUserName.getText().toString().trim());
                if (!isValidUserName) {
                    Drawable drawable = getActivity().getResources().getDrawable(R.drawable.text_error_border);
                    txtUserName.setBackground(drawable);
                    txtErrorMessage.setVisibility(View.VISIBLE);
                    txtErrorMessage.setText("Please enter valid email address.");
                } else {
                    Drawable drawable = getActivity().getResources().getDrawable(R.drawable.edittextborderwhitebackground);
                    txtUserName.setBackground(drawable);
                    txtErrorMessage.setVisibility(View.GONE);
                    validateLogin();

                }
            }
        });


    }

    @Override
    public void onResume() {
        super.onResume();
        if(!isNetworkStateChecked){
            new CheckNetworkTask().execute();}
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        if(savedInstanceState == null)
        {
            isNetworkStateChecked = false;            
        }
        else if (savedInstanceState.getBoolean("Checked"))
        {
            isNetworkStateChecked = true;
        }
       
    
    }

    private boolean validateLogin() {
        if (isValidUserName && txtUserName.getText().toString().equals("admin@admin.com") && txtPassword.getText().toString() != "" && txtPassword.getText().toString().equals("admin")) {
            ApplicationUser user = new ApplicationUser();
            user.setFullName("Vishal Mokal");
            user.setEmailAddress("admin@admin.com");
            user.setAddress("2/9 Pandurang Krupa ,LineAli Shivaji Road Panvel");
            user.setMobileNumber("889870929");
            user.setSex("Male");
            user.setDateOfBirth("09/04/1989");
            user.setPassword("admin");
            Controller.getControllerInstance().setAppuser(user);
            writeDataToSharedPreferences(user);
            communicator.launchSelectServiceScreen();
            return true;
        } else {
            txtErrorMessage.setVisibility(View.VISIBLE);
            txtErrorMessage.setText("Invalid user.");
            return false;

        }
    }

    public void readDataFromSharedPreferences() {

        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        if (sharedPref.contains("tnecs")) {
        String email = sharedPref.getString("EmailAddress", null);
        if (!email.equals(null)) {
            txtUserName.setText(email);
            txtPassword.requestFocus();
        }
    }

    }



    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d("Called" , "Saved instance stated called");
        outState.putBoolean("Checked" , true);

    }
    public void writeDataToSharedPreferences(ApplicationUser user) {
        SharedPreferences sharedPref = getActivity().getSharedPreferences("tnecs" , Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("FullName", user.getFullName());
        editor.putString("EmailAddress", user.getEmailAddress());
        editor.putString("Address", user.getAddress());
        editor.putString("MobileNumber", user.getMobileNumber());
        editor.putString("Sex", user.getSex());
        editor.putString("DOB", user.getDateOfBirth());
        editor.putString("Password", user.getPassword());
        editor.commit();
    }

    private class CheckNetworkTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressbarLayout.setVisibility(View.VISIBLE);

        }
        
        

        @Override
        protected String doInBackground(String... params) {

          /*  Connection connection = new Connection(getActivity());
            isNetWorkAvailable = connection.isNetworkAvailable();
            if (isNetWorkAvailable) {
                isHostAvailable = connection.isHostAvailable(isNetWorkAvailable);
            }*/
            try {
                Thread.sleep(3000);
            } catch (Exception e) {
                return null;
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressbarLayout.setVisibility(View.GONE);
           /* if (!isNetWorkAvailable) {
                txtErrorMessage.setVisibility(View.VISIBLE);
                txtErrorMessage.setText("Sorry! No internet connection available.");
            }
            if (!isHostAvailable) {
                txtErrorMessage.setVisibility(View.VISIBLE);
                txtErrorMessage.setText("Sorry! Host not reachable.");
            }
            ;*/
            readDataFromSharedPreferences();
        }
    }

}
