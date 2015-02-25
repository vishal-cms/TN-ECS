package cms.com.tn_ecs.interfaces;

import cms.com.tn_ecs.utils.SERVICE_TYPE;

/**
 * Created by vishal_mokal on 29/1/15.
 * This interfaces is implemented by ContainerActivity
 * method in this interface is overwrten in main activity
 * and used in fragements for inter fragemtn communication.
 */
public interface FragmentCommunicator {

    public void actionBarTitle(String title);

    public void launcgSplashScreen();

    public void launchSearchScreen();

    public void launchSelectServiceScreen();

    public void detachSplashScreen();

    public void launchSelectedService(SERVICE_TYPE service_type);

    //if dateformat = dmy onDateSet method will return date in DD/MM/YYYY format
    //if dateformat = mdy onDateSet method will return date in MM/DD/YYYY format
    public void launchDateDialog(String dateFormat);

    public void showDate();

    public void launchCertificateList();

    public void launchDispalyCertificateScreen();

    public void launchRegisterNewUserScreen();

    public void viewCertificate();

    public void launchPropertyTaxScreen();

    public void launchPropertyTaxShowScreen();

    public void launchMessageDialog(String dialogMessage , String dialogTitle);


}