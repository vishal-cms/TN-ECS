package cms.com.tn_ecs.fragments;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import cms.com.tn_ecs.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ErrorDialogFragment extends android.support.v4.app.DialogFragment {

    String errorMessage;

    public ErrorDialogFragment(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = createErrorDialog();
        return dialog;


    }

    private Dialog createErrorDialog() {
        Context context = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        View customeView = getActivity().getLayoutInflater().inflate(R.layout.fragment_error_dialog, null);

        builder.setView(customeView);
        TextView title = (TextView) customeView.findViewById(R.id.txt_title);
        TextView txtErrorMessage = (TextView) customeView.findViewById(R.id.txt_errorMessage);
        txtErrorMessage.setText(errorMessage);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dismiss();
            }
        });

        Dialog dialog = builder.create();

        return dialog;
    }

}