package com.softmed.htmr_facility.activities;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.softmed.htmr_facility.R;
import com.softmed.htmr_facility.adapters.AppointmentRecyclerAdapter;
import com.softmed.htmr_facility.base.BaseActivity;
import com.softmed.htmr_facility.dom.objects.PatientAppointment;
import fr.ganfra.materialspinner.MaterialSpinner;

/**
 * Created by issy on 12/14/17.
 *
 * @issyzac issyzac.iz@gmail.com
 * On Project HFReferralApp
 */

public class AppointmentActivity extends BaseActivity {

    private Toolbar toolbar;
    private RecyclerView appointmentRecycler;
    private EditText patientName, fromDate, toDate;
    private MaterialSpinner statusSpinner, appointmentType;

    private AppointmentRecyclerAdapter adapter;
    private mAdapter appointmentTypeAdapter;

    private List<PatientAppointment> appointments = new ArrayList<>();

    private List<String> appointmentTypeList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);
        setupviews();

        if (toolbar!=null){
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Patient Appointment");
        }

        appointmentTypeList.add("All");
        appointmentTypeList.add("CTC");
        appointmentTypeList.add("TB");
        appointmentTypeAdapter = new mAdapter(this, R.layout.subscription_plan_items_drop_down, appointmentTypeList);
        appointmentType.setAdapter(appointmentTypeAdapter);

        appointmentType.setSelection(1);

        appointmentType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == -1){

                }else if (i == 0){
                    new GetAppointmentList().execute(0);
                }else if (i == 1){
                    new GetAppointmentList().execute(1);
                }else {
                    new GetAppointmentList().execute(2);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        adapter = new AppointmentRecyclerAdapter(appointments,this,baseDatabase);
        appointmentRecycler.setAdapter(adapter);

        new GetAppointmentList().execute(0);

    }

    private void setupviews(){

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        appointmentRecycler = (RecyclerView) findViewById(R.id.appointment_recycler);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        appointmentRecycler.setLayoutManager(layoutManager);
        appointmentRecycler.setHasFixedSize(true);

        appointmentType = (MaterialSpinner) findViewById(R.id.spin_appointment_type);
        statusSpinner = (MaterialSpinner) findViewById(R.id.spin_status);
        patientName = (EditText) findViewById(R.id.client_name_et);
        fromDate = (EditText) findViewById(R.id.from_date_et);
        toDate = (EditText) findViewById(R.id.to_date_et);

    }

    class GetAppointmentList extends AsyncTask<Integer, Void, Void>{

        List<PatientAppointment> list;

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            adapter.addItems(list);
        }

        @Override
        protected Void doInBackground(Integer... types) {
            if (types[0] == 0){
                list = baseDatabase.appointmentModelDao().getAllAppointments();
            }else if (types[0] == 1){
                list = baseDatabase.appointmentModelDao().getAllCTCAppointments();
            }else{
                list = baseDatabase.appointmentModelDao().getAllTbAppointments();
            }

            return null;
        }

    }

    class mAdapter extends ArrayAdapter<String> {

        List<String> items = new ArrayList<>();
        Context act;

        public mAdapter(Context context, int resource, List<String> mItems) {
            super(context, resource, mItems);
            this.items = mItems;
            act = context;
        }


        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            View rowView = convertView;
            LayoutInflater vi = (LayoutInflater) act.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = vi.inflate(R.layout.subscription_plan_items_drop_down, null);

            TextView tvTitle =(TextView)rowView.findViewById(R.id.rowtext);
            tvTitle.setText(items.get(position));

            return rowView;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            View rowView = convertView;
            LayoutInflater vi = (LayoutInflater) act.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = vi.inflate(R.layout.single_text_spinner_view_item, null);

            TextView tvTitle = (TextView)rowView.findViewById(R.id.rowtext);
            tvTitle.setText(items.get(position));

            return rowView;
        }

        @Override
        public int getCount() {
            return items.size();
        }

        public void updateItems(List<String> newItems){
            this.items = null;
            this.items = newItems;
            this.notifyDataSetChanged();
        }

    }

}
