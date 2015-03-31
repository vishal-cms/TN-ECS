package cms.com.tn_ecs.activity;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import cms.com.tn_ecs.R;
import cms.com.tn_ecs.controller.Controller;
import cms.com.tn_ecs.fragments.CertificateList;
import cms.com.tn_ecs.fragments.CertificateSearch;
import cms.com.tn_ecs.fragments.ChangePasswordFragment;
import cms.com.tn_ecs.fragments.DatePickerFragment;
import cms.com.tn_ecs.fragments.DisplayCertificate;
import cms.com.tn_ecs.fragments.MessageDialogFragment;
import cms.com.tn_ecs.fragments.PropertyTaxGet;
import cms.com.tn_ecs.fragments.PropertyTaxShow;
import cms.com.tn_ecs.fragments.RegisterUser;
import cms.com.tn_ecs.fragments.SelectServices;
import cms.com.tn_ecs.fragments.SplashScreen;
import cms.com.tn_ecs.interfaces.FragmentCommunicator;
import cms.com.tn_ecs.utils.SERVICE_TYPE;
import cms.com.tn_ecs.utils.URLConstants;

//import cms.com.tn_ecs.fragments.SplashScreen;


public class ContainerActivity extends ActionBarActivity implements FragmentCommunicator, android.support.v4.app.FragmentManager.OnBackStackChangedListener {
    android.support.v4.app.FragmentManager manager;

    SplashScreen splashscreenfragment;
    CertificateSearch searchCertificateFragment;
    Controller controller;
    CertificateSearch certificateSearch;
    CertificateList certificatelist;
    RegisterUser registerUser;


    @Override
    public void showDate() {
        String date = null;
        if (controller.getSelectedDate() != null && !controller.getSelectedDate().equalsIgnoreCase("false")) {
            date = controller.getSelectedDate();
            if (certificateSearch != null) {
                certificateSearch.showDate(date);
            } else if (registerUser != null) {
                registerUser.showDate(date);
            }

        } else {
            launchMessageDialog("Please Select Correct Date.", "Error");
        }
    }

    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);
        //setActionbarLogo();

        manager = getSupportFragmentManager();
        manager.addOnBackStackChangedListener(this);

        getSupportActionBar().setDisplayShowHomeEnabled(false);
        controller = Controller.getControllerInstance();
        
        controller.setSelectedService(SERVICE_TYPE.USER_LOGIN);
        controller.setRequestedUrl(URLConstants.USER_LOGIN_URL);
        if (!new File(URLConstants.APPLICATION_BASE_PATH + "/.zone.txt").exists()) {
            copyAssets();
            createApplicationDir();
        }
        launchSplashScreen();

    }

    @Override
    protected void onResume() {
        super.onResume();
       
        

    }

    public void showUpNavigation() {
        if (manager.getBackStackEntryCount() > 0)
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_TITLE);
        else if (manager.getBackStackEntryCount() == 0)
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE);
    }

    @Override
    public boolean onSupportNavigateUp() {
        manager.popBackStack();
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.d("Saved instance called", "Saved instance called");
    }

    @Override
    public void launchSelectServiceScreen() {
        SelectServices selectServices = new SelectServices();
        android.support.v4.app.FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fragment_container, selectServices, "selectServices");
        manager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        transaction.commitAllowingStateLoss();
    }

    @Override
    public void actionBarTitle(String title) {
        android.support.v7.app.ActionBar actionBar = this.getSupportActionBar();
        actionBar.setTitle(title);


    }

    @Override
    public void launchSplashScreen() {
        splashscreenfragment = new SplashScreen();
        android.support.v4.app.FragmentTransaction transaction = manager.beginTransaction();

        transaction.add(R.id.fragment_container, splashscreenfragment, "splashscreen");
        actionBarTitle("Tamilnadu E-Seva");
       
        transaction.commit();
    }

    @Override
    public void launchDispalyCertificateScreen() {
        DisplayCertificate displayCertificate = new DisplayCertificate();
        android.support.v4.app.FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fragment_container, displayCertificate, "displaycertificatescreen");
        transaction.addToBackStack("displaycertificatescreen");
        transaction.commit();
    }


    @Override
    public void launchMessageDialog(String dialogMessage, String dialogTitle) {
        MessageDialogFragment messageDialogFragment = new MessageDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString("dialogMessage" ,dialogMessage);
        bundle.putString("dialogTitle" ,dialogTitle);
        messageDialogFragment.setArguments(bundle);

        messageDialogFragment.show(manager, "ErrorDialog");
    }

    @Override
    public void showActionBar() {
        android.support.v7.app.ActionBar actionBar = this.getSupportActionBar();
        actionBar.show();
    }

    @Override
    public void hideActionBar() {
        android.support.v7.app.ActionBar actionBar = this.getSupportActionBar();
        actionBar.hide();
    }

    @Override
    public void launchPropertyTaxShowScreen() {
        PropertyTaxShow propertyTaxShow = new PropertyTaxShow();
        android.support.v4.app.FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fragment_container, propertyTaxShow, "propertyTaxShow");
        transaction.addToBackStack("propertyTaxShow");

        transaction.commit();
    }

    @Override
    public void launchCertificateList() {
        certificatelist = new CertificateList();
        android.support.v4.app.FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fragment_container, certificatelist, "certificateList");
        transaction.addToBackStack("certificateList");
        transaction.commit();
    }

    @Override
    public void launchSearchScreen() {
        certificateSearch = new CertificateSearch();
        android.support.v4.app.FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fragment_container, certificateSearch, "certificateSearch");
        transaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_left, 0, 0);
        transaction.addToBackStack("certificateSearch");
        transaction.commit();
    }

    public void detachSplashScreen() {
        android.support.v4.app.Fragment framgnet = manager.findFragmentByTag("splashscreen");
        android.support.v4.app.FragmentTransaction transaction = manager.beginTransaction();
        transaction.detach(framgnet);
        transaction.commit();
    }

    @Override
    public void launchDateDialog(String dateFormat) {
        DatePickerFragment datePicker = new DatePickerFragment(dateFormat);
        datePicker.show(manager, "datepicker");
    }

    @Override
    public void launchRegisterNewUserScreen() {
        registerUser = new RegisterUser();
        android.support.v4.app.FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fragment_container, registerUser, "registerUser");
        transaction.addToBackStack("registerUser");

        transaction.commit();
    }

    @Override
    public void launchSelectedService(SERVICE_TYPE service_type) {

        switch (service_type) {
            case BIRTH_CERTIFICATE:
                controller.setRequestedUrl(URLConstants.BIRTH_CERTIFICATE_URL);
                controller.setRequestedDownloadUrl(URLConstants.BIRTH_CERTIFICATE_PDF_URL);
                controller.setSelectedService(SERVICE_TYPE.BIRTH_CERTIFICATE);

                launchSearchScreen();
                break;
            case DEATH_CERTIFICATE:
                controller.setRequestedUrl(URLConstants.DEATH_CERTIFICATE_URL);
                controller.setRequestedDownloadUrl(URLConstants.DEATH_CERTIFICATE_PDF_URL);
                controller.setSelectedService(SERVICE_TYPE.DEATH_CERTIFICATE);

                launchSearchScreen();
                break;
            case PROPERTY_TAX:
                controller.setSelectedService(SERVICE_TYPE.PROPERTY_TAX);
                controller.setRequestedUrl(URLConstants.PROPERTY_TAX_MASTER_URL);
                launchPropertyTaxScreen();
                break;
            case REGISTER_USER:
                controller.setSelectedService(SERVICE_TYPE.REGISTER_USER);
                controller.setRequestedUrl(URLConstants.USER_REGISTRATION_URL);
                launchRegisterNewUserScreen();

                break;

        }

    }


    @Override
    public void viewCertificate() {
        if (certificatelist != null) {
            certificatelist.downloadAndViewCertificate();
        }
    }

    private void createApplicationDir() {
        File f = new File(URLConstants.APPLICATION_BASE_PATH);
        if (!f.exists()) {
            f.mkdir();
        }
    }

    private void copyAssets() {
        AssetManager assetManager = getAssets();
        String rootPath = null;
        String files[] = null;
        try {
            rootPath = URLConstants.APPLICATION_BASE_PATH;
            Log.d("Root path", rootPath);
            Controller.getControllerInstance().setApplicationRootPath(rootPath);
            files = assetManager.list("");
        } catch (Exception e) {
            Log.e("tag", "Failed to get asset file list.", e);
        }

        InputStream in = null;
        OutputStream out = null;
        try {
            in = assetManager.open("zone.txt");
            File outFile = new File(rootPath, "/.zone.txt");
            out = new FileOutputStream(outFile);
            copyFile(in, out);
        } catch (IOException e) {
            Log.e("tag", "Failed to copy asset file: " + "/.zone.txt", e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    // NOOP
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    // NOOP
                }
            }
        }

    }

    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }


    @Override
    public void onBackStackChanged() {
        showUpNavigation();
    }


    @Override
    public void launchChangePasswordFragment(String emailAddress) {

        ChangePasswordFragment changePasswordFragment = new ChangePasswordFragment();
        FragmentTransaction transaction = manager.beginTransaction();
        Bundle arguments = new Bundle();
        arguments.putString("email", emailAddress);
        changePasswordFragment.setArguments(arguments);
        transaction.replace(R.id.fragment_container, changePasswordFragment, "changepassword");
        transaction.addToBackStack("changepassword");
        transaction.commit();


    }
    @Override
    public void launchPropertyTaxScreen() {
        PropertyTaxGet propertyTaxScreen = new PropertyTaxGet();
        android.support.v4.app.FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fragment_container, propertyTaxScreen, "PropertyTaxScreen");
        transaction.addToBackStack("PropertyTaxScreen");

        transaction.commit();
    }
    @Override
    public void cleareBackStack() {
        
    }
}