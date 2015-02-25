package cms.com.tn_ecs.fragments;


import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;

import cms.com.tn_ecs.R;
import cms.com.tn_ecs.interfaces.FragmentCommunicator;
import cms.com.tn_ecs.network.Connection;
import cms.com.tn_ecs.network.ParseResult;
import cms.com.tn_ecs.utils.GeneralUtilities;
import cms.com.tn_ecs.utils.Messages;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterUser extends android.support.v4.app.Fragment implements View.OnClickListener {

    TextView txtSelectDate;
    FragmentCommunicator communicator;
    RadioButton rbMale, rbFemale;
    EditText txtName;
    EditText txtEmailAddress;
    EditText txtPhoneNumber;
    EditText txtAddress;
    EditText txtPassword;
    EditText txtReEnterPassword;
    String sex;
    Button btnRegister;


    public RegisterUser() {
        sex = "M";

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register_user, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        communicator = (FragmentCommunicator) getActivity();
        communicator.actionBarTitle("New User Registration");
        txtSelectDate = (TextView) getActivity().findViewById(R.id.btn_selectDOB);
        txtName = (EditText) getActivity().findViewById(R.id.txtName);
        txtEmailAddress = (EditText) getActivity().findViewById(R.id.txtEmailAddress);
        txtPhoneNumber = (EditText) getActivity().findViewById(R.id.txtContactNumber);
        txtAddress = (EditText) getActivity().findViewById(R.id.txtAddress);
        txtPassword = (EditText) getActivity().findViewById(R.id.txtpassword);
        txtReEnterPassword = (EditText) getActivity().findViewById(R.id.txtreEnterpassword);
        rbMale = (RadioButton) getActivity().findViewById(R.id.rb_GenderMale);
        rbMale.setChecked(true);
        rbFemale = (RadioButton) getActivity().findViewById(R.id.rb_GenderFemale);
        btnRegister = (Button) getActivity().findViewById(R.id.btn_register);
        rbFemale.setOnClickListener(this);
        rbMale.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
        txtSelectDate.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_selectDOB:
                communicator.launchDateDialog("mdy");
                break;
            case R.id.btn_register:
                
                //check if all fields are entered properly
                if (!txtName.getText().toString().trim().equalsIgnoreCase("")&&
                        !txtEmailAddress.getText().toString().trim().equalsIgnoreCase("")&&
                        !txtAddress.getText().toString().trim().equalsIgnoreCase("")&&
                        !txtSelectDate.getText().toString().trim().equalsIgnoreCase("")&&
                        !txtPhoneNumber.getText().toString().trim().equalsIgnoreCase("")&&
                        !txtPassword.getText().toString().trim().equalsIgnoreCase("")) {
                    
                    //check if user have entered correnct email address.
                    if(new GeneralUtilities().validateUserName(txtEmailAddress.getText().toString().trim())) {
                        
                        //check if user have entered same password and reenter password. 
                        if(txtPassword.getText().toString().trim().equals(txtReEnterPassword.getText().toString().trim())) {
                            
                            //generate parametrized user registration link method written in Connection class.

                            ArrayList<NameValuePair> userDetails  = new ArrayList<NameValuePair>();
                            
                           
                            userDetails.add(new BasicNameValuePair("Userdtls" , getUserJsonObject().toString()));
                            
                            String userRegistrationUrl =  new Connection(getActivity()).getParametriseUrl(userDetails);

                            Log.d("Url" , userRegistrationUrl);
                            
                            new RegisterUserTask(userRegistrationUrl).execute();
                        }
                        else
                        {
                            Toast.makeText(getActivity() , "Password and ReEntered Password Dose Not Match" , Toast.LENGTH_SHORT).show();
                        }
                        }
                    else
                    {
                        Toast.makeText(getActivity() , "Please Enter Correct Email Address" , Toast.LENGTH_SHORT).show();
                    }
                    }
                else
                {
                    Toast.makeText(getActivity() , Messages.MANDETORY_FIELDS_MESSAGE  ,Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.rb_GenderMale:
                sex = "M";

                break;
            case R.id.rb_GenderFemale:
                sex = "F";
                break;
        }

    }

    private JSONObject getUserJsonObject() {
        JSONObject userObject;
        try {
          
                userObject = new JSONObject();
                userObject.put("name", txtName.getText());
                userObject.put("email", txtEmailAddress.getText());
                userObject.put("address", txtAddress.getText());
                userObject.put("mobile" , txtPhoneNumber.getText());
                userObject.put("sex", sex);
                userObject.put("Dob", txtSelectDate.getText());
                userObject.put("password", txtPassword.getText());
            return userObject;
        } catch (Exception e) {
            userObject = null;
            return userObject;
        }
    }

    public void showDate(String date) {
        txtSelectDate.setText(date);
    }


    private class RegisterUserTask extends AsyncTask<String, Void, String> {
        String registrationUrl;
        String result;
        String[] parseResults;
        ProgressDialog progressdialog;
        private RegisterUserTask(String registrationUrl) {
            this.registrationUrl = registrationUrl;
        }

        @Override
        protected String doInBackground(String... params) {

            result = new Connection(getActivity()).getResult(registrationUrl);
            parseResults = new ParseResult().parseUserRegistrationResult(result);
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
            if (result.equals("false")) {
                Toast.makeText(getActivity(), " registration failed", Toast.LENGTH_LONG).show();
            }
            if (progressdialog != null)
            {
                progressdialog.dismiss();
            }
            
           if(parseResults != null)
           {
               if (parseResults[0].equals("1"))
               {
                   
                   communicator.launchSelectServiceScreen();
               }
               else if(parseResults[0].equals("0"))
               {
                   Toast.makeText(getActivity() , parseResults[1].toString().toUpperCase(), Toast.LENGTH_LONG).show();
               }
               
           }
            else
           {
               Toast.makeText(getActivity() , "Registration failed! Please Try After Some Time" , Toast.LENGTH_LONG).show();
           }

        }
    }

}