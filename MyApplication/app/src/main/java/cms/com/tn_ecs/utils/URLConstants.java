package cms.com.tn_ecs.utils;

import android.os.Environment;

/**
 * Created by vishal_mokal on 29/1/15.
 * this class contain all url  related data
 */
public class URLConstants {

    public static final String BIRTH_CERTIFICATE_URL = "http://218.248.24.70:8080/callcenter/BirthCertificate_xml.jsp?";
    public static final String DEATH_CERTIFICATE_URL = "http://218.248.24.70:8080/callcenter/DeathCertificate_xml.jsp?";
    public static final String BIRTH_CERTIFICATE_PDF_URL = "http://218.248.24.70:8080/CORPBIRTH/online-civic-services/birthCertificate.do?";
    public static final String DEATH_CERTIFICATE_PDF_URL = " http://218.248.24.70:8080/CORPBIRTH/online-civic-services/deathCertificate.do?";
    public static final String PUBLIC_URL = "http://218.248.24.70:8080/callcenter/";
    public static final String VIEW_PDF_URL = "http://docs.google.com/gview?embedded=true&url=";
    public static final String APPLICATION_BASE_PATH = Environment.getExternalStorageDirectory() + "/TN_ECS/";
    public static final String PROPERTY_TAX_MASTER_URL = "http://164.100.134.92:8280/ptis/external/collectionservice?";   
    public static final String USER_REGISTRATION_URL = "http://182.74.160.158/MobileRegistration/RegistrationDetails/UserRegistration?";


}