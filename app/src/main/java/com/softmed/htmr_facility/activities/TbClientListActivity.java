package com.softmed.htmr_facility.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import com.softmed.htmr_facility.R;
import com.softmed.htmr_facility.adapters.TbClientListAdapter;
import com.softmed.htmr_facility.base.BaseActivity;
import com.softmed.htmr_facility.dom.objects.Patient;
import com.softmed.htmr_facility.viewmodels.PatientsListViewModel;

import static com.softmed.htmr_facility.utils.constants.TB_SERVICE_ID;

/**
 * Created by issy on 12/28/17.
 *
 * @issyzac issyzac.iz@gmail.com
 * On Project HFReferralApp
 */

public class TbClientListActivity extends BaseActivity {

    private Toolbar toolbar;
    private RecyclerView patientsRecycler;
    private TbClientListAdapter adapter;
    private EditText clientFNameInput, clientLNameInput, clientCTCNumberInput, clientVillageInput;
    private Button filterButton;

    private String clientFirstNameValue, clientLastNameValue, clientCTCNumberValue, clientVillageValue;
    private PatientsListViewModel patientsListViewModel;
    private List<Patient> patientList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_referals);
        setupview();

        if (toolbar != null){
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        adapter = new TbClientListAdapter(new ArrayList<Patient>(), this, TB_SERVICE_ID);
        patientsListViewModel = ViewModelProviders.of(this).get(PatientsListViewModel.class);
        patientsListViewModel.getTbPatientsOnly().observe(TbClientListActivity.this, new Observer<List<Patient>>() {
            @Override
            public void onChanged(@Nullable List<Patient> patients) {
                patientList = patients;
                adapter.addItems(patients);
            }
        });

        patientsRecycler.setAdapter(adapter);

        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getInput()){
                    filterList(patientList);
                }
            }
        });

    }

    private void filterList(List<Patient> patients){
        List<Patient> patientsResultList = new ArrayList<>();
        Patient[] result1 = patients.stream()
                .filter((p) -> clientFirstNameValue.equals(p.getPatientFirstName()) &&
                        clientLastNameValue.equals(p.getPatientSurname()) &&
                        clientCTCNumberValue.equals(p.getPhone_number()) &&
                        clientVillageValue.equals(p.getVillage())
                )
                .toArray(Patient[]::new);

        for (int i=0; i<result1.length; i++){
            patientsResultList.add(result1[i]);
        }

        adapter.addItems(patientsResultList);

    }

    private boolean getInput(){
        if (clientFNameInput.getText().toString().isEmpty() &&
                clientLNameInput.getText().toString().isEmpty() &&
                clientCTCNumberInput.getText().toString().isEmpty() &&
                clientVillageInput.getText().toString().isEmpty() ){
            Toast.makeText(this, "Something to search needed!", Toast.LENGTH_LONG).show();
            return false;

        }else {
            clientFirstNameValue = clientFNameInput.getText().toString();
            clientLastNameValue = clientLNameInput.getText().toString();
            clientCTCNumberValue = clientCTCNumberInput.getText().toString();
            clientVillageValue = clientVillageInput.getText().toString();

            return true;
        }

    }

    private void setupview(){

        filterButton = (Button) findViewById(R.id.filter_button);

        clientFNameInput = (EditText) findViewById(R.id.client_name_et);
        clientLNameInput = (EditText) findViewById(R.id.client_last_name_et);
        clientCTCNumberInput = (EditText) findViewById(R.id.client_ctc_number_et);
        clientVillageInput = (EditText) findViewById(R.id.client_village_et);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        patientsRecycler = (RecyclerView) findViewById(R.id.patients_recycler);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        patientsRecycler.setLayoutManager(layoutManager);
        patientsRecycler.setHasFixedSize(true);
    }

}
