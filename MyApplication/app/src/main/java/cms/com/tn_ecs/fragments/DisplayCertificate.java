package cms.com.tn_ecs.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;

import org.apache.http.NameValuePair;

import java.util.ArrayList;

import cms.com.tn_ecs.R;
import cms.com.tn_ecs.controller.Controller;
import cms.com.tn_ecs.objectholders.Certificate;


public class DisplayCertificate extends android.support.v4.app.Fragment {

    WebView wv_certificateViewer;
    Button btn_download;
    Controller controller;
    Certificate certificate;
    String certificatePDFUrl;
    ProgressDialog progressdialog;
    String googleDocs;
    ArrayList<NameValuePair> params;

    public DisplayCertificate() {
        controller = Controller.getControllerInstance();
        certificate = controller.getSelectedCertificate();
        params = new ArrayList<NameValuePair>();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_display_certificate, container, false);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        wv_certificateViewer = (WebView) getActivity().findViewById(R.id.wv_certificateViewer);
        btn_download = (Button) getActivity().findViewById(R.id.btn_download);
        WebSettings webSetting = wv_certificateViewer.getSettings();
        webSetting.setJavaScriptEnabled(true);
        webSetting.setDisplayZoomControls(true);
        webSetting.setSupportZoom(true);
        webSetting.setDomStorageEnabled(true);


    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }


}
