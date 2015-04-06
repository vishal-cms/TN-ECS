package cms.com.tn_ecs.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import cms.com.tn_ecs.controller.Controller;
import cms.com.tn_ecs.interfaces.FragmentCommunicator;
import cms.com.tn_ecs.utils.SERVICE_TYPE;
import cms.com.tn_ecs.utils.URLConstants;

/**
 * Created by vishal_mokal on 29/1/15.
 * This class is responsible for all network related for operatoions
 * calling web services and downloding resources.
 */
public class Connection {

    Context context;
    String requestUrlForDownload = "";
    String requestUrl = "";
    SERVICE_TYPE serviceType;
    Controller controller;
    FragmentCommunicator communicator;

    public Connection(Context context) {
        this.context = context;
        communicator = (FragmentCommunicator) context;
        controller = Controller.getControllerInstance();
        requestUrl = controller.getRequestedUrl();
        requestUrlForDownload = controller.getRequestedDownloadUrl();
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }

    public SERVICE_TYPE getServiceType() {
        return serviceType;
    }

    public void setServiceType(SERVICE_TYPE serviceType) {
        this.serviceType = serviceType;


    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public boolean isHostAvailable(boolean isNetWorkAvailable) {
        if (isNetWorkAvailable) {
            try {
                URL url = new URL(URLConstants.PUBLIC_URL);
                HttpURLConnection urlconnection = (HttpURLConnection) url.openConnection();
                urlconnection.setConnectTimeout(3000);
                urlconnection.connect();
                if (urlconnection.getResponseCode() == 200) {
                    return true;
                } else {
                    return false;
                }
            } catch (Exception e) {
                return false;
            }
        } else {
            return false;
        }

    }


    public String getParametriseUrl(ArrayList<NameValuePair> parameters) {
        try {
            ArrayList<NameValuePair> parameterList = parameters;
            String paramString = URLEncodedUtils.format(parameterList, "utf-8");
            requestUrl = requestUrl + paramString;
            return requestUrl;
        } catch (Exception e) {
            return "Some parameter are missing.";
        }
    }


    public String getResult(String requestdUrl) {
        InputStream inputStream = null;
        String result = null;
        try {

            HttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, 45000);
            HttpConnectionParams.setSoTimeout(httpParams, 45000);
            HttpClient httpClient = new DefaultHttpClient(httpParams);
            HttpResponse httpResponse = httpClient.execute(new HttpGet(requestdUrl));
            inputStream = httpResponse.getEntity().getContent();
            if (inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "False";
            return result;
        } catch (Exception e) {

            return "False";
        }


    }

    public boolean downloadCertificate(String url) {
        try {
            URL downloadCertificateUrl = new URL(url);
            HttpURLConnection httpConnection = (HttpURLConnection) downloadCertificateUrl.openConnection();
            httpConnection.setRequestMethod("GET");
            httpConnection.setDoOutput(true);
            String certificateDirectory = "";
            
            String fileName = "";
            if(controller.getSelectedService() == SERVICE_TYPE.BIRTH_CERTIFICATE)
            {
                certificateDirectory = URLConstants.APPLICATION_BASE_PATH + "BirthCertificate/";
            }
            else if(controller.getSelectedService() == SERVICE_TYPE.DEATH_CERTIFICATE)
            {
                certificateDirectory = URLConstants.APPLICATION_BASE_PATH + "DeathCertificate/";
            }
            fileName = controller.getSelectedCertificate().getName().toString().trim() + "_" + controller.getSelectedCertificate().getRegNo().toString().trim().replace("/" ,"-") + ".pdf";
            
            File certificate_dir = new File(certificateDirectory);
            if (!certificate_dir.exists())
            {
                certificate_dir.mkdir();
            }
            
            File file = new File(certificateDirectory, fileName);
            if(!file.exists()) {

                FileOutputStream fileOutput = new FileOutputStream(file);
                InputStream inputStream = httpConnection.getInputStream();
                int totalSize = httpConnection.getContentLength();
                byte[] buffer = new byte[1024];
                int bufferLength = 0;
                while ((bufferLength = inputStream.read(buffer)) > 0) {
                    fileOutput.write(buffer, 0, bufferLength);
                    totalSize += bufferLength;
                }
                fileOutput.close();
                return true;
            }
            else
            {
                return true;
            }


            
        } catch (Exception e) {
            return false;
        }
    }

    public String getParametriseUrlForDownload(ArrayList<NameValuePair> parameters) {
        try {
            ArrayList<NameValuePair> parameterList = parameters;
            String paramString = URLEncodedUtils.format(parameterList, "utf-8");
            requestUrl = requestUrlForDownload + paramString;
            return requestUrl;
        } catch (Exception e) {
            return "Some parameter are missing.";
        }
    }


}





