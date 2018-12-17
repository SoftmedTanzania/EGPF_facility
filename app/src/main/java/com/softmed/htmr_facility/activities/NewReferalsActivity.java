package com.softmed.htmr_facility.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

import com.softmed.htmr_facility.R;
import com.softmed.htmr_facility.adapters.PatientsListAdapter;
import com.softmed.htmr_facility.base.BaseActivity;
import com.softmed.htmr_facility.dom.objects.Patient;
import com.softmed.htmr_facility.viewmodels.PatientsListViewModel;

import static com.softmed.htmr_facility.utils.constants.HIV_SERVICE_ID;

/**
 * Created by issy on 11/16/17.
 */

public class NewReferalsActivity extends BaseActivity {

    private Toolbar toolbar;
    private RecyclerView patientsRecycler;
    private PatientsListAdapter adapter;

    private PatientsListViewModel patientsListViewModel;
    private int service;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent().getExtras() != null){
            service = getIntent().getIntExtra("service", 0);
        }

        if (service == HIV_SERVICE_ID){
            setContentView(R.layout.activity_hiv_new_referrals);
        }else {
            setContentView(R.layout.activity_new_referals);
        }

        setupview();

        if (toolbar != null){
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        adapter = new PatientsListAdapter(new ArrayList<Patient>(), this, service);
        patientsListViewModel = ViewModelProviders.of(this).get(PatientsListViewModel.class);

        if (service == HIV_SERVICE_ID){
            patientsListViewModel.getHivPatientsOnly().observe(NewReferalsActivity.this, new Observer<List<Patient>>() {
                @Override
                public void onChanged(@Nullable List<Patient> patients) {
                    adapter.addItems(patients);
                }
            });
        }else {
            patientsListViewModel.getPatientsList().observe(NewReferalsActivity.this, new Observer<List<Patient>>() {
                @Override
                public void onChanged(@Nullable List<Patient> patients) {
                    adapter.addItems(patients);
                }
            });
        }

        patientsRecycler.setAdapter(adapter);

    }

    private void setupview(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        patientsRecycler = (RecyclerView) findViewById(R.id.patients_recycler);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        patientsRecycler.setLayoutManager(layoutManager);
        patientsRecycler.setHasFixedSize(true);
    }

}
