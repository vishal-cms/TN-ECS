package cms.com.tn_ecs.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by vishal_mokal on 3/2/15.
 */
public class GeneralUtilities {

    Context context;

    public GeneralUtilities() {
    }

    public GeneralUtilities(Context context) {
        this.context = context;
    }

    public static boolean validateEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.^_]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }

    public static boolean validateUserName(String name) {
        String ePattern = "[a-zA-Z.]+";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(name);
        return m.matches();
    }


    public void writeDataToSharedPreferences(String emailAddress, String appUserName) {
        try {
            SharedPreferences sharedPref = context.getSharedPreferences("tnecs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("FullName", appUserName);
            editor.putString("EmailAddress", emailAddress);
            editor.commit();
        } catch (Exception e) {
            Log.d("error", e.getLocalizedMessage());
        }
    }

    public String readDataFromSharedPreferences() {

        SharedPreferences sharedPref = context.getSharedPreferences("tnecs", Context.MODE_PRIVATE);

        String email = sharedPref.getString("EmailAddress", null);

        return email;
    }


    public boolean validatePhoneNumber(String phonenumber) {
        boolean isValidPhoneNumber;

        if (phonenumber.length() == 10 || phonenumber.length() == 12) {
            if (!phonenumber.contains("#") || !phonenumber.contains("*")) {
                if (phonenumber.startsWith("91") || phonenumber.startsWith("0") || phonenumber.startsWith("7") || phonenumber.startsWith("8") || phonenumber.startsWith("9")) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }

        } else {
            return false;
        }
    }


}
