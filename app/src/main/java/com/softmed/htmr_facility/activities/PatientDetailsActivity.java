package com.softmed.htmr_facility.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.softmed.htmr_facility.R;
import com.softmed.htmr_facility.base.BaseActivity;
import com.softmed.htmr_facility.dom.objects.Patient;
import com.softmed.htmr_facility.fragments.IssueReferralDialogueFragment;

/**
 * Created by issy on 06/02/2018.
 *
 * @issyzac issyzac.iz@gmail.com
 * On Project HFReferralApp
 */

public class PatientDetailsActivity extends BaseActivity {

    private Button cancelButton, referButton;
    private TextView clientNames, clientAge, clientGender, clientVEO, clientWard, clientVillage, clientMapCue, careTakerName, careTakerPhone, careTakerRelationship;
    private int service;
    private Patient currentPatient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_details);
        setupviews();

        if (getIntent().getExtras() != null){
            service = getIntent().getIntExtra("service", 0);
            currentPatient = (Patient) getIntent().getSerializableExtra("patient");
        }

        if (currentPatient != null){

            clientNames.setText(currentPatient.getPatientFirstName()+" "+currentPatient.getPatientSurname());
            clientGender.setText(currentPatient.getGender() == null? "n/a" : currentPatient.getGender());
            clientVillage.setText(currentPatient.getVillage() == null?"n/a":currentPatient.getVillage());
            clientWard.setText(currentPatient.getWard() == null ? "n/a" : currentPatient.getWard());
            clientMapCue.setText(currentPatient.getHamlet() == null ? "n/a" : currentPatient.getHamlet());
            clientVEO.setText("");

            careTakerName.setText(currentPatient.getCareTakerName() == null ? "n/a" : currentPatient.getCareTakerName());
            careTakerPhone.setText(currentPatient.getCareTakerPhoneNumber() == null ? "n/a" : currentPatient.getCareTakerPhoneNumber());
            careTakerRelationship.setText(currentPatient.getCareTakerRelationship() == null ? "n/a" : currentPatient.getCareTakerRelationship());

        }

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        referButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callReferralFragmentDialogue(currentPatient);
            }
        });

    }

    private void setupviews(){
        cancelButton = (Button) findViewById(R.id.cancel_button);
        referButton = (Button) findViewById(R.id.referal_button);

        clientAge = (TextView) findViewById(R.id.client_age_value);
        clientNames = (TextView) findViewById(R.id.client_name);
        clientGender = (TextView) findViewById(R.id.patient_gender_value);
        clientVEO  = (TextView) findViewById(R.id.mwenyekiti_name_value);
        clientWard = (TextView) findViewById(R.id.client_kata_value);
        clientVillage = (TextView) findViewById(R.id.client_kijiji_value);
        clientMapCue = (TextView) findViewById(R.id.client_map_cue);
        careTakerName = (TextView) findViewById(R.id.care_taker_name);
        careTakerPhone = (TextView) findViewById(R.id.care_taker_phone);
        careTakerRelationship = (TextView) findViewById(R.id.care_taker_relationship);

    }

    private void callReferralFragmentDialogue(Patient patient){
        FragmentManager fm = this.getSupportFragmentManager();

        IssueReferralDialogueFragment issueReferralDialogueFragment = IssueReferralDialogueFragment.newInstance(patient, service);
        issueReferralDialogueFragment.show(fm, "referral_fragment_from_adapter");

    }

}
