package com.softmed.htmr_facility.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.softmed.htmr_facility.R;
import com.softmed.htmr_facility.base.AppDatabase;
import com.softmed.htmr_facility.base.BaseActivity;
import com.softmed.htmr_facility.dom.objects.Patient;
import com.softmed.htmr_facility.dom.objects.Referral;

import static com.softmed.htmr_facility.utils.constants.REFERRAL_STATUS_COMPLETED;

/**
 * Created by issy on 1/6/18.
 *
 * @issyzac issyzac.iz@gmail.com
 * On Project HFReferralApp
 */

public class FeedbackDetailsActivity extends BaseActivity {

    private Toolbar toolbar;
    public TextView ctcNumber, referalReasons, villageLeaderValue, referrerName;
    private EditText servicesOfferedEt, otherInformationEt;
    private CheckBox hivStatus;
    public TextView clientNames, wardText, villageText, hamletText, patientGender;

    private Referral currentReferral;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_referral_feedback);
        setupviews();

        if (getIntent().getExtras() != null){
            currentReferral = (Referral) getIntent().getSerializableExtra("referal");
            if (currentReferral != null){
                if (currentReferral.getReferralStatus() == REFERRAL_STATUS_COMPLETED){

                    servicesOfferedEt.setEnabled(false);
                    servicesOfferedEt.setText(currentReferral.getServiceGivenToPatient());

                    otherInformationEt.setText(currentReferral.getOtherNotesAndAdvices());
                    otherInformationEt.setEnabled(false);

                    hivStatus.setEnabled(false);

                }
                hivStatus.setChecked(currentReferral.isTestResults());
                referalReasons.setText(currentReferral.getReferralReason() == null ? "" : currentReferral.getReferralReason());
                villageLeaderValue.setText(currentReferral.getVillageLeader() == null ? "" : currentReferral.getVillageLeader());
                referrerName.setText(currentReferral.getServiceProviderUIID() == null ? "" : currentReferral.getServiceProviderUIID());
                new FeedbackDetailsActivity.patientDetailsTask(baseDatabase, currentReferral.getPatient_id()).execute();
            }
        }

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    private void setupviews(){

        hivStatus = (CheckBox) findViewById(R.id.hiv_status);
        hivStatus.setEnabled(false);

        servicesOfferedEt = (EditText) findViewById(R.id.service_offered_et);
        servicesOfferedEt.setEnabled(false);
        otherInformationEt = (EditText) findViewById(R.id.other_information_et);
        otherInformationEt.setEnabled(false);

        referrerName = (TextView) findViewById(R.id.referer_name_value);
        villageLeaderValue = (TextView) findViewById(R.id.mwenyekiti_name_value);

        patientGender = (TextView) findViewById(R.id.patient_gender_value);
        wardText = (TextView) findViewById(R.id.client_kata_value);
        villageText = (TextView) findViewById(R.id.client_kijiji_value);
        hamletText = (TextView) findViewById(R.id.client_kitongoji_value);

        clientNames = (TextView) findViewById(R.id.client_name);

        referalReasons = (TextView) findViewById(R.id.sababu_ya_rufaa_value);

    }

    class patientDetailsTask extends AsyncTask<Void, Void, Void> {

        String patientNames, patientId;
        Patient patient;
        AppDatabase db;

        patientDetailsTask(AppDatabase database, String patientID){
            this.db = database;
            this.patientId = patientID;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            patientNames = db.patientModel().getPatientName(patientId);
            patient = db.patientModel().getPatientById(patientId);
            Log.d("", "PATIENT : "+patient.getPatientId());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.d("reckless", "Done background!"+patientNames);
            clientNames.setText(patientNames);
            if (patient != null){
                wardText.setText(patient.getWard() == null ? "Kata :  " : "Kata : "+patient.getWard());
                villageText.setText(patient.getVillage() == null ? "Kijiji :  " : "Kijiji : "+patient.getVillage());
                hamletText.setText(patient.getHamlet() == null ? "Kitongoji :  " : "Kitongoji : "+patient.getHamlet());
                patientGender.setText(patient.getGender());
            }
            //adapter.notifyDataSetChanged();
        }

    }

}
