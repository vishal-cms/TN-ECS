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
import java.util.Random;

import cms.com.tn_ecs.R;
import cms.com.tn_ecs.controller.Controller;
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
    TextView txtCaptchaText;

    FragmentCommunicator communicator;
    RadioButton rbMale, rbFemale;
    EditText txtName;
    EditText txtEmailAddress;
    EditText txtPhoneNumber;
    EditText txtAddress;
    EditText txtPassword;
    EditText txtCaptchaAnswer;
    EditText txtReEnterPassword;
    String sex;
    Button btnRegister;
    Controller controller;
    String userRegistrationUrl;
    Captcha captcha;

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
        communicator.showActionBar();
        communicator.actionBarTitle("New User Registration");
        txtSelectDate = (TextView) getActivity().findViewById(R.id.btn_selectDOB);
        txtName = (EditText) getActivity().findViewById(R.id.txtName);

        txtEmailAddress = (EditText) getActivity().findViewById(R.id.txtEmailAddress);
        txtPhoneNumber = (EditText) getActivity().findViewById(R.id.txtContactNumber);
        txtAddress = (EditText) getActivity().findViewById(R.id.txtAddress);
        txtPassword = (EditText) getActivity().findViewById(R.id.txtpassword);
        txtReEnterPassword = (EditText) getActivity().findViewById(R.id.txtreEnterpassword);
        txtCaptchaAnswer = (EditText) getActivity().findViewById(R.id.txtAnswer);
        txtCaptchaText = (TextView) getActivity().findViewById(R.id.txtCaptchaText);
        rbMale = (RadioButton) getActivity().findViewById(R.id.rb_GenderMale);
        rbMale.setChecked(true);
        rbFemale = (RadioButton) getActivity().findViewById(R.id.rb_GenderFemale);
        btnRegister = (Button) getActivity().findViewById(R.id.btn_register);
        rbFemale.setOnClickListener(this);
        rbMale.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
        txtSelectDate.setOnClickListener(this);
        controller = Controller.getControllerInstance();
        txtPhoneNumber.getOnFocusChangeListener();

        showCaptcha();
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_selectDOB:
                communicator.launchDateDialog("mdy");
                break;
            case R.id.btn_register:
                try {
                    //check if all fields are entered properly
                    if (!txtName.getText().toString().trim().equalsIgnoreCase("") &&
                            !txtEmailAddress.getText().toString().trim().equalsIgnoreCase("") &&
                            !txtAddress.getText().toString().trim().equalsIgnoreCase("") &&
                            !txtSelectDate.getText().toString().trim().equalsIgnoreCase("") &&
                            !txtPhoneNumber.getText().toString().trim().equalsIgnoreCase("") &&
                            !txtPassword.getText().toString().trim().equalsIgnoreCase("")) {

                        if (GeneralUtilities.validateUserName(txtName.getText().toString().trim())) {
                            //check if user have entered correnct email address.
                            if (new GeneralUtilities().validateEmailAddress(txtEmailAddress.getText().toString().trim())) {


                                //check if user have entered same password and reenter password. 
                                if (txtPassword.getText().toString().trim().equals(txtReEnterPassword.getText().toString().trim())) {

                                    //checking is captcha is enter properly
                                    if (!txtCaptchaAnswer.getText().toString().trim().equals("")) {
                                        int userAnswer = Integer.parseInt(txtCaptchaAnswer.getText().toString().trim());
                                        if (userAnswer == captcha.getAnswer()) {
                                            //generate parametrized user registration link method written in Connection class.
                                            if (new GeneralUtilities(getActivity()).validatePhoneNumber(txtPhoneNumber.getText().toString().trim())) {
                                                ArrayList<NameValuePair> userDetails = new ArrayList<NameValuePair>();


                                                userDetails.add(new BasicNameValuePair("Userdtls", getUserJsonObject().toString()));

                                                userRegistrationUrl = new Connection(getActivity()).getParametriseUrl(userDetails);

                                                Log.d("Url", userRegistrationUrl);

                                                new RegisterUserTask(userRegistrationUrl).execute();
                                            } else {
                                                txtPhoneNumber.setError("Please Enter Correct PhoneNumber");
                                                txtPhoneNumber.requestFocus();
                                                txtCaptchaAnswer.setText("");
                                                showCaptcha();

                                            }
                                        } else {
                                            txtCaptchaAnswer.setError("Please Enter Correct Answer");
                                            ;
                                            //   txtCaptchaAnswer.setBackgroundResource(R.drawable.text_error_border);
                                            txtCaptchaAnswer.requestFocus();
                                            txtCaptchaAnswer.setText("");
                                            showCaptcha();
                                        }
                                    } else {
                                        txtCaptchaAnswer.setError("Please Enter Correct Answer");

                                    }
                                } else {
                                    Toast.makeText(getActivity(), "Password and ReEntered Password Dose Not Match", Toast.LENGTH_SHORT).show();
                                    txtCaptchaAnswer.setText("");
                                    showCaptcha();
                                }
                            } else {
                                txtEmailAddress.setError("Please Enter Correct Email Address");
                                txtEmailAddress.requestFocus();
                                txtCaptchaAnswer.setText("");
                                showCaptcha();
                            }
                        } else {
                            txtName.setError("Please Enter Correct Name");
                            txtName.requestFocus();
                            txtCaptchaAnswer.setText("");
                            showCaptcha();
                        }
                    } else {
                        Toast.makeText(getActivity(), Messages.MANDETORY_FIELDS_MESSAGE, Toast.LENGTH_LONG).show();
                        txtCaptchaAnswer.setText("");
                        showCaptcha();
                    }

                } catch (Exception e) {
                    Toast.makeText(getActivity(), "Please Enter Correct Data.", Toast.LENGTH_LONG).show();
                    txtCaptchaAnswer.setText("");
                    showCaptcha();
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
            userObject.put("mobile", txtPhoneNumber.getText());
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

    private void showCaptcha() {
        captcha = getCaptcha();
        txtCaptchaText.setText(captcha.getCaptchaText());
    }

    ////Captcha Related Logic

    //following function will return Captcha object
    //contain captcha Text And its Answer
    private Captcha getCaptcha() {
        Random random = new Random();
        Captcha captcha = new Captcha();
        int firstRandomValue = random.nextInt((9 - 0) + 1) + 0;
        int SecondRandomValue = random.nextInt((9 - 0) + 1) + 0;
        int operations = random.nextInt((2 - 0) + 1) + 0;

        switch (operations) {
            case 0:
                captcha.setCaptchaText("" + firstRandomValue + "+" + SecondRandomValue + " = ");
                captcha.setAnswer(firstRandomValue + SecondRandomValue);
                break;
            case 1:
                captcha.setCaptchaText("" + firstRandomValue + "-" + SecondRandomValue + " = ");
                captcha.setAnswer(firstRandomValue - SecondRandomValue);
                break;
            case 2:
                captcha.setCaptchaText("" + firstRandomValue + "*" + SecondRandomValue + " = ");
                captcha.setAnswer(firstRandomValue * SecondRandomValue);
                break;
        }
        return captcha;
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
            if (progressdialog != null) {
                progressdialog.dismiss();
            }


            if (!result.equalsIgnoreCase("false") || parseResults != null) {
                if (parseResults[0].equals("1")) {
                    communicator.launchMessageDialog("User Registration Successfull.", "Thank You.");
                    communicator.detachSplashScreen();

                    new GeneralUtilities(getActivity()).writeDataToSharedPreferences(txtEmailAddress.getText().toString(), txtName.getText().toString());
                    controller.setApplicationUserName(parseResults[2].toString().trim());
                    communicator.launchSelectServiceScreen();
                } else if (parseResults[0].equals("0")) {
                    communicator.launchMessageDialog(parseResults[1].toString().toUpperCase(), "Error.");
                    // Toast.makeText(getActivity() , parseResults[1].toString().toUpperCase(), Toast.LENGTH_LONG).show();
                }

            } else {
                communicator.launchMessageDialog("Registration failed! Please Try After Some Time.", "Error.");

            }

        }
    }

    private class Captcha {
        String captchaText;
        int answer;

        public String getCaptchaText() {
            return captchaText;
        }

        public void setCaptchaText(String captchaText) {
            this.captchaText = captchaText;
        }

        public int getAnswer() {
            return answer;
        }

        public void setAnswer(int answer) {
            this.answer = answer;
        }
    }

    
    /*private boolean validatePhoneNumber(String phoneNumber)
    {
        if(phoneNumber.length()<=9 )
    }*/


}