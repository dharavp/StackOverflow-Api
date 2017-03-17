package com.example.dsk221.firstapidemo.dialogs;

import android.app.DatePickerDialog;
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
import com.example.dsk221.firstapidemo.utility.Utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class FilterDialog extends DialogFragment {
    private static final String TAG = "";
    private Button btnFromDate, btnToDate, btnPositive, btnNegative, btnReset;
    private Calendar calToDate, calFromDate;
    private Spinner spinnerOrder, spinnerSort;
    private String selectedOrderData, selectedSortData;
    private String selectedToDate = "";
    private String selectedFromDate = "";
    public String selectedDrawerName = "";
    private String[] orderUserArray, sortUserArray;

    public static final String ARG_ORDER = "order";
    public static final String ARG_SORT = "sort";
    public static final String ARG_TODATE = "todate";
    public static final String ARG_FROMDATE = "fromdate";
    public static final String ARG_DRAWERNAME = "drawername";

    private OnResult callbackOnResult;

    public interface OnResult {
        void sendData(String orderData, String sortData, String todateData, String fromdateData);
    }

    public static FilterDialog newInstance(String drawerName, String order, String sort, String todate,
                                           String fromdate) {
        FilterDialog filterDialogFragment = new FilterDialog();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_DRAWERNAME, drawerName);
        bundle.putString(ARG_ORDER, order);
        bundle.putString(ARG_SORT, sort);
        bundle.putString(ARG_TODATE, todate);
        bundle.putString(ARG_FROMDATE, fromdate);
        filterDialogFragment.setArguments(bundle);
        return filterDialogFragment;
    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        try {
//            callbackOnResult = (OnResult) getParentFragment();
//        } catch (ClassCastException e) {
//            e.printStackTrace();
//        }
//    }

    public void setCallbackOnResult(OnResult callbackOnResult) {
        this.callbackOnResult = callbackOnResult;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.dialog_filter, container,
                false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        Bundle bundle = getArguments();

        selectedOrderData = bundle.getString(ARG_ORDER);
        selectedSortData = bundle.getString(ARG_SORT);
        selectedFromDate = bundle.getString(ARG_FROMDATE);
        selectedToDate = bundle.getString(ARG_TODATE);
        selectedDrawerName = bundle.getString(ARG_DRAWERNAME);

        calFromDate = convertToCalender(selectedFromDate);
        calToDate = convertToCalender(selectedToDate);

        btnFromDate = (Button) rootView.findViewById(R.id.btn_from_date);
        btnToDate = (Button) rootView.findViewById(R.id.btn_to_date);
        btnPositive = (Button) rootView.findViewById(R.id.btn_positive);
        btnNegative = (Button) rootView.findViewById(R.id.btn_negative);
        btnReset = (Button) rootView.findViewById(R.id.btn_reset);

        spinnerOrder = (Spinner) rootView.findViewById(R.id.spinner_order);
        spinnerSort = (Spinner) rootView.findViewById(R.id.spinner_sort);

        ArrayAdapter<CharSequence> adapterSpinnerOrder = ArrayAdapter.createFromResource(
                getActivity(), R.array.spinnerUserOrder, android.R.layout.simple_spinner_item);
        adapterSpinnerOrder.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerOrder.setAdapter(adapterSpinnerOrder);

        orderUserArray = getResources().getStringArray(R.array.spinnerUserOrder);
        if (selectedDrawerName.equalsIgnoreCase("userDrawer")) {
            sortUserArray = getResources().getStringArray(R.array.spinnerUserSort);
        } else {
            sortUserArray = getResources().getStringArray(R.array.spinnerQuestionSort);
        }
        ArrayAdapter<CharSequence> adapterSpinnerSort = new ArrayAdapter<CharSequence>(getActivity(),
                android.R.layout.simple_spinner_item, sortUserArray);
        adapterSpinnerSort.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSort.setAdapter(adapterSpinnerSort);


        btnFromDate.setText(selectedFromDate);
        btnToDate.setText(selectedToDate);
        spinnerOrder.setSelection(((ArrayAdapter<String>) spinnerOrder.getAdapter())
                .getPosition(selectedOrderData));
        spinnerSort.setSelection(((ArrayAdapter<String>) spinnerSort.getAdapter())
                .getPosition(selectedSortData));

        spinnerSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedSortData = sortUserArray[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spinnerOrder.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedOrderData = orderUserArray[position];
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

                if (callbackOnResult != null) {
                    callbackOnResult.sendData(
                            selectedOrderData,
                            selectedSortData,
                            selectedToDate,
                            selectedFromDate);
                }
                getDialog().cancel();
            }
        });

        btnNegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().cancel();
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedToDate = "";
                selectedFromDate = "";
                selectedOrderData = orderUserArray[0];
                selectedSortData = sortUserArray[0];

                callbackOnResult.sendData(
                        selectedOrderData,
                        selectedSortData,
                        selectedToDate,
                        selectedFromDate);
                getDialog().cancel();

            }
        });

        calFromDate = Calendar.getInstance();
        return rootView;
    }

    private Calendar convertToCalender(String date) {
        Calendar mCalender = Calendar.getInstance();
        if (date == null) {
            return mCalender;
        } else {
            try {
                DateFormat formatter = new SimpleDateFormat(Constants.DATE_FORMAT);
                Date returnDate = formatter.parse(date);
                mCalender = Calendar.getInstance();
                mCalender.setTime(returnDate);
                return mCalender;
            } catch (ParseException e) {
                e.printStackTrace();
                return mCalender;
            }
        }
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

    public DatePickerDialog.OnDateSetListener fromdateListener =
            new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear,
                                      int dayOfMonth) {
                    calFromDate.set(year, monthOfYear, dayOfMonth);
                    selectedFromDate = Utils.returnFormattedDate(calFromDate.getTimeInMillis());
                    btnFromDate.setText(selectedFromDate);
                }
            };

    DatePickerDialog.OnDateSetListener todateListener =
            new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear,
                                      int dayOfMonth) {
                    calToDate.set(year, monthOfYear, dayOfMonth);
                    selectedToDate = Utils.returnFormattedDate(calToDate.getTimeInMillis());
                    btnToDate.setText(selectedToDate);
                }
            };
}
