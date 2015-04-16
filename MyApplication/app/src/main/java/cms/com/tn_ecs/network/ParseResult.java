package cms.com.tn_ecs.network;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import cms.com.tn_ecs.controller.Controller;
import cms.com.tn_ecs.objectholders.Certificate;
import cms.com.tn_ecs.objectholders.CertificateList;
import cms.com.tn_ecs.objectholders.HalfYear;
import cms.com.tn_ecs.objectholders.InstallmentInfo;
import cms.com.tn_ecs.objectholders.PropertyTaxArrears;
import cms.com.tn_ecs.objectholders.ReceiptDetails;
import cms.com.tn_ecs.objectholders.ZoneInfo;
import cms.com.tn_ecs.utils.SERVICE_TYPE;
import cms.com.tn_ecs.utils.URLConstants;

/**
 * Created by vishal_mokal on 4/2/15.
 */
public class ParseResult {

    String resultString;
    InputStream inputStream;
    DocumentBuilderFactory factory;
    DocumentBuilder documentBuilder;
    Controller controller;
    SERVICE_TYPE serviceType;

    Certificate certificate_object = null;
    ArrayList<Certificate> certificate_object_list = null;

    public ParseResult() {
        controller = Controller.getControllerInstance();
    }

    public ParseResult(String resultString) {
        this.resultString = resultString.trim().replace("&", "&amp;");
        Log.d("resylt", resultString);
        inputStream = new ByteArrayInputStream(this.resultString.getBytes());
        certificate_object_list = new ArrayList<Certificate>();
        controller = Controller.getControllerInstance();
    }

    public boolean parseBirthCertificate() {

        try {
            factory = DocumentBuilderFactory.newInstance();
            documentBuilder = factory.newDocumentBuilder();
            Document document = documentBuilder.parse(inputStream);
            Element rootElemtnt = document.getDocumentElement();

            String rootElementName = rootElemtnt.getTagName();
            NodeList resultItemList = rootElemtnt.getElementsByTagName("BREC");
            NodeList resultItemElementList = null;
            Node resultItem = null;
            Node resultItemElement = null;
            Log.d("size", "" + resultItemList.getLength());

            for (int i = 0; i < resultItemList.getLength(); i++) {
                resultItem = resultItemList.item(i);
                resultItemElementList = resultItem.getChildNodes();
                this.certificate_object = new Certificate();
                for (int j = 0; j < resultItemElementList.getLength(); j++) {
                    resultItemElement = resultItemElementList.item(j);

                    if (resultItemElement.getNodeName().equalsIgnoreCase("REGNO")) {
                        this.certificate_object.setRegNo(resultItemElement.getTextContent());
                    }

                    if (resultItemElement.getNodeName().equalsIgnoreCase("CHILDNAME")) {
                        this.certificate_object.setName(resultItemElement.getTextContent());
                    }
                    if (resultItemElement.getNodeName().equalsIgnoreCase("SEX")) {
                        this.certificate_object.setSex(resultItemElement.getTextContent());
                    }
                    if (resultItemElement.getNodeName().equalsIgnoreCase("FATHERNAME")) {
                        this.certificate_object.setFatherName(resultItemElement.getTextContent());
                    }
                    if (resultItemElement.getNodeName().equalsIgnoreCase("MOTHERNAME")) {
                        this.certificate_object.setMotherName(resultItemElement.getTextContent());
                    }
                    if (resultItemElement.getNodeName().equalsIgnoreCase("DOB")) {
                        this.certificate_object.setDate(resultItemElement.getTextContent());
                    }
                    if (resultItemElement.getNodeName().equalsIgnoreCase("ENGLISHURL")) {
                        this.certificate_object.setEnglishUrl(resultItemElement.getTextContent());
                    }

                }
                certificate_object_list.add(this.certificate_object);
            }


            CertificateList certificateList = new CertificateList();
            certificateList.setCertificatelist(certificate_object_list);
            controller.setCertificateList(certificateList);
            return true;

        } catch (Exception e) {
            Log.d("Parsing Error", "ParsingError");
            return false;
        }
    }


    public boolean parseDeathCertificate() {

        try {
            factory = DocumentBuilderFactory.newInstance();
            documentBuilder = factory.newDocumentBuilder();
            Document document = documentBuilder.parse(inputStream);
            Element rootElemtnt = document.getDocumentElement();

            String rootElementName = rootElemtnt.getTagName();
            NodeList resultItemList = rootElemtnt.getElementsByTagName("DREC");
            NodeList resultItemElementList = null;
            Node resultItem = null;
            Node resultItemElement = null;
            Log.d("size", "" + resultItemList.getLength());

            for (int i = 0; i < resultItemList.getLength(); i++) {
                resultItem = resultItemList.item(i);
                resultItemElementList = resultItem.getChildNodes();
                this.certificate_object = new Certificate();
                for (int j = 0; j < resultItemElementList.getLength(); j++) {
                    resultItemElement = resultItemElementList.item(j);

                    if (resultItemElement.getNodeName().equalsIgnoreCase("REGNO")) {
                        this.certificate_object.setRegNo(resultItemElement.getTextContent());
                    } else if (resultItemElement.getNodeName().equalsIgnoreCase("DEATHPERSONNAME")) {
                        this.certificate_object.setName(resultItemElement.getTextContent());
                    } else if (resultItemElement.getNodeName().equalsIgnoreCase("SEX")) {
                        this.certificate_object.setSex(resultItemElement.getTextContent());
                    } else if (resultItemElement.getNodeName().equalsIgnoreCase("FATHERNAME")) {
                        this.certificate_object.setFatherName(resultItemElement.getTextContent());
                    } else if (resultItemElement.getNodeName().equalsIgnoreCase("MOTHERNAME")) {
                        this.certificate_object.setMotherName(resultItemElement.getTextContent());
                    } else if (resultItemElement.getNodeName().equalsIgnoreCase("DOD")) {
                        this.certificate_object.setDate(resultItemElement.getTextContent());
                    } else if (resultItemElement.getNodeName().equalsIgnoreCase("ENGLISHURL")) {
                        this.certificate_object.setEnglishUrl(resultItemElement.getTextContent());
                    }

                }
                certificate_object_list.add(this.certificate_object);
            }


            CertificateList certificateList = new CertificateList();
            certificateList.setCertificatelist(certificate_object_list);
            controller.setCertificateList(certificateList);
            return true;

        } catch (Exception e) {
            Log.d("Parsing Error", "ParsingError");
            return false;
        }
    }


    public boolean ParseZonalDetails() {
        boolean result = false;
        try {
            String jsonData = readFile(URLConstants.APPLICATION_BASE_PATH + "/.zone.txt");
            JSONObject zoneObject = new JSONObject(jsonData);
            JSONArray zondetails = (JSONArray) zoneObject.getJSONArray("ZoneDetails");
            ArrayList<ZoneInfo> zoneList = new ArrayList<ZoneInfo>();
            ZoneInfo zoneclassobject;
            for (int i = 0; i < zondetails.length(); i++) {
                zoneclassobject = new ZoneInfo();
                JSONObject zonedetail = zondetails.getJSONObject(i);
                zoneclassobject.setZoneID(zonedetail.getString("zoneid"));
                zoneclassobject.setZoneName(zonedetail.getString("zonename"));
                zoneclassobject.setSunDivisions(zonedetail.getString("subDivision"));
                zoneList.add(zoneclassobject);
            }
            controller.setZoneInfo(zoneList);
            return result = true;
        } catch (Exception e) {
            return result = false;
        }
    }


    public String readFile(String filepath) throws IOException {
        File f = new File(filepath);
        if (f.exists()) {
            FileInputStream in = new FileInputStream(f);
            int size = in.available();
            byte c[] = new byte[size];
            for (int i = 0; i < size; i++) {
                c[i] = (byte) in.read();
            }
            String filedata = new String(c, "utf-8");
            return filedata;
        } else {
            return null;
        }
    }

    public boolean parseGerArriesResult() {
        try {
            PropertyTaxArrears propertyTaxArrears = new PropertyTaxArrears();
            HalfYear halfYear;
            ArrayList<HalfYear> halfYearList = new ArrayList<HalfYear>();
            factory = DocumentBuilderFactory.newInstance();
            documentBuilder = factory.newDocumentBuilder();
            Document document = documentBuilder.parse(inputStream);
            Element rootElemtnt = document.getDocumentElement();
            HalfYear halfYearObject = null;
            String rootElementName = rootElemtnt.getTagName();
            Log.d("root element", rootElementName);
            NodeList resultelements = rootElemtnt.getChildNodes();
            int size = resultelements.getLength();
            for (int i = 0; i < size; i++) {
                if (resultelements.item(i).getNodeName().equalsIgnoreCase("name")) {
                    propertyTaxArrears.setName(resultelements.item(i).getTextContent());
                } else if (resultelements.item(i).getNodeName().equalsIgnoreCase("od")) {
                    propertyTaxArrears.setOd(resultelements.item(i).getTextContent());
                } else if (resultelements.item(i).getNodeName().equalsIgnoreCase("st")) {
                    propertyTaxArrears.setSt(resultelements.item(i).getTextContent());
                } else if (resultelements.item(i).getNodeName().equalsIgnoreCase("halfyear")) {
                    NodeList halfyearlist = resultelements.item(i).getChildNodes();
                    halfYearObject = new HalfYear();
                    for (int j = 0; j < halfyearlist.getLength(); j++) {

                        if (halfyearlist.item(j).getNodeName().equalsIgnoreCase("hy")) {
                            halfYearObject.setHy(halfyearlist.item(j).getTextContent());
                        } else if (halfyearlist.item(j).getNodeName().equalsIgnoreCase("demand")) {
                            halfYearObject.setDemand(halfyearlist.item(j).getTextContent());
                        } else if (halfyearlist.item(j).getNodeName().equalsIgnoreCase("coll")) {
                            halfYearObject.setColl(halfyearlist.item(j).getTextContent());
                        } else if (halfyearlist.item(j).getNodeName().equalsIgnoreCase("arrears")) {
                            halfYearObject.setArrears(halfyearlist.item(j).getTextContent());
                        }
                    }
                    halfYearList.add(halfYearObject);
                }


                Log.d("size", "" + halfYearList.size());

            }
            propertyTaxArrears.setHalfYear(halfYearList);
            controller.setPropertyTaxArrears(propertyTaxArrears);
            return true;
        } catch (Exception e) {
            Log.d("size", "Error");
            controller.setPropertyTaxArrears(null);
            return false;
        }
    }

    public boolean parseOldReceiptResult() {
        try {

            factory = DocumentBuilderFactory.newInstance();
            documentBuilder = factory.newDocumentBuilder();
            Document document = documentBuilder.parse(inputStream);
            Element rootElemtnt = document.getDocumentElement();
            NodeList resultelements = rootElemtnt.getChildNodes();
            int NodeListsize = resultelements.getLength();
            ArrayList<String> oldReceiptList = new ArrayList<String>();
            for (int i = 0; i < NodeListsize; i++) {
                Node resultItem = resultelements.item(i);
                if (resultItem.getNodeName().equalsIgnoreCase("BILLID")) {
                    oldReceiptList.add(resultelements.item(i).getTextContent());
                }
            }
            controller.setOldReceiptNumber(oldReceiptList);
            return true;
        } catch (Exception e) {
            Log.d("Error", "Error");
            return false;
        }

    }


    public String[] parseUserRegistrationResult(String result) {
        String[] results = new String[3];
        try {
            JSONObject userRegistrationResultObject = new JSONObject(result);
            String userRegistrationResultString = (String) userRegistrationResultObject.get("UserRegistrationResult");
            JSONObject userRegistrationResult = new JSONObject(userRegistrationResultString);
            results[0] = "" + userRegistrationResult.getInt("status");
            results[1] = (String) userRegistrationResult.get("usermessage");
            results[2] = (String) userRegistrationResult.get("username");
            return results;
        } catch (Exception e) {
            results = null;
            return results;
        }
    }

    public String[] parseUserLoginResult(String result) {
        String[] results = new String[3];
        try {
            JSONObject userRegistrationResultObject = new JSONObject(result);
            String userRegistrationResultString = (String) userRegistrationResultObject.get("UserVerficationResult");
            JSONObject userRegistrationResult = new JSONObject(userRegistrationResultString);
            results[0] = "" + userRegistrationResult.getInt("status");
            results[1] = (String) userRegistrationResult.get("usermessage");
            results[2] = (String) userRegistrationResult.get("username");
            return results;
        } catch (Exception e) {
            results = null;
            return results;
        }
    }


    public String[] parseChangePasswordResult(String result) {
        String[] results = new String[3];
        try {
            JSONObject userRegistrationResultObject = new JSONObject(result);
            String userRegistrationResultString = (String) userRegistrationResultObject.get("ChangePasswordResult");
            JSONObject userRegistrationResult = new JSONObject(userRegistrationResultString);
            results[0] = "" + userRegistrationResult.getInt("status");
            results[1] = (String) userRegistrationResult.get("usermessage");
            results[2] = (String) userRegistrationResult.get("username");
            return results;
        } catch (Exception e) {
            results = null;
            return results;
        }
    }

    public String[] parseForgotPasswordResult(String result) {
        String[] results = new String[3];
        try {
            JSONObject userRegistrationResultObject = new JSONObject(result);
            String userRegistrationResultString = (String) userRegistrationResultObject.get("ForgotPasswordResult");
            JSONObject userRegistrationResult = new JSONObject(userRegistrationResultString);
            results[0] = "" + userRegistrationResult.getInt("status");
            results[1] = (String) userRegistrationResult.get("usermessage");
            results[2] = (String) userRegistrationResult.get("username");
            return results;
        } catch (Exception e) {
            results = null;
            return results;
        }
    }

    public ReceiptDetails parsePropertyTaxReceiptDetails() {
        ReceiptDetails receiptDetails = new ReceiptDetails();
        try {
            ArrayList<InstallmentInfo>  installmentInfos = new ArrayList<InstallmentInfo>();
            factory = DocumentBuilderFactory.newInstance();
            documentBuilder = factory.newDocumentBuilder();
            Document document = documentBuilder.parse(inputStream);
            Element rootElemtnt = document.getDocumentElement();
            String rootElementName = rootElemtnt.getTagName();
            NodeList mainresultItemList = rootElemtnt.getElementsByTagName("ADJARR");
            for (int k = 0; k < mainresultItemList.getLength(); k++) {
                NodeList resultItemList = mainresultItemList.item(k).getChildNodes();
                int nodeCount = resultItemList.getLength();
                for (int i = 0; i < nodeCount; i++) {
                    Node receipt = resultItemList.item(i);
                    if (receipt.getNodeName().equalsIgnoreCase("RCPTNO")) {
                        receiptDetails.setReceptNo(receipt.getTextContent());
                    } else if (receipt.getNodeName().equalsIgnoreCase("OLDBILLNO")) {
                        receiptDetails.setOldBillNumber(receipt.getTextContent());
                    } else if (receipt.getNodeName().equalsIgnoreCase("NEWBILLNO")) {
                        receiptDetails.setNewBillNumber(receipt.getTextContent());
                    } else if (receipt.getNodeName().equalsIgnoreCase("ASNAME")) {
                        receiptDetails.setAsName(receipt.getTextContent());
                    } else if (receipt.getNodeName().equalsIgnoreCase("DOORNO")) {
                        receiptDetails.setDoorNo(receipt.getTextContent());
                    } else if (receipt.getNodeName().equalsIgnoreCase("STNAME")) {
                        receiptDetails.setStreetName(receipt.getTextContent());
                    } else if (receipt.getNodeName().equalsIgnoreCase("PAYMENTMODE")) {
                        receiptDetails.setPaymentMode(receipt.getTextContent());
                    } else if (receipt.getNodeName().equalsIgnoreCase("RCPTDT")) {
                        receiptDetails.setReceipt_date(receipt.getTextContent());
                    } else if (receipt.getNodeName().equalsIgnoreCase("RCPTAMT")) {
                        receiptDetails.setReceipt_payment(receipt.getTextContent());
                    } else if (receipt.getNodeName().equalsIgnoreCase("CHQDDNO")) {
                        receiptDetails.setCheque_number(receipt.getTextContent());
                    } else if (receipt.getNodeName().equalsIgnoreCase("CHQDDDT")) {
                        receiptDetails.setCheque_date(receipt.getTextContent());
                    } else if (receipt.getNodeName().equalsIgnoreCase("BANK")) {
                        receiptDetails.setBane_name(receipt.getTextContent());
                    } else if (receipt.getNodeName().equalsIgnoreCase("BRANCH")) {
                        receiptDetails.setBane_branch(receipt.getTextContent());
                    } else if (receipt.getNodeName().equalsIgnoreCase("HY")) {
                        NodeList installmentList = receipt.getChildNodes();
                        InstallmentInfo installmentInfo = new InstallmentInfo();
                        for (int j = 0; j < installmentList.getLength(); j++) {
                            Node installment = installmentList.item(j);
                            
                            if (installment.getNodeName().equals("HALFYR")) {
                                installmentInfo.setInstallment(installment.getTextContent());
                            } else if (installment.getNodeName().equals("ADJAMT")) {
                              installmentInfo.setAdjistment(installment.getTextContent());
                            }
                            
                        }
                        installmentInfos.add(installmentInfo);
                    } else if (receipt.getNodeName().equalsIgnoreCase("BAL")) {
                        receiptDetails.setBalance(receipt.getTextContent());
                    }
                }
            }
            receiptDetails.setInstallmentInfos(installmentInfos);
            return receiptDetails;

        } catch (Exception e) {
            receiptDetails = null;
            return receiptDetails;
        }

    }
}


