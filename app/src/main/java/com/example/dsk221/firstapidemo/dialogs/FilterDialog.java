package com.example.dsk221.firstapidemo.dialogs;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;

import com.example.dsk221.firstapidemo.R;
import com.example.dsk221.firstapidemo.utility.Constants;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class FilterDialog extends DialogFragment {
    private static final String TAG = "";
    private Button btnFromDate, btnToDate, btnPositive, btnNegative;
    private Calendar calToDate, calFromDate;
    private Spinner spinnerOrder, spinnerSort;
    private String selectedOrderData, selectedSortData;
    private String selectedToDate = "";
    private String selectedFromDate = "";
    private String[] orderArray, sortArray;

    public static final String ARG_ORDER="order";
    public static final String ARG_SORT="sort";
    public static final String ARG_TODATE="todate";
    public  static final String ARG_FROMDATE="fromdate";

    private OnResult callbackOnResult;

    public interface OnResult {
        void sendData(String orderData, String sortData, String todateData, String fromdateData);
    }


    public static FilterDialog newInstance(String order, String sort, String todate,
                                           String fromdate) {
        FilterDialog filterDialogFragment = new FilterDialog();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_ORDER, order);
        bundle.putString(ARG_SORT, sort);
        bundle.putString(ARG_TODATE, todate);
        bundle.putString(ARG_FROMDATE, fromdate);
        filterDialogFragment.setArguments(bundle);
        return filterDialogFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        callbackOnResult = (OnResult) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.filter_customdialog_layout, container,
                false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        Bundle bundle = getArguments();
        String order = bundle.getString(ARG_ORDER);
        String sort = bundle.getString(ARG_SORT);
        String todate = bundle.getString(ARG_TODATE);
        String fromdate = bundle.getString(ARG_FROMDATE);

        selectedOrderData = order;
        selectedSortData = sort;
        selectedFromDate = fromdate;
        selectedToDate = todate;

        DateFormat formatter = new SimpleDateFormat(Constants.DATE_FORMATE);
        if (selectedFromDate == null) {
            calFromDate = Calendar.getInstance();
        } else {
            try {
                Date date = formatter.parse(fromdate);
                calToDate = Calendar.getInstance();
                calToDate.setTime(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        if (selectedToDate == null) {
            calToDate = Calendar.getInstance();
        } else {
            try {
                Date date1 = formatter.parse(fromdate);
                calFromDate = Calendar.getInstance();
                calFromDate.setTime(date1);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        btnFromDate = (Button) rootView.findViewById(R.id.btn_from_date);
        btnToDate = (Button) rootView.findViewById(R.id.btn_to_date);
        btnPositive = (Button) rootView.findViewById(R.id.btn_positive);
        btnNegative = (Button) rootView.findViewById(R.id.btn_negative);

        spinnerOrder = (Spinner) rootView.findViewById(R.id.spinner_order);
        spinnerSort = (Spinner) rootView.findViewById(R.id.spinner_sort);

        orderArray = getResources().getStringArray(R.array.spinnerOrder);
        sortArray = getResources().getStringArray(R.array.spinnerSort);

        btnFromDate.setText(fromdate);
        btnToDate.setText(todate);
        spinnerOrder.setSelection(((ArrayAdapter<String>) spinnerOrder.getAdapter()).getPosition(order));
        spinnerSort.setSelection(((ArrayAdapter<String>) spinnerSort.getAdapter()).getPosition(sort));

        spinnerSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedSortData = sortArray[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spinnerOrder.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedOrderData = orderArray[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btnFromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker(fromdateListener, calFromDate, null);
            }
        });
        btnToDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker(todateListener, calToDate, calFromDate);
            }
        });

        btnPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                long toDateMiliSec = selectedToDate == null ? 0 : ((calToDate.getTime().getTime())/1000);
//                long fromDateMiliSec = selectedFromDate == null ? 0 :  ((calToDate.getTime().getTime())/1000);
//
                callbackOnResult.sendData(
                        selectedOrderData,
                        selectedSortData,
                        selectedToDate,
                        selectedFromDate);

                getDialog().cancel();
            }
        });

        btnNegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().cancel();
            }
        });


        calFromDate = Calendar.getInstance();
        return rootView;
    }

    private void showDatePicker(DatePickerDialog.OnDateSetListener listener,
                                Calendar calendar,
                                Calendar minDateCalender) {
        DatePickerDialog dialog = new DatePickerDialog(getActivity(),
                listener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        if (minDateCalender != null) {
            dialog.getDatePicker().setMinDate(minDateCalender.getTimeInMillis());
        }
        dialog.show();
    }

    public DatePickerDialog.OnDateSetListener fromdateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            calFromDate.set(year, monthOfYear, dayOfMonth);
            SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.DATE_FORMATE,
                    Locale.getDefault());
            selectedFromDate = dateFormat.format(calFromDate.getTime());
            btnFromDate.setText(selectedFromDate);
        }
    };

    DatePickerDialog.OnDateSetListener todateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            calToDate.set(year, monthOfYear, dayOfMonth);
            SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.DATE_FORMATE,
                    Locale.getDefault());
            selectedToDate = dateFormat.format(calToDate.getTime());
            btnToDate.setText(selectedToDate);
//            calToDate.set(year, monthOfYear, dayOfMonth);
//            if (calToDate.getTimeInMillis() > calFromDate.getTimeInMillis()) {
//                String selectedDate = String.valueOf(year)
//                        + "-" + String.valueOf(monthOfYear + 1)
//                        + "-" + String.valueOf(dayOfMonth);
//                btnToDate.setText(selectedDate);
//            } else {
//                Toast.makeText(getActivity(),R.string.text_to_date_error, Toast.LENGTH_SHORT).show();
//            }
        }
    };
//    public long milliseconds(String date)
//    {
//        //String date_ = date;
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        try
//        {
//            Date mDate = sdf.parse(date);
//            long timeInMilliseconds = mDate.getTime();
//            return timeInMilliseconds;
//        }
//        catch (ParseException e)
//        {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//
//        return 0;
//    }
}
