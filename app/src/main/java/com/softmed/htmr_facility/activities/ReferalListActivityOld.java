package com.softmed.htmr_facility.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.rey.material.widget.ProgressView;
import com.softmed.htmr_facility.R;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.softmed.htmr_facility.adapters.ReferalListRecyclerAdapter;
import com.softmed.htmr_facility.base.AppDatabase;
import com.softmed.htmr_facility.base.BaseActivity;
import com.softmed.htmr_facility.dom.objects.Client;
import com.softmed.htmr_facility.dom.objects.Referral;
import com.softmed.htmr_facility.viewmodels.ReferalListViewModel;
import fr.ganfra.materialspinner.MaterialSpinner;

import static com.softmed.htmr_facility.utils.constants.HIV_SERVICE_ID;
import static com.softmed.htmr_facility.utils.constants.STATUS_COMPLETED;
import static com.softmed.htmr_facility.utils.constants.STATUS_NEW;
import static com.softmed.htmr_facility.utils.constants.getReferralStatusValue;

/**
 * Created by issy on 11/16/17.
 */

public class ReferalListActivityOld extends BaseActivity {

    private ReferalListViewModel listViewModel;

    private Toolbar toolbar;
    private RecyclerView clientRecyclerView, oldClientsRecycler;
    private MaterialSpinner statusSpinner;
    private EditText fromDateText, toDateText, clientNameText, clientCtcNumberText, clientLastName;
    private ProgressView progressView;
    private Button filterButton;

    private List<Client> mClientList = new ArrayList<>();
    private List<Client> mOldClientList = new ArrayList<>();

    private ReferalListRecyclerAdapter adapter;

    private Date fromDate, toDate;
    private String clientName, clientCtcNumber, lastName;
    private int selectedStatus;
    private boolean notSelectedStatus, notSelectedFromDate, notSelectedTodate;

    private AppDatabase database;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_referal_list);
        setupviews();

        database = AppDatabase.getDatabase(this);

        if (toolbar != null){
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        final String[] status = {STATUS_COMPLETED, STATUS_NEW};
        ArrayAdapter<String> spinAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, status);
        spinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpinner.setAdapter(spinAdapter);

        notSelectedStatus = true;
        notSelectedFromDate = true;
        notSelectedTodate = true;

        statusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i >= 0){
                    selectedStatus = getReferralStatusValue((String) statusSpinner.getItemAtPosition(i));
                    Log.d("ReferalFilters", "Selected Status is : "+selectedStatus);
                }else {
                    notSelectedStatus = false;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getInputs()){
                    filterButton.setVisibility(View.INVISIBLE);
                    progressView.setVisibility(View.VISIBLE);

                    QueryReferals queryReferals = new QueryReferals(clientName, lastName, clientCtcNumber, fromDate, toDate, selectedStatus, database);
                    queryReferals.execute();

                }else {
                    Toast.makeText(ReferalListActivityOld.this,"Please Fill in any field ", Toast.LENGTH_SHORT).show();
                }
            }
        });

        fromDateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Open Date Picker
                fromDatePicker.show(getFragmentManager(),"fromDate");
                fromDatePicker.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

                        /*
                        Cursor vacinationCursor = mydb.getReadableDatabase().rawQuery("SELECT COUNT(*) FROM " + SQLHandler.Tables.VACCINATION_EVENT +
                                        " where " + SQLHandler.VaccinationEventColumns.CHILD_ID + "=? and " +
                                        SQLHandler.VaccinationEventColumns.VACCINATION_STATUS + "= 'true'",
                                new String[]{currentChild.getId()});
                        vacinationCursor.moveToFirst();
                        */

                        fromDateText.setText((dayOfMonth < 10 ? "0" + dayOfMonth : dayOfMonth) + "-" + ((monthOfYear + 1) < 10 ? "0" + (monthOfYear + 1) : monthOfYear + 1) + "-" + year);
                        Calendar fromCalendar = Calendar.getInstance();
                        fromCalendar.set(year, monthOfYear, dayOfMonth);
                        fromDate = fromCalendar.getTime();
                        notSelectedFromDate = false;
                    }

                });

            }
        });

        toDateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Open Date Picker
                toDatePicker.show(getFragmentManager(),"toDate");
                toDatePicker.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

                        /*
                        Cursor vacinationCursor = mydb.getReadableDatabase().rawQuery("SELECT COUNT(*) FROM " + SQLHandler.Tables.VACCINATION_EVENT +
                                        " where " + SQLHandler.VaccinationEventColumns.CHILD_ID + "=? and " +
                                        SQLHandler.VaccinationEventColumns.VACCINATION_STATUS + "= 'true'",
                                new String[]{currentChild.getId()});
                        vacinationCursor.moveToFirst();
                        */

                        toDateText.setText((dayOfMonth < 10 ? "0" + dayOfMonth : dayOfMonth) + "-" + ((monthOfYear + 1) < 10 ? "0" + (monthOfYear + 1) : monthOfYear + 1) + "-" + year);
                        Calendar toCalendar = Calendar.getInstance();
                        toCalendar.set(year, monthOfYear, dayOfMonth);
                        toDate = toCalendar.getTime();
                        notSelectedTodate = false;
                    }

                });
            }
        });


        adapter = new ReferalListRecyclerAdapter();
        listViewModel = ViewModelProviders.of(this).get(ReferalListViewModel.class);
        listViewModel.getReferalList().observe(ReferalListActivityOld.this, new Observer<List<Referral>>() {
            @Override
            public void onChanged(@Nullable List<Referral> referrals) {
                adapter.addItems(referrals);
            }
        });

        clientRecyclerView.setAdapter(adapter);
        //getReferalClientsList();

    }

    private boolean getInputs(){

        if (
                clientNameText.getText().toString().isEmpty() &
                clientCtcNumberText.getText().toString().isEmpty() &
                clientLastName.getText().toString().isEmpty() &
                notSelectedFromDate &
                notSelectedTodate &
                notSelectedStatus){
            return false;
        }else {
            clientName = clientNameText.getText().toString().isEmpty() ? "" : clientNameText.getText().toString();
            clientCtcNumber = clientCtcNumberText.getText().toString().isEmpty()? "" : clientCtcNumberText.getText().toString();
            lastName = clientLastName.getText().toString().isEmpty()? "" : clientLastName.getText().toString();
            return true;
        }

    }

    private class QueryReferals extends AsyncTask<Void,Void, Void>{

        String clientName, lastName, ctcNumber;
        AppDatabase db;
        List<Referral> fReferrals;
        int referalStatus;
        Date frmDate, toDate;

        QueryReferals(String name, String lastName, String ctc, Date dateFrom, Date dateTo, int refStatus, AppDatabase database){
            this.clientName = name;
            this.db = database;
            this.referalStatus = refStatus;
            this.frmDate = fromDate;
            this.toDate = dateTo;
            this.lastName = lastName;
            this.ctcNumber = ctc;
            Log.d("", "Status "+refStatus);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            adapter.addItems(fReferrals);
            filterButton.setVisibility(View.VISIBLE);
            progressView.setVisibility(View.GONE);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            fReferrals = db.referalModel().getFilteredReferal(clientName, lastName, referalStatus, HIV_SERVICE_ID);
            return null;
        }
    }

    private void setupviews(){

        filterButton = (Button) findViewById(R.id.filter_button);

        progressView = (ProgressView) findViewById(R.id.progress_bar);
        progressView.setVisibility(View.INVISIBLE);

        fromDateText = (EditText) findViewById(R.id.from_date);
        toDateText = (EditText) findViewById(R.id.to_date);
        clientNameText = (EditText) findViewById(R.id.client_name_et);
        clientCtcNumberText = (EditText) findViewById(R.id.client_ctc_number_et);
        clientLastName = (EditText) findViewById(R.id.client_last_name_et);


        statusSpinner = (MaterialSpinner) findViewById(R.id.spin_status);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        clientRecyclerView = (RecyclerView) findViewById(R.id.clients_recycler);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        clientRecyclerView.setLayoutManager(layoutManager);
        clientRecyclerView.setHasFixedSize(true);

    }

}
