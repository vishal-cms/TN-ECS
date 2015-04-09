package cms.com.tn_ecs.fragments;


import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import cms.com.tn_ecs.R;
import cms.com.tn_ecs.adapters.CertificateListAdapter;
import cms.com.tn_ecs.controller.Controller;
import cms.com.tn_ecs.interfaces.FragmentCommunicator;
import cms.com.tn_ecs.network.Connection;
import cms.com.tn_ecs.objectholders.Certificate;
import cms.com.tn_ecs.utils.SERVICE_TYPE;
import cms.com.tn_ecs.utils.URLConstants;

/**
 * A simple {@link Fragment} subclass.
 */
public class CertificateList extends android.support.v4.app.Fragment {

    Controller controller;
    ListView certificateListView;
    RelativeLayout empltyLayout;
    FragmentCommunicator communicator;
    ArrayList<Certificate> searchedCertificateList;
    TextWatcher MyTextWatchwe = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence searchText, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            ArrayList<Certificate> sortedList = new ArrayList<Certificate>();
            ArrayList<Certificate> actualList = new ArrayList<Certificate>();
            actualList = searchedCertificateList;
            Set<Certificate> sortedQuestionSet = new HashSet<Certificate>();
            String searchQuery = s.toString().toUpperCase();
            if (searchQuery.length() > 0) {
                for (int i = 0; i < actualList.size(); i++) {
                    Certificate certficate = actualList.get(i);
                    if (certficate.getName().toString().contains(searchQuery) || certficate.getFatherName().toString().contains(searchQuery) || certficate.getMotherName().toString().contains(searchQuery))

                        sortedList.add(actualList.get(i));
                }
                CertificateListAdapter certificateAdapter = new CertificateListAdapter(getActivity(), sortedList);
                certificateListView.setAdapter(certificateAdapter);
                certificateListView.setEmptyView(empltyLayout);
                txtListSiz.setText("Showing " + sortedList.size() + " of " + searchedCertificateList.size());
            } else {
                CertificateListAdapter certificateAdapter = new CertificateListAdapter(getActivity(), actualList);
                certificateListView.setAdapter(certificateAdapter);
                certificateListView.setEmptyView(empltyLayout);
                txtListSiz.setText("" + searchedCertificateList.size() + " Record Found.");
            }


        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
    SERVICE_TYPE service_type;
    TextView txtListSiz;
    EditText txtsearchBox;
    ArrayList<NameValuePair> params;
    ProgressDialog progressdialog;


    public CertificateList() {

        searchedCertificateList = new ArrayList<Certificate>();

        if (controller == null) {
            controller = Controller.getControllerInstance();
        }

        params = new ArrayList<NameValuePair>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_certificate_list, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        communicator = (FragmentCommunicator) getActivity();
        service_type = controller.getSelectedService();
        if (service_type == SERVICE_TYPE.BIRTH_CERTIFICATE) {
            communicator.actionBarTitle("Birth Certificate Search Result");
        } else if (service_type == SERVICE_TYPE.DEATH_CERTIFICATE) {
            communicator.actionBarTitle("Death Certificate Search Result");
        }
        searchedCertificateList = controller.getCertificateList().getCertificatelist();
        certificateListView = (ListView) getActivity().findViewById(R.id.certificateList);
        certificateListView.setDivider(null);
        empltyLayout = (RelativeLayout) getActivity().findViewById(R.id.panel_emptyView);
        CertificateListAdapter certificateAdapter = new CertificateListAdapter(getActivity(), searchedCertificateList);
        certificateListView.setAdapter(certificateAdapter);
        certificateListView.setEmptyView(empltyLayout);
        txtListSiz = (TextView) getActivity().findViewById(R.id.txtlistSize);
        txtListSiz.setText("" + searchedCertificateList.size() + " Record Found.");
        txtsearchBox = (EditText) getActivity().findViewById(R.id.txtSearchBox);
        txtsearchBox.addTextChangedListener(MyTextWatchwe);
    }

    private String generateDownloadUrl() {
        String requestedDownloadUrl = "";
        params.clear();
        Certificate certificate = controller.getSelectedCertificate();
        SERVICE_TYPE service_type = controller.getSelectedService();
        if (service_type == SERVICE_TYPE.BIRTH_CERTIFICATE) {
            params.add(new BasicNameValuePair("do", "BirthCertificate"));
            params.add(new BasicNameValuePair("registrationNumber", certificate.getRegNo()));
        } else if (service_type == SERVICE_TYPE.DEATH_CERTIFICATE) {
            params.add(new BasicNameValuePair("do", "DeathCertificate"));
            params.add(new BasicNameValuePair("registrationNumber", certificate.getRegNo()));
        }
        requestedDownloadUrl = new Connection(getActivity()).getParametriseUrlForDownload(params);
        return requestedDownloadUrl;
    }


    public void downloadAndViewCertificate() {
        new downloadCertificate().execute();
    }


    public class downloadCertificate extends AsyncTask<String, Void, String> {
        String url = null;
        boolean result;

        public downloadCertificate() {

            this.url = generateDownloadUrl();
            Log.d("DownloadUrl", url);
        }

        @Override
        protected String doInBackground(String... params) {


            Connection connection = new Connection(getActivity());
            result = connection.downloadCertificate(url);

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
                progressdialog.hide();
            }
            String certificateDirectory = "";
            if (controller.getSelectedService() == SERVICE_TYPE.BIRTH_CERTIFICATE) {
                certificateDirectory = URLConstants.APPLICATION_BASE_PATH + "BirthCertificate/";
            } else if (controller.getSelectedService() == SERVICE_TYPE.DEATH_CERTIFICATE) {
                certificateDirectory = URLConstants.APPLICATION_BASE_PATH + "DeathCertificate/";
            }
            String fileName = certificateDirectory + controller.getSelectedCertificate().getName().toString().trim() + "_" + controller.getSelectedCertificate().getRegNo().toString().trim().replace("/", "-") + ".pdf";
            File pdffile = new File(fileName);

            if (pdffile.exists()) {

                try {
                    final Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.fromFile(pdffile), "application/pdf");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    Intent targate = Intent.createChooser(intent, "Open File");

                    startActivity(targate);
                } catch (Exception e) {
                    Toast.makeText(getActivity(), "No PDF viewer available . File downloaded at" + URLConstants.APPLICATION_BASE_PATH, Toast.LENGTH_LONG).show();
                }
            }

        }
    }


}
