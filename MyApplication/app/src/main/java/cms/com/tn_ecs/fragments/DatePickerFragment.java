package cms.com.tn_ecs.fragments;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.DatePicker;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import cms.com.tn_ecs.controller.Controller;
import cms.com.tn_ecs.interfaces.FragmentCommunicator;

/**
 * A simple {@link android.app.Fragment} subclass.
 */

public class DatePickerFragment extends android.support.v4.app.DialogFragment implements DatePickerDialog.OnDateSetListener {

    FragmentCommunicator communicator;
    Controller controller;
    String dateFormat;

    
    //if dateformat = dmy onDateSet method will return date in DD/MM/YYYY format
    //if dateformat = mdy onDateSet method will return date in MM/DD/YYYY format

    public DatePickerFragment() {
    }

    public DatePickerFragment(String dateFormat) {
        controller = Controller.getControllerInstance();
        this.dateFormat = dateFormat;

    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        communicator = (FragmentCommunicator) getActivity();
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        String month;
        String day = "" + dayOfMonth;
        if ((monthOfYear + 1) < 10) {
            month = "0" + (monthOfYear + 1);
        } else {
            month = "" + (monthOfYear + 1);
        }

        if (dayOfMonth < 10) {
            day = "0" + dayOfMonth;
        }
        String date = null;
        
        if(dateFormat.equals("dmy")) {
            date = "" + day.trim() + "/" + month.trim() + "/" + year;
            
            
        }
        else if(dateFormat.equals("mdy")) {
            date = "" +month.trim() + "/" +  day.trim() + "/" + year;
        }
        
        
        if(validateDate(date,dateFormat)) {
            controller.setSelectedDate(date);
        }
        else
        {
            controller.setSelectedDate("false");
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d("Detach", "Detached");
        communicator.showDate();

    }
    
    
    private boolean validateDate(String date , String format)
    {
        
        DateFormat dateformat = null;
        try {
            if (format.equals("mdy")) {
                dateformat = new SimpleDateFormat("MM/dd/yyyy");
            } else if (format.equals("dmy")) {

                dateformat = new SimpleDateFormat("dd/MM/yyyy");
            }

            Date selectedDate = dateformat.parse(date);
            
            Date currentDate = new Date();
            
            if(selectedDate.after(currentDate))
            {
                return false;
            }
            else
            {
                return true;
            }
            

        }
        catch (Exception e)
        {
            Log.d("Date Exception" , e.toString());
            return false;
        }
        
   
        
    }
}
