package cms.com.tn_ecs.fragments;


import android.app.Fragment;
import android.app.ProgressDialog;
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

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;

import cms.com.tn_ecs.R;
import cms.com.tn_ecs.controller.Controller;
import cms.com.tn_ecs.interfaces.FragmentCommunicator;
import cms.com.tn_ecs.network.Connection;
import cms.com.tn_ecs.network.ParseResult;
import cms.com.tn_ecs.utils.GeneralUtilities;
import cms.com.tn_ecs.utils.Messages;
import cms.com.tn_ecs.utils.SERVICE_TYPE;
import cms.com.tn_ecs.utils.URLConstants;

/**
 * A simple {@link Fragment} subclass.
 */
public class SplashScreen extends android.support.v4.app.Fragment {


    TextView txtErrorMessage;
    EditText txtUserName;
    EditText txtPassword;
    TextView txtnewuser;
    TextView btnchangePassword;
    TextView btnForgotPassword;
    
    Controller controller;
    Button btnLogin;
    ProgressDialog progressdialog;
    ScrollView loginPanel;
    boolean isNetWorkAvailable;
    boolean isHostAvailable = true;
    boolean isValidUserName;
    FragmentCommunicator communicator;
    boolean isNetworkStateChecked;
    RelativeLayout progressbarLayout;
    JSONObject userLoginDetailsObject;
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
        communicator.hideActionBar();
       String email =  new GeneralUtilities(getActivity()).readDataFromSharedPreferences();
        communicator.actionBarTitle("Tamilnadu E-Sevai");
        txtErrorMessage = (TextView) getActivity().findViewById(R.id.txt_errorMessage);
        txtErrorMessage.setVisibility(View.GONE);
        progressbarLayout = (RelativeLayout) getActivity().findViewById(R.id.relativelayout_progress);
        progressbarLayout.setVisibility(View.GONE);
       controller = Controller.getControllerInstance();
        txtUserName = (EditText) getActivity().findViewById(R.id.txtUserName);
        txtPassword = (EditText) getActivity().findViewById(R.id.txtPassword);
        btnLogin = (Button) getActivity().findViewById(R.id.btn_login);
        loginPanel = (ScrollView) getActivity().findViewById(R.id.loginPanel);
        txtnewuser = (TextView) getActivity().findViewById(R.id.btn_newUser);
        btnchangePassword = (TextView) getActivity().findViewById(R.id.btn_ChangePassword);
        btnForgotPassword = (TextView) getActivity().findViewById(R.id.btn_ForgotPassword);
        txtnewuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                communicator.launchSelectedService(SERVICE_TYPE.REGISTER_USER);
            }
        });
        btnchangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!txtUserName.getText().toString().trim().equals("") && new GeneralUtilities(getActivity()).validateUserName(txtUserName.getText().toString().trim())) {
                    communicator.launchChangePasswordFragment(txtUserName.getText().toString().trim());
                } else {
                    txtErrorMessage.setVisibility(View.VISIBLE);
                    txtErrorMessage.setText("Please Enter Correct Email Address.");

                }
            }
        });
        
        btnForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!txtUserName.getText().toString().trim().equals("") && new GeneralUtilities(getActivity()).validateUserName(txtUserName.getText().toString().trim())) {
                    try {
                        controller.setSelectedService(SERVICE_TYPE.FORGOT_PASSWORD);

                        controller.setRequestedUrl(URLConstants.FORGOT_PASSWORD_URL);
                        JSONObject usernamedetails = new JSONObject();
                        usernamedetails.put("UserName", txtUserName.getText().toString().trim());
                        ArrayList<NameValuePair> usernamelist = new ArrayList<NameValuePair>();
                        usernamelist.add(new BasicNameValuePair("UserName", usernamedetails.toString()));
                        String forgotpasswordUrl = new Connection(getActivity()).getParametriseUrl(usernamelist);
                        new GetPasswordTask(forgotpasswordUrl).execute();
                        
                    }
                    catch(Exception e)
                    {
                        txtErrorMessage.setVisibility(View.VISIBLE);
                        txtErrorMessage.setText("Some Problem Occured Please Try After Some Time.");
                    }
                } else {
                    txtErrorMessage.setVisibility(View.VISIBLE);
                    txtErrorMessage.setText("Please Enter Correct Email Address.");
                }
            }
        });
        
        if(email != null)
        {
            txtUserName.setText(email);
            txtPassword.requestFocus();
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(!txtUserName.getText().toString().trim().equals("") && !txtPassword.getText().toString().trim().equals(""))
               {
                   if(new GeneralUtilities(getActivity()).validateUserName(txtUserName.getText().toString().trim()))
                   {
                       try {
                           userLoginDetailsObject = new JSONObject();
                           userLoginDetailsObject.put("username", txtUserName.getText().toString().trim());
                           userLoginDetailsObject.put("password", txtPassword.getText().toString().trim());
                           ArrayList<NameValuePair> loginDetails= new ArrayList<NameValuePair>();
                           loginDetails.add(new BasicNameValuePair("LoginDtls" , userLoginDetailsObject.toString()));
                          String loginUrl = new Connection(getActivity()).getParametriseUrl(loginDetails);
                           Log.d("LoginUrl" , loginUrl);
                           new ValidateLoginTask(loginUrl).execute();
                           
                       }
                       catch (Exception e)
                       {
                           Log.d("Error" , "Login Error");
                       }
                   }
                   else
                   {
                       txtErrorMessage.setVisibility(View.VISIBLE);
                       txtErrorMessage.setText("Please Enter Correct Email Address.");   
                   }
               }
                else
               {
                   txtErrorMessage.setVisibility(View.VISIBLE);
                   txtErrorMessage.setText(Messages.MANDETORY_FIELDS_MESSAGE);
               }
            }
        });


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


    @Override
    public void onResume() {
        super.onResume();
        controller.setSelectedService(SERVICE_TYPE.USER_LOGIN);
        controller.setRequestedUrl(URLConstants.USER_LOGIN_URL);
    }

    private class ValidateLoginTask extends AsyncTask<String, Void, String> {

        String requestedUrl;
        String result;
        String[] results;

        private ValidateLoginTask(String requestedUrl) {
            this.requestedUrl = requestedUrl;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressbarLayout.setVisibility(View.VISIBLE);

        }
        
        

        @Override
        protected String doInBackground(String... params) {

          result =  new Connection(getActivity()).getResult(requestedUrl);
            
           results =  new ParseResult().parseUserLoginResult(result);
            
         
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressbarLayout.setVisibility(View.GONE);
            if (results != null)
            {
                if(results[0].equals("1"))
                {
                    new GeneralUtilities(getActivity()).writeDataToSharedPreferences(results[2].toString().trim() , "");
                    controller.setApplicationUserName(results[2].toString().trim());
                    communicator.launchSelectServiceScreen();
                }
                else if(!results[0].equals("1"))
                {
                    txtErrorMessage.setVisibility(View.VISIBLE);
                    txtErrorMessage.setText(results[1].toString().toUpperCase());
                }
            }
            else
            {
                txtErrorMessage.setVisibility(View.VISIBLE);
                txtErrorMessage.setText("Unable to Login Please Try After Some Time.");
            }
         
        }
    }
    
    
    
    
    
    ///////Forgot Password Task //////////////////


    private class GetPasswordTask extends AsyncTask<String, Void, String> {
        String forgotPasswordUrl;
        String result;
        String[] parseResults;
        ProgressDialog progressdialog;

        private GetPasswordTask(String registrationUrl) {
            this.forgotPasswordUrl = registrationUrl;
        }

        @Override
        protected String doInBackground(String... params) {

            result = new Connection(getActivity()).getResult(forgotPasswordUrl);
            Log.d("result" , result);
            parseResults = new ParseResult().parseForgotPasswordResult(result);
            
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
                progressdialog.dismiss();
            }
            controller.setSelectedService(SERVICE_TYPE.USER_LOGIN);
            controller.setRequestedUrl(URLConstants.USER_LOGIN_URL);
            if (parseResults != null)
            {
                if(parseResults[0].equals("1"))
                {
                    txtErrorMessage.setVisibility(View.VISIBLE);
                    txtErrorMessage.setText("Password Is Emailed To Your Email Address.");
                }
                else if(!parseResults[0].equals("1"))
                {
                    txtErrorMessage.setVisibility(View.VISIBLE);
                    txtErrorMessage.setText(parseResults[1].toUpperCase());
                }
            }
            else
            {
                txtErrorMessage.setVisibility(View.VISIBLE);
                txtErrorMessage.setText("Unable to Login Please Try After Some Time.");
            }

        }
    }
}
