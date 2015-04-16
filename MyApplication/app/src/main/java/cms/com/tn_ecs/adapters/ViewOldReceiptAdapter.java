package cms.com.tn_ecs.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.File;
import java.util.ArrayList;

import cms.com.tn_ecs.R;
import cms.com.tn_ecs.controller.Controller;
import cms.com.tn_ecs.interfaces.FragmentCommunicator;
import cms.com.tn_ecs.network.Connection;
import cms.com.tn_ecs.network.ParseResult;
import cms.com.tn_ecs.objectholders.PropertyTaxSearchDetails;
import cms.com.tn_ecs.objectholders.ReceiptDetails;
import cms.com.tn_ecs.utils.URLConstants;
import cms.com.tn_ecs.utils.ViewFileGenerator;

/**
 * Created by vishal_mokal on 19/2/15.
 */
public class ViewOldReceiptAdapter extends BaseAdapter {

    ArrayList<String> oldreceipts;
    LayoutInflater inflater;
    Context context;
    ArrayList<NameValuePair> parameterlsit;
    Controller controller;
    FragmentCommunicator communicator;
    ProgressDialog progressdialog;

    public ViewOldReceiptAdapter(Context context, ArrayList<String> oldreceipts) {

        this.oldreceipts = oldreceipts;
        if (oldreceipts == null) {
            this.oldreceipts = new ArrayList<String>();
            this.oldreceipts.clear();
        }
        this.context = context;
        inflater = LayoutInflater.from(context);
        controller = Controller.getControllerInstance();
        communicator = (FragmentCommunicator) context;
    }

    @Override
    public int getCount() {
        return oldreceipts.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final viewHolder viewholder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.adapter_old_receipt_view, null);
            viewholder = new viewHolder();
            convertView.setTag(viewholder);
        } else {
            viewholder = (ViewOldReceiptAdapter.viewHolder) convertView.getTag();
        }

        viewholder.txtoldreceiptview = (TextView) convertView.findViewById(R.id.txt_Receipt_Number);
        viewholder.txtoldreceiptview.setText(oldreceipts.get(position));
        viewholder.txtoldreceiptview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.setRequestedUrl(URLConstants.PROPERTY_TAX_MASTER_URL);
                String propType = controller.getProprtyPropertyTaxSearchDetails().getPropertyType();
                String result = "";
                if(propType.equalsIgnoreCase("new"))
                {
                    result = getRequeastUrls(viewholder.txtoldreceiptview.getText().toString().trim());
                }
                else if(propType.equalsIgnoreCase("old"))
                {
                    result = getRequestUrlForOldPropertyTax(viewholder.txtoldreceiptview.getText().toString().trim());
                }
                
                Log.d("receiptUrl", result);

                String fileName = viewholder.txtoldreceiptview.getText().toString().trim().replace("/", "-");

                File file = new File(URLConstants.APPLICATION_BASE_PATH + "/PropertyTax/" + fileName + ".html");

                if (file.exists()) {
                    controller.setSelectedReceiptFilePath(file.getPath());
                    communicator.launchViewReceiptFragment();
                } else {
                    new GetReceiptData(result).execute();

                }


            }
        });

        return convertView;
    }


    class viewHolder {
        TextView txtoldreceiptview;
    }
    
    private String getRequeastUrls(String receiptNo) {

        PropertyTaxSearchDetails propertyTaxSearchDetails = controller.getProprtyPropertyTaxSearchDetails();
        parameterlsit = new ArrayList<NameValuePair>();
        parameterlsit.add(new BasicNameValuePair("channelID", propertyTaxSearchDetails.getChannelID()));
        parameterlsit.add(new BasicNameValuePair("ZONE", propertyTaxSearchDetails.getZONE()));
        parameterlsit.add(new BasicNameValuePair("DIV_CD", propertyTaxSearchDetails.getDIV_CD()));
        parameterlsit.add(new BasicNameValuePair("OLD_BILL", propertyTaxSearchDetails.getOLD_BILL()));
        parameterlsit.add(new BasicNameValuePair("OLD_SUB", propertyTaxSearchDetails.getOLD_SUB()));
        parameterlsit.add(new BasicNameValuePair("RCPT_NO", receiptNo));
        parameterlsit.add(new BasicNameValuePair("serviceId", "reprintRcpt"));
        String result = new Connection(context).getParametriseUrl(parameterlsit);
        controller.setReceiptUrl(result);
        return result;
    }
    
    private String getRequestUrlForOldPropertyTax(String receiptNo)
    {
        PropertyTaxSearchDetails propertyTaxSearchDetails = controller.getProprtyPropertyTaxSearchDetails();
        controller.setRequestedUrl(URLConstants.OLD_PROPERTY_TAX_MASTER_URL);
        parameterlsit = new ArrayList<NameValuePair>();
        parameterlsit.add(new BasicNameValuePair("channelID", propertyTaxSearchDetails.getChannelID()));
        parameterlsit.add(new BasicNameValuePair("OLD_PROP_ID", propertyTaxSearchDetails.getOLD_BILL()));
        parameterlsit.add(new BasicNameValuePair("RCPT_NO", receiptNo));
        parameterlsit.add(new BasicNameValuePair("serviceId", "reprintRcpt"));
        String result = new Connection(context).getParametriseUrl(parameterlsit);
        return result;
    }
    private class GetReceiptData extends AsyncTask<String, Void, String> {
        String requestUrl;
        String result;
        boolean isReceiptFileGenerated;

        private GetReceiptData(String requestUrl) {
            this.requestUrl = requestUrl;
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                result = new Connection(context).getResult(requestUrl);
                Log.d("resylt" , result);
                Log.d("receiptData", result);
                if (result.contentEquals("ERROR")) {
                    communicator.launchMessageDialog("Error", "Sorry ! No Record Found This might Because Low Internet Connectivity Please Try After Some Time.");
                } else {
                  
                    ReceiptDetails receiptDetails = new ParseResult(result).parsePropertyTaxReceiptDetails();
                    controller.setReceiptDetails(receiptDetails);
                    isReceiptFileGenerated = new ViewFileGenerator().generatePropertyTaxReceiptHTML();
                }

            } catch (Exception e) {
                result = e.toString();
            }


            return result;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressdialog = new ProgressDialog(context);
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

            if (!isReceiptFileGenerated) {
                Toast.makeText(context, "Sorry! Problem Generating Receipt Please Try After Some Time.", Toast.LENGTH_LONG).show();
            } else {
                communicator.launchViewReceiptFragment();
            }


        }
    }

}
