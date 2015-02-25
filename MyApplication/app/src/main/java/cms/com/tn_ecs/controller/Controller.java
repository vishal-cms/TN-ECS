package cms.com.tn_ecs.controller;

import java.util.ArrayList;

import cms.com.tn_ecs.objectholders.ApplicationUser;
import cms.com.tn_ecs.objectholders.Certificate;
import cms.com.tn_ecs.objectholders.CertificateList;
import cms.com.tn_ecs.objectholders.PropertyTaxArrears;
import cms.com.tn_ecs.objectholders.ZoneInfo;
import cms.com.tn_ecs.utils.SERVICE_TYPE;

/**
 * Created by vishal_mokal on 29/1/15.
 * this is singolton class only one object is created of this class in one application instance.
 * this class hold all necessary objects required for the application.
 */
public class Controller {

    private static Controller controller = new Controller();
    private ApplicationUser appuser;
    private String requestedUrl;
    private String requestedDownloadUrl;
    private SERVICE_TYPE selectedService;
    private String selectedDate;
    private Certificate selectedCertificate;
    private CertificateList certificateList;
    private String applicationRootPath;
    private ArrayList<ZoneInfo> zoneInfo;
    private ArrayList<String> oldReceiptNumber;


    private PropertyTaxArrears propertyTaxArrears;


    public Controller() {
    }

    public static Controller getControllerInstance() {
        if (controller == null) {
            controller = new Controller();
        }
        return controller;
    }

    public String getApplicationRootPath() {
        return applicationRootPath;
    }

    public void setApplicationRootPath(String applicationRootPath) {
        this.applicationRootPath = applicationRootPath;
    }

    public ApplicationUser getAppuser() {
        return appuser;
    }

    public void setAppuser(ApplicationUser appuser) {
        this.appuser = appuser;
    }

    public String getRequestedUrl() {
        return requestedUrl;
    }

    public void setRequestedUrl(String requestedUrl) {
        this.requestedUrl = requestedUrl;
    }

    public String getRequestedDownloadUrl() {
        return requestedDownloadUrl;
    }

    public void setRequestedDownloadUrl(String requestedDownloadUrl) {
        this.requestedDownloadUrl = requestedDownloadUrl;
    }

    public SERVICE_TYPE getSelectedService() {
        return selectedService;
    }

    public void setSelectedService(SERVICE_TYPE selectedService) {
        this.selectedService = selectedService;
    }

    public String getSelectedDate() {
        return selectedDate;
    }

    public void setSelectedDate(String selectedDate) {
        this.selectedDate = selectedDate;


    }

    public CertificateList getCertificateList() {
        return certificateList;
    }

    public void setCertificateList(CertificateList certificateList) {
        this.certificateList = certificateList;
    }

    public Certificate getSelectedCertificate() {
        return selectedCertificate;
    }

    public void setSelectedCertificate(Certificate selectedCertificate) {
        this.selectedCertificate = selectedCertificate;
    }

    public ArrayList<ZoneInfo> getZoneInfo() {
        return zoneInfo;
    }

    public void setZoneInfo(ArrayList<ZoneInfo> zoneInfo) {
        this.zoneInfo = zoneInfo;
    }


    public PropertyTaxArrears getPropertyTaxArrears() {
        return propertyTaxArrears;
    }

    public void setPropertyTaxArrears(PropertyTaxArrears propertyTaxArrears) {
        this.propertyTaxArrears = propertyTaxArrears;
    }

    public ArrayList<String> getOldReceiptNumber() {
        return oldReceiptNumber;
    }

    public void setOldReceiptNumber(ArrayList<String> oldReceiptNumber) {
        this.oldReceiptNumber = oldReceiptNumber;
    }
}